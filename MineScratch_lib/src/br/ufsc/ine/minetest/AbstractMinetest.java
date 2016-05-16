package br.ufsc.ine.minetest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.minetest.commands.SendChat;
import br.ufsc.ine.minetest.commands.Teleport;
import br.ufsc.ine.minetest.models.Character;
import br.ufsc.ine.minetest.network.MinetestPacket;
import br.ufsc.ine.minetest.network.Receiver;
import br.ufsc.ine.minetest.network.Sender;
import br.ufsc.ine.scratch.Scratch;
import br.ufsc.ine.utils.Utils;

public abstract class AbstractMinetest implements Runnable {
	private Sender sender;
	private Receiver receiver;
	private Character character;
	private MinetestConnector connector;
	private Semaphore connectionSemaphore;
	private Scratch scratch;
	private Map<String, Command> commands;

	public AbstractMinetest(String host, Integer port, String username, String password) throws InterruptedException {
		character = new Character();
		character.setName(username);
		connector = new MinetestConnector(host, port, username, password);
		connectionSemaphore = new Semaphore(1);
		sender = new Sender(host, port, connectionSemaphore);
		receiver = new Receiver(sender.getMinetestProtocol(), connectionSemaphore, port);

		this.commands = new HashMap<>();
		this.addCommand("teleport", new Teleport(this));
		this.addCommand("send_chat", new SendChat(this));
	}

	public void addCommand(String key, Command command) {
		this.commands.put(key, command);
	}

	public void executeCommand(String commandKey, Parameter param) throws Exception {
		commands.get(commandKey).execute(param);
	}

	private void startApplication() throws Exception {
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

	public void setScratch(Scratch scratch) {
		this.scratch = scratch;
	}

	private void receiveMessage() throws InterruptedException {
		byte[] receiveCommand = sender.getMinetestProtocol().receiveCommand();
		if (receiveCommand != null && receiveCommand.length >= 2) {
			short tipo = Utils.byteToShort(ArrayUtils.subarray(receiveCommand, 0, 2));
			byte[] data = ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length);

			if (tipo == Messages.TOCLIENT_CHAT_MESSAGE) {
				String str = new String(data, StandardCharsets.UTF_16BE);
				System.out.println(str);
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

	@Override
	public void run() {
		try {
			startApplication();
		} catch (Exception e) {
		}
	}
	
	public void sendCommand(MinetestPacket packet) throws Exception {
		this.sender.sendCommand(packet);
	}

	public Character getCharacter() {
		return character;
	}
}
