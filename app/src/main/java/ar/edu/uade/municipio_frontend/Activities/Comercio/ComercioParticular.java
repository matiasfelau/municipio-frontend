package ar.edu.uade.municipio_frontend.Activities.Comercio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ar.edu.uade.municipio_frontend.Models.Comercio;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ComercioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComercioParticular extends AppCompatActivity {
    TextView descripcionComercioParticular;
    TextView apertura;
    TextView direccion;
    TextView cierre;
    TextView nroTelefono;
    TextView nombreComercio;
    MapView map;
    Marker marker;
    ImageButton botonVolver;
    ImageButton novedades;
    Autenticacion autenticacion;
    LinearLayout contenedorFotos;
    Comercio comercio;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_comercio_particular);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        comercio = new Comercio();

        descripcionComercioParticular = findViewById(R.id.descripcionComercioParticular);

        autenticacion = new Autenticacion();

        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacion.setTipo("Vecino");

        apertura = findViewById(R.id.apertura);

        cierre = findViewById(R.id.cierre);

        direccion = findViewById(R.id.direccion);

        nroTelefono = findViewById(R.id.nroTelefono);

        nombreComercio = findViewById(R.id.nombreComercio);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));

        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));

        map = findViewById(R.id.map);

        map.setMultiTouchControls(true);

        map.getController().setZoom(15.0);

        map.invalidate();

        marker = new Marker(map);

        botonVolver = findViewById(R.id.botonVolver);

        novedades = findViewById(R.id.Novedades);

        contenedorFotos = findViewById(R.id.contenedorFotos);

        getComercio(getIntent().getIntExtra("id",0));
        //Listener
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(ComercioParticular.this, VerComercio.class);
                if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                }

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });

        novedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(ComercioParticular.this, ComercioParticular.class);//TODO FUNCIONAMIENTO CUANDO ESTE NOVEDADES
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("from", "ComercioParticular");

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                startActivity(nuevaActividad);
                //TODO FALTA ALGO QUE HAGA QUE SOLO TE MUESTRE LAS NOVEDADES DEL RECLAMO EN EL QUE ESTABAS
            }
        });

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;

            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;

            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);

        map.getOverlays().add(mapEventsOverlay);

    }

    private void getComercio(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ComercioService comercioService = retrofit.create(ComercioService.class);

        System.out.println(autenticacion.getToken());

        Call<Comercio> call = comercioService.getComercio(id, autenticacion);

        call.enqueue(new Callback<Comercio>() {
            @Override
            public void onResponse(@NonNull Call<Comercio> call, @NonNull Response<Comercio> response) {
                if (response.code() == 200) {//este created
                    System.out.println(response.code());
                    System.out.println("entro");

                    comercio = response.body();

                    assert comercio != null;
                    nombreComercio.setText(String.valueOf(comercio.getNombre()));

                    apertura.setText(String.valueOf(comercio.getApertura()));

                    cierre.setText(String.valueOf(comercio.getCierre()));

                    direccion.setText(String.valueOf(comercio.getDireccion()));

                    nroTelefono.setText(String.valueOf(comercio.getTelefono()));

                    descripcionComercioParticular.setText(comercio.getDescripcion());

                    getFotos(comercio.getIdComercio());

                    System.out.println(comercio.toString());


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
            public void onFailure(@NonNull Call<Comercio> call, @NonNull Throwable t) {

            }
        });
    }

    private void getFotos(int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ComercioService comercioService = retrofit.create(ComercioService.class);

        Call<List<String>> call = comercioService.getFotos(id);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.code() == 200) {//este created
                    System.out.println("LAS FOTOS DAN" + response.code());

                    List<String> imageUrls = response.body();
                    assert imageUrls != null;
                    for (String url : imageUrls) {
                        addImageToLayout(url);
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

    private void addImageToLayout(String url) {

        if (contenedorFotos == null) {
            System.out.println("El contenedor de fotos es nulo");
            return;
        }

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        contenedorFotos.addView(imageView);


        // Usar Glide para cargar la imagen desde la URL
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder) // Asegúrate de tener un recurso placeholder
                .error(R.drawable.error) // Asegúrate de tener un recurso de error
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.logRootCauses("Glide");
                        }
                        System.out.println("Error al cargar la imagen: ");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Imagen cargada exitosamente
                        return false;
                    }
                })
                .into(imageView);
        //Picasso.get().load(url).into(imageView);
    }


}
