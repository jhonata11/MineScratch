package br.ufsc.ine.minetest.commands;

import java.util.HashMap;
import java.util.List;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.Position;

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
		List<Float> playerPosition = minetest.getCharacter().getPosition();
		Float x = playerPosition.get(0) + deltaPosition.getPosition().get(0);
		Float y = playerPosition.get(1) + deltaPosition.getPosition().get(1);
		Float z = playerPosition.get(2) + deltaPosition.getPosition().get(2);
		Float pitch = minetest.getCharacter().getAngle().get(0) + deltaPosition.getAngle().get(0);
		Float yaw = minetest.getCharacter().getAngle().get(1) + deltaPosition.getAngle().get(1);
		
		
		Position currentPosition = new Position();
		currentPosition.setPosition(playerPosition.get(0), playerPosition.get(1), playerPosition.get(2));
		System.err.println(currentPosition.asList());
		
		Position newPosition = new Position();
		newPosition.setPosition(x, y, z);
		
		HashMap<Position, List<Position>> allowed = this.minetest.getProperties().getAllowed();
		allowed.keySet().forEach(i -> System.err.println(i.asList()));
		
		System.err.println(allowed.containsKey(currentPosition));
		
//		if(allowed.get(currentPosition).contains(newPosition)){
			Coordinate newCoordinate = new Coordinate();
			newCoordinate.setPosition(x, y, z);
			newCoordinate.setAngle(pitch, yaw);
			minetest.executeCommand("teleport", new Parameter(Coordinate.class, newCoordinate));
//		} else {
//			System.err.println("erro");
//		}
		
		
	}

}
