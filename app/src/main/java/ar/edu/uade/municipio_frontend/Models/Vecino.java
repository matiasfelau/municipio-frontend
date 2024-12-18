package ar.edu.uade.municipio_frontend.Models;

import android.content.ContentValues;

import ar.edu.uade.municipio_frontend.Database.DataContract;

public class Vecino {
    private String nombre;
    private String apellido;
    private String documento;
    private String email;
    private String password;

    public Vecino() {
    }

    public Vecino(String nombre, String apellido, String documento, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.email = email;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Vecino{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", documento='" + documento + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DataContract.VecinosEntry.NOMBRE,nombre);
        cv.put(DataContract.VecinosEntry.APELLIDO,apellido);
        cv.put(DataContract.VecinosEntry.DOCUMENTO,documento);
        cv.put(DataContract.VecinosEntry.EMAIL,email);
        cv.put(DataContract.VecinosEntry.PASSWORD,password);
        return cv;
    }

}
