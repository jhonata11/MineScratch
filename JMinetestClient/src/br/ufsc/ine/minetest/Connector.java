package br.ufsc.ine.minetest;

public class Connector {
	
	private Sender sender;
	private Receiver receiver;
	
	private int port;
	private String username;
	private String password;

	public Connector(String host, int port, String username, String password) throws InterruptedException {
		this.port = port;
		this.username = username;
		this.password = password;
		sender = new Sender(host, port);
		receiver = new Receiver(sender);
	}

	public void connect() throws Exception{
		this.sender.startHandshake(username, password);
		this.sender.startReliableConnection();

	}

	public void listen() throws Exception {
		this.receiver.listen(port);
	}

}
