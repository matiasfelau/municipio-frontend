package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.Models.Sitio;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface SitioService {
    public final String API_ROUTE = "/sitio";

    @PUT(API_ROUTE+"/todos")
    Call<List<Sitio>> getSitios(@Body Autenticacion autenticacion);
}
