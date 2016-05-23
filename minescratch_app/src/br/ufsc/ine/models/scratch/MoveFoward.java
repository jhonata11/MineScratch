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
			Integer steps = Integer.parseInt(param);
			minetest.executeCommand("send_chat", new Parameter(String.class, String.format("andou %s passos", steps)));
			minetest.executeCommand("andar_para_frente", new Parameter(Integer.class, steps));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
