package vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.Equipos;
import modelo.Equipo;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEquipoLocal;
	private JTextField textFieldEquipoVisitante;
	private JTextField textFieldGolesLocal;
	private JTextField textFieldGolesVisitante;
	private JTextField textFieldLugar;
	private JTextField textFieldFecha;
	private JTable table;
	private Equipo equipo;
	private Equipos equipos = new Equipos();
	private boolean validarDatos = false;

	private final String ARCHIVO = "Resultados.dat";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEquipoLocal = new JLabel("Equipo Local");
		lblEquipoLocal.setBounds(10, 38, 110, 14);
		contentPane.add(lblEquipoLocal);

		JLabel lblEquipoViitante = new JLabel("Equipo Visitante");
		lblEquipoViitante.setBounds(10, 90, 110, 14);
		contentPane.add(lblEquipoViitante);

		JLabel lblGolesLocal = new JLabel("Goles Local");
		lblGolesLocal.setBounds(10, 140, 110, 14);
		contentPane.add(lblGolesLocal);

		JLabel lblGolesVisitante = new JLabel("Goles Visitante");
		lblGolesVisitante.setBounds(10, 190, 110, 14);
		contentPane.add(lblGolesVisitante);

		JLabel lblLugar = new JLabel("Lugar");
		lblLugar.setBounds(10, 240, 110, 14);
		contentPane.add(lblLugar);

		JLabel lblFecha = new JLabel("Fecha");
		lblFecha.setBounds(10, 290, 110, 14);
		contentPane.add(lblFecha);

		textFieldEquipoLocal = new JTextField();
		textFieldEquipoLocal.setBounds(161, 35, 170, 20);
		contentPane.add(textFieldEquipoLocal);
		textFieldEquipoLocal.setColumns(10);

		textFieldEquipoVisitante = new JTextField();
		textFieldEquipoVisitante.setColumns(10);
		textFieldEquipoVisitante.setBounds(161, 87, 170, 20);
		contentPane.add(textFieldEquipoVisitante);

		textFieldGolesLocal = new JTextField();
		textFieldGolesLocal.setColumns(10);
		textFieldGolesLocal.setBounds(161, 137, 170, 20);
		contentPane.add(textFieldGolesLocal);

		textFieldGolesVisitante = new JTextField();
		textFieldGolesVisitante.setColumns(10);
		textFieldGolesVisitante.setBounds(161, 187, 170, 20);
		contentPane.add(textFieldGolesVisitante);

		textFieldLugar = new JTextField();
		textFieldLugar.setColumns(10);
		textFieldLugar.setBounds(161, 237, 170, 20);
		contentPane.add(textFieldLugar);

		textFieldFecha = new JTextField();
		textFieldFecha.setColumns(10);
		textFieldFecha.setBounds(161, 287, 170, 20);
		contentPane.add(textFieldFecha);

		JButton btnAñadir = new JButton("Añadir");
		btnAñadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					equipo = new Equipo();

					equipo.setEquipoLocal(textFieldEquipoLocal.getText());
					equipo.setEquipoVisitante(textFieldEquipoVisitante.getText());
					equipo.setGolesLocal(Integer.parseInt(textFieldGolesLocal.getText()));
					equipo.setGolesVisitante(Integer.parseInt(textFieldGolesVisitante.getText()));
					equipo.setLugar(textFieldLugar.getText());
					equipo.setFecha(textFieldFecha.getText());

					validarDatos = equipos.validarDatos(equipo);

					if (validarDatos == true) {
						insertarDatos(equipo);
					} else {
						JOptionPane.showMessageDialog(Main.this,
								"Los datos no tienen el formato correcto (verifica la fecha).", "Error de Validación",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(Main.this, "Por favor, introduce números válidos en los goles.",
							"Error de Formato", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAñadir.setBounds(10, 340, 89, 23);
		contentPane.add(btnAñadir);

		JButton btnCargar = new JButton("Cargar");
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File archivo = new File(ARCHIVO);
				if (!archivo.exists()) {
					JOptionPane.showMessageDialog(Main.this, "No existe ningún archivo de datos guardado.",
							"Información", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
					@SuppressWarnings("unchecked")
					ArrayList<Equipo> listaCargada = (ArrayList<Equipo>) ois.readObject();

					DefaultTableModel modeloTabla = (DefaultTableModel) table.getModel();
					modeloTabla.setRowCount(0);

					for (Equipo eq : listaCargada) {
						Object[] fila = new Object[] { eq.getEquipoLocal(), eq.getEquipoVisitante(), eq.getGolesLocal(),
								eq.getGolesVisitante(), eq.getLugar(), eq.getFecha() };
						modeloTabla.addRow(fila);
					}

					JOptionPane.showMessageDialog(Main.this, "Datos cargados correctamente desde " + ARCHIVO, "Éxito",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Main.this, "Error al cargar los datos.", "Error I/O",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCargar.setBounds(140, 340, 89, 23);
		contentPane.add(btnCargar);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel modeloTabla = (DefaultTableModel) table.getModel();
				int totalFilas = modeloTabla.getRowCount();

				if (totalFilas == 0) {
					JOptionPane.showMessageDialog(Main.this, "La tabla está vacía. No hay nada que guardar.",
							"Atención", JOptionPane.WARNING_MESSAGE);
					return;
				}

				ArrayList<Equipo> listaAGuardar = new ArrayList<>();

				for (int i = 0; i < totalFilas; i++) {
					Equipo eq = new Equipo();
					eq.setEquipoLocal((String) modeloTabla.getValueAt(i, 0));
					eq.setEquipoVisitante((String) modeloTabla.getValueAt(i, 1));
					eq.setGolesLocal((int) modeloTabla.getValueAt(i, 2));
					eq.setGolesVisitante((int) modeloTabla.getValueAt(i, 3));
					eq.setLugar((String) modeloTabla.getValueAt(i, 4));
					eq.setFecha((String) modeloTabla.getValueAt(i, 5));

					listaAGuardar.add(eq);
				}

				try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
					oos.writeObject(listaAGuardar);
					JOptionPane.showMessageDialog(Main.this, "Datos guardados exitosamente en " + ARCHIVO, "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Main.this, "Error crítico al intentar guardar el archivo.",
							"Error I/O", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnGuardar.setBounds(280, 340, 89, 23);
		contentPane.add(btnGuardar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 374, 414, 176);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Equipo Local", "Equipo Visitante",
				"Goles Local", "Goles Visitante", "Lugar", "Fecha" }));
		scrollPane.setViewportView(table);
	}

	public void insertarDatos(Equipo equipo) {
		DefaultTableModel modeloTabla = (DefaultTableModel) table.getModel();

		Object[] fila = new Object[] { equipo.getEquipoLocal(), equipo.getEquipoVisitante(), equipo.getGolesLocal(),
				equipo.getGolesVisitante(), equipo.getLugar(), equipo.getFecha() };

		modeloTabla.addRow(fila);

		limpiarCampos();
	}

	private void limpiarCampos() {
		textFieldEquipoLocal.setText("");
		textFieldEquipoVisitante.setText("");
		textFieldGolesLocal.setText("");
		textFieldGolesVisitante.setText("");
		textFieldLugar.setText("");
		textFieldFecha.setText("");
	}
}
