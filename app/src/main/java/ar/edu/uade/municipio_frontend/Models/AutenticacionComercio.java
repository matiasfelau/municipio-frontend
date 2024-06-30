package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionComercio {

    private Autenticacion autenticacion;

    private Comercio comercio;

    public AutenticacionComercio(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    @Override
    public String toString() {
        return "AutenticacionComercio{" +
                "autenticacion=" + autenticacion +
                ", comercio=" + comercio +
                '}';
    }
}
