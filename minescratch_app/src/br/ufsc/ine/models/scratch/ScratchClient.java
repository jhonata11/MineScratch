package br.ufsc.ine.models.scratch;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.scratch.Scratch;

public class ScratchClient extends Scratch{

		public ScratchClient(AbstractMinetest minetest) {
			this.addInstruction("andar_para_frente", new MoveFoward(minetest));
			this.addInstruction("girar_para_direita", new TurnRight(minetest));
			this.addInstruction("girar_para_esquerda", new TurnLeft(minetest));
		}
}
