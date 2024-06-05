package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Sector;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface SectorService {
    public final String API_ROUTE = "/rubro";

    @PUT(API_ROUTE+"/todos")
    Call<List<Sector>> getSectores(@Body Autenticacion autenticacion);
}
