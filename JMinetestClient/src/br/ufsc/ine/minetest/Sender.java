package br.ufsc.ine.minetest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import br.ufsc.ine.models.MinetestPacket;
import br.ufsc.ine.models.PacketBuilder;

public class Sender {

	private static final short SEQNUM_INITIAL = (short) 65500;
	private short seqNum;
	private PacketBuilder packetBuilder;

	private String host;
	private int port;
	private short acked;


	public Sender(String host, int port) throws InterruptedException {
		this.seqNum = SEQNUM_INITIAL;
		packetBuilder = new PacketBuilder(this);
		
		this.host = host;
		this.port = port;
	}

	/**
	 * Starts the handshake with the MinetestServer
	 * @param username 
	 * @param password 
	 * @throws Exception 
	 */
	public void startHandshake(String username, String password) throws Exception {
		MinetestPacket packet = new MinetestPacket();
		byte[] initialHandshakeBytes = packetBuilder.createHandshakePacket(username, password);
		packet.addToBodyStart(initialHandshakeBytes);
		this.sendCommand(packet);
	}

	public void handShakeEnd() throws Exception {
		byte[] toServerInit2 = ByteBuffer.allocate(2).putShort((short) 0x11).array();
		MinetestPacket a = new MinetestPacket();
		a.addToBodyStart(toServerInit2);
		this.sendCommand(a);
	}
	
	public void startReliableConnection() throws Exception{
		this.sendCommand(new MinetestPacket());
	}
	
	public void ack(short seqnum) throws Exception{
		MinetestPacket packet = new MinetestPacket();
		packet.addToBodyEnd(packetBuilder.createAckPackage(seqnum));
		this.send(packet);
	}

	/**
	 * Sends a command to the MinetestServer with a given message
	 * @param packet the message to be sent to the MinetestServer
	 * @throws Exception 
	 */
	public void sendCommand(MinetestPacket packet) throws Exception {
		packet.addToBodyStart(packetBuilder.createCommandByte());
		this.sendReliable(packet);
	}

	private void sendReliable(MinetestPacket packet) throws Exception {
		packet.addToBodyStart(packetBuilder.createReliableBytes(this.seqNum));
		this.seqNum++;
		this.send(packet);
	}

	private void send(MinetestPacket packet) throws Exception {
		packet.addToHeader(packetBuilder.createHeader());
		byte[] sendData = packet.converToMessage();
        sendDataToServer(sendData);
//		for (byte b : sendData) {
////		    System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1) + ": " + new Integer(b));
//		    System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
//
//		}
	}

	
	private void sendDataToServer(byte[] sendData) throws Exception {
        InetAddress address = InetAddress.getByName(this.host);
        DatagramPacket packet3 = new DatagramPacket(sendData, sendData.length, address, this.port);
        DatagramSocket datagramSocket = new DatagramSocket(this.port);
        datagramSocket.send(packet3);
        datagramSocket.close();
        
		System.out.println("SENT: " + Arrays.toString(sendData));
	}

	public void setAcked(short b) {
		this.acked = b;
	}

	public void setPeedId(short valor) {
		this.packetBuilder.setPeerId(valor);
	}
}
