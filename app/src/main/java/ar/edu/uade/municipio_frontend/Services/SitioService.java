package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionSitio;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.Models.Sitio;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SitioService {
    public final String API_ROUTE = "/sitio";

    @PUT(API_ROUTE+"/todos")
    Call<List<Sitio>> getSitios(@Body Autenticacion autenticacion);

    @POST(API_ROUTE+"/nuevo")
    Call<Integer> nuevoSitio(@Body AutenticacionSitio autenticacionSitio);

    @GET(API_ROUTE+"/particular"+"/{idSitio}")
    Call<Sitio> getSitio(@Path("idSitio") Integer idSitio);
}
