package ar.edu.uade.municipio_frontend.Models;

import androidx.annotation.NonNull;

public class MovimientoDenuncia {
    int idMovimiento;
    String responsable;
    String causa;
    String fecha;
    int idDenuncia;

    public MovimientoDenuncia() {
    }

    public MovimientoDenuncia(int idMovimiento, String responsable, String causa, String fecha, int idReclamo) {
        this.idMovimiento = idMovimiento;
        this.responsable = responsable;
        this.causa = causa;
        this.fecha = fecha;
        this.idDenuncia = idReclamo;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(int idReclamo) {
        this.idDenuncia = idReclamo;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovimientoDenuncia{" +
                "idMovimiento=" + idMovimiento +
                ", responsable='" + responsable + '\'' +
                ", causa='" + causa + '\'' +
                ", fecha=" + fecha +
                ", idDenuncia=" + idDenuncia +
                '}';
    }
}
