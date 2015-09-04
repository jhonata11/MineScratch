package minetest;

import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.minetest.Sender;

public class TestSender {
	private Sender sender;

	@Before
	public void setUp() throws InterruptedException {
		this.sender = new Sender("192.168.0.14", 30000, new Semaphore(0));
	}
	
	@Test
	public void test() throws Exception {
		fail("implementar testes");
	}

	

}
