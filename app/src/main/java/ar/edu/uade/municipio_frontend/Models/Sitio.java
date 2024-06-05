package ar.edu.uade.municipio_frontend.Models;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Sitio {
    int idSitio;
    BigDecimal latitud;
    BigDecimal longitud;
    String calle;
    int numero;
    String entreCalleA;
    String entreCalleB;
    String descripcion;
    String aCargoDe;
    LocalTime apertura;
    LocalTime cierre;
    String comentarios;

    public Sitio() {
    }

    public Sitio(int idSitio, BigDecimal latitud, BigDecimal longitud, String calle, int numero, String entreCalleA, String entreCalleB, String descripcion, String aCargoDe, LocalTime apertura, LocalTime cierre, String comentarios) {
        this.idSitio = idSitio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.calle = calle;
        this.numero = numero;
        this.entreCalleA = entreCalleA;
        this.entreCalleB = entreCalleB;
        this.descripcion = descripcion;
        this.aCargoDe = aCargoDe;
        this.apertura = apertura;
        this.cierre = cierre;
        this.comentarios = comentarios;
    }

    public int getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(int idSitio) {
        this.idSitio = idSitio;
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEntreCalleA() {
        return entreCalleA;
    }

    public void setEntreCalleA(String entreCalleA) {
        this.entreCalleA = entreCalleA;
    }

    public String getEntreCalleB() {
        return entreCalleB;
    }

    public void setEntreCalleB(String entreCalleB) {
        this.entreCalleB = entreCalleB;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getaCargoDe() {
        return aCargoDe;
    }

    public void setaCargoDe(String aCargoDe) {
        this.aCargoDe = aCargoDe;
    }

    public LocalTime getApertura() {
        return apertura;
    }

    public void setApertura(LocalTime apertura) {
        this.apertura = apertura;
    }

    public LocalTime getCierre() {
        return cierre;
    }

    public void setCierre(LocalTime cierre) {
        this.cierre = cierre;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "Sitio{" +
                "idSitio=" + idSitio +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", calle='" + calle + '\'' +
                ", numero=" + numero +
                ", entreCalleA='" + entreCalleA + '\'' +
                ", entreCalleB='" + entreCalleB + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", aCargoDe='" + aCargoDe + '\'' +
                ", apertura=" + apertura +
                ", cierre=" + cierre +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
