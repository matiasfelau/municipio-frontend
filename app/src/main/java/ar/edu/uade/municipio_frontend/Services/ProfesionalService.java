package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.AutenticacionReclamo;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Token;
import ar.edu.uade.municipio_frontend.Utilities.Container.AutenticacionProfesional;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProfesionalService {
    public final String API_ROUTE = "/profesionales";

    @PUT(API_ROUTE+"/todos"+"/{pagina}")
    Call<List<Profesional>> getAll(@Path("pagina") int pagina, @Body Autenticacion autenticacion);

    @PUT(API_ROUTE+"/cantidad-paginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);

    @POST(API_ROUTE+"/nuevo")
    Call<Profesional> nuevoProfesional(@Body AutenticacionProfesional autenticacionProfesional);

    @Multipart
    @POST(API_ROUTE+"/subir-imagenes"+"/{idProfesional}")
    Call<ResponseBody> uploadImages(@Path("idProfesional") int idProfesional, @Part List<MultipartBody.Part> image);
}
