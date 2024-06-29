package ar.edu.uade.municipio_frontend.Models;

import java.math.BigDecimal;

public class Comercio {

    private Integer idComercio;
    private String nombre;
    private String apertura;
    private String cierre;
    private String direccion;
    private Integer telefono;
    private String descripcion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String documento;

    public Comercio() {

    }

    public Comercio(Integer idComercio, String nombre, String apertura, String cierre, String direccion, Integer telefono, String descripcion, BigDecimal latitud, BigDecimal longitud, String documento) {
        this.idComercio = idComercio;
        this.nombre = nombre;
        this.apertura = apertura;
        this.cierre = cierre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.documento = documento;
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

    public String getApertura() {
        return apertura;
    }

    public void setApertura(String apertura) {
        this.apertura = apertura;
    }

    public String getCierre() {
        return cierre;
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Comercio{" +
                "idComercio=" + idComercio +
                ", nombre='" + nombre + '\'' +
                ", apertuna=" + apertura +
                ", cierre=" + cierre +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", descripcion='" + descripcion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", documento='" + documento + '\'' +
                '}';
    }
}
