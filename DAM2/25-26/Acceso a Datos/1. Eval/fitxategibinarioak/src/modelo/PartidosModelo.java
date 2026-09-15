package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartidosModelo {
    private List<ResultadoPartido> lista = new ArrayList<>();
    private final String FICHERO = "Resultados.dat";

    public void añadir(ResultadoPartido p) {
        lista.add(p);
    }

    public List<ResultadoPartido> getLista() {
        return new ArrayList<>(lista);
    }

    public void limpiar() {
        lista.clear();
    }

    public void guardar() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FICHERO))) {
            dos.writeInt(lista.size());
            for (ResultadoPartido p : lista) {
                dos.writeUTF(p.getEquipoLocal());
                dos.writeUTF(p.getEquipoVisitante());
                dos.writeInt(p.getGolesLocal());
                dos.writeInt(p.getGolesVisitante());
                dos.writeUTF(p.getLugar());
                dos.writeLong(p.getFecha().getTime());
            }
        }
    }

    public void cargar() throws IOException {
        lista.clear();
        File f = new File(FICHERO);
        if (!f.exists() || f.length() == 0) return;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            int n = dis.readInt();
            for (int i = 0; i < n; i++) {
                String eqLocal = dis.readUTF();
                String eqVisitante = dis.readUTF();
                int golesLoc = dis.readInt();
                int golesVis = dis.readInt();
                String lugarStr = dis.readUTF();
                long fechaMillis = dis.readLong();
                lista.add(new ResultadoPartido(eqLocal, eqVisitante, golesLoc, golesVis, lugarStr, new Date(fechaMillis)));
            }
        }
    }
}
