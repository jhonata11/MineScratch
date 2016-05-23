package br.ufsc.ine.models.minetest;

import java.util.List;
import java.util.Random;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.commands.TurnLeft;
import br.ufsc.ine.minetest.commands.TurnRight;
import br.ufsc.ine.minetest.commands.Walk;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.models.Position;

public class MinetestClient extends AbstractMinetest {

	private Position[] positions;

	public MinetestClient(String host, Integer port, String username, String password) throws Exception {
		super(host, port, username, password);

		addPositions();
		selectPlayerPosition();
		teleport();

		this.addCommand("andar_para_frente", new Walk(this));
		this.addCommand("girar_para_direita", new TurnRight(this));
		this.addCommand("girar_para_esquerda", new TurnLeft(this));
	}

	private void teleport() throws Exception {
		List<Float> selectecPosition = selectPlayerPosition();
		Float x = selectecPosition.get(0);
		Float y = selectecPosition.get(1);
		Float z = selectecPosition.get(2);

		Coordinate coordinate = new Coordinate();
		coordinate.setPosition(x, y, z);
		coordinate.setAngle(new Float(0), new Float(0));
		coordinate.setSpeed(new Float(0), new Float(0), new Float(0));

		this.executeCommand("teleport", new Parameter(Coordinate.class, coordinate));
	}

	private List<Float> selectPlayerPosition() {
		Random generator = new Random();
		int index = generator.nextInt(positions.length - 1);
		return positions[index].asList();
	}

	private void addPositions() {
		positions = new Position[7];
		Position position1 = new Position();
		Position position2 = new Position();
		Position position3 = new Position();
		Position position4 = new Position();
		Position position5 = new Position();
		Position position6 = new Position();
		Position position7 = new Position();

		position1.setPosition(new Float(-43.1), new Float(61.5), new Float(34.1));
		position2.setPosition(new Float(-57.9), new Float(61.5), new Float(31.0));
		position3.setPosition(new Float(-42.9), new Float(61.5), new Float(33.9));
		position4.setPosition(new Float(29.4), new Float(61.5), new Float(32.9));
		position5.setPosition(new Float(27.9), new Float(61.5), new Float(16.0));
		position6.setPosition(new Float(28.9), new Float(61.5), new Float(0.5));
		position7.setPosition(new Float(42.7), new Float(61.5), new Float(-0.1));

		positions[0] = position1;
		positions[1] = position2;
		positions[2] = position3;
		positions[3] = position4;
		positions[4] = position5;
		positions[5] = position6;
		positions[6] = position7;
	}
}
