package minetest;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufsc.ine.controllers.ModelController;
import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.minetest.models.Coordinate;

public class TestMinetestController {
	@Test
	public void testTeleport() throws Exception {
		ModelController minetestController = new ModelController(new Sender(null, 0, null));
		Coordinate coordinates = new Coordinate();
		coordinates.setPosition(150, 150, 150);
		minetestController.teleport(coordinates);
	}
	
	@Test
	public void testMessage() throws Exception {
		ModelController minetestController = new ModelController(new Sender(null, 0, null));
		minetestController.sendChatMessage("TESTE");
	}
}
