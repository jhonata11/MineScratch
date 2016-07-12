package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import br.ufsc.ine.minetest.models.Position;


public class TestJson {
	
	@Test
	public void testReadJson() throws Exception {
		PropertiesReader props = new PropertiesReader("./properties.json");
		Properties properties = props.readProperties();
		Position currentPosition = new Position();
		currentPosition.setPosition(new Float(-28.9), new Float(61.5), new Float(0.5));
		HashMap<Position, List<Position>> allowed = properties.getAllowed();
		
		assertTrue(allowed.containsKey(currentPosition));
		List<Position> allowedPos = allowed.get(currentPosition);
		Position position = new Position();
		position.setPosition(new Float(-29.9), new Float(61.5), new Float(0.5));
		assertEquals(position, allowedPos.get(0));
	}
	

}