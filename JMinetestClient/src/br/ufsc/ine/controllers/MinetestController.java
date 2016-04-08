package br.ufsc.ine.controllers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import br.ufsc.ine.minetest.MinetestPacket;
import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.PlayerInfo;

public class MinetestController {

	private static final short CHAT_MESSAGE = 0x32;
	private static final short PLAYER_POSITION = 0x23;

	private Sender sender;
	private PlayerInfo playerInfo;
	
	
	

	public MinetestController(Sender sender) {
		this.sender = sender;
	}

	public void sendChatMessage(String mensagem) {
		byte[] encodedMessage = Charset.forName("UTF-16BE").encode(mensagem).array();
		byte[] messageLength = ByteBuffer.allocate(2).putShort((short) mensagem.getBytes().length).array();
		MinetestPacket packet = this.createPacket(CHAT_MESSAGE);
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
		MinetestPacket packet = this.createPacket(PLAYER_POSITION);
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
