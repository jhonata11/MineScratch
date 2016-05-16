package br.ufsc.ine.models.minetest;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.commands.TurnLeft;
import br.ufsc.ine.minetest.commands.TurnRight;
import br.ufsc.ine.minetest.commands.Walk;

public class MinetestClient extends AbstractMinetest{
	public MinetestClient(String host, Integer port, String username, String password) throws InterruptedException {
		super(host, port, username, password);
		
		this.addCommand("andar_para_frente", new Walk(this));
		this.addCommand("girar_para_direita", new TurnRight(this));
		this.addCommand("girar_para_esquerda", new TurnLeft(this));
	}
}
