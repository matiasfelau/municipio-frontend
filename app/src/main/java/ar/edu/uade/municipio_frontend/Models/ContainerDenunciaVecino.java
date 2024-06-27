package ar.edu.uade.municipio_frontend.Models;

public class ContainerDenunciaVecino {
    private Denuncia denuncia;
    private VecinoDenunciado vecinoDenunciado;

    public ContainerDenunciaVecino(Denuncia denuncia, VecinoDenunciado vecinoDenunciado) {
        this.denuncia = denuncia;
        this.vecinoDenunciado = vecinoDenunciado;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public VecinoDenunciado getVecinoDenunciado() {
        return vecinoDenunciado;
    }

    public void setVecinoDenunciado(VecinoDenunciado vecinoDenunciado) {
        this.vecinoDenunciado = vecinoDenunciado;
    }

    @Override
    public String toString() {
        return "ContainerDenunciaVecino{" +
                "denuncia=" + denuncia +
                ", vecinoDenunciado=" + vecinoDenunciado +
                '}';
    }
}
