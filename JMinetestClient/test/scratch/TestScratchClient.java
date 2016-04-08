package scratch;

import static org.junit.Assert.*;

import java.net.ServerSocket;

import org.junit.Ignore;
import org.junit.Test;

import br.ufsc.ine.minetest.Sender;
import br.ufsc.ine.scratch.ScratchClient;

public class TestScratchClient {
	
	@Ignore
	@Test
	public void testScratchConnection() throws Exception {
		ScratchClient client = new ScratchClient(new Sender(null, 0, null));
		int port = 50210;
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Servidor iniciado na porta : " + port);
		while(true){
			client.listen(serverSocket);
		}
	}

}
