package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.Models.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfesionalService {
    public final String API_ROUTE = "/profesionales";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Profesional>> getAll(@Path("pagina") int pagina, @Body Autenticacion autenticacion);

    @PUT(API_ROUTE+"/cantidad-paginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);
}
