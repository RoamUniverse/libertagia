package com.carl.main;

import java.awt.EventQueue;

import com.carl.controller.MainController;
import com.carl.window.MainWindow;

public class Begin {
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainController controller = new MainController();
					MainWindow window = new MainWindow();
					controller.setWindow(window);
					window.setController(controller);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
