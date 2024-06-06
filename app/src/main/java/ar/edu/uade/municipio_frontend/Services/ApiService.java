package ar.edu.uade.municipio_frontend.Services;

import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Token;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    public final String API_ROUTE = "/reclamo";

    @Multipart
    @POST(API_ROUTE+"/subirImagenes"+"/{idReclamo}")
    Call<ResponseBody> uploadImages(@Path("idReclamo") Integer idReclamo, @Part List<MultipartBody.Part> image);
}
