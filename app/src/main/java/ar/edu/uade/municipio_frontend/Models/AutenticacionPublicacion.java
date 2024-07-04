package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionPublicacion {
    private Publicacion publicacion;
    private Autenticacion autenticacion;

    public AutenticacionPublicacion(Publicacion publicacion, Autenticacion autenticacion) {
        this.publicacion = publicacion;
        this.autenticacion = autenticacion;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    @Override
    public String toString() {
        return "AutenticacionPublicacion{" +
                "publicacion=" + publicacion +
                ", autenticacion=" + autenticacion +
                '}';
    }
}