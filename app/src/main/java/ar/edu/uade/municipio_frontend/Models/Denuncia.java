package ar.edu.uade.municipio_frontend.Models;

public class Denuncia {

    private Integer idDenuncia;
    private String descripcion;
    private String estado;
    private Boolean aceptarResponsabilidad;
    private String documento;

    public Denuncia(Integer idDenuncia, String descripcion, String estado, Boolean aceptarResponsabilidad, String documento) {
        this.idDenuncia = idDenuncia;
        this.descripcion = descripcion;
        this.estado = estado;
        this.aceptarResponsabilidad = aceptarResponsabilidad;
        this.documento = documento;
    }

    public Integer getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(Integer idDenuncia) {
        this.idDenuncia = idDenuncia;
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

    public Boolean getAceptarResponsabilidad() {
        return aceptarResponsabilidad;
    }

    public void setAceptarResponsabilidad(Boolean aceptarResponsabilidad) {
        this.aceptarResponsabilidad = aceptarResponsabilidad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Denuncia{" +
                "idDenuncia=" + idDenuncia +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", aceptarResponsabilidad=" + aceptarResponsabilidad +
                ", documento='" + documento + '\'' +
                '}';
    }
}
