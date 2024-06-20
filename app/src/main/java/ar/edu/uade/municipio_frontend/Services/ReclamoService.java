package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.AutenticacionReclamo;
import ar.edu.uade.municipio_frontend.Models.MovimientoReclamo;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReclamoService {
    public final String API_ROUTE = "/reclamo";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Reclamo>> getReclamos(@Path("pagina") int pagina, @Body AutenticacionFiltro autenticacionFiltro);

    @PUT(API_ROUTE+"/particular"+"/{id}")
    Call<Reclamo> getReclamo(@Path("id") int id, @Body Autenticacion autenticacion);

    @PUT(API_ROUTE+"/cantidadPaginas")
    Call<Integer> getPaginas(@Body AutenticacionFiltro autenticacionFiltro);

    @POST(API_ROUTE+"/nuevo")
    Call<Reclamo> nuevoReclamo(@Body AutenticacionReclamo autenticacionReclamo);

    @GET(API_ROUTE+"/fotos"+"/{idReclamo}")
    Call<List<String>> getFotos(@Path("idReclamo") Integer idReclamo);

    @GET(API_ROUTE+"/movimientos"+"/{idReclamo}")
    Call<List<MovimientoReclamo>> getMovimientos(@Path("idReclamo") Integer idReclamo);
}
