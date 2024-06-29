package ar.edu.uade.municipio_frontend.Models;

public class AutenticacionDenuncia {
    Autenticacion autenticacion;

    String documento;

    public AutenticacionDenuncia() {
    }

    public AutenticacionDenuncia(Autenticacion autenticacion, String documento) {
        this.autenticacion = autenticacion;
        this.documento = documento;
    }

    public Autenticacion getAutenticacion() {
        return autenticacion;
    }

    public void setAutenticacion(Autenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
