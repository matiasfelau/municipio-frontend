package ar.edu.uade.municipio_frontend.Models;

public class Autenticacion {
    private String token;
    private String tipoUsuario;

    public Autenticacion() {
    }

    public Autenticacion(String token, String tipoUsuario) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Autenticacion{" +
                "token='" + token + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
