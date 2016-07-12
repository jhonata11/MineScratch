package br.ufsc.ine.scratch;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public abstract class ScratchServer{
    private static final int PORT = 50210;
	protected HttpServer server;

	public ScratchServer() throws IOException {
    	server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/poll", new PollHandler());
	}
	
	public void addCommand(Instruction instruction){
		server.createContext("/"+instruction.getStringCommand(), instruction);
	}
	
	public void listen(){
		System.err.println("Ouvindo Scratch na porta "+ PORT);
		server.start();
	}


}
