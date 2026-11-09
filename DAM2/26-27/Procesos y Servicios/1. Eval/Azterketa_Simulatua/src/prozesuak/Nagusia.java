package prozesuak;

import java.io.File;

public class Nagusia {

	public void run() throws Exception {

		ProcessBuilder pb = new ProcessBuilder("java", "prozesuak.Nagusia");
		pb.directory(new File("bin"));

		Process process = pb.start();

	}
}
