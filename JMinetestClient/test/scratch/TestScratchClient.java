package scratch;

import java.net.ServerSocket;

import org.junit.Ignore;
import org.junit.Test;

import br.ufsc.ine.controllers.MinetestClient;
import br.ufsc.ine.scratch.ScratchClient;

public class TestScratchClient {
	
	@Ignore
	@Test
	public void testScratchConnection() throws Exception {
		ScratchClient client = new ScratchClient(new MinetestClient(null, 0, null, null));
		int port = 50210;
		ServerSocket serverSocket = new ServerSocket(port);
		System.err.println("Servidor iniciado na porta : " + port);
		while(true){
			client.listen(serverSocket);
		}
	}

}
