package ar.edu.uade.municipio_frontend.Models;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Profesional {
    private String nombre;
    private String rubro;
    private String descripcion;
    private String direccion;
    private int telefono;
    private String email;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String inicioJornada;
    private String finJornada;
    private String documento;
    private List<String> images = new ArrayList<>();

    public Profesional(String nombre, String rubro, String descripcion, String direccion, int telefono, String email, BigDecimal latitud, BigDecimal longitud, String inicioJornada, String finJornada, String documento, List<String> images) {
        this.nombre = nombre;
        this.rubro = rubro;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.latitud = latitud;
        this.longitud = longitud;
        this.inicioJornada = inicioJornada;
        this.finJornada = finJornada;
        this.documento = documento;
        this.images = images;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(String inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public String getFinJornada() {
        return finJornada;
    }

    public void setFinJornada(String finJornada) {
        this.finJornada = finJornada;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Profesional{" +
                "nombre='" + nombre + '\'' +
                ", rubro='" + rubro + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", inicioJornada='" + inicioJornada + '\'' +
                ", finJornada='" + finJornada + '\'' +
                ", documento='" + documento + '\'' +
                ", images=" + images +
                '}';
    }
}
