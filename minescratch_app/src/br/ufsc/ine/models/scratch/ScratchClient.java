package br.ufsc.ine.models.scratch;

import java.io.IOException;
import java.util.List;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.scratch.Instruction;
import br.ufsc.ine.scratch.ScratchServer;

public class ScratchClient extends ScratchServer{
	private AbstractMinetest minetest;
	private List<Instruction> instructions;

	public ScratchClient(AbstractMinetest minetest) throws IOException {
		this.minetest = minetest;
		addCommands();
	}

	private void addCommands() {
		MoveFoward foward = new MoveFoward(minetest, "andar_para_frente");
		TurnRight right = new TurnRight(minetest, "girar_para_direita");
		Click click = new Click(minetest, "clicar_na_frente");
		
		this.addCommand(foward);
		this.addCommand(right);
		this.addCommand(click);
	}
	
	public void executeAll(){
		instructions.forEach(instruction -> instruction.executeAction());
	}
}
