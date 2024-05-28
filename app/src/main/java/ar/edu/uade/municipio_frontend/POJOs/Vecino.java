package ar.edu.uade.municipio_frontend.POJOs;

import android.content.ContentValues;

import androidx.annotation.NonNull;

import ar.edu.uade.municipio_frontend.Utilities.DataContract;

public class Vecino {
    private String nombre;
    private String apellido;
    private String documento;
    private String email;

    public Vecino(String nombre, String apellido, String documento, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.email = email;
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
    public ContentValues toConentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DataContract.VecinosEntry.NOMBRE,nombre);
        cv.put(DataContract.VecinosEntry.APELLIDO,apellido);
        cv.put(DataContract.VecinosEntry.DOCUMENTO,documento);
        cv.put(DataContract.VecinosEntry.EMAIL,email);
        return cv;
    }

}
