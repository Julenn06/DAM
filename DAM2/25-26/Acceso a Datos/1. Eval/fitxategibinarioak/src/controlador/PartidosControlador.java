package controlador;

import modelo.PartidosModelo;
import modelo.ResultadoPartido;
import vista.MainVista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PartidosControlador {
    private PartidosModelo modelo;
    private MainVista vista;

    public PartidosControlador(PartidosModelo modelo, MainVista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vista.addAñadirListener(new AñadirListener());
        this.vista.addCargarListener(new CargarListener());
        this.vista.addGuardarListener(new GuardarListener());
        this.vista.setTablaDatos(modelo.getLista());
    }

    private boolean validarTexto(String texto) {
        return texto.matches("[A-Za-z0-9 ]{1,20}");
    }

    private boolean validarGoles(String goles) {
        return goles.matches("\\d{1,2}");
    }

    class AñadirListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String eqLocal = vista.getEquipoLocal();
            String eqVisitante = vista.getEquipoVisitante();
            String golesLocStr = vista.getGolesLocal();
            String golesVisStr = vista.getGolesVisitante();
            String lugarStr = vista.getLugar();
            String fechaStr = vista.getFecha();
            if (!validarTexto(eqLocal)) {
                vista.mostrarMensaje("Equipo local: 1-20 letras/números.");
                return;
            }
            if (!validarTexto(eqVisitante)) {
                vista.mostrarMensaje("Equipo visitante: 1-20 letras/números.");
                return;
            }
            if (!validarTexto(lugarStr)) {
                vista.mostrarMensaje("Lugar: 1-20 letras/números.");
                return;
            }
            if (!validarGoles(golesLocStr)) {
                vista.mostrarMensaje("Goles local: 1-2 dígitos.");
                return;
            }
            if (!validarGoles(golesVisStr)) {
                vista.mostrarMensaje("Goles visitante: 1-2 dígitos.");
                return;
            }
            Date fechaDate;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                sdf.setLenient(false);
                fechaDate = sdf.parse(fechaStr);
            } catch (ParseException ex) {
                vista.mostrarMensaje("Fecha: formato dd/MM/yy.");
                return;
            }
            int golesLoc = Integer.parseInt(golesLocStr);
            int golesVis = Integer.parseInt(golesVisStr);
            ResultadoPartido rp = new ResultadoPartido(eqLocal, eqVisitante, golesLoc, golesVis, lugarStr, fechaDate);
            modelo.añadir(rp);
            vista.setTablaDatos(modelo.getLista());
            vista.limpiarCampos();
        }
    }

    class CargarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                modelo.cargar();
                vista.setTablaDatos(modelo.getLista());
                vista.mostrarMensaje("Datos cargados correctamente.");
            } catch (Exception ex) {
                vista.setTablaDatos(modelo.getLista());
                vista.mostrarMensaje("Error al cargar los datos: " + ex.getMessage());
            }
        }
    }

    class GuardarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                modelo.guardar();
                vista.mostrarMensaje("Datos guardados correctamente.");
            } catch (Exception ex) {
                vista.mostrarMensaje("Error al guardar los datos: " + ex.getMessage());
            }
        }
    }
}
