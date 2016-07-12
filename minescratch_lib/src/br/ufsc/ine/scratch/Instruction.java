package br.ufsc.ine.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;

public abstract class Instruction extends ScratchHandler{

	protected AbstractMinetest minetest;
	
	public Instruction(AbstractMinetest minetest, String command) {
		this.minetest = minetest;
		this.command = command;
	}
	public String getStringCommand() {
		return command;
	}

	public void setStringCommand(String stringCommand) {
		this.command = stringCommand;
	}
}
