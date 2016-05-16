package minetest;

import org.junit.Before;
import org.junit.Test;

import br.ufsc.ine.minetest.AbstractMinetest;
import br.ufsc.ine.minetest.Command;
import br.ufsc.ine.minetest.Parameter;
import br.ufsc.ine.minetest.commands.SendChat;
import br.ufsc.ine.minetest.commands.Teleport;
import br.ufsc.ine.minetest.commands.Walk;
import br.ufsc.ine.scratch.Instruction;
import br.ufsc.ine.scratch.Scratch;

public class TestController {

	private Instruction andarParaFrente;
	private Instruction girarParaDireita;
	private ScratchClient scratch;
	private Command comandoFalar;
	private MinetestClient minetest;

	@Before
	public void setUp() throws InterruptedException {
		minetest = new MinetestClient("192.168.0.13", 30000, "JAVA", "senha");
		girarParaDireita = new Instruction() {
			@Override
			public void execute(String param) {
				try {
					Parameter parametro = new Parameter(String.class, "TESTE");
					minetest.executeCommand("girar_para_direita", parametro);
				} catch (Exception e) {}
			}
		};
		andarParaFrente = new Instruction() {
			@Override
			public void execute(String param) {
				Parameter distance = new Parameter(Integer.class, Integer.parseInt(param));
				try {
					minetest.executeCommand("andar_para_frente", distance);
				} catch (Exception e) {}
			}
		};
		
		//Instruções Scratch
		scratch.addInstruction("girar_para_direita", girarParaDireita);
		scratch.addInstruction("andar_para_frente", andarParaFrente);
		minetest.setScratch(scratch);
		
		//Minetest Commands
		comandoFalar = new SendChat(minetest);
		minetest.addCommand("girar_para_direita", comandoFalar);
		minetest.addCommand("andar_para_frente", new Walk(minetest));
		minetest.addCommand("teleport", new Teleport(minetest));
		scratch = new ScratchClient();
		scratch.addInstruction("andar_para_frente", andarParaFrente);
		scratch.addInstruction("girar_para_direita", girarParaDireita);
		

	}



	
	@Test
	public void testMinetest() throws Exception {
		Thread minetestThread = new Thread(minetest);
		minetestThread.run();
	}

}


class MinetestClient extends AbstractMinetest{
	public MinetestClient(String host, Integer port, String username, String password) throws InterruptedException {
		super(host, port, username, password);
	}
}

class ScratchClient extends Scratch {
	
}