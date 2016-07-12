package scratch;

import java.io.IOException;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.scratch.Instruction;
import br.ufsc.ine.scratch.ScratchServer;

public class TestScratchClient {

	
	public static void main(String[] args) throws IOException{
	   ScratchServer server = new ConcreteScratch();
	   MoveFoward foward = new MoveFoward(null, "andar_para_frente");
	   server.addCommand(foward);
	   
	   server.listen();
	}
	
}

class ConcreteScratch extends ScratchServer{

	public ConcreteScratch() throws IOException {
		super();
	}
	
}

class MoveFoward extends Instruction {
	public MoveFoward(AbstractMinetest minetest, String command) {
		super(minetest, command);
	}

	@Override
	public void executeAction(String param) {
		System.out.println("teste " +param);
		
	}

}
