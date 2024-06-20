package ar.edu.uade.municipio_frontend.Models;

import androidx.annotation.NonNull;

public class MovimientoReclamo {
    int idMovimiento;
    String responsable;
    String causa;
    String fecha;
    int idReclamo;

    public MovimientoReclamo() {
    }

    public MovimientoReclamo(int idMovimiento, String responsable, String causa, String fecha, int idReclamo) {
        this.idMovimiento = idMovimiento;
        this.responsable = responsable;
        this.causa = causa;
        this.fecha = fecha;
        this.idReclamo = idReclamo;
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

    public int getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(int idReclamo) {
        this.idReclamo = idReclamo;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovimientoReclamo{" +
                "idMovimiento=" + idMovimiento +
                ", responsable='" + responsable + '\'' +
                ", causa='" + causa + '\'' +
                ", fecha=" + fecha +
                ", idReclamo=" + idReclamo +
                '}';
    }
}
