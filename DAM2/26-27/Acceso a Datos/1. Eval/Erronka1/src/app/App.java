package app;

import java.awt.EventQueue;

import com.formdev.flatlaf.FlatLightLaf;

import controller.DBConnection;
import view.Lehioa;

public class App {

	public static void main(String[] args) {
		FlatLightLaf.setup();

		DBConnection.warmUpAsync();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Lehioa frame = new Lehioa();
				frame.setVisible(true);
			}
		});
	}
}
