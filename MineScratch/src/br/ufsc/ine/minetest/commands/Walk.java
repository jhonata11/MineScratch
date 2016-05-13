package br.ufsc.ine.minetest.commands;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.models.Coordinate;

public class Walk extends Command{

	public Walk(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		Integer distance = (Integer) param.getParameter();
		Float dx = new Float(distance * Math.cos((90 + minetest.getCharacter().getAngle().get(1)) / 180 * Math.PI));
		Float dz = new Float(distance * Math.sin((90 + minetest.getCharacter().getAngle().get(1)) / 180 * Math.PI));
		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(dx, (float) 0, dz);

		this.move(newCoordinate);
	}
	
	private void move(Coordinate deltaPosition) throws Exception {
		Float x = minetest.getCharacter().getPosition().get(0) + deltaPosition.getPosition().get(0);
		Float y = minetest.getCharacter().getPosition().get(1) + deltaPosition.getPosition().get(1);
		Float z = minetest.getCharacter().getPosition().get(2) + deltaPosition.getPosition().get(2);
		Float pitch = minetest.getCharacter().getAngle().get(0) + deltaPosition.getAngle().get(0);
		Float yaw = minetest.getCharacter().getAngle().get(1) + deltaPosition.getAngle().get(1);

		Coordinate newCoordinate = new Coordinate();
		newCoordinate.setPosition(x, y, z);
		newCoordinate.setAngle(pitch, yaw);
		minetest.executeCommand("teleport", new Parameter(Coordinate.class, newCoordinate));
	}

}
