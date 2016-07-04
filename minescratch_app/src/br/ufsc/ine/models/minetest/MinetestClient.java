package br.ufsc.ine.models.minetest;

import java.util.Random;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.commands.RightClick;
import br.ufsc.ine.minetest.commands.Turn;
import br.ufsc.ine.minetest.commands.Walk;

public class MinetestClient extends AbstractMinetest {


	public MinetestClient(String host, Integer port, String username, String password) throws Exception {
		super(host, port, username, password);


		this.addCommand("andar_para_frente", new Walk(this));
		this.addCommand("girar", new Turn(this));
		this.addCommand("click", new RightClick(this));
	}

	public void teleport() throws Exception {
		double[] position = selectPlayerPosition();
		System.err.println(String.format("Posição inicial: %s %s %s", position[0], position[1], position[2]));
		String coordinate = String.format("/teleport %s %s %s", position[0], position[1], position[2]);
		
		Thread.sleep(500);
		this.executeCommand("send_chat", new Parameter(String.class, coordinate));
	}
	

	private double[] selectPlayerPosition() {
		double[][] positions = addPositions();
		Random generator = new Random();
		int index = generator.nextInt(positions.length - 1);
		return  positions[index];
	}

	private double[][] addPositions() {
		double [][] position = { 
				{-43.1, 61.5, 34.1} ,
				{-57.9, 61.5, 31.0},
				{-42.9, 61.5, 33.9},
				{-29.4, 61.5, 32.9},
				{-27.9, 61.5, 16.0},
				{-28.9, 61.5, 0.5},
				{-42.7, 61.5, -0.1}
			};
		return position;
	}

	public Thread getThread() {
		// TODO Auto-generated method stub
		return null;
	}
}
