package ar.edu.uade.municipio_frontend.Models;

public class Filtro {
    private String tipo;
    private String dato;

    public Filtro() {
    }

    public Filtro(String tipo, String dato) {
        this.tipo = tipo;
        this.dato = dato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
                "tipoFiltro='" + tipo + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}
