package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.MovimientoReclamo;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Sitio;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
import ar.edu.uade.municipio_frontend.Services.SitioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReclamoParticular extends AppCompatActivity {
    Context context = this;
    ImageButton botonVolver;
    TextView idReclamoParticular;
    TextView estadoReclamo;
    TextView descripcionReclamo;
    MapView mapa;
    Marker marker;
    LinearLayout contenedorFotos;
    ListView historialMovimientosReclamo;
    ImageButton botonCambiarPaginaIzquierda;
    TextView paginaActual;
    ImageButton botonCambiarPaginaDerecha;
    Autenticacion autenticacion;
    Reclamo reclamo;
    ListView listMovimientos;
    List<String> movimientos;
    ArrayAdapter<String> adapter;

    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_reclamo_particular);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listMovimientos = findViewById(R.id.listMovimientos);

        movimientos = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movimientos);

        listMovimientos.setAdapter(adapter);

        botonVolver = findViewById(R.id.botonVolver);

        idReclamoParticular = findViewById(R.id.nombreComercio);

        estadoReclamo = findViewById(R.id.cierre);

        descripcionReclamo = findViewById(R.id.descripcionComercioParticular);

        mapa = findViewById(R.id.map);

        contenedorFotos = findViewById(R.id.contenedorFotos);

        historialMovimientosReclamo = findViewById(R.id.listReclamos);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        paginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO")
        );

        reclamo = new Reclamo();

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));

        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));

        mapa = findViewById(R.id.map);

        mapa.setMultiTouchControls(true);

        mapa.getController().setZoom(15.0);

        mapa.invalidate();

        marker = new Marker(mapa);

        getReclamo(getIntent().getIntExtra("id", 0), autenticacion);



        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(ReclamoParticular.this, VerReclamos.class);
                if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                } else if (Objects.equals(getIntent().getStringExtra("USUARIO"), "EMPLEADO")) {
                    nuevaActividad.putExtra("legajo", getIntent().getStringExtra("legajo"));

                }

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
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

        mapa.getOverlays().add(mapEventsOverlay);

    }

    private void getLocation() {
        mapa.getController().setZoom(20.0);

        GeoPoint startPoint = new GeoPoint(39.56968069719455, 2.6505419523701215);

        marker = new Marker(mapa);

        marker.setPosition(startPoint);

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        mapa.getOverlays().add(marker);

        mapa.getController().setCenter(startPoint);

        mapa.invalidate();

    }

    private void checkPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean granted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;

                break;

            }
        }

        if (!granted) {
            ActivityCompat.requestPermissions(this, permissions, 1);

        }
        getLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    public void onResume() {
        super.onResume();

        mapa.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        mapa.onPause();

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
                        System.out.println("Error al cargar la imagen: " + e.getMessage());
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

    private void getFotos(int id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamoService = retrofit.create(ReclamoService.class);

        Call<List<String>> call = reclamoService.getFotos(id);

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




    private void addItem(MovimientoReclamo movimientoReclamo){
        if (movimientoReclamo != null) {
            System.out.println(movimientoReclamo.toString());
            adapter.add(movimientoReclamo.getCausa()+" "+movimientoReclamo.getFecha().substring(0,10));
        }

    }

    private void getReclamo(Integer id, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamoService = retrofit.create(ReclamoService.class);

        System.out.println(autenticacion.getToken());

        Call<Reclamo> call = reclamoService.getReclamo(id, autenticacion);

        call.enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(@NonNull Call<Reclamo> call, @NonNull Response<Reclamo> response) {
                if (response.code() == 200) {//este created
                    System.out.println(response.code());

                    reclamo = response.body();

                    assert reclamo != null;
                    idReclamoParticular.setText(String.valueOf(reclamo.getIdReclamo()));

                    estadoReclamo.setText(reclamo.getEstado());

                    descripcionReclamo.setText(reclamo.getDescripcion());

                    getFotos(reclamo.getIdReclamo());

                    System.out.println(reclamo.toString());

                    getSitio(reclamo.getIdSitio());

                    getMovimientos(reclamo.getIdReclamo());

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
            public void onFailure(@NonNull Call<Reclamo> call, @NonNull Throwable t) {

            }
        });
    }

    private void getSitio(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SitioService sitioService = retrofit.create(SitioService.class);

        Call<Sitio> call = sitioService.getSitio(id);

        call.enqueue(new Callback<Sitio>() {
            @Override
            public void onResponse(@NonNull Call<Sitio> call, @NonNull Response<Sitio> response) {
                if (response.code() == 200) {//este created
                    System.out.println(response.code());

                    Sitio sitio = response.body();

                    assert sitio != null;
                    GeoPoint reclamoPoint = new GeoPoint(sitio.getLatitud().doubleValue(), sitio.getLongitud().doubleValue());

                    mapa.getController().setZoom(15.0);

                    marker.setPosition(reclamoPoint);

                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                    mapa.getOverlays().add(marker);

                    mapa.getController().setCenter(reclamoPoint);

                    mapa.invalidate();

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
            public void onFailure(@NonNull Call<Sitio> call, @NonNull Throwable t) {

            }
        });
    }

    private void getMovimientos(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamoService = retrofit.create(ReclamoService.class);

        Call<List<MovimientoReclamo>> call = reclamoService.getMovimientos(id);

        call.enqueue(new Callback<List<MovimientoReclamo>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovimientoReclamo>> call, @NonNull Response<List<MovimientoReclamo>> response) {
                System.out.println("BRODA UAHHH");
                if (response.code() == 200) {//este created
                    System.out.println("SI ENTRA");
                    assert response.body() != null;
                    for (MovimientoReclamo movimientoReclamo : response.body()) {
                        addItem(movimientoReclamo);

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
            public void onFailure(@NonNull Call<List<MovimientoReclamo>> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

}