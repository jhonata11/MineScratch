package br.ufsc.ine.minetest.commands;

import java.nio.ByteBuffer;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Messages;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.network.MinetestPacket;

public class RightClick extends Command {

	public RightClick(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		/*
		 * 1- 00 0000 0000 000e 0001 ffd5 003e 0010 ffd5 003f 0010

		 */
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_INTERACT);
		packet.appendLast(ByteBuffer.allocate(1).put((byte) 0x00).array());
		
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x0000).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x0000).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x000e).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x0001).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0xffd5).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x003e).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x0010).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0xffd5).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x003f).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 0x0010).array());

		
		System.err.println(packet.bytesToHex());
		minetest.sendCommand(packet);
	}
}
