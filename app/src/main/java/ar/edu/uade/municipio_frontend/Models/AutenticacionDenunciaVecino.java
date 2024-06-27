package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionDenunciaVecino {
    Autenticacion autenticacion;
    ContainerDenunciaVecino denuncia;

    public AutenticacionDenunciaVecino(Autenticacion autenticacion, ContainerDenunciaVecino denuncia) {
        this.autenticacion = autenticacion;
        this.denuncia = denuncia;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public ContainerDenunciaVecino getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(ContainerDenunciaVecino denuncia) {
        this.denuncia = denuncia;
    }

    @Override
    public String toString() {
        return "AutenticacionDenuncia{" +
                "autenticacion=" + autenticacion +
                ", denuncia=" + denuncia +
                '}';
    }
}
