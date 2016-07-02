package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.scratch.Instruction;

public class TurnLeft extends Instruction {

	private AbstractMinetest minetest;

	public TurnLeft(AbstractMinetest minetest) {
		this.minetest = minetest;
	}

	@Override
	public void execute(String param) {
		try {
			Float degrees = Float.parseFloat(param);
			minetest.executeCommand("send_chat", new Parameter(String.class, String.format("girou %s graus", degrees)));
			minetest.executeCommand("girar", new Parameter(Float.class,  degrees));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
