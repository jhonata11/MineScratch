package controllers;



import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.controllers.Controller;
import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;
import br.ufsc.ine.utils.Utils;

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
		controller.connectToMinetest("192.168.0.14", "30000", "jhonata11", "senha");
	}
	
	@Test
	public void segundoPacoteEnviado() throws Exception {
		byte[] a = {79, 69, 116, 3, 0, 3, 0, 0, 0, -1, -35};
		for (int i = 0; i < a.length; i++) {
			System.out.println(Utils.integerToBinary(a[i]));
		}
		String meu = "0100111101000101011101000000001100000000000000000000000000000011111111111101110100000001";
		String dele = "0100111101000101011101000000001100000000000000000000000000000011111111111101110100000001";

		assertEquals(dele, meu);
	}
	
	@Test
	public void terceiroPacoteEnviado() throws Exception {
		String meu = "0100111101000101011101000000001100000000000000000000000000000000000000001111111111011100";
		String dele = "0100111101000101011101000000001100000000000000000000000000000000000000001111111111011100";

		assertEquals(dele, meu);
	}
	
	@Test
	public void quartoPacoteEnviadoHandshakeEnd() throws Exception {
		String meu = "01001111010001010111010000000011000000000000001100000000000000111111111111011110000000010000000000010001";
		String dele = "01001111010001010111010000000011000000000000001100000000000000111111111111011110000000010000000000010001";

		assertEquals(dele, meu);
	}
	
	@Test
	public void quintoPacoteEnviado() throws Exception {
		String meu = "0100111101000101011101000000001100000000000000110000000000000000000000001111111111011101";
		String dele = "0100111101000101011101000000001100000000000000110000000000000000000000001111111111011101";
		assertEquals(dele, meu);
		
		byte[] encoded =  Charset.forName("UTF-16BE").encode("teste").array();
		String str = new String(encoded, StandardCharsets.UTF_16BE);
		System.out.println(str);
	}
	

}
