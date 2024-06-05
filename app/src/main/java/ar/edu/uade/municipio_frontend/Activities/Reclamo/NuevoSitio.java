package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;

import ar.edu.uade.municipio_frontend.R;

public class NuevoSitio extends AppCompatActivity {
    MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuevo_sitio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));



        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

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

        checkPermissions();

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double latitude = p.getLatitude();
                double longitude = p.getLongitude();
                Toast.makeText(NuevoSitio.this, "Lat: " + latitude + ", Lon: " + longitude, Toast.LENGTH_SHORT).show();

                // Añadir un marcador en el punto tocado
                Marker marker = new Marker(map);
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
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permisos concedidos
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
}