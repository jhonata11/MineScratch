package br.ufsc.ine.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	
	public void listen() throws Exception{
		 // création de la socket
        int port = 50210;
        ServerSocket serverSocket = new ServerSocket(port);
        System.err.println("Servidor iniciado na porta : " + port);

        // repeatedly wait for connections, and process
        while (true) {
            // on reste bloqué sur l'attente d'une demande client
            Socket clientSocket = serverSocket.accept();
//            System.err.println("Cliente conectado");

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
                //System.out.println("POOL");
                // out.write("_problem Hardware is not connected\r\n");
                out.write("digital_read\\5 15\n\r");
                out.write("digital_read\\4 15\n\r");
//            out.write("HTTP/1.0 200 OK\r\n");
//            out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
//            out.write("Server: Apache/0.8.4\r\n");
//            out.write("Content-Type: text/html\r\n");
//            out.write("Content-Length: 57\r\n");
//            out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
//            out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
//            out.write("\r\n");
//            out.write("<TITLE>Exemple</TITLE>");
//            out.write("<P>Ceci est une page d'exemple.</P>");
            } else if (message.toString().contains("crossdomain.xml")) {
                System.out.println("Entrou aqui!");

                out.write("<?xml version=\'1.0\'?>");
                out.write("<!DOCTYPE cross-domain-policy SYSTEM 'http://www.adobe.com/xml/dtds/cross-domain-policy.dtd'>");
                out.write("<cross-domain-policy>");
                out.write("<allow-access-from domain=' *.scratch.mit.edu' to-ports='*'/>");
                out.write("<allow-access-from domain=' *.media.mit.edu' to-ports='*'/>");
                out.write("</cross-domain-policy>");

            }else if (message.toString().contains("andar_para_frente")) {

                String[] linhas = message.toString().split("\n");
                for (String linha : linhas) {
                    if (linha.contains("andar_para_frente")){
                        String[] comandos = linha.split("/");
                        comando = new Command(comandos[1], comandos[2], comandos[3], comandos[4]);
                        break;
                    }
                }
                
                System.out.println("COMANDO RECEBIDO: "+comando);
                
//                out.write("<?xml version=\'1.0\'?>");
//                out.write("<!DOCTYPE cross-domain-policy SYSTEM 'http://www.adobe.com/xml/dtds/cross-domain-policy.dtd'>");
//                out.write("<cross-domain-policy>");
//                out.write("<allow-access-from domain=' *.scratch.mit.edu' to-ports='*'/>");
//                out.write("<allow-access-from domain=' *.media.mit.edu' to-ports='*'/>");
//                out.write("</cross-domain-policy>");

            }

//            System.err.println("Conexao com o cliente terminada");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
	}

}
}
