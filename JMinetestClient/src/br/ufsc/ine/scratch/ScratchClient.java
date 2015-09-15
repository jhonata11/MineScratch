package br.ufsc.ine.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import br.ufsc.ine.minetest.MinetestPacket;
import br.ufsc.ine.minetest.Sender;

public class ScratchClient implements Runnable {

	private Sender sender;
	private String botName;

	public ScratchClient(Sender sender) {
		this.sender = sender;
		this.botName = "";
		
	}

	private void createBot(String name) {
		String randomName = new Date().toString().replaceAll(" ", "").replaceAll(":", "");
		this.botName = name == "null" ? randomName : name;
		System.out.println(name);
		String mensagem = String.format("bot %s criar", this.botName);
		sendMessage(mensagem);		
	}

	public void listen(ServerSocket serverSocket) throws Exception {
		
		// cr�ation de la socket
		// int port = 50210;
		// ServerSocket serverSocket = new ServerSocket(port);
		// System.err.println("Servidor iniciado na porta : " + port);

		// repeatedly wait for connections, and process
		// on reste bloqu� sur l'attente d'une demande client
		Socket clientSocket = serverSocket.accept();
		// System.err.println("Cliente conectado");

		// on ouvre un flux de converation
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

		// Inicializacao variaveis.
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
			
			Integer passos = Integer.parseInt(valor);
			for (int i = 0; i < passos; i++){
				moveFoward();
			}

		} else if (message.toString().contains("usar_ferramenta")) {
			this.sender.disconnect();
		} else if(message.toString().contains("girar_para_tras")){
			System.out.println("tras");
			moveBackward();
		} else if(message.toString().contains("girar_para_esquerda")){
			System.out.println("esquerda");
			moveLeft();
		}else if(message.toString().contains("girar_para_direita")){	
			System.out.println("direita");
			moveRight();
		} else if(message.toString().contains("criar_bot")){
			String[] linhas = message.toString().split("\n");
			for (String linha : linhas) {
				if (linha.contains("criar_bot")) {
					String[] comandos = linha.split("/");
					comando = new Command(comandos[1], comandos[2], comandos[3], comandos[4]);
					break;
				}
			}
			String valor = new String(comando.getStatus());
			valor = valor.substring(0, valor.length()-5);
			this.createBot(valor);
		} else if (message.toString().contains("destruir_bot")) {
			eraseBot();
		}


		// System.err.println("Conexao com o cliente terminada");
		out.flush();
		out.close();
		in.close();
		clientSocket.close();

	}

	private void eraseBot() {
		String mensagem = String.format("bot %s destruir", this.botName);
		sendMessage(mensagem);
	}

	private void moveRight() {
		String mensagem = String.format("bot %s mover direita", this.botName);
		sendMessage(mensagem);
	}

	private void moveLeft() {
		String mensagem = String.format("bot %s mover esquerda", this.botName);
		sendMessage(mensagem);
	}

	private void moveFoward() {
		String mensagem = String.format("bot %s mover frente", this.botName);
		sendMessage(mensagem);
	}

	private void sendMessage(String mensagem) {
		byte[] encoded = Charset.forName("UTF-16BE").encode(mensagem).array();
		byte[] chat = ByteBuffer.allocate(2).putShort((short) 0x32).array();
		byte[] tamanho = ByteBuffer.allocate(2).putShort((short) mensagem.getBytes().length).array();

		MinetestPacket packet = new MinetestPacket();
		packet.addToBodyStart(chat);
		packet.addToBodyEnd(tamanho);
		packet.addToBodyEnd(encoded);

		try {
			this.sender.sendCommand(packet);
			String str = new String(encoded, StandardCharsets.UTF_16BE);

			System.err.println("======== mensagem enviada: " + str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moveBackward() {
		String mensagem = String.format("bot %s mover tras", this.botName);
		sendMessage(mensagem);
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

		} catch (Exception e) {

		}

	}
}