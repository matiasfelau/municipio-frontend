package ar.edu.uade.municipio_frontend.Models;

public class Reclamo {
    private int idReclamo;
    private String descripcion;
    private String estado;
    private String documento;

    public Reclamo() {
    }

    public Reclamo(int idReclamo, String descripcion, String estado, String documento) {
        this.idReclamo = idReclamo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.documento = documento;
    }

    public int getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(int idReclamo) {
        this.idReclamo = idReclamo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Reclamo{" +
                "idReclamo=" + idReclamo +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", documento='" + documento + '\'' +
                '}';
    }
}
