package ar.edu.uade.municipio_frontend.Models;

import java.util.ArrayList;
import java.util.List;

public class Publicacion {

    private Integer id;
    private String titulo;
    private String descripcion;
    private String autor;
    private String fecha;
    private List<String> imageUris;

    public Publicacion(Integer id, String titulo, String descripcion, String autor, String fecha, List<String> imageUris) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.autor = autor;
        this.fecha = fecha;
        this.imageUris = imageUris;
    }

    public Publicacion() {
    }

    public Publicacion(String titulo, String descripcion, String autor, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.autor = autor;
        this.fecha = fecha;
    }

    public Publicacion(int id, String titulo, String descripcion, String autor, String fecha, List<String> imageUris) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.autor = autor;
        this.fecha = fecha;
        this.imageUris = imageUris;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<String> getImageUris() {
        return imageUris;
    }

    public void setImageUris(List<String> imageUris) {
        this.imageUris = imageUris;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", autor='" + autor + '\'' +
                ", fecha='" + fecha + '\'' +
                ", imageUris=" + imageUris +
                '}';
    }
}
