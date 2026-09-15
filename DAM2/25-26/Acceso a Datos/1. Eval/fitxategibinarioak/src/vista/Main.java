package vista;

import controlador.PartidosControlador;
import modelo.PartidosModelo;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            PartidosModelo modelo = new PartidosModelo();
            MainVista vista = new MainVista();
            new PartidosControlador(modelo, vista);
            vista.setVisible(true);
        });
    }
}