package br.ufsc.ine.models.scratch;

import java.io.IOException;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.scratch.ScratchServer;

public class ScratchClient extends ScratchServer{
	private AbstractMinetest minetest;

	public ScratchClient(AbstractMinetest minetest) throws IOException {
		this.minetest = minetest;
		addCommands();
	}

	private void addCommands() {
		MoveFoward foward = new MoveFoward(minetest, "andar_para_frente");
		TurnLeft left = new TurnLeft(minetest, "girar_para_esquerda");
		TurnRight right = new TurnRight(minetest, "girar_para_direita");
		
		this.addCommand(foward);
		this.addCommand(right);
		this.addCommand(left);
	}
}
