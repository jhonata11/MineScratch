package br.ufsc.ine.scratch;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class ScratchHandler implements HttpHandler {
    protected String response = "digital_read\\5 15\n\rdigital_read\\4 15\n\r";
    protected String command;
    protected String param;
    
	public void handle(HttpExchange t) throws IOException {
		String path = t.getRequestURI().toString();
		if(command != null && path.contains(command)){
			param = path.split("/")[2];
		}
		executeAction(param);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
	
	public abstract void executeAction(String param);
}