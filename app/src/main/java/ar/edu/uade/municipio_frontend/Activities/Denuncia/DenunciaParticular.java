package ar.edu.uade.municipio_frontend.Activities.Denuncia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Denuncia;
import ar.edu.uade.municipio_frontend.Models.Denunciado;
import ar.edu.uade.municipio_frontend.Models.MovimientoDenuncia;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.DenunciaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DenunciaParticular extends AppCompatActivity {
    //instanciacion
    ImageButton botonVolver;
    Autenticacion autenticacion;
    String idDenunciaString;
    TextView idDenuncia;
    TextView estadoDenuncia;
    TextView descripcion;
    TextView denunciado;
    ListView listMovimientosDenuncias;
    LinearLayout contenedorArchivos;
    Denuncia denuncia;
    List<String> movimientos;
    ArrayAdapter<String> adapter;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_denuncia_particular);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        //inicializacion
        botonVolver = findViewById(R.id.botonVolver);

        idDenuncia = findViewById(R.id.idDenuncia);

        idDenunciaString = getIntent().getStringExtra("id");

        idDenuncia.setText(idDenunciaString);

        estadoDenuncia = findViewById(R.id.estadoDenuncia);

        descripcion = findViewById(R.id.descripcion);

        denunciado = findViewById(R.id.idDenunciado);

        listMovimientosDenuncias = findViewById(R.id.listDenuncias);

        movimientos = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movimientos);

        listMovimientosDenuncias.setAdapter(adapter);

        contenedorArchivos = findViewById(R.id.linearLayoutFotos);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO")
        );

        getDenuncia(getIntent().getIntExtra("id", 0), autenticacion);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(DenunciaParticular.this, VerDenuncias.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);

            }
        });


    }

    private void getDenuncia(Integer id, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        System.out.println(autenticacion.getToken());

        Call<Denuncia> call = denunciaService.getDenuncia(id, autenticacion);

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                if (response.code() == 200) {//este created
                    System.out.println(response.code());

                    denuncia = response.body();

                    assert denuncia != null;
                    idDenuncia.setText(String.valueOf(denuncia.getIdDenuncia()));

                    estadoDenuncia.setText(denuncia.getEstado());

                    descripcion.setText(denuncia.getDescripcion());

                    getDenunciado(denuncia.getIdDenuncia());

                    getFotos(denuncia.getIdDenuncia());

                    System.out.println(denuncia.toString());

                    getMovimientos(denuncia.getIdDenuncia());

                } else if (response.code() == 400) {//este? badrequest?
                    System.out.println(response.code());
                } else if (response.code() == 401) {//este? unauthorized?
                    System.out.println(response.code());
                } else if (response.code() == 403) {//este forbbiden
                    System.out.println(response.code());
                } else if (response.code() == 404) {//not found?
                    System.out.println(response.code());
                } else if (response.code() == 500) {//este internal error server
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Denuncia> call, @NonNull Throwable t) {

            }
        });
    }

    private void getDenunciado(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        System.out.println("BUSCANDO DENUNCIADO...");

        Call<Denunciado> call = denunciaService.getDenunciado(id);

        call.enqueue(new Callback<Denunciado>() {
            @Override
            public void onResponse(@NonNull Call<Denunciado> call, @NonNull Response<Denunciado> response) {
                if (response.code() == 200) {//este OK
                    System.out.println("DENUNCIADO ENCONTRADO...");
                    if(response.body() != null){
                        denunciado.setText(response.body().getDenunciado());
                    }else{
                        System.out.println("ERROR DENUNCIADO VACIO");
                    }

                } else if (response.code() == 400) {//este? badrequest?
                    System.out.println(response.code());
                } else if (response.code() == 401) {//este? unauthorized?
                    System.out.println(response.code());
                } else if (response.code() == 403) {//este forbbiden
                    System.out.println(response.code());
                } else if (response.code() == 404) {//not found?
                    System.out.println(response.code());
                } else if (response.code() == 500) {//este internal error server
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Denunciado> call, @NonNull Throwable t) {
                System.out.println("error en denunciado: "+ t.getMessage());
            }
        });
    }

    private void addFileToLayout(String url) {
        if (contenedorArchivos == null) {
            System.out.println("El contenedor de archivos es nulo");
            return;
        }

        if (url.toLowerCase().endsWith(".pdf")) {
            mostrarPdf(url);
        } else if (url.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
            addImageToLayout(url);
        } else {
            Toast.makeText(this, "Tipo de archivo no soportado", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void mostrarPdf(String urlPdf) {
        WebView webView = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        webView.setLayoutParams(params);
        contenedorArchivos.addView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String pdfUrl = "https://docs.google.com/gview?embedded=true&url=" + urlPdf;
        webView.loadUrl(pdfUrl);
    }

    private void addImageToLayout(String url) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        contenedorArchivos.addView(imageView);
        System.out.println("URL de la imagen: " + url);

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.logRootCauses("Glide");
                        }
                        System.out.println("Error al cargar la imagen: " + (e != null ? e.getMessage() : "Desconocido"));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        System.out.println("Imagen cargada exitosamente");
                        return false;
                    }
                })
                .into(imageView);
    }
    private void getFotos(int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        Call<List<String>> call = denunciaService.getFotos(id);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.code() == 200) {//este created
                    System.out.println("LAS FOTOS DAN" + response.code());

                    List<String> imageUrls = response.body();
                    assert imageUrls != null;
                    for (String url : imageUrls) {
                        addFileToLayout(url);
                    }

                } else if (response.code() == 400) {//este? badrequest?
                    System.out.println(response.code());
                } else if (response.code() == 401) {//este? unauthorized?
                    System.out.println(response.code());
                } else if (response.code() == 403) {//este forbbiden
                    System.out.println(response.code());
                } else if (response.code() == 404) {//not found?
                    System.out.println(response.code());
                } else if (response.code() == 500) {//este internal error server
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {

            }
        });
    }

    private void addItem(MovimientoDenuncia movimientoDenuncia){
        if (movimientoDenuncia != null) {
            System.out.println(movimientoDenuncia.toString());
            adapter.add(movimientoDenuncia.getCausa()+" "+movimientoDenuncia.getFecha().substring(0,10));
        }

    }

    private void getMovimientos(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

            DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        Call<List<MovimientoDenuncia>> call = denunciaService.getMovimientos(id);

        call.enqueue(new Callback<List<MovimientoDenuncia>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovimientoDenuncia>> call, @NonNull Response<List<MovimientoDenuncia>> response) {
                System.out.println("Llamando a movimientos");
                if (response.code() == 200) {//este created

                    assert response.body() != null;
                    for (MovimientoDenuncia movimientoDenuncia : response.body()) {
                        addItem(movimientoDenuncia);

                    }
                    adapter.notifyDataSetChanged();

                } else if (response.code() == 400) {//este? badrequest?
                    System.out.println(response.code());
                } else if (response.code() == 401) {//este? unauthorized?
                    System.out.println(response.code());
                } else if (response.code() == 403) {//este forbbiden
                    System.out.println(response.code());
                } else if (response.code() == 404) {//not found?
                    System.out.println(response.code());
                } else if (response.code() == 500) {//este internal error server
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MovimientoDenuncia>> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

}
