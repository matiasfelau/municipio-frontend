package ar.edu.uade.municipio_frontend.POJOs;

public class Empleado {
    int legajo;
    String password;

    public Empleado(int legajo, String password) {
        this.legajo = legajo;
        this.password = password;
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "legajo=" + legajo +
                ", password='" + password + '\'' +
                '}';
    }
}
