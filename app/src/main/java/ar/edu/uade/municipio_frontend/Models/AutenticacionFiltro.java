package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionFiltro {
    private Autenticacion autenticacion;
    private Filtro filtro;
    public AutenticacionFiltro() {
    }

    public AutenticacionFiltro(Autenticacion autenticacion, Filtro filtro) {
        this.autenticacion = autenticacion;
        this.filtro = filtro;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    @Override
    public String toString() {
        return "AutenticacionFiltro{" +
                "autenticacion=" + autenticacion +
                ", filtro=" + filtro +
                '}';
    }
}
