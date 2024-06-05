package ar.edu.uade.municipio_frontend.Models;

public class Reclamo {
    private Integer idReclamo;
    private String descripcion;
    private String estado;
    private String documento;
    private int idSitio;
    private int idDesperfecto;

    public Reclamo() {
    }

    public Reclamo(String descripcion, String estado, String documento, int idSitio, int idDesperfecto) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.documento = documento;
        this.idSitio = idSitio;
        this.idDesperfecto = idDesperfecto;
    }

    public Reclamo(int idReclamo, String descripcion, String estado, String documento, int idSitio, int idDesperfecto) {
        this.idReclamo = idReclamo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.documento = documento;
        this.idSitio = idSitio;
        this.idDesperfecto = idDesperfecto;
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

    public int getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(int idSitio) {
        this.idSitio = idSitio;
    }

    public int getIdDesperfecto() {
        return idDesperfecto;
    }

    public void setIdDesperfecto(int idDesperfecto) {
        this.idDesperfecto = idDesperfecto;
    }

    @Override
    public String toString() {
        return "Reclamo{" +
                "idReclamo=" + idReclamo +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", documento='" + documento + '\'' +
                ", idSitio=" + idSitio +
                ", idDesperfecto=" + idDesperfecto +
                '}';
    }
}
