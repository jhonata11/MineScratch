package br.ufsc.ine.minetest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver {
	

	private MinetestProtocol minetestProtocol;

	public Receiver(MinetestProtocol minetest) {
		this.minetestProtocol = minetest;
	}

	public void listen(int port) throws Exception {
		this.receiveAndProcess(port);
	}

	private void receiveAndProcess(int port) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] initial = new byte[1024];
		DatagramPacket packet = new DatagramPacket(initial, initial.length);
		serverSocket.receive(packet);
		byte[] receivedData = new byte[packet.getLength()];

		System.arraycopy(packet.getData(), packet.getOffset(), receivedData, 0, packet.getLength());
//		System.out.println("RECEIVED: " + Arrays.toString(receivedData));
		
		byte[] bodyContent = this.minetestProtocol.receiveAndProcess(receivedData);
		
		serverSocket.close();
		minetestProtocol.processPacket(bodyContent);
	}
}