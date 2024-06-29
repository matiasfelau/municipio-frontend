package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionComercio;
import ar.edu.uade.municipio_frontend.Models.AutenticacionReclamo;
import ar.edu.uade.municipio_frontend.Models.Comercio;
import ar.edu.uade.municipio_frontend.Models.Denuncia;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ComercioService {
    public final String API_ROUTE = "/comercio";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Comercio>> getComercios(@Path("pagina") int pagina, @Body Autenticacion autenticacion);

    @PUT(API_ROUTE+"/cantidadPaginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);

    @PUT(API_ROUTE+"/particular"+"/{id}")
    Call<Comercio> getComercio(@Path("id") int id, @Body Autenticacion autenticacion);

    @GET(API_ROUTE+"/fotos"+"/{idComercio}")
    Call<List<String>> getFotos(@Path("idComercio") Integer idComercio);

    @POST(API_ROUTE+"/nuevo")
    Call<Comercio> nuevoComercio(@Body AutenticacionComercio autenticacionComercio);
}
