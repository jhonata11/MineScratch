package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.scratch.Instruction;

public class MoveFoward extends Instruction{
	
	private AbstractMinetest minetest;

	public MoveFoward(AbstractMinetest minetest) {
		this.minetest = minetest;
	}

	@Override
	public void execute(String param) {
		try {
			minetest.executeCommand("andar_para_frente", new Parameter(Integer.class, Integer.parseInt(param)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
