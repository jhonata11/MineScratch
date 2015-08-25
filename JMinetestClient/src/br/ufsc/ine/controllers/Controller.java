package br.ufsc.ine.controllers;

import java.net.UnknownHostException;

import br.ufsc.ine.minetest.Connector;
import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;

public class Controller {

	private static final int USERNAME_LIMIT_LENGTH = 20;
	private static final int PASSWORD_LIMIT_LENGTH = 20;
	private Connector connector;

	public void startConnection(String host, String port, String username, String password) throws InterruptedException, Exception, UnknownHostException {
		this.verifyArguments(host, port, username, password);

		this.connector = new Connector(host, Integer.parseInt(port), username, password);
		this.connector.connect();

		System.out.printf("Listening on udp:%s:%s", host, port);
		while (true) {
			this.connector.receive();
		}
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

}
