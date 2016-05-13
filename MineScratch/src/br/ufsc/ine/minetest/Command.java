package br.ufsc.ine.minetest;

import java.nio.ByteBuffer;

import br.ufsc.ine.minetest.network.MinetestPacket;

public abstract class Command {
	protected AbstractMinetest minetest;

	public Command(AbstractMinetest minetest) {
		this.minetest = minetest;
	}
	
	public abstract void execute(Parameter param) throws Exception;
	
	
	protected MinetestPacket createPacket(short commandType) {
		byte[] type = ByteBuffer.allocate(2).putShort((short) commandType).array();
		MinetestPacket packet = new MinetestPacket();
		packet.appendFirst(type);
		return packet;
	}
}
