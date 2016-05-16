package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.scratch.Instruction;

public class TurnRight extends Instruction{

	private AbstractMinetest minetest;

	public TurnRight(AbstractMinetest minetest) {
		this.minetest = minetest;
	}

	@Override
	public void execute(String param) {
		try {
			minetest.executeCommand("girar_para_esquerda", new Parameter(Float.class, Float.parseFloat(param)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
