package br.ufsc.ine.client;

import java.util.Arrays;

import br.ufsc.ine.models.MinetestPacket;
import br.ufsc.ine.models.PacketBuilder;

public class MinetestProtocol {

	private static final short SEQNUM_INITIAL = (short) 65500;
	private short seqNum;
	private PacketBuilder packetBuilder;

	private String host;
	private int port;
	private String username;
	private String password;


	public MinetestProtocol(String host, int port, String username, String password) throws InterruptedException {
		this.seqNum = SEQNUM_INITIAL;
		packetBuilder = new PacketBuilder();
		
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Starts the handshake with the MinetestServer
	 */
	public void startHandshake() {
		MinetestPacket packet = new MinetestPacket();
		byte[] initialHandshakeBytes = packetBuilder.createHandshakePacket(this.username, this.password);
		packet.addToBodyStart(initialHandshakeBytes);
		this.sendCommand(packet);
	}

	/**
	 * Sends a command to the MinetestServer with a given message
	 * @param packet the message to be sent to the MinetestServer
	 */
	public void sendCommand(MinetestPacket packet) {
		packet.addToBodyStart(packetBuilder.createCommandByte());
		this.sendReliable(packet);
	}

	private void sendReliable(MinetestPacket packet) {
		packet.addToBodyStart(packetBuilder.createReliableBytes(this.seqNum));
		this.seqNum++;
		this.send(packet);
	}

	private void send(MinetestPacket packet) {
		packet.addToHeader(packetBuilder.createHeader());
		byte[] sendData = packet.converToMessage();
		System.out.println(Arrays.toString(sendData));
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
