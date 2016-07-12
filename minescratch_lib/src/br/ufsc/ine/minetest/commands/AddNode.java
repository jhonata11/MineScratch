package br.ufsc.ine.minetest.commands;


import java.nio.ByteBuffer;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.network.MinetestPacket;
import br.ufsc.ine.utils.Utils;

public class AddNode extends Command{

	public AddNode(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		
		MinetestPacket packet = this.createPacket((short) 0x39);
		packet.appendLast(ByteBuffer.allocate(1).put((byte) 3).array());
		packet.appendLast(ByteBuffer.allocate(2).putShort((short) 1).array());
		packet.appendLast(ByteBuffer.allocate(4).putInt(0x0e).array());
		
		
		byte [] a = ByteBuffer.allocate(2).putShort((short)0x01).array();
		byte [] b = ByteBuffer.allocate(2).putShort((short)0xffd5).array();
		byte [] c = ByteBuffer.allocate(2).putShort((short)0x003d).array();
		byte [] d = ByteBuffer.allocate(2).putShort((short)0x0020).array();
		byte [] e = ByteBuffer.allocate(2).putShort((short)0xffd5).array();
		byte [] f = ByteBuffer.allocate(2).putShort((short)0x003e).array();
		byte [] h = ByteBuffer.allocate(2).putShort((short)0x0020).array();
		
		byte [] pointedThing = Utils.concatenateBytes(a, b, c, d, e, f,  h);
		packet.appendLast(pointedThing);
		
		System.err.println(packet.bytesToHex());
		
		minetest.sendCommand(packet);
		
		//00 39 03 00 01 00 00 00 0E 00 00 00 01 00 00 FF D5 00 00 00 3D 00 00 00 20 00 00 FF D5 00 00 00 3E 00 00 00 3D 00 00 00 20
		//00 39 03 00 01 00 00 00 0E 00 01 FF D5 00 00 00 3D 00 00 00 20 00 00 FF D5 00 00 00 3E 00 00 00 3D 00 00 00 20 00 00
		//00 39 03 00 01 00 00 00 0E 00 01 FF D5 00 3D 00 20 FF D5 00 3E 00 3D 00 20
		
	}

}
