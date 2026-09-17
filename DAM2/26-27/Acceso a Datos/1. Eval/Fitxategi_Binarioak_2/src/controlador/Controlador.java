package controlador;

import java.io.IOException;

import javax.swing.JOptionPane;

import modelo.Estadisticas;
import modelo.GestorResultados;
import modelo.Partido;
import vista.PanelFormulario;
import vista.VentanaPrincipal;

public class Controlador {
	private VentanaPrincipal vista;
	private GestorResultados modelo;

	public Controlador(VentanaPrincipal vista, GestorResultados modelo) {
		this.vista = vista;
		this.modelo = modelo;
		initListeners();
		refrescarPantalla();
	}

	@SuppressWarnings("unused")
	private void initListeners() {
		PanelFormulario form = vista.getPanelFormulario();

		form.getBtnAñadir().addActionListener((e) -> accionAñadir());
		form.getBtnCargar().addActionListener(e -> accionCargar());
		form.getBtnGuardar().addActionListener(e -> accionGuardar());
	}

	private void accionAñadir() {
		PanelFormulario form = vista.getPanelFormulario();

		String local = form.getLocal();
		String visitante = form.getVisitante();
		String golesLStr = form.getGolesL();
		String golesVStr = form.getGolesV();
		String lugar = form.getLugar();
		String fecha = form.getFecha();

		String regexAlfa = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]{1,20}$";
		String regexGoles = "^\\d{1,2}$";
		String regexFecha = "^\\d{2}/\\d{2}/\\d{2}$";

		if (!local.matches(regexAlfa) || !visitante.matches(regexAlfa) || !lugar.matches(regexAlfa)) {
			mostrarError("Los campos de texto deben ser alfanuméricos (1 a 20 caracteres).");
			return;
		}
		if (!golesLStr.matches(regexGoles) || !golesVStr.matches(regexGoles)) {
			mostrarError("Los goles deben ser valores numéricos enteros de 1 o 2 dígitos.");
			return;
		}
		if (!fecha.matches(regexFecha)) {
			mostrarError("La fecha debe cumplir rigurosamente el formato dd/MM/yy.");
			return;
		}

		int golesL = Integer.parseInt(golesLStr);
		int golesV = Integer.parseInt(golesVStr);

		Partido nuevoPartido = new Partido(local, visitante, golesL, golesV, lugar, fecha);
		modelo.añadirPartido(nuevoPartido);

		form.limpiarCampos();
		refrescarPantalla();
	}

	private void accionCargar() {
		try {
			modelo.cargarResultados();
			Estadisticas estAux = modelo.cargarEstadisticasAuxiliar();

			refrescarPantalla();

			StringBuilder sb = new StringBuilder(
					"Los partidos han sido cargados correctamente desde Resultados.dat.\n\n");
			if (estAux != null) {
				sb.append("Resumen leído de Estadisticas.dat:\n").append(estAux.getNumeroPartidos())
						.append(" partidos  •  ").append(estAux.getTotalGolesLocal()).append("-")
						.append(estAux.getTotalGolesVisitante()).append(" goles totales");
			} else {
				sb.append("Aviso: No se localizó el archivo auxiliar de estadísticas.");
			}

			JOptionPane.showMessageDialog(vista, sb.toString(), "Carga completa", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception ex) {
			refrescarPantalla();
		}
	}

	private void accionGuardar() {
		if (modelo.getListaPartidos().isEmpty()) {
			JOptionPane.showMessageDialog(vista, "La tabla está vacía. No hay datos que guardar.", "Atención",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			modelo.guardarArchivos();
			Estadisticas est = modelo.calcularEstadisticas();

			String mensaje = String.format(
					"Los partidos se han guardado correctamente.\n\n" + "- Resultados.dat: %d partidos\n"
							+ "- Estadisticas.dat: %d-%d goles totales",
					modelo.getListaPartidos().size(), est.getTotalGolesLocal(), est.getTotalGolesVisitante());

			JOptionPane.showMessageDialog(vista, mensaje, "Guardado completo", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			mostrarError("Error crítico de E/S al escribir en disco: " + ex.getMessage());
		}
	}

	private void refrescarPantalla() {
		vista.getPanelTabla().actualizarTabla(modelo.getListaPartidos());
		Estadisticas est = modelo.calcularEstadisticas();
		vista.getPanelEstadisticas().actualizarResumen(est.getNumeroPartidos(), est.getTotalGolesLocal(),
				est.getTotalGolesVisitante());
	}

	private void mostrarError(String msg) {
		JOptionPane.showMessageDialog(vista, msg, "Error de Validación", JOptionPane.ERROR_MESSAGE);
	}
}
