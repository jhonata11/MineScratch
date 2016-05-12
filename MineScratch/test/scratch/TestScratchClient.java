package scratch;

import org.junit.Ignore;
import org.junit.Test;

import br.ufsc.ine.scratch.Command;
import br.ufsc.ine.scratch.Scratch;

public class TestScratchClient {
	class ScratchClient extends Scratch {
	}

	private Command girarParaDireita;
	private Command andarParaFrente;
	private ScratchClient scratch;

	@Test
	public void setUp() throws Exception {
		andarParaFrente = new Command() {
			@Override
			public void execute(String param) {
				System.out.println("andar para frente: " + param);
			}
		};

		girarParaDireita = new Command() {
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
		scratch.addCommand("andar_para_frente", andarParaFrente);
		scratch.addCommand("girar_para_direita", girarParaDireita);
		scratch.run();
	}
}
