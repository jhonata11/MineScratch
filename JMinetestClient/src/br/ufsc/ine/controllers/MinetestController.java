package br.ufsc.ine.controllers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import br.ufsc.ine.minetest.MinetestPacket;
import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.minetest.models.TeleportCoordinates;
import br.ufsc.ine.utils.Utils;

public class MinetestController {

	private static final short CHAT_MESSAGE = 0x32;
	private static final short PLAYER_POSITION = 0x23;

	private Sender sender;

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

	public void teleport(TeleportCoordinates position) {
		MinetestPacket packet = this.createPacket(PLAYER_POSITION);
		for (int i : position.getPosition()) {
			byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
			packet.appendLast(bytes);
		}
		for (int i : position.getAngle()) {
			byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
			packet.appendLast(bytes);
		}
		for (int i : position.getSpeed()) {
			byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
			packet.appendLast(bytes);
		}

		packet.appendLast(ByteBuffer.allocate(4).putInt(0x01).array());
		sendCommand(packet);
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
