package br.ufsc.ine.utils;

import java.util.Date;

import javax.swing.JTextArea;

public class PrettyPrinter {
	
	private JTextArea textArea;

	public void print(String origin, String message){
		String log = String.format("%s %s -> %s\n", new Date(), origin, message);
		try {
			this.getTextArea().append(log);
		} catch (NullPointerException e){
			System.out.print(log);
		}
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

}
