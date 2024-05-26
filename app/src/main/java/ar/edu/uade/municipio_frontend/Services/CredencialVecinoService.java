package ar.edu.uade.municipio_frontend.Services;

import ar.edu.uade.municipio_frontend.POJOs.CredencialVecino;
import ar.edu.uade.municipio_frontend.POJOs.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CredencialVecinoService {
    public final String API_ROUTE = "/vecino";

    @POST(API_ROUTE+"/registro")
    Call<Boolean> postVecino(@Body CredencialVecino credencialVecino);

    @POST(API_ROUTE+"/ingreso")
    Call<Token> getToken(@Body CredencialVecino credencialVecino);

    @GET(API_ROUTE+"/confirmar-primer-ingreso"+"/{documento}")
    Call<Boolean> getConfirmacion(@Path("documento") String documento);

    @PUT(API_ROUTE+"/primer-ingreso")
    Call<Boolean> modificarPassword(@Body CredencialVecino credencialVecino);
}
