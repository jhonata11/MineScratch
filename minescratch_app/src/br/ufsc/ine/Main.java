package br.ufsc.ine;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.ufsc.ine.view.MainWindow;

public class Main {

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
		}

		MainWindow window = new MainWindow();
		window.init();
	}
}
