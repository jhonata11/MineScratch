package br.ufsc.ine.minetest.commands;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.models.Coordinate;

public class TurnLeft  extends Command{

	public TurnLeft(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		Float degrees = (Float) param.getParameter();
		
		Coordinate newCoordinate = new Coordinate();

		newCoordinate.setPosition(minetest.getCharacter().getPosition().get(0), minetest.getCharacter().getPosition().get(1), minetest.getCharacter().getPosition().get(2));
		newCoordinate.setAngle(minetest.getCharacter().getAngle().get(0), minetest.getCharacter().getAngle().get(1) + (degrees * -1));
		minetest.executeCommand("teleport", new Parameter(Coordinate.class, newCoordinate));		
	}

}
