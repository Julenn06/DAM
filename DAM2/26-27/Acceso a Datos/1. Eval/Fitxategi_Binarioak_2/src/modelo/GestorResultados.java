package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestorResultados {
	private ArrayList<Partido> listaPartidos;
	private final String FILE_RESULTADOS = "Resultados.dat";
	private final String FILE_ESTADISTICAS = "Estadisticas.dat";

	public GestorResultados() {
		this.listaPartidos = new ArrayList<>();
	}

	public void añadirPartido(Partido partido) {
		listaPartidos.add(partido);
	}

	public ArrayList<Partido> getListaPartidos() {
		return listaPartidos;
	}

	public void setListaPartidos(ArrayList<Partido> lista) {
		this.listaPartidos = lista;
	}

	public Estadisticas calcularEstadisticas() {
		int golesLocal = 0;
		int golesVisitante = 0;
		for (Partido p : listaPartidos) {
			golesLocal += p.getGolesLocal();
			golesVisitante += p.getGolesVisitante();
		}
		return new Estadisticas(listaPartidos.size(), golesLocal, golesVisitante);
	}

	public void guardarArchivos() throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_RESULTADOS))) {
			oos.writeObject(listaPartidos);
		}

		Estadisticas est = calcularEstadisticas();
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILE_ESTADISTICAS))) {
			dos.writeInt(est.getNumeroPartidos());
			dos.writeInt(est.getTotalGolesLocal());
			dos.writeInt(est.getTotalGolesVisitante());
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarResultados() throws IOException, ClassNotFoundException {
		File file = new File(FILE_RESULTADOS);
		if (file.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				this.listaPartidos = (ArrayList<Partido>) ois.readObject();
			}
		}
	}

	public Estadisticas cargarEstadisticasAuxiliar() throws IOException {
		File file = new File(FILE_ESTADISTICAS);
		if (file.exists()) {
			try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
				int numPartidos = dis.readInt();
				int golesL = dis.readInt();
				int golesV = dis.readInt();
				return new Estadisticas(numPartidos, golesL, golesV);
			}
		}
		return null;
	}
}
