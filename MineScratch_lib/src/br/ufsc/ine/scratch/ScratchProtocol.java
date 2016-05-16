package br.ufsc.ine.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ScratchProtocol {
	public String readMessage(BufferedReader in) throws IOException {
		String s = "";
		StringBuilder message = new StringBuilder();

		while ((s = in.readLine()) != null) {
			message = message.append(s);
			if (s.isEmpty()) {
				break;
			}
		}
		
//		if(message.toString().contains("andar_para_frente")){
//			System.out.println(message.toString());
//		}
		
		System.out.println(message);
		return message.toString();
	}

	public void confirmConnection(BufferedWriter out, String message) throws IOException {
		if (message.toString().contains("GET /poll HTTP/1.1")) {
			out.write("digital_read\\5 15\n\r");
			out.write("digital_read\\4 15\n\r");
		} else if (message.toString().contains("crossdomain.xml")) {
			out.write("<?xml version=\'1.0\'?>");
			out.write("<!DOCTYPE cross-domain-policy SYSTEM 'http://www.adobe.com/xml/dtds/cross-domain-policy.dtd'>");
			out.write("<cross-domain-policy>");
			out.write("<allow-access-from domain=' *.scratch.mit.edu' to-ports='*'/>");
			out.write("<allow-access-from domain=' *.media.mit.edu' to-ports='*'/>");
			out.write("</cross-domain-policy>");
		}
	}

	public void closeConnection(Socket clientSocket, BufferedReader in, BufferedWriter out) throws IOException {
		out.flush();
		out.close();
		in.close();
		clientSocket.close();
	}
}
