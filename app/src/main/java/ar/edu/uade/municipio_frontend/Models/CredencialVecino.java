package ar.edu.uade.municipio_frontend.Models;

public class CredencialVecino {
    private String documento;
    private String password;
    private String email;

    public CredencialVecino(String documento, String password, String email) {
        this.documento = documento;
        this.password = password;
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CredencialVecino{" +
                "documento='" + documento + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
