package br.ufsc.ine.minetest;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.utils.Utils;

public class MinetestProtocol {


	private static final int PROTOCOL_ID = 0x4F457403;

	// Packet types.
	private static final byte CONTROL = 0x00;
	private static final byte ORIGINAL = 0x01;
	 private static final short SPLIT = 0x02;
	private static final byte RELIABLE = 0x03;
	// Needed. Don't know why know why
	private static final byte SER_FMT_VER_HIGHEST_READ = 0x1A;
	// Client -> Server command ids.
	private static final short TOSERVER_INIT = 0x10;
	// Supported protocol versions lifted from official client.
	private static final short MIN_SUPPORTED_PROTOCOL = 0x0d;
	private static final short MAX_SUPPORTED_PROTOCOL = 0x16;

	// Types of CONTROL packets.
	private static final byte CONTROLTYPE_ACK = 0x00;
	private static final byte CONTROLTYPE_PING = 0x02;
	private static final byte CONTROLTYPE_SET_PEER_ID = 0x01;




	private short peerId = 0;
	private byte channel = 0;
	private Sender sender;
	private Queue<byte[]> receiveBuffer;
	private HashMap<Short, HashMap<Short, byte[]>> splitBuffers;
	
	
	

	public MinetestProtocol(Sender sender) {
		this.sender = sender;
		this.receiveBuffer = new LinkedBlockingQueue<byte[]>();
		this.splitBuffers = new HashMap<>();
	}

	public byte[] createHeader() {
		byte[] protocolId = ByteBuffer.allocate(4).putInt(PROTOCOL_ID).array();
		byte[] headerPeerId = ByteBuffer.allocate(2).putShort(this.peerId).array();
		byte[] headerChannel = ByteBuffer.allocate(1).put(this.channel).array();

		return Utils.concatenateBytes(protocolId, headerPeerId, headerChannel);
	}

	public byte[] createHandshakePacket(String username, String userPassword) {
		byte[] toServerInit = ByteBuffer.allocate(2).putShort(TOSERVER_INIT).array();
		byte[] serFmtVerHighestRead = ByteBuffer.allocate(1).put(SER_FMT_VER_HIGHEST_READ).array();
		byte[] userName = ByteBuffer.allocate(20).put(Charset.forName("UTF-8").encode(username)).array();
		byte[] password = ByteBuffer.allocate(28).put(Charset.forName("UTF-8").encode(userPassword)).array();
		byte[] minSupportedProtocol = ByteBuffer.allocate(2).putShort(MIN_SUPPORTED_PROTOCOL).array();
		byte[] maxSupportedProtocol = ByteBuffer.allocate(2).putShort(MAX_SUPPORTED_PROTOCOL).array();

		byte[] packet = Utils.concatenateBytes(toServerInit, serFmtVerHighestRead, userName, password, minSupportedProtocol, maxSupportedProtocol);
		return packet;
	}

	public byte[] createAckPackage(short seqNum) {
		byte[] control = { CONTROL };
		byte[] controlAck = { CONTROLTYPE_ACK };
		byte[] messageSeqNum = ByteBuffer.allocate(2).putShort((short) (seqNum & 0xFFFF)).array();
		return Utils.concatenateBytes(control, controlAck, messageSeqNum);

	}

	public byte[] createCommandByte() {
		return ByteBuffer.allocate(1).put(ORIGINAL).array();
	}

	public byte[] createReliableBytes(int seqNum) {
		byte[] reliable = ByteBuffer.allocate(1).put(RELIABLE).array();
		byte[] messageSeqNum = ByteBuffer.allocate(2).putShort((short) (seqNum & 0xFFFF)).array();
		return Utils.concatenateBytes(reliable, messageSeqNum);
	}

	public byte[] receiveAndProcess(byte[] receivedData) {
		byte[] header = ArrayUtils.subarray(receivedData, 0, 7);
		
		int protocoID = Utils.byteToInt(ArrayUtils.subarray(header, 0, 4));
		short headerPeerID = Utils.byteToShort(ArrayUtils.subarray(header, 4, 6));
//		byte headerChannelID = ArrayUtils.subarray(header, 6, 7)[0];
		
		assert (protocoID == PROTOCOL_ID): "protocolo inesperado";
		assert (headerPeerID == peerId):  String.format("peer id devia ser 1, e veio %s", peerId);

		byte[] bodyContent = ArrayUtils.subarray(receivedData, 7, receivedData.length);
		return bodyContent;
	}

	public void processPacket(byte[] packet) throws Exception {
		byte type = packet[0];
		byte[] data = ArrayUtils.subarray(packet, 1, packet.length);

//		System.out.println("TYPE: " + type);
		if (type == RELIABLE) {
			reliable(data);
		} else if (type == CONTROL) {
			control(data);
		} else if(type == ORIGINAL){
			this.receiveBuffer.add(data);
		} else if(type == SPLIT){
			split(data);
		}
	}

	private void split(byte[] data) {
		int headerSize = 6;
		byte [] splitHeader = ArrayUtils.subarray(data, 0, headerSize);
		byte [] splitData = ArrayUtils.subarray(data, headerSize, data.length);
		Short seqnumber = new Short(Utils.byteToShort(ArrayUtils.subarray(data, 0, 2)));
		short chunk_count = new Short(Utils.byteToShort(ArrayUtils.subarray(data, 2, 4)));
		short chunk_num = new Short(Utils.byteToShort(ArrayUtils.subarray(data, 4, 6)));
		
		HashMap<Short, byte[]> temp = new HashMap<>();
		temp.put(chunk_num, splitData);
		this.splitBuffers.put(seqnumber, temp);
		
		if(this.splitBuffers.get(seqnumber).containsKey((short) (chunk_count - 1))){
			ArrayList<byte[]> complete = new ArrayList<>();
			for (int i = 0; i < chunk_count; i++) {
				complete.add(this.splitBuffers.get(seqnumber).get(i));
			}
			this.receiveBuffer.add(Utils.listToArray(complete));
			this.splitBuffers.remove(seqnumber);
		}
	}

	private void control(byte[] data) throws Exception {
		byte controlType = data[0];
		
		if(data.length == 1){
			assert(data[0] == CONTROLTYPE_PING): "devia ser CONTROLTYPE_PING";
			return;
		}
		
		short value = Utils.byteToShort(ArrayUtils.subarray(data, 1, 4));
		if (controlType == CONTROLTYPE_ACK) {
			this.sender.setAcked(value);
		} else if (controlType == CONTROLTYPE_SET_PEER_ID) {
			System.out.println("peer ID: " + value);
			this.setPeerId(value);
			sender.handShakeEnd();
		}
	}

	private void reliable(byte[] data) throws Exception {
		byte[] seq = ArrayUtils.subarray(data, 0, 2);
		short seqnum = Utils.byteToShort(seq);
		sender.ack(seqnum);
		this.processPacket(ArrayUtils.subarray(data, 2, data.length));
	}
	
	public byte[] receiveCommand(){
		if(this.receiveBuffer.peek() != null){
			return this.receiveBuffer.poll();
		}
		return null;
	}

	/**
	 * @return the peerId
	 */
	public short getPeerId() {
		return peerId;
	}

	/**
	 * @param peerId the peerId to set
	 */
	public void setPeerId(short peerId) {
		this.peerId = peerId;
	}

	public byte[] disconnect() {
		return ByteBuffer.allocate(2).putShort(RELIABLE).array();
	}
}
