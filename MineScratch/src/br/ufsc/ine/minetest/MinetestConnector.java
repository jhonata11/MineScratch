package br.ufsc.ine.minetest;

import br.ufsc.ine.minetest.network.Receiver;
import br.ufsc.ine.minetest.network.Sender;

//import br.ufsc.ine.utils.PrettyPrinter;

public class MinetestConnector {

	private Sender sender;
	private Receiver receiver;

	private int port;
	private String username;
	private String password;


	public MinetestConnector(String host, int port, String username, String password) throws InterruptedException {
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public void connect() throws Exception {
		this.sender.startHandshake(username, password);
		this.sender.startReliableConnection();
		Thread receiverThread = new Thread(receiver);
		receiverThread.start();

	}

	public void disconnect() throws Exception {
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
	 * @param receiver
	 *            the receiver to set
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
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}

//	public void setPrinter(PrettyPrinter printer) {
//		this.printer = printer;
//	}
}