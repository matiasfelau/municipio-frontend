package ar.edu.uade.municipio_frontend.Utilities.Container;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Profesional;

public class AutenticacionProfesional {
    private Autenticacion autenticacion;
    private Profesional profesional;

    public AutenticacionProfesional(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    @Override
    public String toString() {
        return "AutenticacionProfesional{" +
                "autenticacion=" + autenticacion +
                ", profesional=" + profesional +
                '}';
    }
}
