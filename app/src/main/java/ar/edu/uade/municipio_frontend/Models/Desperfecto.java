package ar.edu.uade.municipio_frontend.Models;

public class Desperfecto {
    int idDesperfecto;
    String descripcion;
    int idRubro;

    public Desperfecto() {
    }

    public Desperfecto(String descripcion, int idRubro) {
        this.descripcion = descripcion;
        this.idRubro = idRubro;
    }

    public Desperfecto(int idDesperfecto, String descripcion, int idRubro) {
        this.idDesperfecto = idDesperfecto;
        this.descripcion = descripcion;
        this.idRubro = idRubro;
    }

    public int getIdDesperfecto() {
        return idDesperfecto;
    }

    public void setIdDesperfecto(int idDesperfecto) {
        this.idDesperfecto = idDesperfecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(int idRubro) {
        this.idRubro = idRubro;
    }

    @Override
    public String toString() {
        return "Desperfecto{" +
                "idDesperfecto=" + idDesperfecto +
                ", descripcion='" + descripcion + '\'' +
                ", idRubro=" + idRubro +
                '}';
    }
}
