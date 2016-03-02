package br.ufsc.ine.minetest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.ArrayUtils;

import br.ufsc.ine.scratch.ScratchClient;
import br.ufsc.ine.utils.PrettyPrinter;
import br.ufsc.ine.utils.Utils;

public class Connector {
	
	private Sender sender;
	private Receiver receiver;
	
	private int port;
	private String username;
	private String password;
	
	private Semaphore semaphore;
	private PrettyPrinter printer;

	public Connector(String host, int port, String username, String password) throws InterruptedException {
		this.port = port;
		this.username = username;
		this.password = password;
		this.semaphore = new Semaphore(1);
		
		sender = new Sender(host, port, semaphore);
		receiver = new Receiver(sender.getMinetestProtocol(), semaphore, port);
	}

	public void connect() throws Exception{	
		this.sender.startHandshake(username, password);
		this.sender.startReliableConnection();
		
		Thread receiverThread = new Thread(receiver);
		receiverThread.start();

		ScratchClient client = new ScratchClient(this.sender);
		Thread scratchThread = new Thread(client);
		scratchThread.start();
		
		while(true){
			receiveMessage();
		}
		
		
	}

	private void receiveMessage() {
		byte[] receiveCommand = sender.getMinetestProtocol().receiveCommand();

		if(receiveCommand != null && receiveCommand.length >=2){
			short tipo = Utils.byteToShort(ArrayUtils.subarray(receiveCommand, 0, 2));
			if(tipo == 0x30){
				String str = new String(ArrayUtils.subarray(receiveCommand, 2, receiveCommand.length), StandardCharsets.UTF_16BE);
				
				printer.print("Minetest", str);
			}
		}
	}
	
	public void disconnect() throws Exception{
		this.sender.disconnect();
	}

	public void listen() throws Exception {
		this.receiver.listen(port);
	}

	/**
	 * @return the receiver
	 */
	public Receiver getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the sender
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public void setPrinter(PrettyPrinter printer) {
		this.printer = printer;
	}
}
