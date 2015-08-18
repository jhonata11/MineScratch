package controllers;


import org.junit.Test;

import br.ufsc.ine.controllers.JMinetestController;
import br.ufsc.ine.exceptions.HostFormatException;
import br.ufsc.ine.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.exceptions.PortFormatException;
import br.ufsc.ine.exceptions.UsernameLimitExcededException;

public class TestJMinetestController {
	
	@Test (expected = HostFormatException.class)
	public void testControllerBadHostIP() throws Exception {
		JMinetestController controller = new JMinetestController();
		controller.init("192.168.0", "30000", "username", "password");
	}
	
	@Test (expected = PortFormatException.class)
	public void testControllerLetterPort() throws Exception {
		JMinetestController controller = new JMinetestController();
		controller.init("192.168.0.14", "bad port", "username", "password");
	}
	
	@Test (expected = UsernameLimitExcededException.class)
	public void testControllerUsernameOutOfLimit() throws Exception {
		JMinetestController controller = new JMinetestController();
		controller.init("192.168.0.14", "30000", "012345678901234567890", "password");
	}
	
	@Test (expected = PasswordLimitExcededException.class)
	public void testControllerPasswordOutOfLimit() throws Exception {
		JMinetestController controller = new JMinetestController();
		controller.init("192.168.0.14", "30000", "username", "012345678901234567890");
	}
}
