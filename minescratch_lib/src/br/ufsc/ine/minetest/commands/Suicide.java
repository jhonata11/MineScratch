package br.ufsc.ine.minetest.commands;

import java.nio.ByteBuffer;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.network.MinetestPacket;

public class Suicide extends Command{

	public Suicide(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		MinetestPacket packet = this.createPacket((short)0x35);
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 19).array());
		System.err.println("machucou");
		minetest.sendCommand(packet);
	}

}
