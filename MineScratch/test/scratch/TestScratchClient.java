package scratch;

import org.junit.Ignore;
import org.junit.Test;

import br.ufsc.ine.scratch.Instruction;
import br.ufsc.ine.scratch.Scratch;

public class TestScratchClient {
	class ScratchClient extends Scratch {
	}

	private Instruction girarParaDireita;
	private Instruction andarParaFrente;
	private ScratchClient scratch;

	@Test
	public void setUp() throws Exception {
		andarParaFrente = new Instruction() {
			@Override
			public void execute(String param) {
				System.out.println("andar para frente: " + param);
			}
		};

		girarParaDireita = new Instruction() {
			@Override
			public void execute(String param) {
				System.out.println("girar para direita: " + param);
			}
		};
		scratch = new ScratchClient();
	}

	@Ignore

	@Test
	public void testScratchConnections() throws Exception {
		scratch.addInstruction("andar_para_frente", andarParaFrente);
		scratch.addInstruction("girar_para_direita", girarParaDireita);
		scratch.run();
	}
}
