package ar.edu.uade.municipio_frontend.Services;

import java.util.List;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PublicacionService {
    @GET("/publicaciones")
    Call<List<Publicacion>> getPublicaciones(@Query("pagina") int pagina);

    @POST("/publicaciones")
    Call<Publicacion> nuevaPublicacion(@Body Publicacion publicacion);
}
