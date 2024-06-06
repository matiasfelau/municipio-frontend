package ar.edu.uade.municipio_frontend.Models;

import android.content.ContentValues;

import java.math.BigDecimal;
import java.time.LocalTime;

import ar.edu.uade.municipio_frontend.Database.DataContract;

public class Sitio {
    Integer idSitio;
    BigDecimal latitud;
    BigDecimal longitud;
    String calle;
    Integer numero;
    String entreCalleA;
    String entreCalleB;
    String descripcion;
    String aCargoDe;
    LocalTime apertura;
    LocalTime cierre;
    String comentarios;
    String idPrevisorio;

    public Sitio() {
    }

    public Sitio(Integer idSitio, String descripcion) {
        this.idSitio = idSitio;
        this.descripcion = descripcion;
    }

    public Sitio(BigDecimal latitud, BigDecimal longitud, String descripcion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    public Sitio(BigDecimal latitud, BigDecimal longitud, String calle, int numero, String entreCalleA, String entreCalleB, String descripcion, String aCargoDe, LocalTime apertura, LocalTime cierre, String comentarios) {
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

    public Sitio(Integer idSitio, BigDecimal latitud, BigDecimal longitud, String calle, int numero, String entreCalleA, String entreCalleB, String descripcion, String aCargoDe, LocalTime apertura, LocalTime cierre, String comentarios) {
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

    public Integer getIdSitio() {
        return idSitio;
    }
    public void setIdSitio(Integer idSitio) {
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

    public String getIdPrevisorio() {
        return idPrevisorio;
    }

    public void setIdPrevisorio(String idPrevisorio) {
        this.idPrevisorio = idPrevisorio;
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

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DataContract.ReclamosEntry.IDPRELIMINAR,idPrevisorio);
        cv.put(DataContract.SitiosEntry.LATITUD,latitud.toString());
        cv.put(DataContract.SitiosEntry.LONGITUD,longitud.toString());
        cv.put(DataContract.SitiosEntry.DESCRIPCION,descripcion);
        return cv;
    }
}
