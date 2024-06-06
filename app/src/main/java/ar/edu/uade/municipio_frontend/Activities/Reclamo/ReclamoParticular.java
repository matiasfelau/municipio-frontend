package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;

import ar.edu.uade.municipio_frontend.R;

public class ReclamoParticular extends AppCompatActivity {
    TextView idReclamo;
    TextView estadoReclamo;
    TextView descripcionReclamo;
    MapView mapa;
    Marker marker;
    ListView historialMovimientosReclamo;
    ImageButton botonCambiarPaginaIzquierda;
    TextView paginaActual;
    ImageButton botonCambiarPaginaDerecha;
    @SuppressLint("CutPasteId")
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

        idReclamo = findViewById(R.id.idReclamo);

        estadoReclamo = findViewById(R.id.estadoReclamo);

        descripcionReclamo = findViewById(R.id.descripcion);

        mapa = findViewById(R.id.map);

        historialMovimientosReclamo = findViewById(R.id.listReclamos);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        paginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        Configuration.getInstance().setOsmdroidBasePath(new File(getCacheDir(), "osmdroid"));

        Configuration.getInstance().setOsmdroidTileCache(new File(getCacheDir(), "osmdroid/tiles"));

        mapa = findViewById(R.id.map);

        mapa.setMultiTouchControls(true);

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

}