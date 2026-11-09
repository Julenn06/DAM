package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import firebase.FirebasePersonasRepository;
import format.DatPersonas;
import format.JsonPersonas;
import format.TxtPersonas;
import format.XmlPersonas;
import model.Persona;

public class PersonasWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private final FirebasePersonasRepository repository;
	private final DefaultTableModel tableModel;
	private final JTable table;

	public PersonasWindow(FirebasePersonasRepository repository) throws Exception {
		super("Personas - Firebase / MVC");
		this.repository = repository;
		String[] columnas = { "ID", "Nombre", "Fecha de nacimiento", "Años" };
		tableModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		cargarTabla();

		JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton anadir = new JButton("Añadir");
		JButton editar = new JButton("Editar / actualizar");
		JButton eliminar = new JButton("Eliminar");
		JButton recargar = new JButton("Recargar");
		JButton txt = new JButton("TXT");
		JButton json = new JButton("JSON");
		JButton xml = new JButton("XML");
		JButton dat = new JButton("DAT");
		botones.add(anadir);
		botones.add(editar);
		botones.add(eliminar);
		botones.add(recargar);
		botones.add(txt);
		botones.add(json);
		botones.add(xml);
		botones.add(dat);
		add(botones, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		anadir.addActionListener(event -> abrirFormulario(null));
		editar.addActionListener(event -> abrirFormularioEditar());
		eliminar.addActionListener(event -> eliminarDesdeVentana());
		recargar.addActionListener(event -> recargarTabla());
		txt.addActionListener(event -> exportarTxt());
		json.addActionListener(event -> exportarJson());
		xml.addActionListener(event -> exportarXml());
		dat.addActionListener(event -> exportarDat());

		setSize(980, 320);
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void cargarTabla() throws Exception {
		tableModel.setRowCount(0);
		List<Persona> personas = repository.listar();
		personas.forEach(persona -> tableModel.addRow(new Object[] { persona.getId(), persona.getNombre(),
				persona.getFechaNacimiento(), persona.getEdad() }));
	}

	private Persona seleccionarPersona() {
		int fila = table.getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(this, "Selecciona una persona en la tabla.");
			return null;
		}
		try {
			return repository.buscarPorId(tableModel.getValueAt(fila, 0).toString());
		} catch (Exception exception) {
			mostrarError(exception);
			return null;
		}
	}

	private void abrirFormulario(Persona persona) {
		JDialog dialogo = new JDialog(this, persona == null ? "Añadir persona" : "Editar persona", true);
		JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
		campos.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
		JTextField id = new JTextField(persona == null ? "" : persona.getId());
		JTextField nombre = new JTextField(persona == null ? "" : persona.getNombre());
		JTextField fecha = new JTextField(persona == null ? "" : persona.getFechaNacimiento().toString());
		campos.add(new JLabel("ID:"));
		campos.add(id);
		campos.add(new JLabel("Nombre:"));
		campos.add(nombre);
		campos.add(new JLabel("Fecha (AAAA-MM-DD):"));
		campos.add(fecha);
		if (persona != null)
			id.setEditable(false);

		JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton guardar = new JButton("Guardar");
		JButton salir = new JButton("Salir");
		acciones.add(guardar);
		acciones.add(salir);
		guardar.addActionListener(event -> {
			try {
				Persona nueva = new Persona(id.getText().trim(), nombre.getText().trim(),
						LocalDate.parse(fecha.getText().trim()));
				if (persona == null)
					repository.crear(nueva);
				else
					repository.actualizar(nueva);
				cargarTabla();
				dialogo.dispose();
			} catch (Exception exception) {
				mostrarError(exception);
			}
		});
		salir.addActionListener(event -> dialogo.dispose());

		dialogo.add(campos, BorderLayout.CENTER);
		dialogo.add(acciones, BorderLayout.SOUTH);
		dialogo.pack();
		dialogo.setLocationRelativeTo(this);
		dialogo.setVisible(true);
	}

	private void abrirFormularioEditar() {
		try {
			List<Persona> personas = repository.listar();
			if (personas.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No hay personas en Firebase para editar.");
				return;
			}
			JDialog dialogo = new JDialog(this, "Editar persona", true);
			JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
			campos.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
			JComboBox<String> selector = new JComboBox<>();
			personas.forEach(persona -> selector.addItem(persona.getId()));
			JTextField nombre = new JTextField();
			JTextField fecha = new JTextField();
			campos.add(new JLabel("Persona:"));
			campos.add(selector);
			campos.add(new JLabel("Nombre:"));
			campos.add(nombre);
			campos.add(new JLabel("Fecha (AAAA-MM-DD):"));
			campos.add(fecha);

			selector.addActionListener(event -> cargarSeleccion(selector, nombre, fecha));
			cargarSeleccion(selector, nombre, fecha);

			JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton guardar = new JButton("Guardar");
			JButton salir = new JButton("Salir");
			acciones.add(guardar);
			acciones.add(salir);
			guardar.addActionListener(event -> {
				try {
					Persona actualizada = new Persona((String) selector.getSelectedItem(), nombre.getText().trim(),
							LocalDate.parse(fecha.getText().trim()));
					repository.actualizar(actualizada);
					cargarTabla();
					dialogo.dispose();
				} catch (Exception exception) {
					mostrarError(exception);
				}
			});
			salir.addActionListener(event -> dialogo.dispose());
			dialogo.add(campos, BorderLayout.CENTER);
			dialogo.add(acciones, BorderLayout.SOUTH);
			dialogo.pack();
			dialogo.setLocationRelativeTo(this);
			dialogo.setVisible(true);
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void eliminarDesdeVentana() {
		Persona persona = seleccionarPersona();
		if (persona == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar " + persona.getNombre() + " de Firebase?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				repository.eliminar(persona.getId());
				cargarTabla();
			} catch (Exception exception) {
				mostrarError(exception);
			}
		}
	}

	private void cargarSeleccion(JComboBox<String> selector, JTextField nombre, JTextField fecha) {
		try {
			Persona seleccionada = repository.buscarPorId((String) selector.getSelectedItem());
			nombre.setText(seleccionada.getNombre());
			fecha.setText(seleccionada.getFechaNacimiento().toString());
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void recargarTabla() {
		try {
			cargarTabla();
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private List<Persona> personasDeTabla() {
		List<Persona> personas = new ArrayList<>();
		for (int fila = 0; fila < tableModel.getRowCount(); fila++) {
			personas.add(
					new Persona(tableModel.getValueAt(fila, 0).toString(), tableModel.getValueAt(fila, 1).toString(),
							LocalDate.parse(tableModel.getValueAt(fila, 2).toString())));
		}
		return personas;
	}

	private File carpetaExportacion() throws Exception {
		Path carpeta = Path.of("datos-firebase");
		Files.createDirectories(carpeta);
		return carpeta.toFile();
	}

	private void exportarTxt() {
		try {
			TxtPersonas.escribirTabla(personasDeTabla(), new File(carpetaExportacion(), "personas.txt"));
			JOptionPane.showMessageDialog(this, "Tabla exportada a TXT.");
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void exportarJson() {
		try {
			JsonPersonas.escribirArray(personasDeTabla(), new File(carpetaExportacion(), "personas-array.json"));
			JOptionPane.showMessageDialog(this, "Tabla exportada a JSON.");
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void exportarXml() {
		try {
			XmlPersonas.escribir(personasDeTabla(), new File(carpetaExportacion(), "personas.xml"));
			JOptionPane.showMessageDialog(this, "Tabla exportada a XML.");
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void exportarDat() {
		try {
			DatPersonas.escribirDat(personasDeTabla(), new File(carpetaExportacion(), "personas.dat"));
			JOptionPane.showMessageDialog(this, "Tabla exportada a DAT.");
		} catch (Exception exception) {
			mostrarError(exception);
		}
	}

	private void mostrarError(Exception exception) {
		JOptionPane.showMessageDialog(this, exception.getMessage(), "Error Firebase", JOptionPane.ERROR_MESSAGE);
	}

}