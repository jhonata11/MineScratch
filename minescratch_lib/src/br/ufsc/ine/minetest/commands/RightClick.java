package br.ufsc.ine.minetest.commands;

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
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_INTERACT);
		minetest.sendCommand(packet);
	}
}
