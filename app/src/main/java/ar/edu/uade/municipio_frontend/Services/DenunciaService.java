package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionDenuncia;
import ar.edu.uade.municipio_frontend.Models.AutenticacionDenunciaComercio;
import ar.edu.uade.municipio_frontend.Models.AutenticacionDenunciaVecino;
import ar.edu.uade.municipio_frontend.Models.Denuncia;
import ar.edu.uade.municipio_frontend.Models.Denunciado;
import ar.edu.uade.municipio_frontend.Models.MovimientoDenuncia;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DenunciaService {
    public final String API_ROUTE = "/denuncia";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Denuncia>> getDenuncias(@Path("pagina") int pagina, @Body AutenticacionDenuncia autenticaciondenuncia);
    @PUT(API_ROUTE+"/particular"+"/{id}")
    Call<Denuncia> getDenuncia(@Path("id") int id, @Body Autenticacion autenticacion);
    @PUT(API_ROUTE+"/cantidadPaginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);
    @POST(API_ROUTE+"/nueva-denuncia-vecino")
    Call<Denuncia> nuevaDenunciaVecino(@Body AutenticacionDenunciaVecino autenticacionDenunciaVecino);
    @POST(API_ROUTE+"/nueva-denuncia-comercio")
    Call<Denuncia> nuevaDenunciaComercio(@Body AutenticacionDenunciaComercio autenticacionDenunciaComercio);
    @GET(API_ROUTE+"/denunciado/{id}")
    Call<Denunciado> getDenunciado(@Path("id") int id);
    @GET(API_ROUTE+"/fotos"+"/{idDenuncia}")
    Call<List<String>> getFotos(@Path("idDenuncia") Integer idDenuncia);
    @GET(API_ROUTE+"/movimientos"+"/{idDenuncia}")
    Call<List<MovimientoDenuncia>> getMovimientos(@Path("idDenuncia") Integer idDenuncia);
}
