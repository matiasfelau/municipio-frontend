package ar.edu.uade.municipio_frontend.Models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Comercio {
    private Integer idComercio;
    private String nombre;
    private String documento;
    private String direccion;
    private String descripcion;
    private Integer telefono;
    private String apertura;
    private String cierre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private List<String> images = new ArrayList<>();


    public Comercio() {

    }

    public Comercio(String nombre,
                    String documento,
                    String direccion,
                    String descripcion,
                    Integer telefono,
                    String apertura,
                    String cierre,
                    BigDecimal latitud,
                    BigDecimal longitud,
                    List<String> images) {
        this.nombre = nombre;
        this.documento = documento;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.apertura = apertura;
        this.cierre = cierre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.images = images;
    }

    public Comercio(Integer idComercio, String nombre, String documento, String direccion, String descripcion, Integer telefono, String apertura, String cierre, BigDecimal latitud, BigDecimal longitud, List<String> images) {
        this.idComercio = idComercio;
        this.nombre = nombre;
        this.documento = documento;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.apertura = apertura;
        this.cierre = cierre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.images = images;
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
                ", documento='" + documento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", telefono=" + telefono +
                ", apertura='" + apertura + '\'' +
                ", cierre='" + cierre + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", images=" + images +
                '}';
    }
}
