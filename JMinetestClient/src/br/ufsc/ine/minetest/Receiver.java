package br.ufsc.ine.minetest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.utils.Utils;

public class Receiver {
	
	private MinetestProtocol minetest;

	public Receiver(MinetestProtocol minetest) {
		this.minetest = minetest;
	}

	public void listen(int port) throws Exception {
		this.receiveAndProcess(port);
	}

	private void receiveAndProcess(int port) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] initial = new byte[1024];
		DatagramPacket packet = new DatagramPacket(initial, initial.length);

		serverSocket.receive(packet);
		
		byte[] data = new byte[packet.getLength()];
		System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
		
		System.out.println("RECEIVED: " + Arrays.toString(data));
//		byte[] header = ArrayUtils.subarray(data, 0, 7);
//		int protocoID = Utils.byteToInt(ArrayUtils.subarray(header, 0, 4));
//		short headerPeerID = Utils.byteToShort(ArrayUtils.subarray(header, 4, 6));
//		byte headerChannelID = ArrayUtils.subarray(header, 6, 7)[0];
		byte[] conteudo = ArrayUtils.subarray(data, 7, data.length);
		
		serverSocket.close();
		this.processPacket(conteudo);

	}

	private void processPacket(byte[] packet) throws Exception {
		byte tipo = packet[0];
		byte[] data = ArrayUtils.subarray(packet, 1, packet.length);
		
		
		if(tipo == 3){
			byte[] seq = ArrayUtils.subarray(data, 0, 2);
			short seqnum = Utils.byteToShort(seq);
			this.minetest.ack(seqnum);
			System.out.println("enviado depois do ack: "+ Arrays.toString(ArrayUtils.subarray(data, 2, data.length)));
			
			this.processPacket(ArrayUtils.subarray(data, 2, data.length));
		} else if(tipo == 0){
			byte tipoDeControle = data[0];
			short valor = Utils.byteToShort(ArrayUtils.subarray(data,0,3));
			if(tipoDeControle == 0 ){
				this.minetest.setAcked(valor);
			} else if(tipoDeControle == 1){
				minetest.setPeedId(valor);
				minetest.handShakeEnd();
			}
		}
	}
}