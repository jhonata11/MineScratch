package minetest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.controllers.Controller;
import br.ufsc.ine.minetest.MinetestProtocol;
import br.ufsc.ine.minetest.Receiver;

public class TestMinetestProtocol {
	private MinetestProtocol minetestProtocol;

	@Before
	public void setUp() throws InterruptedException {
		minetestProtocol = new MinetestProtocol("192.168.0.14", 30000, "jhonata11", "senha");
	}
	
	@Test
	public void initializationTest() throws Exception {
		minetestProtocol = new MinetestProtocol("192.168.0.14", 30000, "jhonata11", "senha");
		
		assertEquals("192.168.0.14", minetestProtocol.getHost());
		assertEquals(30000, minetestProtocol.getPort());
		assertEquals("jhonata11", minetestProtocol.getUsername());
		assertEquals("senha", minetestProtocol.getPassword());
	}
	
	
	@Test
	public void sendPacket() throws Exception {
		minetestProtocol.startHandshake();
		minetestProtocol.startReliableConnection();

		new Receiver(minetestProtocol).listen(30000);
	}

	

}
