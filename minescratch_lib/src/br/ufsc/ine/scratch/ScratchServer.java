package br.ufsc.ine.scratch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

public abstract class ScratchServer{
    private static final int PORT = 50210;
	protected HttpServer server;
	protected List<Instruction> instructions;

	public ScratchServer() throws IOException {
		instructions = new LinkedList<>();
    	server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/poll", new PollHandler());
	}
	
	public void addCommand(Instruction instruction){
		server.createContext("/"+instruction.getStringCommand(), instruction);
	}
	
	public void listen(){
		server.start();
	}


}
