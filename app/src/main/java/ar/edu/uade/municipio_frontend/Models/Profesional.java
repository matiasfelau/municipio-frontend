package ar.edu.uade.municipio_frontend.Models;

import android.os.Build;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Profesional {
    private int idProfesional;
    private String nombre;
    private String direccion;
    private int telefono;
    private String email;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String inicioJornada;
    private String finJornada;
    private String documento;

    public Profesional(int idProfesional, String nombre, String direccion, int telefono, String email, BigDecimal latitud, BigDecimal longitud, String inicioJornada, String finJornada, String documento) {
        this.idProfesional = idProfesional;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.latitud = latitud;
        this.longitud = longitud;
        this.inicioJornada = inicioJornada;
        this.finJornada = finJornada;
        this.documento = documento;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
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

    @NonNull
    @Override
    public String toString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return this.nombre
                    + "\n"
                    + this.direccion
                    + " "
                    + this.inicioJornada.substring(0,5)
                    + " "
                    + this.finJornada.substring(0,5);
        }
        return "";
    }
}
