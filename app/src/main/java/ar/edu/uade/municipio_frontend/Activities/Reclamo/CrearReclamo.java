package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.Models.Sitio;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
import ar.edu.uade.municipio_frontend.Services.SectorService;
import ar.edu.uade.municipio_frontend.Services.SitioService;
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
    List<String> sectores;
    ArrayAdapter<String> adapterSector;
    List<String> sitios;
    ArrayAdapter<String> adapterSitio;
    Spinner dropdownSector;
    Autenticacion autenticacion;
    ImageButton botonAgregarSitio;
    MapView map;
    LocationManager locationManager;
    boolean permisos;
    Marker marker;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reclamo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas

        editTextDescripcion = findViewById(R.id.editTextDescripcion);

        buttonGenerar = findViewById(R.id.buttonGenerar);

        buttonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        autenticacion = new Autenticacion();

        autenticacion.setTipo(getIntent().getStringExtra("USUARIO"));

        autenticacion.setToken(getIntent().getStringExtra("token"));

        sectores = new ArrayList<>();

        dropdownSector = findViewById(R.id.spinnerDesperfecto);

        adapterSector = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectores);

        adapterSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownSector.setAdapter(adapterSector);

        getSectores(autenticacion);

        sitios = new ArrayList<>();

        dropdownSitio = findViewById(R.id.spinnerSitio);

        adapterSitio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sitios);

        adapterSitio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownSitio.setAdapter(adapterSitio);

        getSitios(autenticacion);

        botonAgregarSitio = findViewById(R.id.botonAgregar);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        botonAgregarSitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(CrearReclamo.this, NuevoSitio.class);
                startActivity(nuevaActividad);
            }
        });

        // Configurar el botón de generar reclamo
        buttonGenerar.setOnClickListener(view -> {

            // Mostrar el popup de éxito
            FragmentManager fragmentManager = getSupportFragmentManager();
            ReclamoExitosoDialog dialog = new ReclamoExitosoDialog();
            dialog.show(fragmentManager, "ReclamoExitosoDialog");
        });
        /*
        // Configurar el botón de adjuntar archivos
        buttonAdjuntarArchivos.setOnClickListener(view -> {
            // Aquí puedes añadir la lógica para adjuntar archivos
            Toast.makeText(NuevoReclamo.this, "Adjuntar archivos no implementado", Toast.LENGTH_SHORT).show();
        });

         */
        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));



        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        /*
        // Configurar el controlador del mapa
        map.getController().setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(-34.60330398993216, -58.38173460895764); // Coordenadas de la Torre Eiffel
        map.getController().setCenter(startPoint);
        // Añadir un marcador en el mapa
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Torre Eiffel");
        map.getOverlays().add(startMarker);

         */
        marker = new Marker(map);
        System.out.println(marker);
        if (marker!=null) {
            checkPermissions();
        }

        //borrar marcador si se hace uno nuevo
        //ubicacion del celular


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

    private void getSectores(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SectorService sectorService = retrofit.create(SectorService.class);

        Call<List<Sector>> call = sectorService.getSectores(autenticacion);

        call.enqueue(new Callback<List<Sector>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sector>> call, @NonNull Response<List<Sector>> response) {
                if (response.code() == 200) {//este ok
                    System.out.println(response.code());
                    assert response.body() != null;
                    List<Sector> ss = response.body();
                    for (Sector s : ss) {
                        String c = s.getDescripcion().substring(0, 1).toUpperCase();
                        sectores.add(c + s.getDescripcion().substring(1));
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
            public void onFailure(@NonNull Call<List<Sector>> call, @NonNull Throwable t) {

            }
        });

    }
    /*

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                System.out.println("LATITUD Y LONGITUD");
                System.out.println(location.getLatitude());
                System.out.println(location.getLongitude());
                map.getController().setZoom(15.0);
                GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                map.getController().setCenter(startPoint);

                marker.setPosition(startPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(marker);


                map.invalidate();
            }
        });
    }

     */
    private void showCurrentLocation() {
        // Obtener la ubicación actual del dispositivo
        Location location = getLastKnownLocation();

        // Si se encontró la ubicación, marcarla en el mapa
        if (location != null) {
            map.getController().setZoom(15.0);
            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            marker.setPosition(startPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(marker);
            map.getController().setCenter(startPoint);
            map.invalidate();
        }
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
}