package scratch;

import static org.junit.Assert.*;

import java.net.ServerSocket;

import org.junit.Test;

import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.scratch.Client;

public class TestScratchClient {
	
	@Test
	public void testScratchConnection() throws Exception {
		Client client = new Client(new Sender(null, 0, null));
		int port = 50210;
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Servidor iniciado na porta : " + port);
		while(true){
			client.listen(serverSocket);
		}
	}

}
