package controlador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetId {

	public int getLastId(String filename) {
	    int lastId = 0;
	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            if (!line.trim().isEmpty()) {
	                String[] parts = line.split("\t");
	                int id = Integer.parseInt(parts[0]);
	                if (id > lastId) {
	                    lastId = id;
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.out.println("Archivo no encontrado, se creará uno nuevo.");
	    }
	    return lastId;
	}

}
