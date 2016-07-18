package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.scratch.Instruction;

public class MoveFoward extends Instruction {
	
	public MoveFoward(AbstractMinetest minetest, String command) {
		super(minetest, command);
	}

	@Override
	public void executeAction() {
		try {
			Integer steps = 1;
			minetest.executeCommand("andar_para_frente", new Parameter(Integer.class, steps));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
