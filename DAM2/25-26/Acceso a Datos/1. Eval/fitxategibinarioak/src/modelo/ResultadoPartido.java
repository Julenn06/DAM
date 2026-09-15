package modelo;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ResultadoPartido implements Serializable {
    private static final long serialVersionUID = 1L;
    private String equipoLocal;
    private String equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private String lugar;
    private Date fecha;

    public ResultadoPartido(String equipoLocal, String equipoVisitante, int golesLocal, int golesVisitante, String lugar, Date fecha) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public String getEquipoLocal() { return equipoLocal; }
    public String getEquipoVisitante() { return equipoVisitante; }
    public int getGolesLocal() { return golesLocal; }
    public int getGolesVisitante() { return golesVisitante; }
    public String getLugar() { return lugar; }
    public Date getFecha() { return fecha; }
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(fecha);
    }
}