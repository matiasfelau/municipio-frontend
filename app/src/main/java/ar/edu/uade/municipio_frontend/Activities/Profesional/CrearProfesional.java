package ar.edu.uade.municipio_frontend.Activities.Profesional;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ProfesionalService;
import ar.edu.uade.municipio_frontend.Utilities.Container.AutenticacionProfesional;
import ar.edu.uade.municipio_frontend.Utilities.MapHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearProfesional extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText inputNombreProfesional;
    private EditText inputDireccion;
    private MapView mapa;
    private EditText inputTelefono;
    private EditText inputEmail;
    private ImageButton botonDisminuirInicioJornada;
    private TextView inicioJornada;
    private ImageButton botonAumentarInicioJornada;
    private ImageButton botonDisminuirFinJornada;
    private TextView finJornada;
    private ImageButton botonAumentarFinJornada;
    private Button botonAdjuntarArchivos;
    private Button botonEnviarSolicitud;
    private MapHelper mapHelper;
    private Marker marker;
    private List<Uri> imageUris = new ArrayList<>();
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        ClipData clipData = result.getData().getClipData();

                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                imageUris.add(clipData.getItemAt(i).getUri());

                            }
                        } else {
                            Uri selectedImage = result.getData().getData();

                            imageUris.add(selectedImage);

                        }
                    }
                }
            }
            );
    Autenticacion autenticacion;
    AutenticacionProfesional autenticacionProfesional;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_crear_profesional);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        inputNombreProfesional = findViewById(R.id.inputNombre);

        inputDireccion = findViewById(R.id.inputDireccion);

        mapa = findViewById(R.id.map);

        inputTelefono = findViewById(R.id.inputTelefono);

        inputEmail = findViewById(R.id.inputEmail);

        botonDisminuirInicioJornada = findViewById(R.id.disminuirInicioJornada);

        inicioJornada = findViewById(R.id.inicioJornada);

        botonAumentarInicioJornada = findViewById(R.id.aumentarInicioJornada);

        botonDisminuirFinJornada = findViewById(R.id.disminuirFinJornada);

        finJornada = findViewById(R.id.finJornada);

        botonAumentarFinJornada = findViewById(R.id.aumentarFinJornada);

        botonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        botonEnviarSolicitud = findViewById(R.id.buttonGenerar);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("USUARIO"),
                getIntent().getStringExtra("token")
        );

        autenticacionProfesional = new AutenticacionProfesional(autenticacion);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        marker = new Marker(mapa);

        mapa.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissions();

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double latitude = p.getLatitude();

                double longitude = p.getLongitude();

                marker.setPosition(p);

                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                marker.setTitle("Marcador en Lat: " + latitude + ", Lon: " + longitude);

                mapa.getOverlays().add(marker);

                mapa.invalidate();

                return true;

            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(mReceive);

        mapa.getOverlays().add(eventsOverlay);

        botonDisminuirInicioJornada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int horas = Integer.parseInt(inicioJornada.getText().toString().substring(0,2));

                int minutos = Integer.parseInt(inicioJornada.getText().toString().substring(3,5));

                minutos -= 30;

                if (minutos < 0 && horas > 0) {
                    minutos = 30;

                    horas--;

                } else if (minutos < 0 && horas == 0) {
                    minutos = 30;

                    horas = 23;

                }

                String h = String.valueOf(horas);
                if(horas < 10){
                    h = "0"+horas;
                }
                String m = String.valueOf(minutos);
                if (minutos == 0) {
                    m = "00";
                }
                inicioJornada.setText(h + ":" + m);
            }
        });

        botonAumentarInicioJornada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int horas = Integer.parseInt(inicioJornada.getText().toString().substring(0, 2));

                int minutos = Integer.parseInt(inicioJornada.getText().toString().substring(3, 5));

                minutos += 30;

                if (minutos > 30 && horas < 23) {
                    minutos = 0;

                    horas++;

                } else if (minutos > 30 && horas == 23) {
                    minutos = 0;

                    horas = 0;

                }
                String h = String.valueOf(horas);
                if(horas < 10){
                    h = "0"+horas;
                }
                String m = String.valueOf(minutos);
                if (minutos == 0) {
                    m = "00";
                }
                inicioJornada.setText(h + ":" + m);
            }
        });

        botonDisminuirFinJornada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int horas = Integer.parseInt(finJornada.getText().toString().substring(0,2));

                int minutos = Integer.parseInt(finJornada.getText().toString().substring(3,5));

                minutos -= 30;

                if (minutos < 0 && horas > 0) {
                    minutos = 30;

                    horas--;

                } else if (minutos < 0 && horas == 0) {
                    minutos = 30;

                    horas = 23;

                }

                String h = String.valueOf(horas);
                if(horas < 10){
                    h = "0"+horas;
                }
                String m = String.valueOf(minutos);
                if (minutos == 0) {
                    m = "00";
                }
                finJornada.setText(h + ":" + m);
            }
        });

        botonAumentarFinJornada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int horas = Integer.parseInt(finJornada.getText().toString().substring(0,2));

                int minutos = Integer.parseInt(finJornada.getText().toString().substring(3,5));

                minutos += 30;

                if (minutos > 30 && horas < 23) {
                    minutos = 0;

                    horas++;

                } else if (minutos > 30 && horas == 23) {
                    minutos = 0;

                    horas = 0;

                }

                String h = String.valueOf(horas);
                if(horas < 10){
                    h = "0"+horas;
                }
                String m = String.valueOf(minutos);
                if (minutos == 0) {
                    m = "00";
                }
                finJornada.setText(h + ":" + m);
            }
        });

        botonAdjuntarArchivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        botonEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Profesional profesional = new Profesional(
                            null,
                            inputNombreProfesional.getText().toString(),
                            inputDireccion.getText().toString(),
                            Integer.parseInt(inputTelefono.getText().toString()),
                            inputEmail.getText().toString(),
                            BigDecimal.valueOf(marker.getPosition().getLatitude()),
                            BigDecimal.valueOf(marker.getPosition().getLongitude()),
                            inicioJornada.getText().toString(),
                            finJornada.getText().toString(),
                            getIntent().getStringExtra("documento"));
                }
            }
        });
    }

    private void requestPermissions() {
        String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
        boolean fineLocationStatus = ActivityCompat.checkSelfPermission(this, fineLocationPermission) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationStatus = ActivityCompat.checkSelfPermission(this, coarseLocationPermission) == PackageManager.PERMISSION_GRANTED;
        if (!fineLocationStatus && !coarseLocationStatus) {
            ActivityCompat.requestPermissions(this, new String[]{
                    fineLocationPermission,
                    coarseLocationPermission}, PERMISSION_REQUEST_CODE);
        } else {
            mapHelper.initializeMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapHelper.initializeMap();
            } else {
                Log.e("PermissionError", "Location permissions are required to use this feature.");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkFilesPermissions() {
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
        };

        boolean granted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;

                openGallery();

                break;

            }
        }

        if (!granted) {
            ActivityCompat.requestPermissions(this, permissions, 1);

        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        galleryLauncher.launch(intent);

    }

    private void nuevoProfesional(Profesional profesional) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ProfesionalService profesionalService = retrofit.create(ProfesionalService.class);

        autenticacionProfesional.setProfesional(profesional);

        Call<Profesional> call = profesionalService.nuevoProfesional(autenticacionProfesional);

        call.enqueue(new Callback<Profesional>() {
            @Override
            public void onResponse(@NonNull Call<Profesional> call, @NonNull Response<Profesional> response) {
                System.out.println("CREAR PROFESIONAL");

                if (response.code() == 201) {

                    Profesional profesional = response.body();

                    uploadImages(profesional.getIdProfesional());

                    Intent nuevaActividad = new Intent(CrearProfesional.this, VerReclamos.class);

                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                    nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                    nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                    startActivity(nuevaActividad);

                } else if (response.code() == 400) {
                    System.out.println(response.code());

                } else if (response.code() == 401) {
                    System.out.println(response.code());

                } else if (response.code() == 403) {
                    System.out.println(response.code());

                } else if (response.code() == 404) {
                    System.out.println(response.code());

                } else if (response.code() == 500) {
                    System.out.println(response.code());

                } else {
                    System.out.println(response.code());

                }
            }
            @Override
            public void onFailure(@NonNull Call<Profesional> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    private void uploadImages(int idProfesional) {
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Uri uri : imageUris) {
            File file = new File(getRealPathFromURI(uri));

            RequestBody requestFile = RequestBody.Companion.create(file, MediaType.parse("multipart/form-data"));

            MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);

            parts.add(body);

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProfesionalService service = retrofit.create(ProfesionalService.class);

        Call<ResponseBody> call = service.uploadImages(idProfesional, parts);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle")
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            return uri.getPath();

        } else {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            return cursor.getString(idx);

        }
    }
}