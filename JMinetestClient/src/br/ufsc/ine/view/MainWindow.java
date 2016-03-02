package br.ufsc.ine.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import br.ufsc.ine.controllers.Controller;
import br.ufsc.ine.utils.PrettyPrinter;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton startButton;
	private JButton stopButton;
	private JTextArea outputConsole;
	private JScrollPane scrollPane;
	private Controller controller;
	private PlaceholderTextField hostaddressTextField;
	private PlaceholderTextField portNumberTextField;
	private PlaceholderTextField usernameTextField;
	private PlaceholderTextField passwordTextField;
	
	private Thread connectionThread;
	public MainWindow() {
		connectionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				connectToMinetest();
			}
		});
		
		panel = new JPanel();
		definePanel();
		addToPanel();

		controller = new Controller();
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

		hostaddressTextField.setText("192.168.0.14");
		portNumberTextField.setText("30000");
		usernameTextField.setText("jhonata11");
		passwordTextField.setText("senha");

		panel.add(new JSeparator(SwingConstants.HORIZONTAL));
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
		outputConsole.setEditable(true);
		outputConsole.setLineWrap(true);

		scrollPane = new JScrollPane(outputConsole);

		panel.setPreferredSize(new Dimension(584, 430));
	}

	private void defineButtons() {
		startButton = new JButton("Iniciar");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectionThread.isInterrupted()){
					connectionThread.run();
				}else {
					connectionThread.start();
				}
			}
		});

		stopButton = new JButton("Parar");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disconnectMinetest();
			}
		});
	}

	protected void disconnectMinetest() {
		try {
			this.controller.disconnect();
			connectionThread.interrupt();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// disconnectMinetest();
				e.getWindow().dispose();
			}
		});
		this.pack();
		this.setVisible(true);
	}
}
