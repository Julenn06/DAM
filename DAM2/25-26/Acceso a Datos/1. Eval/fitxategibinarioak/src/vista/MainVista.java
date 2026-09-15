package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainVista extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField local;
    private JTextField visitante;
    private JTextField golesLocal;
    private JTextField golesVisitante;
    private JTextField lugar;
    private JTextField fecha;
    private DefaultTableModel modelo;
    private JTable tabla;
    private JButton btnAñadir, btnCargar, btnGuardar;

    public MainVista() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 520, 540);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Equipo Local");
        lblNewLabel.setBounds(70, 55, 104, 14);
        contentPane.add(lblNewLabel);

        JLabel lblEquipoVisitante = new JLabel("Equipo Visitante");
        lblEquipoVisitante.setBounds(70, 86, 104, 14);
        contentPane.add(lblEquipoVisitante);

        JLabel lblNewLabel_1_1 = new JLabel("Goles Local");
        lblNewLabel_1_1.setBounds(70, 117, 104, 14);
        contentPane.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel("Goles Visitante");
        lblNewLabel_1_1_1.setBounds(70, 148, 104, 14);
        contentPane.add(lblNewLabel_1_1_1);

        JLabel lblNewLabel_1_1_1_1 = new JLabel("Lugar");
        lblNewLabel_1_1_1_1.setBounds(70, 179, 104, 14);
        contentPane.add(lblNewLabel_1_1_1_1);

        JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Fecha");
        lblNewLabel_1_1_1_1_1.setBounds(70, 210, 104, 14);
        contentPane.add(lblNewLabel_1_1_1_1_1);

        local = new JTextField();
        local.setBounds(200, 52, 86, 20);
        contentPane.add(local);
        local.setColumns(10);

        visitante = new JTextField();
        visitante.setColumns(10);
        visitante.setBounds(200, 83, 86, 20);
        contentPane.add(visitante);

        golesLocal = new JTextField();
        golesLocal.setColumns(10);
        golesLocal.setBounds(200, 114, 86, 20);
        contentPane.add(golesLocal);

        golesVisitante = new JTextField();
        golesVisitante.setColumns(10);
        golesVisitante.setBounds(200, 145, 86, 20);
        contentPane.add(golesVisitante);

        lugar = new JTextField();
        lugar.setColumns(10);
        lugar.setBounds(200, 176, 86, 20);
        contentPane.add(lugar);

        fecha = new JTextField();
        fecha.setColumns(10);
        fecha.setBounds(200, 207, 86, 20);
        contentPane.add(fecha);

        String[] columnas = { "Equipo local", "Equipo visitante", "Goles local", "Goles visitante", "Lugar", "Fecha" };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBounds(10, 300, 486, 192);

        btnAñadir = new JButton("Añadir");
        btnAñadir.setBounds(101, 266, 89, 23);
        contentPane.add(btnAñadir);

        btnCargar = new JButton("Cargar");
        btnCargar.setBounds(200, 266, 89, 23);
        contentPane.add(btnCargar);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(299, 266, 89, 23);
        contentPane.add(btnGuardar);
    }

    public String getEquipoLocal() { return local.getText().trim(); }
    public String getEquipoVisitante() { return visitante.getText().trim(); }
    public String getGolesLocal() { return golesLocal.getText().trim(); }
    public String getGolesVisitante() { return golesVisitante.getText().trim(); }
    public String getLugar() { return lugar.getText().trim(); }
    public String getFecha() { return fecha.getText().trim(); }

    public void limpiarCampos() {
        local.setText("");
        visitante.setText("");
        golesLocal.setText("");
        golesVisitante.setText("");
        lugar.setText("");
        fecha.setText("");
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void setTablaDatos(java.util.List<modelo.ResultadoPartido> lista) {
        modelo.setRowCount(0);
        for (modelo.ResultadoPartido rp : lista) {
            modelo.addRow(new Object[] {
                rp.getEquipoLocal(),
                rp.getEquipoVisitante(),
                rp.getGolesLocal(),
                rp.getGolesVisitante(),
                rp.getLugar(),
                rp.getFechaFormateada()
            });
        }
    }

    public void addAñadirListener(ActionListener l) { btnAñadir.addActionListener(l); }
    public void addCargarListener(ActionListener l) { btnCargar.addActionListener(l); }
    public void addGuardarListener(ActionListener l) { btnGuardar.addActionListener(l); }
}
