package utils;

public final class SessionManager {
    private static SessionManager instantzia;
    private CheckLogin.ErabiltzaileData erabiltzaileaktuala;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instantzia == null) {
            instantzia = new SessionManager();
        }
        return instantzia;
    }

    public void saioaHasi(CheckLogin.ErabiltzaileData erabiltzailea) {
        this.erabiltzaileaktuala = erabiltzailea;
    }

    public CheckLogin.ErabiltzaileData getErabiltzaileaktuala() {
        return erabiltzaileaktuala;
    }

    public void saioaItxi() {
        this.erabiltzaileaktuala = null;
    }

    public boolean badagoSaioAktiboa() {
        return erabiltzaileaktuala != null;
    }

    public Long getErabiltzaileId() {
        return erabiltzaileaktuala != null ? erabiltzaileaktuala.getId() : null;
    }

    public String getErabiltzaileEmaila() {
        return erabiltzaileaktuala != null ? erabiltzaileaktuala.getEmail() : null;
    }

    public String getErabiltzaileIzenOsoa() {
        return erabiltzaileaktuala != null ? erabiltzaileaktuala.getIzenOsoa() : "Erabiltzailea";
    }

    public Integer getErabiltzaileMota() {
        return erabiltzaileaktuala != null ? erabiltzaileaktuala.getMota() : null;
    }
}