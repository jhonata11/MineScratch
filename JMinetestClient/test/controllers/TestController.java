package controllers;



import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.controllers.Controller;
import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;

public class TestController {
	
	private Controller controller;
	
	@Before
	public void setUp(){
		controller = new Controller();
	}

	@Test (expected = HostFormatException.class)
	public void testControllerBadHostIP() throws Exception {
		controller.verifyArguments("192.168.0", "30000", "username", "password");
	}
	
	@Test (expected = PortFormatException.class)
	public void testControllerLetterPort() throws Exception {
		controller.verifyArguments("192.168.0.14", "bad port", "username", "password");
	}
	
	@Test (expected = UsernameLimitExcededException.class)
	public void testControllerUsernameOutOfLimit() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "012345678901234567890", "password");
	}
	
	@Test (expected = PasswordLimitExcededException.class)
	public void testControllerPasswordOutOfLimit() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "username", "012345678901234567890");
	}
	
	@Test
	public void testControllerCorrectParameters() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "jhonata", "camisa");
	}
	
	@Test
	public void testConnection() throws Exception {
		controller = new Controller();
		controller.startConnection("192.168.0.14", "30000", "jhonata11", "senha");
	}
	

}
