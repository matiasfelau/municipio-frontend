package ar.edu.uade.municipio_frontend.Utilities;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.math.BigDecimal;

public class MapHelper {
    private Context context;
    private MapView map;
    private Marker marker;

    public MapHelper(Context context, MapView map, Marker marker) {
        this.context = context;
        this.map = map;
        this.marker = marker;
    }

    public void initializeMap() {
        Location location = getLastKnownLocation();
        if (location != null) {
            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            map.getController().setZoom(15.0);
            map.getController().setCenter(startPoint);
            marker.setPosition(startPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Ubicación Actual");
            map.getOverlays().add(marker);
        } else {
            Log.e("MapError", "La ubicación es nula");
        }
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("LocationError", "GPS Provider is not enabled");
                return null;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                Log.e("LocationError", "No last known location found for GPS provider, trying NETWORK_PROVIDER");
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    Log.e("LocationError", "No last known location found for NETWORK provider either");
                }
            }
            return location;
        }
        Log.e("LocationError", "LocationManager is null");
        return null;
    }

    public BigDecimal getMarkerLatitude() {
        return BigDecimal.valueOf(marker.getPosition().getLatitude());
    }

    public BigDecimal getMarkerLongitude() {
        return BigDecimal.valueOf(marker.getPosition().getLongitude());
    }
}
