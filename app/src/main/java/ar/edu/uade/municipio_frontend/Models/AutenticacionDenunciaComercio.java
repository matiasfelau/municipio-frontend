package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionDenunciaComercio {
    private Autenticacion autenticacion;
    private ContainerDenunciaComercio containerDenunciaComercio;

    public AutenticacionDenunciaComercio(Autenticacion autenticacion, ContainerDenunciaComercio containerDenunciaComercio) {
        this.autenticacion = autenticacion;
        this.containerDenunciaComercio = containerDenunciaComercio;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public ContainerDenunciaComercio getContainerDenunciaComercio() {
        return containerDenunciaComercio;
    }

    public void setContainerDenunciaComercio(ContainerDenunciaComercio containerDenunciaComercio) {
        this.containerDenunciaComercio = containerDenunciaComercio;
    }

    @Override
    public String toString() {
        return "AutenticacionDenunciaComercio{" +
                "autenticacion=" + autenticacion +
                ", containerDenunciaComercio=" + containerDenunciaComercio +
                '}';
    }
}
