package ar.edu.uade.municipio_frontend.Services;

import ar.edu.uade.municipio_frontend.POJOs.CredencialVecino;
import ar.edu.uade.municipio_frontend.POJOs.Empleado;
import ar.edu.uade.municipio_frontend.POJOs.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmpleadoService {
    public final String API_ROUTE = "/empleado";

    @POST(API_ROUTE+"/ingreso")
    Call<Token> getToken(@Body Empleado empleado);
}
