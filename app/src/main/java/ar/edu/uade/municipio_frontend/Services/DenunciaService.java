package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.MovimientoDenuncia;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DenunciaService {
    public final String API_ROUTE = "/denuncia";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Reclamo>> getDenuncias(@Path("pagina") int pagina, @Body Autenticacion autenticacion);
    @PUT(API_ROUTE+"/particular"+"/{id}")
    Call<Reclamo> getDenuncia(@Path("id") int id, @Body Autenticacion autenticacion);
    @PUT(API_ROUTE+"/cantidadPaginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);
    @POST(API_ROUTE+"/nuevo")
    Call<Reclamo> nuevaDenuncia(@Body Autenticacion autenticacion);
    @GET(API_ROUTE+"/fotos"+"/{idDenuncia}")
    Call<List<String>> getFotos(@Path("idDenuncia") Integer idDenuncia);
    @GET(API_ROUTE+"/movimientos"+"/{idDenuncia}")
    Call<List<MovimientoDenuncia>> getMovimientos(@Path("idDenuncia") Integer idDenuncia);
}
