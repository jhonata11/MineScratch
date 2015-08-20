package br.ufsc.ine.service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.rmi.CORBA.Util;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.utils.Utils;

public class Receiver {

	public void run(int port) {
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[8];

			System.out.printf("Listening on udp:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), port);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			while (true) {
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("RECEIVED");
				
				byte[] header = ArrayUtils.subarray(receiveData, 0, 7);
				byte[] data = ArrayUtils.subarray(receiveData, 7, receiveData.length);
				
				int protocoID = Utils.byteToInt(ArrayUtils.subarray(header, 0, 4));
				short headerPeerID = Utils.byteToShort(ArrayUtils.subarray(header, 4, 6));
				byte headerChannelID = ArrayUtils.subarray(header, 6, 7)[0];
				
				
				byte tipo = data[0];
				byte[] dados = ArrayUtils.subarray(data, 7, data.length);
				
				if(tipo == 3){
					byte[] seq = ArrayUtils.subarray(header, 0, 2);
					short seqnum = Utils.byteToShort(seq);
				}
				
				// now send acknowledgement packet back to sender
//				InetAddress receivedAddress = receivePacket.getAddress();
//				String sendString = "polo";
//				byte[] sendData = sendString.getBytes("UTF-8");
//				int receivedPort = receivePacket.getPort();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivedAddress, receivedPort);
//				serverSocket.send(sendPacket);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		// should close serverSocket in finally block
	}
}