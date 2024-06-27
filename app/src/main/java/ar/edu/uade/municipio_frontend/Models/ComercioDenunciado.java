package ar.edu.uade.municipio_frontend.Models;

public class ComercioDenunciado {
    private Integer idDenuncia;
    private Integer idComercio;
    private String nombre;
    private String direccion;

    public ComercioDenunciado(Integer idDenuncia, Integer idComercio, String nombre, String direccion) {
        this.idDenuncia = idDenuncia;
        this.idComercio = idComercio;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Integer getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(Integer idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public Integer getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(Integer idComercio) {
        this.idComercio = idComercio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "ComercioDenunciado{" +
                "idDenuncia=" + idDenuncia +
                ", idComercio=" + idComercio +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
