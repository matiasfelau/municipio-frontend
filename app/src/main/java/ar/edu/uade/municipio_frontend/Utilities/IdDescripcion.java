package ar.edu.uade.municipio_frontend.Utilities;

public class IdDescripcion {
    private String id;
    private String descripcion;

    public IdDescripcion() {
    }

    public IdDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public IdDescripcion(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " Descripción: " + this.descripcion;
    }
}
