package br.ufsc.ine.controllers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

public class ModelController {

	private static final short TO_SERVER_CHAT_MESSAGE = 0x32;
	private static final short TO_SERVER_PLAYER_POSITION = 0x23;
	
	private static final short TO_CLIENT_CHAT_MESSAGE = 0x30;
	private static final short TO_CLIENT_MOVE_PLAYER = 0x34;


	private Sender sender;
	private Receiver receiver;
	
	private PlayerInfo playerInfo;
	private MinetestConnector connector;
	private Semaphore semaphore;
	
	

	public ModelController(String host, int port, String username, String password) throws InterruptedException {
		this.connector = new MinetestConnector(host, port, username, password);
		this.semaphore = new Semaphore(1);

		sender = new Sender(host, port, semaphore);
		receiver = new Receiver(sender.getMinetestProtocol(), semaphore, port);
		
	}
	
	public void startApplication() throws Exception{
		this.connector.setSender(sender);
		this.connector.setReceiver(receiver);
		this.connector.connect();
//		this.initScratch();
		while (true) {
			receiveMessage();
		}
	}
	
	
	
	private void initScratch(){
		ScratchClient client = new ScratchClient(this.sender);
		Thread scratchThread = new Thread(client);
		scratchThread.start();
	}
	
	
	private void receiveMessage() {
		byte[] receiveCommand = sender.getMinetestProtocol().receiveCommand();
		if (receiveCommand != null && receiveCommand.length >= 2) {
			short tipo = Utils.byteToShort(ArrayUtils.subarray(receiveCommand, 0, 2));
			System.out.println(tipo);

			if (tipo == TO_CLIENT_CHAT_MESSAGE) {
				String str = new String(ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length),
						StandardCharsets.UTF_16BE);
				System.out.println(str);
//				printer.print("Minetest", str);
			} else if(tipo == TO_CLIENT_MOVE_PLAYER){
				String str = new String(ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length),
						StandardCharsets.UTF_16BE);
				System.out.println(str);
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
		double x = coordinates.getPosition().get(0) + deltaPosition.getPosition().get(0);
		double y = coordinates.getPosition().get(1) + deltaPosition.getPosition().get(1);
		double z = coordinates.getPosition().get(2) + deltaPosition.getPosition().get(2);
		int pitch = coordinates.getAngle().get(0) + deltaPosition.getAngle().get(0);
		int yaw = coordinates.getAngle().get(1) + deltaPosition.getAngle().get(1);
		
		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(x, y, z);
		newCoordinate.setAngle(pitch, yaw);
		
		this.teleport(newCoordinate);
		
	}
	
	public void walk(Integer distance){
		Coordinate coordinates = this.playerInfo.getCoordinates();
		Double dx = Math.cos((90 + coordinates.getAngle().get(1)) / 180 * Math.PI);
		Double dz = Math.sin((90 + coordinates.getAngle().get(1)) / 180 * Math.PI);
		Coordinate newPosition = new Coordinate();
		newPosition.setPosition(dx, 0, dz);
		
		this.move(newPosition);

	}

	public void teleport(Coordinate newPosition) {
		MinetestPacket packet = this.createPacket(TO_SERVER_PLAYER_POSITION);
		newPosition.getPosition().forEach((number) -> allocateBytes(packet, number));
		newPosition.getAngle().forEach((number) -> allocateBytes(packet, number));
		newPosition.getSpeed().forEach((number) -> allocateBytes(packet, number));

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