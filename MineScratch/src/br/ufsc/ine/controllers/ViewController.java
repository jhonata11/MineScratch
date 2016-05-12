package br.ufsc.ine.controllers;

import java.net.UnknownHostException;

import org.junit.Test;

import br.ufsc.ine.minetest.Minetest;
import br.ufsc.ine.minetest.MinetestConnector;
import br.ufsc.ine.minetest.exceptions.HostFormatException;
import br.ufsc.ine.minetest.exceptions.PasswordLimitExcededException;
import br.ufsc.ine.minetest.exceptions.PortFormatException;
import br.ufsc.ine.minetest.exceptions.UsernameLimitExcededException;
import br.ufsc.ine.scratch.Command;
import br.ufsc.ine.scratch.Scratch;
import br.ufsc.ine.utils.PrettyPrinter;

public class ViewController {

	private static final int USERNAME_LIMIT_LENGTH = 20;
	private static final int PASSWORD_LIMIT_LENGTH = 20;
	private MinetestConnector connector;
	private Minetest controller;
	private PrettyPrinter printer;


	public void connectToMinetest(String host, String port, String username, String password, Scratch scratch) throws InterruptedException, Exception, UnknownHostException {
		this.verifyArguments(host, port, username, password);
		this.controller = new Minetest(host, Integer.parseInt(port), username, password);
		this.controller.startApplication(scratch);
//		this.connector = new MinetestConnector(host, Integer.parseInt(port), username, password);
//		this.connector.setPrinter(printer);
//		this.connector.connect();
	}
	
	public void disconnect() throws Exception{
		this.connector.disconnect();
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

	public Minetest getController() {
		return controller;
	}

	public void setController(Minetest controller) {
		this.controller = controller;
	}

}
