package client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.client.MinetestProtocol;

public class TestProtocol {
	private MinetestProtocol minetestProtocol;

	@Before
	public void setUp() throws InterruptedException {
		minetestProtocol = new MinetestProtocol("192.168.0.14", 30000, "jhonata11", "senhaQualquer");
	}
	
	@Test
	public void initializationTest() throws Exception {
		minetestProtocol = new MinetestProtocol("192.168.0.14", 30000, "jhonata11", "senhaQualquer");
		
		assertEquals("192.168.0.14", minetestProtocol.getHost());
		assertEquals(30000, minetestProtocol.getPort());
		assertEquals("jhonata11", minetestProtocol.getUsername());
		assertEquals("senhaQualquer", minetestProtocol.getPassword());
	}
	
	

}
