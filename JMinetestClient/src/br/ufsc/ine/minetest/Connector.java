package br.ufsc.ine.minetest;

public class Connector {
	
	private MinetestProtocol minetestProtocol;
	private Receiver receiver;
	private int port;

	public Connector(String host, int port, String username, String password) throws InterruptedException {
		this.port = port;
		minetestProtocol = new MinetestProtocol(host, port, username, password);
		receiver = new Receiver(minetestProtocol);
	}

	public void connect() throws Exception{
		this.minetestProtocol.startHandshake();
		this.minetestProtocol.startReliableConnection();

	}

	public void receive() throws Exception {
		this.receiver.listen(port);
	}

}
