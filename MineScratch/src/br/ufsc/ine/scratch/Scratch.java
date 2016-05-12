package br.ufsc.ine.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Scratch implements Runnable {
	private Map<String, Command> commands;
	private ScratchProtocol protocol;

	public Scratch() {
		this.protocol = new ScratchProtocol();
		this.commands = new HashMap<>();
	}

	public void addCommand(String key, Command command) {
		this.commands.put(key, command);
	}

	public void listen(ServerSocket serverSocket) throws IOException {
		Socket clientSocket = serverSocket.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		this.next(clientSocket, in, out);
	}

	private void next(Socket clientSocket, BufferedReader in, BufferedWriter out) throws IOException {
		String message = protocol.readMessage(in);
		protocol.confirmConnection(out, message);
		this.executeCommand(message);
		protocol.closeConnection(clientSocket, in, out);
	}

	private void executeCommand(String message) {
		for (String key : commands.keySet()) {
			if (message.contains(key)) {
				List<String> lines = Arrays.asList(message.toString().split("/"));
				String param = lines.get(lines.indexOf(key) + 1);
				String value = param.replaceAll("\\D+", "");
				commands.get(key).execute(value);
			}
		}
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
