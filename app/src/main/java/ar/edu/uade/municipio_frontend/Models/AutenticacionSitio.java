package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionSitio {
    Autenticacion autenticacion;
    Sitio sitio;

    @Override
    public String toString() {
        return "AutenticacionSitio{" +
                "autenticacion=" + autenticacion +
                ", sitio=" + sitio +
                '}';
    }

    public AutenticacionSitio() {
    }

    public AutenticacionSitio(Autenticacion autenticacion, Sitio sitio) {
        this.autenticacion = autenticacion;
        this.sitio = sitio;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Sitio getSitio() {
        return sitio;
    }

    public void setSitio(Sitio sitio) {
        this.sitio = sitio;
    }
}
