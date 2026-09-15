package itzultzailea;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ProzezuItzultzailea {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		ProcessBuilder pb = new ProcessBuilder("java", "itzultzailea.Itzultzailea");
		pb.directory(new File("bin"));
		pb.redirectOutput(new File("irteera.txt"));
		Process process = pb.start();
		System.out.println("PID: " + process.pid());
		System.out.println("PID Aita: " + process.toHandle().parent().get().pid());

		System.out.println("Sartu hitz bat");
		String hitza = sc.next();

		process.getOutputStream().write((hitza + "\n").getBytes());
		process.getOutputStream().flush();

		System.out.println(process.getOutputStream().hashCode());
		sc.close();

	}
}
