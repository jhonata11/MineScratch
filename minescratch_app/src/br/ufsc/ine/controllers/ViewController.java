package br.ufsc.ine.controllers;

import java.net.UnknownHostException;

import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;
import br.ufsc.ine.models.minetest.MinetestClient;
import br.ufsc.ine.models.scratch.ScratchClient;
import br.ufsc.ine.utils.PrettyPrinter;

public class ViewController {

	private static final int USERNAME_LIMIT_LENGTH = 20;
	private static final int PASSWORD_LIMIT_LENGTH = 20;
	private PrettyPrinter printer;
	private MinetestClient minetest;
	private Thread minetestThread;


	public void connectToMinetest(String host, String port, String username, String password) throws InterruptedException, Exception, UnknownHostException {
		this.verifyArguments(host, port, username, password);
		
		minetest = new MinetestClient(host, Integer.parseInt(port), username, password);
		ScratchClient scratch = new ScratchClient(minetest);
		minetest.setScratch(scratch);
		minetestThread = new Thread(minetest);
		minetestThread.start();
//		Thread.sleep(5000);
		minetest.init();
	}
	
	public void disconnect() throws Exception{
		minetest.disconnectMinetest();
		this.minetestThread.interrupt();
	}

	public void verifyArguments(String host, String port, String username, String password)
			throws HostFormatException, PortFormatException, UsernameLimitExcededException, PasswordLimitExcededException {
		this.matchHost(host);
		this.matchPort(port);
		this.matchUsername(username);
		this.matchPassword(password);
	}

	private void matchPassword(String password) throws PasswordLimitExcededException {
		if (password.length() > PASSWORD_LIMIT_LENGTH) {
			throw new PasswordLimitExcededException();
		}
	}

	private void matchUsername(String username) throws UsernameLimitExcededException {
		if (username.length() > USERNAME_LIMIT_LENGTH) {
			throw new UsernameLimitExcededException();
		}
	}

	private void matchPort(String port) throws PortFormatException {
		String portPattern = "^([0-9]+)$";

		if (!port.matches(portPattern)) {
			throw new PortFormatException();
		}

	}

	private void matchHost(String host) throws HostFormatException {
		String ipPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		if (!host.matches(ipPattern)) {
			throw new HostFormatException();
		}
	}

	public PrettyPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(PrettyPrinter printer) {
		this.printer = printer;
	}
}
