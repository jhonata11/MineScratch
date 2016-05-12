package controllers;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.controllers.ViewController;
import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;
import br.ufsc.ine.scratch.Command;
import br.ufsc.ine.scratch.Scratch;

public class TestController {

	private ViewController controller;

	@Before
	public void setUp() {
		controller = new ViewController();
	}

	@Test(expected = HostFormatException.class)
	public void testControllerBadHostIP() throws Exception {
		controller.verifyArguments("192.168.0", "30000", "username", "password");
	}

	@Test(expected = PortFormatException.class)
	public void testControllerLetterPort() throws Exception {
		controller.verifyArguments("192.168.0.14", "bad port", "username", "password");
	}

	@Test(expected = UsernameLimitExcededException.class)
	public void testControllerUsernameOutOfLimit() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "012345678901234567890", "password");
	}

	@Test(expected = PasswordLimitExcededException.class)
	public void testControllerPasswordOutOfLimit() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "username", "012345678901234567890");
	}

	@Test
	public void testControllerCorrectParameters() throws Exception {
		controller.verifyArguments("192.168.0.14", "30000", "jhonata", "camisa");
	}

	// @Ignore
	@Test
	public void testConnection() throws Exception {
		class ScratchClient extends Scratch {
		}
		controller = new ViewController();
		Command andarParaFrente = new Command() {

			@Override
			public void execute(String param) {
				controller.getController().walk(Integer.parseInt(param));
			}
		};

		Command girarParaDireita = new Command() {
			@Override
			public void execute(String param) {
				controller.getController().turnRight(Integer.parseInt(param));
				System.out.println("girar para direita: " + param);
			}
		};
		ScratchClient scratch = new ScratchClient();

		scratch.addCommand("andar_para_frente", andarParaFrente);
		scratch.addCommand("girar_para_direita", girarParaDireita);
		controller.connectToMinetest("192.168.0.13", "30000", "JAVA", "senha", scratch);
	}

}
