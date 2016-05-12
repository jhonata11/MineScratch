package br.ufsc.ine.minetest;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.minetest.models.Character;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.network.MinetestPacket;
import br.ufsc.ine.minetest.network.Receiver;
import br.ufsc.ine.minetest.network.Sender;
import br.ufsc.ine.scratch.Scratch;
import br.ufsc.ine.utils.Utils;

public class Minetest {
	private Sender sender;
	private Receiver receiver;

	private Character character;
	private MinetestConnector connector;
	private Semaphore connectionSemaphore;

	public Minetest(String host, int port, String username, String password) throws InterruptedException {
		this.character = new Character();
		this.connector = new MinetestConnector(host, port, username, password);
		this.connectionSemaphore = new Semaphore(1);

		sender = new Sender(host, port, connectionSemaphore);
		receiver = new Receiver(sender.getMinetestProtocol(), connectionSemaphore, port);

	}

	public void startApplication(Scratch scratch) throws Exception {
		this.connector.setSender(sender);
		this.connector.setReceiver(receiver);
		this.connector.connect();
		this.initScratch(scratch);
		while (true) {
			receiveMessage();
		}
	}

	private void initScratch(Scratch scratch) {
		Thread scratchThread = new Thread(scratch);
		scratchThread.start();
	}

	private void receiveMessage() throws InterruptedException {
		byte[] receiveCommand = sender.getMinetestProtocol().receiveCommand();
		if (receiveCommand != null && receiveCommand.length >= 2) {
			short tipo = Utils.byteToShort(ArrayUtils.subarray(receiveCommand, 0, 2));
			byte[] data = ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length);

			if (tipo == Messages.TOCLIENT_CHAT_MESSAGE) {
				String str = new String(data, StandardCharsets.UTF_16BE);
				System.out.println(str);
				// printer.print("Minetest", str);
			} else if (tipo == Messages.TOCLIENT_MOVE_PLAYER) {

				Integer x10000 = Utils.byteToInt(ArrayUtils.subarray(data, 0, 4));
				Integer y10000 = Utils.byteToInt(ArrayUtils.subarray(data, 4, 8));
				Integer z10000 = Utils.byteToInt(ArrayUtils.subarray(data, 8, 12));
				Integer pitch1000 = Utils.byteToInt(ArrayUtils.subarray(data, 12, 16));
				Integer yaw1000 = Utils.byteToInt(ArrayUtils.subarray(data, 16, 20));

				this.character.setPosition(new Float(x10000 / (float) 10000), new Float(y10000 / (float) 10000),
						new Float(z10000 / (float) 10000));
				this.character.setAngle(new Float(pitch1000 / (float) 1000), new Float(yaw1000 / (float) 1000));
			}
		}
	}

	public void sendChatMessage(String mensagem) {
		byte[] encodedMessage = Charset.forName("UTF-16BE").encode(mensagem).array();
		byte[] messageLength = ByteBuffer.allocate(2).putShort((short) mensagem.getBytes().length).array();
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_CHAT_MESSAGE);
		packet.appendLast(messageLength);
		packet.appendLast(encodedMessage);

		String str = new String(encodedMessage, StandardCharsets.UTF_16BE);
		System.err.println("mensagem enviada: " + str);

		sendCommand(packet);
	}

	public void turnRight(Integer degrees) {
		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(character.getPosition().get(0), character.getPosition().get(1),
				character.getPosition().get(2));
		newCoordinate.setAngle(character.getAngle().get(0), character.getAngle().get(1) + degrees);

		this.teleport(newCoordinate);
	}

	public void turnLeft(Integer degrees) {
		Coordinate newCoordinate = new Coordinate();

		newCoordinate.setPosition(character.getPosition().get(0), character.getPosition().get(1),
				character.getPosition().get(2));
		newCoordinate.setAngle(character.getAngle().get(0), character.getAngle().get(1) + (degrees * -1));
		this.teleport(newCoordinate);
	}

	public void move(Coordinate deltaPosition) {
		Float x = character.getPosition().get(0) + deltaPosition.getPosition().get(0);
		Float y = character.getPosition().get(1) + deltaPosition.getPosition().get(1);
		Float z = character.getPosition().get(2) + deltaPosition.getPosition().get(2);
		Float pitch = character.getAngle().get(0) + deltaPosition.getAngle().get(0);
		Float yaw = character.getAngle().get(1) + deltaPosition.getAngle().get(1);

		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(x, y, z);
		newCoordinate.setAngle(pitch, yaw);

		this.teleport(newCoordinate);
	}

	public void walk(Integer distance) {
		Float dx = new Float(distance * Math.cos((90 + character.getAngle().get(1)) / 180 * Math.PI));
		Float dz = new Float(distance * Math.sin((90 + character.getAngle().get(1)) / 180 * Math.PI));
		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(dx, (float) 0, dz);

		this.move(newCoordinate);

	}

	public void teleport(Coordinate newCoordinate) {
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_PLAYER_POSITION);
		List<Float> position = newCoordinate.getPosition();
		List<Float> speed = newCoordinate.getSpeed();
		List<Float> angle = newCoordinate.getAngle();

		position.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 1000)));
		speed.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 100)));
		angle.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 100)));

		this.character.setPosition(position.get(0), position.get(1), position.get(2));
		this.character.setSpeed(speed.get(0), speed.get(1), speed.get(2));
		this.character.setAngle(angle.get(0), angle.get(1));

		packet.appendLast(ByteBuffer.allocate(4).putInt(0x01).array());
		sendCommand(packet);
	}

	public void changePlayerItem(short item) {
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_INTERACT);
		byte[] selectedItem = ByteBuffer.allocate(2).putShort((short) item).array();
		packet.appendLast(selectedItem);

		sendCommand(packet);
	}

	public void interact() {
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_PLAYERITEM);
		byte[] action = ByteBuffer.allocate(2).putShort((short) 0).array();
		byte[] item = ByteBuffer.allocate(2).putShort((short) 1).array();// VERIFICAR
		byte[] lengthOfNextItem = ByteBuffer.allocate(4).putInt(0).array();// VERIFICAR
		byte[] serializedPointerThing = ByteBuffer.allocate(4).putInt(0).array();

		packet.appendLast(action);
		packet.appendLast(item);
		packet.appendLast(lengthOfNextItem);
		packet.appendLast(serializedPointerThing);
	}

	private void printCoordinate(Coordinate coordinate) {
		List<Float> position = coordinate.getPosition();
		List<Float> angle = coordinate.getAngle();
		List<Float> speed = coordinate.getSpeed();
		System.out.println(String.format("posição: %s %s %s", position.get(0), position.get(1), position.get(2)));
		System.out.println(String.format("velocidade: %s %s %s", speed.get(0), speed.get(1), speed.get(2)));
		System.out.println(String.format("angulo: %s %s", angle.get(0), angle.get(1)));
	}

	private void allocateBytes(MinetestPacket packet, Integer value) {
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
