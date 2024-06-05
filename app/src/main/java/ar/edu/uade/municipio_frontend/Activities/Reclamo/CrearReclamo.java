package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionReclamo;
import ar.edu.uade.municipio_frontend.Models.AutenticacionSitio;
import ar.edu.uade.municipio_frontend.Models.Desperfecto;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.Models.Sitio;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.DesperfectoService;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
import ar.edu.uade.municipio_frontend.Services.SectorService;
import ar.edu.uade.municipio_frontend.Services.SitioService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearReclamo extends AppCompatActivity {
    Spinner dropdownSitio;
    EditText editTextDescripcion;
    Button buttonGenerar;
    Button buttonAdjuntarArchivos;
    List<String> desperfectos;
    ArrayAdapter<String> adapterSector;
    List<String> sitios;
    ArrayAdapter<String> adapterSitio;
    Spinner dropdownDesperfecto;
    Autenticacion autenticacion;
    ImageButton botonAgregarSitio;
    MapView map;
    LocationManager locationManager;
    boolean permisos;
    Marker marker;
    CheckBox checkBoxMapa;
    AutenticacionSitio autenticacionSitio;
    AutenticacionReclamo autenticacionReclamo;
    List<Sitio> sitiosObjeto;
    String sitioSeleccionado;
    List<Desperfecto> desperfectoObjeto;
    EditText descripcionReclamo;
    Sitio sitio;
    Desperfecto desperfecto;
    String desperfectoSeleccionado;
    FragmentManager fragmentManager;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @SuppressLint({"MissingInflatedId", "NewApi", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_reclamo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //recalculo()

        // Inicializar vistas

        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        buttonGenerar = findViewById(R.id.buttonGenerar);

        buttonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        autenticacionSitio = new AutenticacionSitio();

        autenticacion = new Autenticacion();

        autenticacion.setTipo(getIntent().getStringExtra("USUARIO"));

        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacionSitio.setAutenticacion(autenticacion);

        autenticacionReclamo = new AutenticacionReclamo();

        autenticacionReclamo.setAutenticacion(autenticacion);

        sitioSeleccionado = "";

        sitiosObjeto = new ArrayList<>();

        desperfectos = new ArrayList<>();

        dropdownDesperfecto = findViewById(R.id.spinnerDesperfecto);

        adapterSector = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, desperfectos);

        adapterSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownDesperfecto.setAdapter(adapterSector);

        getDesperfectos(autenticacion);

        sitios = new ArrayList<>();

        desperfectoObjeto = new ArrayList<>();

        dropdownSitio = findViewById(R.id.spinnerSitio);

        adapterSitio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sitios);

        adapterSitio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownSitio.setAdapter(adapterSitio);

        getSitios(autenticacion);

        botonAgregarSitio = findViewById(R.id.botonAgregar);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkBoxMapa = findViewById(R.id.checkBoxMapa);

        descripcionReclamo = findViewById(R.id.editTextDescripcion);

        fragmentManager = getSupportFragmentManager();

        System.out.println(autenticacion.getToken());

        dropdownDesperfecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                desperfectoSeleccionado = desperfectos.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdownSitio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sitioSeleccionado = sitios.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonAgregarSitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(CrearReclamo.this, NuevoSitio.class);
                startActivity(nuevaActividad);
            }
        });

        // Configurar el botón de generar reclamo
        buttonGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sitio = new Sitio();
                if (checkBoxMapa.isChecked()) {
                    sitio = new Sitio(
                            BigDecimal.valueOf(marker.getPosition().getLatitude()),
                            BigDecimal.valueOf(marker.getPosition().getLongitude()),
                            "Sitio creado por el usuario" + getIntent().getStringExtra("documento")
                    );

                    autenticacionSitio.setSitio(sitio);

                    nuevoSitio(autenticacionSitio);

                } else if (!checkBoxMapa.isChecked()) {
                    for (Sitio s : sitiosObjeto) {
                        String c = s.getDescripcion().substring(0, 1).toUpperCase();
                        if ((c + s.getDescripcion().substring(1)).equals(sitioSeleccionado)) {
                            sitio = s;
                            break;
                        }
                    }
                    for (Desperfecto r : desperfectoObjeto) {
                        String c = r.getDescripcion().substring(0, 1).toUpperCase();
                        if ((c + r.getDescripcion().substring(1)).equals(desperfectoSeleccionado)) {
                            desperfecto = r;
                            break;
                        }
                    }
                    Reclamo reclamo = new Reclamo(
                            descripcionReclamo.getText().toString(),
                            "Nuevo",
                            getIntent().getStringExtra("documento"),
                            sitio.getIdSitio(),
                            desperfecto.getIdDesperfecto()
                    );

                    autenticacionReclamo.setReclamo(reclamo);

                    nuevoReclamo(autenticacionReclamo);
                }
            }
        });

        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));



        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        marker = new Marker(map);
        System.out.println(marker);
        if (marker!=null) {
            checkPermissions();
        }


        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double latitude = p.getLatitude();
                double longitude = p.getLongitude();
                Toast.makeText(CrearReclamo.this, "Lat: " + latitude + ", Lon: " + longitude, Toast.LENGTH_SHORT).show();

                // Añadir un marcador en el punto tocado
                marker.setPosition(p);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle("Marcador en Lat: " + latitude + ", Lon: " + longitude);
                map.getOverlays().add(marker);
                map.invalidate();

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(mReceive);
        map.getOverlays().add(eventsOverlay);
    }

    private void recalculo() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getState().toString().equals("CONNECTED") && Objects.equals(networkInfo.getTypeName(), "WIFI")) {
                nuevoReclamo(autenticacionReclamo);
            }
        }
    }

    private void getDesperfectos(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DesperfectoService desperfectoService = retrofit.create(DesperfectoService.class);

        Call<List<Desperfecto>> call = desperfectoService.getDesperfectos(autenticacion);

        call.enqueue(new Callback<List<Desperfecto>>() {
            @Override
            public void onResponse(@NonNull Call<List<Desperfecto>> call, @NonNull Response<List<Desperfecto>> response) {
                if (response.code() == 200) {//este ok
                    System.out.println(response.code());
                    assert response.body() != null;
                    List<Desperfecto> ss = response.body();
                    for (Desperfecto s : ss) {
                        String c = s.getDescripcion().substring(0, 1).toUpperCase();
                        desperfectoObjeto.add(s);
                        desperfectos.add(c + s.getDescripcion().substring(1));
                    }
                    adapterSector.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<List<Desperfecto>> call, @NonNull Throwable t) {

            }
        });

    }

    private void showCurrentLocation() {
        // Obtener la ubicación actual del dispositivo
        Location location = getLastKnownLocation();

        // Si se encontró la ubicación, marcarla en el mapa
        map.getController().setZoom(20.0);
        GeoPoint startPoint = new GeoPoint(39.56968069719455, 2.6505419523701215);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
        map.getController().setCenter(startPoint);
        map.invalidate();
    }
    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
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
        System.out.println("permisos confirmados");
        showCurrentLocation();

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
        map.onResume(); // Esto llama a la reanudación de MapView
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause(); // Esto llama a la pausa de MapView
    }

    private void getSitios(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SitioService sitioService = retrofit.create(SitioService.class);

        Call<List<Sitio>> call = sitioService.getSitios(autenticacion);

        call.enqueue(new Callback<List<Sitio>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sitio>> call, @NonNull Response<List<Sitio>> response) {
                if (response.code() == 200) {//este ok
                    System.out.println(response.code());
                    assert response.body() != null;
                    List<Sitio> ss = response.body();
                    for (Sitio s : ss) {
                        String c = s.getDescripcion().substring(0, 1).toUpperCase();
                        sitiosObjeto.add(s);
                        sitios.add(c + s.getDescripcion().substring(1));
                    }
                    adapterSitio.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<List<Sitio>> call, @NonNull Throwable t) {

            }
        });
    }

    private void nuevoReclamo(AutenticacionReclamo autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamoService = retrofit.create(ReclamoService.class);

        Call<Reclamo> call = reclamoService.nuevoReclamo(autenticacion);

        call.enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(@NonNull Call<Reclamo> call, @NonNull Response<Reclamo> response) {
                if (response.code() == 201) {//este created
                    System.out.println(response.code());
                    Reclamo reclamo = response.body();
                    assert reclamo != null;
                    System.out.println(reclamo.getIdReclamo());
                    Intent nuevaActividad = new Intent(CrearReclamo.this, VerReclamos.class);


                    if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                        nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                    } else if (Objects.equals(getIntent().getStringExtra("USUARIO"), "EMPLEADO")) {
                        nuevaActividad.putExtra("legajo", getIntent().getStringExtra("legajo"));

                    }

                    nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                    nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                    startActivity(nuevaActividad);
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

    private void nuevoSitio(AutenticacionSitio autenticacionSitio) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SitioService sitioService = retrofit.create(SitioService.class);

        Call<Integer> call = sitioService.nuevoSitio(autenticacionSitio);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.body()!= null) {
                    sitio.setIdSitio(response.body());
                    for (Desperfecto r : desperfectoObjeto) {
                        String c = r.getDescripcion().substring(0, 1).toUpperCase();
                        if ((c + r.getDescripcion().substring(1)).equals(desperfectoSeleccionado)) {
                            desperfecto = r;
                            break;
                        }
                    }


                    Reclamo reclamo = new Reclamo(
                            descripcionReclamo.getText().toString(),
                            "Nuevo",
                            getIntent().getStringExtra("documento"),
                            sitio.getIdSitio(),
                            desperfecto.getIdDesperfecto()
                    );

                    autenticacionReclamo.setReclamo(reclamo);

                    nuevoReclamo(autenticacionReclamo);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                System.out.println("no funciona");
            }
        });
    }
}