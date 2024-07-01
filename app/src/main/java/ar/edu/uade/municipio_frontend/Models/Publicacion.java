package ar.edu.uade.municipio_frontend.Models;

import android.net.Uri;

import java.util.List;

public class Publicacion {

    private String titulo;

    private String descripcion;

    private String autor;

    private String fecha;

    private List<Uri> imageUris;

    public Publicacion(String titulo, String descripcion, String autor, String fecha) {

        this.titulo = titulo;

        this.descripcion = descripcion;

        this.autor = autor;

        this.fecha = fecha;

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

    public List<Uri> getImageUris() {

        return imageUris;

    }

    public void setImageUris(List<Uri> imageUris) {

        this.imageUris = imageUris;

    }

}
