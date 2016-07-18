package utils;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufsc.ine.minetest.models.Properties;
import br.ufsc.ine.minetest.models.PropertiesReader;


public class TestJson {
	
	@Test
	public void testReadJson() throws Exception {
		PropertiesReader props = new PropertiesReader("./properties.json");
		Properties properties = props.readProperties();
		
		assertEquals(7, properties.getInitials().size());
		assertEquals(4, properties.getAllowed().size());

	}

}