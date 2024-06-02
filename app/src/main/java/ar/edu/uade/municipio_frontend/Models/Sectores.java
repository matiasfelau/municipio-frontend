package ar.edu.uade.municipio_frontend.Models;

public class Sectores {
    int idRubro;
    String descripcion;

    public Sectores() {
    }

    public Sectores(int idRubro, String descripcion) {
        this.idRubro = idRubro;
        this.descripcion = descripcion;
    }

    public int getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(int idRubro) {
        this.idRubro = idRubro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Sectores{" +
                "idRubro=" + idRubro +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
