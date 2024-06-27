package ar.edu.uade.municipio_frontend.Models;

public class ContainerDenunciaComercio {
    private Denuncia denuncia;
    private ComercioDenunciado comercioDenunciado;

    public ContainerDenunciaComercio(Denuncia denuncia, ComercioDenunciado comercioDenunciado) {
        this.denuncia = denuncia;
        this.comercioDenunciado = comercioDenunciado;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public ComercioDenunciado getComercioDenunciado() {
        return comercioDenunciado;
    }

    public void setComercioDenunciado(ComercioDenunciado comercioDenunciado) {
        this.comercioDenunciado = comercioDenunciado;
    }

    @Override
    public String toString() {
        return "ContainerDenunciaComercio{" +
                "denuncia=" + denuncia +
                ", comercioDenunciado=" + comercioDenunciado +
                '}';
    }
}
