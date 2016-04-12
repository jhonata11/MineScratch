package br.ufsc.ine.controllers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.minetest.MinetestConnector;
import br.ufsc.ine.minetest.MinetestPacket;
import br.ufsc.ine.minetest.Receiver;
import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.PlayerInfo;
import br.ufsc.ine.scratch.ScratchClient;
import br.ufsc.ine.utils.Utils;

public class MinetestClient {

	private static final short TO_SERVER_CHAT_MESSAGE = 0x32;
	private static final short TO_SERVER_PLAYER_POSITION = 0x23;
	
	private static final short TO_CLIENT_CHAT_MESSAGE = 0x30;
	private static final short TO_CLIENT_MOVE_PLAYER = 0x34;


	private Sender sender;
	private Receiver receiver;
	
	private PlayerInfo playerInfo;
	private MinetestConnector connector;
	private Semaphore connectionSemaphore;
	private Semaphore playerInfoSemaphore;

	
	

	public MinetestClient(String host, int port, String username, String password) throws InterruptedException {
		this.playerInfo = new PlayerInfo();
		this.connector = new MinetestConnector(host, port, username, password);
		this.connectionSemaphore = new Semaphore(1);
		this.playerInfoSemaphore = new Semaphore(1);

		sender = new Sender(host, port, connectionSemaphore);
		receiver = new Receiver(sender.getMinetestProtocol(), connectionSemaphore, port);
		
	}
	
	public void startApplication() throws Exception{
		this.connector.setSender(sender);
		this.connector.setReceiver(receiver);
		this.connector.connect();
		this.initScratch();
		while (true) {
//			this.playerInfoSemaphore.acquire();
			receiveMessage();
		}
	}
	
	
	
	private void initScratch(){
		ScratchClient client = new ScratchClient(this);
		Thread scratchThread = new Thread(client);
		scratchThread.start();
	}
	
	
	private void receiveMessage() throws InterruptedException {
		byte[] receiveCommand = sender.getMinetestProtocol().receiveCommand();
		if (receiveCommand != null && receiveCommand.length >= 2) {
			short tipo = Utils.byteToShort(ArrayUtils.subarray(receiveCommand, 0, 2));
			byte[] data = ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length);

			if (tipo == TO_CLIENT_CHAT_MESSAGE) {
				String str = new String(data,StandardCharsets.UTF_16BE);
				System.out.println(str);
//				printer.print("Minetest", str);
			} else if(tipo == TO_CLIENT_MOVE_PLAYER){
				
				Integer x10000 = Utils.byteToInt(ArrayUtils.subarray(data, 0, 4));
				Integer y10000 = Utils.byteToInt(ArrayUtils.subarray(data, 4, 8));
				Integer z10000 = Utils.byteToInt(ArrayUtils.subarray(data, 8, 12));
				Integer pitch1000 = Utils.byteToInt(ArrayUtils.subarray(data, 12, 16));
				Integer yaw1000 = Utils.byteToInt(ArrayUtils.subarray(data, 16, 20));
				
				this.playerInfo.getCoordinates().setPosition(new Float(x10000/(float)10000), new Float(y10000/(float)10000), new Float(z10000/(float)10000));
				this.playerInfo.getCoordinates().setAngle(new Float(pitch1000/(float)1000), new Float(yaw1000/(float)1000));
				
//				this.playerInfoSemaphore.release();
			}
		}
	}

	public void sendChatMessage(String mensagem) {
		byte[] encodedMessage = Charset.forName("UTF-16BE").encode(mensagem).array();
		byte[] messageLength = ByteBuffer.allocate(2).putShort((short) mensagem.getBytes().length).array();
		MinetestPacket packet = this.createPacket(TO_SERVER_CHAT_MESSAGE);
		packet.appendLast(messageLength);
		packet.appendLast(encodedMessage);

		String str = new String(encodedMessage, StandardCharsets.UTF_16BE);
		System.err.println("mensagem enviada: " + str);

		sendCommand(packet);
	}
	
	
	public void move(Coordinate deltaPosition){
		Coordinate coordinates = this.playerInfo.getCoordinates();
		Float x = coordinates.getPosition().get(0) + deltaPosition.getPosition().get(0);
		Float y = coordinates.getPosition().get(1) + deltaPosition.getPosition().get(1);
		Float z = coordinates.getPosition().get(2) + deltaPosition.getPosition().get(2);
		Float pitch = coordinates.getAngle().get(0) + deltaPosition.getAngle().get(0);
		Float yaw = coordinates.getAngle().get(1) + deltaPosition.getAngle().get(1);
		
		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(x, y, z);
		newCoordinate.setAngle(pitch, yaw);
		
		this.teleport(newCoordinate);
	}
	
	public void walk(Integer distance){
		Coordinate coordinates = this.playerInfo.getCoordinates();
		Float dx = new Float(distance * Math.cos((90 + coordinates.getAngle().get(1)) / 180 * Math.PI));
		Float dz = new Float(distance * Math.sin((90 + coordinates.getAngle().get(1)) / 180 * Math.PI));
		
		Coordinate newPosition = new Coordinate();
		newPosition.setPosition(dx, (float)0, dz);
		
		this.move(newPosition);

	}

	public void teleport(Coordinate newCoordinate) {
		MinetestPacket packet = this.createPacket(TO_SERVER_PLAYER_POSITION);
		List<Float> position = newCoordinate.getPosition();
		List<Float> angle = newCoordinate.getAngle();
		List<Float> speed = newCoordinate.getSpeed();
		
		position.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 1000)));
		angle.forEach((number) -> allocateBytes(packet, new Integer(number.intValue()* 100)));
		speed.forEach((number) -> allocateBytes(packet, new Integer(number.intValue()* 100)));
		
		this.playerInfo.getCoordinates().setPosition(position);
		this.playerInfo.getCoordinates().setAngle(angle);
		this.playerInfo.getCoordinates().setSpeed(speed);
		
		System.out.println("teleport: ");
		System.out.println(String.format("posição: %s %s %s",position.get(0),position.get(1),position.get(2)));
		System.out.println(String.format("velocidade: %s %s %s",speed.get(0),speed.get(1),speed.get(2)));
		System.out.println(String.format("angulo: %s %s",angle.get(0),angle.get(1)));

		
		packet.appendLast(ByteBuffer.allocate(4).putInt(0x01).array());
		sendCommand(packet);
	}
	
	
	private void allocateBytes(MinetestPacket packet, Integer value){
		byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
		packet.appendLast(bytes);
	}

	private MinetestPacket createPacket(short commandType) {
		byte[] type = ByteBuffer.allocate(2).putShort((short) commandType).array();
		MinetestPacket packet = new MinetestPacket();
		packet.appendFirst(type);
		return packet;
	}

	private void sendCommand(MinetestPacket packet) {
		try {
			this.sender.sendCommand(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
