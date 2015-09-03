package scratch;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufsc.ine.scratch.Client;

public class TestScratchClient {
	
	@Test
	public void testScratchConnection() throws Exception {
		Client client = new Client();
		client.listen();
	}

}
