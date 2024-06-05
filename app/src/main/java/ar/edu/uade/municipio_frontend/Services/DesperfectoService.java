package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.Desperfecto;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DesperfectoService {
    public final String API_ROUTE = "/desperfecto";
    @PUT(API_ROUTE+"/todos")
    Call<List<Desperfecto>> getDesperfectos(@Body Autenticacion autenticacion);
}
