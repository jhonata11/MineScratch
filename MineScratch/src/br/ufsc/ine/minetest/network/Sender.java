package br.ufsc.ine.minetest.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

import br.ufsc.ine.minetest.MinetestProtocol;

public class Sender{

	private static final short SEQNUM_INITIAL = (short) 65500;
	private short seqNum;
	private MinetestProtocol minetestProtocol;

	private String host;
	private int port;
	private short acked;
	
	int count = 0;
	private Semaphore semaphore;


	public Sender(String host, int port, Semaphore semaphore) throws InterruptedException {
		this.semaphore = semaphore;
		this.seqNum = SEQNUM_INITIAL;
		minetestProtocol = new MinetestProtocol(this);
		
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
		byte[] initialHandshakeBytes = minetestProtocol.createHandshakePacket(username, password);
		packet.appendFirst(initialHandshakeBytes);
		this.sendCommand(packet);
	}

	public void handShakeEnd() throws Exception {
		byte[] toServerInit2 = ByteBuffer.allocate(2).putShort((short) 0x11).array();
		MinetestPacket a = new MinetestPacket();
		a.appendFirst(toServerInit2);
		
		this.sendCommand(a);
	}
	
	public void disconnect() throws Exception{
		MinetestPacket packet = new MinetestPacket();
		packet.appendFirst(this.minetestProtocol.disconnect());
		this.send(packet);
	}
	
	public void startReliableConnection() throws Exception{
		this.sendCommand(new MinetestPacket());
	}
	
	public void ack(short seqnum) throws Exception{
		MinetestPacket packet = new MinetestPacket();
		packet.appendLast(minetestProtocol.createAckPackage(seqnum));
		this.send(packet);
	}

	/**
	 * Sends a command to the MinetestServer with a given message
	 * @param packet the message to be sent to the MinetestServer
	 * @throws Exception 
	 */
	public void sendCommand(MinetestPacket packet) throws Exception {
		packet.appendFirst(minetestProtocol.createCommandByte());
		this.sendReliable(packet);
	}

	private void sendReliable(MinetestPacket packet) throws Exception {
		packet.appendFirst(minetestProtocol.createReliableBytes(this.seqNum));
		this.seqNum++;
		this.send(packet);
	}

	private void send(MinetestPacket packet) throws Exception {
		packet.addToHeader(minetestProtocol.createHeader());
		byte[] sendData = packet.converToMessage();
        sendDataToServer(sendData);
	}

	
	private void sendDataToServer(byte[] sendData) throws Exception {
		semaphore.acquire();
        InetAddress address = InetAddress.getByName(this.host);
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, this.port);
        DatagramSocket datagramSocket = new DatagramSocket(this.port);
        datagramSocket.send(packet);
        datagramSocket.close();
        
		count++;
		semaphore.release();
	}

	public void setAcked(short b) {
		this.acked = b;
	}

	/**
	 * @return the minetestProtocol
	 */
	public MinetestProtocol getMinetestProtocol() {
		return minetestProtocol;
	}

	/**
	 * @param minetestProtocol the minetestProtocol to set
	 */
	public void setMinetestProtocol(MinetestProtocol minetestProtocol) {
		this.minetestProtocol = minetestProtocol;
	}
	
	public Semaphore getSemaphore(){
		return this.semaphore;
	}
}
