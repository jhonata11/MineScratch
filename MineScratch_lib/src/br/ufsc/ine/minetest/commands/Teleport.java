package br.ufsc.ine.minetest.commands;

import java.nio.ByteBuffer;
import java.util.List;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Messages;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.models.Coordinate;
import br.ufsc.ine.minetest.network.MinetestPacket;

public class Teleport extends Command{
	public Teleport(AbstractMinetest minetest) {
		super(minetest);
	}

	@Override
	public void execute(Parameter param) throws Exception {
		Coordinate coordinate = (Coordinate) param.getParameter();
		
		MinetestPacket packet = this.createPacket(Messages.TOSERVER_PLAYER_POSITION);
		List<Float> position = coordinate.getPosition();
		List<Float> speed = coordinate.getSpeed();
		List<Float> angle = coordinate.getAngle();

		position.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 1000)));
		speed.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 100)));
		angle.forEach((number) -> allocateBytes(packet, new Integer(number.intValue() * 100)));

		minetest.getCharacter().setPosition(position.get(0), position.get(1), position.get(2));
		minetest.getCharacter().setSpeed(speed.get(0), speed.get(1), speed.get(2));
		minetest.getCharacter().setAngle(angle.get(0), angle.get(1));

		packet.appendLast(ByteBuffer.allocate(4).putInt(0x01).array());
		minetest.sendCommand(packet);
	}


	
	private void allocateBytes(MinetestPacket packet, Integer value) {
		byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
		packet.appendLast(bytes);
	}
	
	//USAR APENAS EM DESENVOLVIMENTO
	private void printCoordinate(Coordinate coordinate) {
		List<Float> position = coordinate.getPosition();
		List<Float> angle = coordinate.getAngle();
		List<Float> speed = coordinate.getSpeed();
		System.out.println(String.format("posição: %s %s %s", position.get(0), position.get(1), position.get(2)));
		System.out.println(String.format("velocidade: %s %s %s", speed.get(0), speed.get(1), speed.get(2)));
		System.out.println(String.format("angulo: %s %s", angle.get(0), angle.get(1)));
	}
}
