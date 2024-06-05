package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionReclamo {
    Autenticacion autenticacion;
    Reclamo reclamo;

    public AutenticacionReclamo() {
    }

    public AutenticacionReclamo(Autenticacion autenticacion, Reclamo reclamo) {
        this.autenticacion = autenticacion;
        this.reclamo = reclamo;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }
}
