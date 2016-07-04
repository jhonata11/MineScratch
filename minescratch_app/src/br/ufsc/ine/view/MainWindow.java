package br.ufsc.ine.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.ufsc.ine.controllers.ViewController;
import br.ufsc.ine.utils.PrettyPrinter;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JTextArea outputConsole;
	private JScrollPane scrollPane;
	private ViewController controller;
	private PlaceholderTextField hostaddressTextField;
	private PlaceholderTextField portNumberTextField;
	private PlaceholderTextField usernameTextField;
	private PlaceholderTextField passwordTextField;
	
	private boolean loggedIn;
	public MainWindow() {
		panel = new JPanel();
		definePanel();
		addToPanel();

		controller = new ViewController();
		PrettyPrinter printer = new PrettyPrinter();
		printer.setTextArea(outputConsole);
		controller.setPrinter(printer);
		this.add(panel);
	}

	private void addToPanel() {
		panel.add(hostaddressTextField);
		panel.add(portNumberTextField);
		panel.add(usernameTextField);
		panel.add(passwordTextField);

		hostaddressTextField.setText("192.168.0.10");
		portNumberTextField.setText("30000");
		usernameTextField.setText("teste");
		passwordTextField.setText("");

		panel.add(scrollPane);
		panel.add(startButton);
		panel.add(stopButton);
	}

	private void definePanel() {
		defineButtons();

		hostaddressTextField = new PlaceholderTextField(12);
		hostaddressTextField.setPlaceholder("IP do servidor");
		portNumberTextField = new PlaceholderTextField(12);
		portNumberTextField.setPlaceholder("Número da porta");
		usernameTextField = new PlaceholderTextField(12);
		usernameTextField.setPlaceholder("Nome de usuário");
		passwordTextField = new PlaceholderTextField(12);
		passwordTextField.setPlaceholder("Senha");

		outputConsole = new JTextArea(20, 50);
		PrintStream out = new PrintStream( new TextAreaOutputStream( outputConsole ) );
		// redirect standard output stream to the TextAreaOutputStream
		System.setOut( out );
//		System.setErr( out );
		
		outputConsole.setEditable(false);
		outputConsole.setLineWrap(true);

		scrollPane = new JScrollPane(outputConsole);

		panel.setPreferredSize(new Dimension(440, 430));
	}

	private void defineButtons() {
		startButton = new JButton("Iniciar");
		startButton.setEnabled(!loggedIn);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!loggedIn){
					connectToMinetest();
					startButton.setEnabled(!loggedIn);
					stopButton.setEnabled(loggedIn);
				}
			}
		});

		stopButton = new JButton("Parar");
		stopButton.setEnabled(loggedIn);
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(loggedIn){
					disconnectMinetest();
					startButton.setEnabled(!loggedIn);
					stopButton.setEnabled(loggedIn);
				}
			}
		});
	}

	protected void disconnectMinetest() {
		try {
			if(loggedIn){
				this.controller.disconnect();
				loggedIn = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void connectToMinetest() {
		String host = hostaddressTextField.getText();
		String port = portNumberTextField.getText();
		String username = usernameTextField.getText();
		String password = passwordTextField.getText();
		try {
			controller.connectToMinetest(host, port, username, password);
			loggedIn = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}
