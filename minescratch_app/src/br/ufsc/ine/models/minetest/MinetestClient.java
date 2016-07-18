package br.ufsc.ine.models.minetest;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.commands.RightClick;
import br.ufsc.ine.minetest.commands.Suicide;
import br.ufsc.ine.minetest.commands.Turn;
import br.ufsc.ine.minetest.commands.Walk;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.Position;
import br.ufsc.ine.minetest.models.PropertiesReader;

public class MinetestClient extends AbstractMinetest {
	
	public MinetestClient(String host, Integer port, String username, String password) throws Exception {
		super(host, port, username, password);
		

		this.addCommand("andar_para_frente", new Walk(this));
		this.addCommand("girar", new Turn(this));
		this.addCommand("click", new RightClick(this));
		this.addCommand("suicide", new Suicide(this));
	}
	
	public void init() throws Exception{
		PropertiesReader propReader = new PropertiesReader("./properties.json");
		this.setProperties(propReader.readProperties());
		
		Random generator = new Random();
		int index = generator.nextInt(getProperties().getInitials().size() - 1);
		Position initialPos = getProperties().getInitials().get(index);
		Coordinate initialCoord = new Coordinate();
		List<Float> posList = initialPos.asList();
		//-28.9, 61.5, 0.5 test only
		posList.set(0, (float) -29);
		posList.set(1, (float) 61.5);
		posList.set(2, (float) 1);
		initialCoord.setPosition(posList.get(0), posList.get(1), posList.get(2));
		String pos = String.format("/teleport %s %s %s", posList.get(0), posList.get(1),posList.get(2));
		
		Thread.sleep(1000);
		this.executeCommand("send_chat", new Parameter(String.class, pos));
		this.executeCommand("teleport", new Parameter(Coordinate.class, initialCoord));
	}

}
