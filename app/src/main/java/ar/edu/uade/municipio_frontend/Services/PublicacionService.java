package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PublicacionService {
    @PUT("/publicaciones/{pagina}")
    Call<List<Publicacion>> getPublicaciones(@Path("pagina") int pagina, @Body Autenticacion autenticacion);

    @PUT("/publicacion/{id}")
    Call<Publicacion> getPublicacion(@Path("id") int id, @Body Autenticacion autenticacion);

    @PUT("/publicaciones/cantidadPaginas")
    Call<Integer> getPaginas(@Body Autenticacion autenticacion);

    @POST("/publicaciones")
    Call<Publicacion> nuevaPublicacion(@Body Publicacion publicacion, @Body Autenticacion autenticacion);
}
