package ar.edu.uade.municipio_frontend.Models;

public class Filtro {
    private String tipoFiltro;
    private String dato;

    public Filtro() {
    }

    public Filtro(String tipoFiltro, String dato) {
        this.tipoFiltro = tipoFiltro;
        this.dato = dato;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "Filtro{" +
                "tipoFiltro='" + tipoFiltro + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}
