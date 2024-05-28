package ar.edu.uade.municipio_frontend.Services;

import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmpleadoService {
    public final String API_ROUTE = "/empleado";

    @POST(API_ROUTE+"/ingreso")
    Call<Token> getToken(@Body Empleado empleado);

    @GET(API_ROUTE+"/perfil"+"/{legajo}")
    Call<Empleado> getPerfil(@Path("legajo") int legajo);
}
