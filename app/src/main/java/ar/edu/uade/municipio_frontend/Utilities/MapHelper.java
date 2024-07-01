package ar.edu.uade.municipio_frontend.Utilities;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.math.BigDecimal;

public class MapHelper {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Context context;
    private Activity activity;
    private MapView map;
    private Marker marker;

    public MapHelper(Context context, Activity activity, MapView map) {
        this.context = context;
        this.activity = activity;
        this.map = map;
    }

    public void startService(boolean permissionsGranted) {
        if (checkLocationPermissions() ||
                permissionsGranted) {
            initializeMap();
        }
    }

    private boolean checkLocationPermissions() {
        String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
        if (ActivityCompat.checkSelfPermission(context, fineLocationPermission) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, coarseLocationPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                        fineLocationPermission,
                        coarseLocationPermission},
                    PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    private void initializeMap() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(15.0);
        Location currentLocation = getLastKnownLocation();
        marker = new Marker(map);
        if (currentLocation != null) {
            showCurrentLocation(currentLocation);
        } else {
            Log.e("MapError", "No se pudo encontrar la ubicación del dispositivo.");
        }
        map.getOverlays().add(new MapEventsOverlay(setUpEventsReceiver()));
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("LocationError", "El servicio GPS no está activado.");
                return null;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                Log.e("LocationError", "No se pudo encontrar la ubicación del dispositivo con el servicio GPS. Intentando con el servicio de red.");
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    Log.e("LocationError", "No se pudo encontrar la ubicación del dispositivo con el servicio de red.");
                }
            }
            return location;
        }
        Log.e("LocationError", "LocationManager es nulo.");
        return null;
    }

    private void showCurrentLocation(Location currentLocation) {
        GeoPoint startPoint = new GeoPoint(currentLocation.getLatitude(),
                currentLocation.getLongitude());
        map.getController().setCenter(startPoint);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Ubicación actual.");
        map.getOverlays().add(marker);
    }

    @NonNull
    private MapEventsReceiver setUpEventsReceiver() {
        return new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint newPoint) {
                map.getController().setCenter(newPoint);
                marker.setPosition(newPoint);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle("Nueva ubicación.");
                map.getOverlays().add(marker);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
    }

    public void setLocation(BigDecimal latitude, BigDecimal longitude) {
        GeoPoint startPoint = new GeoPoint(latitude.doubleValue(),
                longitude.doubleValue());
        map.getController().setCenter(startPoint);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Nueva ubicación.");
        map.getOverlays().add(marker);
    }

    public BigDecimal getMarkerLatitude() {
        return BigDecimal.valueOf(marker.getPosition().getLatitude());
    }

    public BigDecimal getMarkerLongitude() {
        return BigDecimal.valueOf(marker.getPosition().getLongitude());
    }
}
