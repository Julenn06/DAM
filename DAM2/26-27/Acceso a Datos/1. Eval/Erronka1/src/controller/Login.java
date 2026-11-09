package controller;

import model.Erabiltzailea;

public class Login {

	public static Erabiltzailea erabil;

	public static Erabiltzailea getErabil() {
		return erabil;
	}

	public static void setErabil(Erabiltzailea erabil) {
		Login.erabil = erabil;
	}

	public Login() {

	}

}
