package ar.edu.uade.municipio_frontend.Models;

import android.content.ContentValues;

import ar.edu.uade.municipio_frontend.Database.DataContract;

public class Invitado {
    int id;
    String nombre;

    public Invitado() {
    }

    public Invitado(String nombre) {
        this.nombre = nombre;
    }

    public Invitado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Invitado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    public ContentValues toContentValues() {
        ContentValues invitado = new ContentValues();

        invitado.put(DataContract.InvitadosEntry.ID, id);

        invitado.put(DataContract.InvitadosEntry.NOMBRE, nombre);

        return invitado;

    }
}
