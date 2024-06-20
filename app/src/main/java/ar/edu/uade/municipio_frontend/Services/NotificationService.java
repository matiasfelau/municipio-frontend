package ar.edu.uade.municipio_frontend.Services;

import ar.edu.uade.municipio_frontend.Models.DocumentoToken;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationService {
    public final String API_ROUTE = "/notificacion";

    @POST(API_ROUTE+"/token")
    Call<Void> sendToken(@Body DocumentoToken documentoToken);
}
