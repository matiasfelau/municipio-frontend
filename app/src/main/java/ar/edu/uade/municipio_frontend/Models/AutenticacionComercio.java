package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionComercio {

    Autenticacion autenticacion;

    Comercio comercio;

    public AutenticacionComercio(){

    }

    public AutenticacionComercio(Autenticacion autenticacion, Comercio comercio) {
        this.autenticacion = autenticacion;
        this.comercio = comercio;
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
}
