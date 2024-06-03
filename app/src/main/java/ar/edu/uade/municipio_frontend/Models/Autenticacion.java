package ar.edu.uade.municipio_frontend.Models;

public class Autenticacion {
    private String tipo;
    private String token;

    public Autenticacion() {
    }

    public Autenticacion(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Autenticacion{" +
                "token='" + token + '\'' +
                ", tipoUsuario='" + tipo + '\'' +
                '}';
    }
}
