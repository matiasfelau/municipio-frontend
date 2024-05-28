package ar.edu.uade.municipio_frontend.Models;

import android.content.ContentValues;

import ar.edu.uade.municipio_frontend.Database.DataContract;

public class Empleado {
    int legajo;

    String nombre;

    String apellido;

    String password;

    String sector;

    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String password, String sector) {
        this.nombre = nombre;

        this.apellido = apellido;

        this.password = password;

        this.sector = sector;

    }

    public Empleado(int legajo, String nombre, String apellido, String password, String sector) {
        this.legajo = legajo;

        this.nombre = nombre;

        this.apellido = apellido;

        this.password = password;

        this.sector = sector;

    }

    public int getLegajo() {
        return legajo;

    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;

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

    public String getPassword() {
        return password;

    }

    public void setPassword(String password) {
        this.password = password;

    }

    public String getSector() {
        return sector;

    }

    public void setSector(String sector) {
        this.sector = sector;

    }

    @Override
    public String toString() {
        return "Empleado{" +
                "legajo=" + legajo +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", password='" + password + '\'' +
                ", sector='" + sector + '\'' +
                '}';

    }

    public ContentValues toContentValues() {
        ContentValues empleado = new ContentValues();

        empleado.put(DataContract.EmpleadosEntry.NOMBRE, nombre);

        empleado.put(DataContract.EmpleadosEntry.APELLIDO, apellido);

        empleado.put(DataContract.EmpleadosEntry.LEGAJO, legajo);

        empleado.put(DataContract.EmpleadosEntry.SECTOR, sector);

        empleado.put(DataContract.EmpleadosEntry.PASSWORD, password);

        return empleado;

    }
}
