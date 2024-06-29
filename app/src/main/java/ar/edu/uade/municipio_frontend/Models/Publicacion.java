package ar.edu.uade.municipio_frontend.Models;

public class Publicacion {
    private String titulo;

    public Publicacion(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
