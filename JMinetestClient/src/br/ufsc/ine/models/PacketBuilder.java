package br.ufsc.ine.models;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.ufsc.ine.utils.Utils;

public class PacketBuilder {

	private static final int PROTOCOL_ID = 0x4F457403;

	// Packet types.
//	private static final short CONTROL = 0x00;
	private static final byte ORIGINAL = 0x01;
//	private static final short SPLIT = 0x02;
	private static final byte RELIABLE = 0x03;
	// Needed. Don't know why know why
	private static final byte SER_FMT_VER_HIGHEST_READ = 0x1A;
	// Client -> Server command ids.
	private static final short TOSERVER_INIT = 0x10;
	// Supported protocol versions lifted from official client.
	private static final short MIN_SUPPORTED_PROTOCOL = 0x0d;
	private static final short MAX_SUPPORTED_PROTOCOL = 0x16;

	private short peerId;
	private byte channel;

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
	
	public byte[] createCommandByte(){
		return ByteBuffer.allocate(1).put(ORIGINAL).array();
	}
	
	public byte[] createReliableBytes(int seqNum){
		/* TODO: verificar como mandar este número ocupando um valor
		 * 
		 * O problema é: 
		 *		-> a variavel 'messageSeqNum' deve ocupar apenas 2 bytes, porém o valor inicial para ela é 
		 * 		65500, que não cabe em um short.
		 * Um detalhe interessante é que fazendo:
		 * 		-> (short)this.seqNum & 0xFFFF, ele retorna o valor original do seqNum 
		 * 
		*/
		
		byte[] reliable = ByteBuffer.allocate(1).put(RELIABLE).array();
		byte[] messageSeqNum = ByteBuffer.allocate(2).putShort((short) (seqNum & 0xFFFF)).array();
		return Utils.concatenateBytes(reliable, messageSeqNum);
	}

}
