package ar.edu.uade.municipio_frontend.Models;

import androidx.annotation.NonNull;

public class DocumentoToken {
    String documento;
    String token;

    public DocumentoToken() {
    }

    public DocumentoToken(String documento, String token) {
        this.documento = documento;
        this.token = token;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public String toString() {
        return "DocumentoToken{" +
                "documento='" + documento + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
