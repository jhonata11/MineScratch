package br.ufsc.ine.models.minetest;

import java.util.Random;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.commands.RightClick;
import br.ufsc.ine.minetest.commands.Turn;
import br.ufsc.ine.minetest.commands.Walk;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.Position;
import utils.PropertiesReader;

public class MinetestClient extends AbstractMinetest {
	

	public MinetestClient(String host, Integer port, String username, String password) throws Exception {
		super(host, port, username, password);


		this.addCommand("andar_para_frente", new Walk(this));
		this.addCommand("girar", new Turn(this));
		this.addCommand("click", new RightClick(this));
	}
	
	public void init() throws Exception{
		PropertiesReader propReader = new PropertiesReader("./properties.json");
		this.setProperties(propReader.readProperties());
		
		Random generator = new Random();
		int index = generator.nextInt(getProperties().getInitials().size() - 1);
		Position initialPos = getProperties().getInitials().get(index);
		Coordinate initialCoord = new Coordinate();
		initialCoord.setPosition(initialPos.asList().get(0), initialPos.asList().get(1), initialPos.asList().get(2));
		
		String pos = String.format("/teleport %s %s %s", initialPos.asList().get(0), initialPos.asList().get(1),initialPos.asList().get(2));
		
		Thread.sleep(1000);
		System.err.println("enviado " + pos);
		this.executeCommand("send_chat", new Parameter(Coordinate.class, pos));
	}

}
