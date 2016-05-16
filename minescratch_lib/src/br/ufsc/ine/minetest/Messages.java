package br.ufsc.ine.minetest;

public class Messages {
	
	public static final int PROTOCOL_ID = 0x4F457403;

	// Packet types.
	public static final byte CONTROL = 0x00;
	public static final byte ORIGINAL = 0x01;
	 public static final short SPLIT = 0x02;
	public static final byte RELIABLE = 0x03;
	
	// Needed. Don't know why know why
	public static final byte SER_FMT_VER_HIGHEST_READ = 0x1A;
	
	
	// Supported protocol versions lifted from official client.
	public static final short MIN_SUPPORTED_PROTOCOL = 0x0d;
	public static final short MAX_SUPPORTED_PROTOCOL = 0x16;

	// Types of CONTROL packets.
	public static final byte CONTROLTYPE_ACK = 0x00;
	public static final byte CONTROLTYPE_PING = 0x02;
	public static final byte CONTROLTYPE_SET_PEER_ID = 0x01;
	
	// Server -> Client command ids
	public static final short TOCLIENT_CHAT_MESSAGE = 0x30;
	public static final short TOCLIENT_MOVE_PLAYER = 0x34;

	// Client -> Server command ids.
	public static final short TOSERVER_INIT = 0x10;
	public static final short TOSERVER_CHAT_MESSAGE = 0x32;
	public static final short TOSERVER_PLAYER_POSITION = 0x23;
	public static final short TOSERVER_PLAYERITEM = 0x37;
	public static final short TOSERVER_INTERACT = 0x39;
}
