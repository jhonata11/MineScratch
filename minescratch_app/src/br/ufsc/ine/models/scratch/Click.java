package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.scratch.Instruction;

public class Click extends Instruction{

	public Click(AbstractMinetest minetest, String command) {
		super(minetest, command);
	}

	@Override
	public void executeAction() {
		try {
			minetest.executeCommand("click", new Parameter(null, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
