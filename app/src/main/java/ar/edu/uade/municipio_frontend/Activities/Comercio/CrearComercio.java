package ar.edu.uade.municipio_frontend.Activities.Comercio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Profesional.CrearProfesional;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionComercio;
import ar.edu.uade.municipio_frontend.Models.Comercio;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ComercioService;
import ar.edu.uade.municipio_frontend.Services.ProfesionalService;
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

public class CrearComercio extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText inputNombreComercio;
    private EditText inputDireccion;
    private MapView mapa;
    private EditText inputDescripcion;
    private EditText inputTelefono;
    private TextView inicioJornada;
    private ImageButton botonAumentarInicioJornada;
    private ImageButton botonDisminuirInicioJornada;
    private TextView finJornada;
    private ImageButton botonAumentarFinJornada;
    private ImageButton botonDisminuirFinJornada;
    private Button botonAdjuntarArchivos;
    private Button botonEnviarSolicitud;
    private ImageButton botonVolver;
    private MapHelper mapHelper;
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
    AutenticacionComercio autenticacionComercio;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_crear_comercio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        inputNombreComercio = findViewById(R.id.nombreDelComercio);

        inputTelefono = findViewById(R.id.telefonoComercio);

        inputDireccion = findViewById(R.id.direccionComercio);

        inputDescripcion = findViewById(R.id.editTextDescripcionComercio);

        mapa = findViewById(R.id.map);

        botonDisminuirInicioJornada = findViewById(R.id.disminuirInicioJornada);

        inicioJornada = findViewById(R.id.inicioJornada);

        botonAumentarInicioJornada = findViewById(R.id.aumentarInicioJornada);

        botonDisminuirFinJornada = findViewById(R.id.disminuirFinJornada);

        finJornada = findViewById(R.id.finJornada);

        botonAumentarFinJornada = findViewById(R.id.aumentarFinJornada);

        botonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        botonEnviarSolicitud = findViewById(R.id.buttonGenerar);

        botonVolver = findViewById(R.id.botonVolver);

        mapHelper = new MapHelper(this,
                this,
                mapa);


        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO")
        );

        autenticacionComercio = new AutenticacionComercio(autenticacion);

        mapHelper.startService(false);

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(CrearComercio.this, VerComercio.class);
                if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                }

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkFilesPermissions();
                }
            }
        });

        botonEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputNombreComercio.getText().toString().isEmpty()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        List<String> imagenes = new ArrayList<>();
                        for (Uri uri : imageUris) {
                            System.out.println(uri);
                            imagenes.add(convertImageToBase64(uri));
                        }
                        Comercio comercio = new Comercio(
                                inputNombreComercio.getText().toString(),
                                getIntent().getStringExtra("documento"),
                                inputDireccion.getText().toString(),
                                inputDescripcion.getText().toString(),
                                Integer.parseInt(inputTelefono.getText().toString()),
                                inicioJornada.getText().toString(),
                                finJornada.getText().toString(),
                                mapHelper.getMarkerLatitude(),
                                mapHelper.getMarkerLongitude(),
                                imagenes);
                        nuevoComercio(comercio);
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapHelper.startService(true);
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

    private void nuevoComercio(Comercio comercio) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ComercioService comercioService = retrofit.create(ComercioService.class);

        autenticacionComercio.setComercio(comercio);

        Call<Comercio> call = comercioService.nuevoComercio(autenticacionComercio);

        call.enqueue(new Callback<Comercio>() {
            @Override
            public void onResponse(@NonNull Call<Comercio> call, @NonNull Response<Comercio> response) {
                System.out.println("CREAR COMERCIO");

                if (response.code() == 201) {

                    Comercio comercio = response.body();

                    Intent nuevaActividad = new Intent(CrearComercio.this, VerComercio.class);

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
            public void onFailure(@NonNull Call<Comercio> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    public String convertImageToBase64(Uri imageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
