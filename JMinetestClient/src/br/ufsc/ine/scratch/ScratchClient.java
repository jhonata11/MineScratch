package br.ufsc.ine.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import br.ufsc.ine.controllers.MinetestClient;

public class ScratchClient implements Runnable {
	
	private MinetestClient minetestClient;

	public ScratchClient(MinetestClient minetestClient) {
		this.minetestClient = minetestClient;
	}


	public void listen(ServerSocket serverSocket) throws Exception {
		
		Socket clientSocket = serverSocket.accept();

		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

		// Iniciando vairáveis.
		String s;
		StringBuilder message = new StringBuilder();
		Command comando = new Command();

		while ((s = in.readLine()) != null) {
			message = message.append(s);
			if (s.isEmpty()) {
				break;
			}
		}

		if (message.toString().contains("GET /poll HTTP/1.1")) {
			out.write("digital_read\\5 15\n\r");
			out.write("digital_read\\4 15\n\r");
		} else if (message.toString().contains("crossdomain.xml")) {
			System.err.println("crossdomain.xml");

			out.write("<?xml version=\'1.0\'?>");
			out.write("<!DOCTYPE cross-domain-policy SYSTEM 'http://www.adobe.com/xml/dtds/cross-domain-policy.dtd'>");
			out.write("<cross-domain-policy>");
			out.write("<allow-access-from domain=' *.scratch.mit.edu' to-ports='*'/>");
			out.write("<allow-access-from domain=' *.media.mit.edu' to-ports='*'/>");
			out.write("</cross-domain-policy>");

		} else if (message.toString().contains("andar_para_frente")) {
			String[] linhas = message.toString().split("\n");
			for (String linha : linhas) {
				if (linha.contains("andar_para_frente")) {
					String[] comandos = linha.split("/");
					comando = new Command(comandos[1], comandos[2], comandos[3], comandos[4]);
					break;
				}
			}
			
			String valor = new String(comando.getStatus());
			valor = valor.replaceAll("\\D+","");
			try {
				Integer passos = Integer.parseInt(valor);
				this.moveFoward(passos);
			} catch (NumberFormatException e){
				this.moveFoward(1);
			}

		} else if (message.toString().contains("usar_ferramenta")) {
//			minetestClient.d().disconnect();
		} else if(message.toString().contains("girar_para_tras")){
			System.out.println("tras");
			moveBackward();
		} else if(message.toString().contains("girar_para_esquerda")){
			System.out.println("esquerda");
			moveLeft();
		}else if(message.toString().contains("girar_para_direita")){	
			System.out.println("direita");
			moveRight();
		} 
		out.flush();
		out.close();
		in.close();
		clientSocket.close();

	}



	private void moveRight() {
//		String mensagem = String.format("bot %s mover direita", this.botName);
//		this.minetestClient.sendChatMessage(mensagem);
//		new MinetestController(sender).sendChatMessage(mensagem);
	}

	private void moveLeft() {
//		String mensagem = String.format("bot %s mover esquerda", this.botName);
//		new MinetestController(sender).sendChatMessage(mensagem);
	}

	private void moveFoward(Integer steps) {
		this.minetestClient.walk(steps);
	}

	
	private void moveBackward() {
//		this.minetestClient.walk(1);
	}

	@Override
	public void run() {
		
		try {
			Thread.sleep(5000);
			int port = 50210;
			
			ServerSocket serverSocket = new ServerSocket(port);
			System.err.println("Servidor iniciado na porta : " + port);
			while (true) {
				listen(serverSocket);
			}

		} catch (Exception e) {}

	}
}
