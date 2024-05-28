package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.POJOs.Vecino;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VecinoService {

    public final String API_ROUTE = "/vecino";

    @GET(API_ROUTE+"/perfil"+"/{documento}")
    Call<Vecino> getPerfil(@Path("documento")String documento);
}
