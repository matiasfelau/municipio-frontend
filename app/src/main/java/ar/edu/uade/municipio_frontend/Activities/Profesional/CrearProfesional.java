package ar.edu.uade.municipio_frontend.Activities.Profesional;

import static org.osmdroid.tileprovider.cachemanager.CacheManager.getFileName;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Comercio.CrearComercio;
import ar.edu.uade.municipio_frontend.Activities.Comercio.VerComercio;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Desperfecto;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.DesperfectoService;
import ar.edu.uade.municipio_frontend.Services.ProfesionalService;
import ar.edu.uade.municipio_frontend.Services.SectorService;
import ar.edu.uade.municipio_frontend.Utilities.Container.AutenticacionProfesional;
import ar.edu.uade.municipio_frontend.Utilities.EmailValidation;
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
    Spinner dropdownRubros;
    private EditText inputDireccion;
    private MapView mapa;
    private EditText inputTelefono;
    private EditText inputEmail;
    private EditText inputDescripcion;
    private ImageButton botonDisminuirInicioJornada;
    private TextView inicioJornada;
    private ImageButton botonAumentarInicioJornada;
    private ImageButton botonDisminuirFinJornada;
    private TextView finJornada;
    private ImageButton botonAumentarFinJornada;
    private Button botonAdjuntarArchivos;
    private Button botonEnviarSolicitud;
    ArrayAdapter<String> adapterRubros;
    List<String> rubros;
    String rubroSeleccionado;
    private MapHelper mapHelper;
    private ImageButton botonVolver;
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
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @SuppressLint({"MissingInflatedId", "ResourceType"})
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

        dropdownRubros = findViewById(R.id.spinnerRubro);

        inputDescripcion = findViewById(R.id.editTextDescripcion);

        mapa = findViewById(R.id.map);

        inputTelefono = findViewById(R.id.inputTelefono);

        inputEmail = findViewById(R.id.inputEmail);

        botonVolver = findViewById(R.id.botonVolver);

        inputDireccion = findViewById(R.id.inputDireccion);

        botonDisminuirInicioJornada = findViewById(R.id.disminuirInicioJornada);

        inicioJornada = findViewById(R.id.inicioJornada);

        botonAumentarInicioJornada = findViewById(R.id.aumentarInicioJornada);

        botonDisminuirFinJornada = findViewById(R.id.disminuirFinJornada);

        finJornada = findViewById(R.id.finJornada);

        botonAumentarFinJornada = findViewById(R.id.aumentarFinJornada);

        botonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        botonEnviarSolicitud = findViewById(R.id.buttonGenerar);

        rubros = new ArrayList<>();

        adapterRubros = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rubros);

        adapterRubros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownRubros.setAdapter(adapterRubros);

        mapHelper = new MapHelper(this,
                this,
                mapa);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO")
        );

        autenticacionProfesional = new AutenticacionProfesional(autenticacion);

        mapHelper.startService(false);

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(CrearProfesional.this, VerProfesionales.class);
                if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                }

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });
        getRubros(autenticacion);

        dropdownRubros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rubroSeleccionado = rubros.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonDisminuirInicioJornada.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int horas = Integer.parseInt(inicioJornada.getText().toString().substring(0, 2));

                int minutos = Integer.parseInt(inicioJornada.getText().toString().substring(3, 5));

                minutos -= 30;

                if (minutos < 0 && horas > 0) {
                    minutos = 30;

                    horas--;

                } else if (minutos < 0 && horas == 0) {
                    minutos = 30;

                    horas = 23;

                }

                String h = String.valueOf(horas);
                if (horas < 10) {
                    h = "0" + horas;
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
                if (horas < 10) {
                    h = "0" + horas;
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
                int horas = Integer.parseInt(finJornada.getText().toString().substring(0, 2));

                int minutos = Integer.parseInt(finJornada.getText().toString().substring(3, 5));

                minutos -= 30;

                if (minutos < 0 && horas > 0) {
                    minutos = 30;

                    horas--;

                } else if (minutos < 0 && horas == 0) {
                    minutos = 30;

                    horas = 23;

                }

                String h = String.valueOf(horas);
                if (horas < 10) {
                    h = "0" + horas;
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
                int horas = Integer.parseInt(finJornada.getText().toString().substring(0, 2));

                int minutos = Integer.parseInt(finJornada.getText().toString().substring(3, 5));

                minutos += 30;

                if (minutos > 30 && horas < 23) {
                    minutos = 0;

                    horas++;

                } else if (minutos > 30 && horas == 23) {
                    minutos = 0;

                    horas = 0;

                }

                String h = String.valueOf(horas);
                if (horas < 10) {
                    h = "0" + horas;
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
                if (!inputNombreProfesional.getText().toString().isEmpty()){
                    String email = inputEmail.getText().toString();
                    if (EmailValidation.patternMatches(email, regexPattern)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            List<String> imagenes = new ArrayList<>();
                            for (Uri uri : imageUris) {
                                System.out.println(uri);
                                imagenes.add(convertImageToBase64(uri));
                            }
                            Profesional profesional = new Profesional(
                                    inputNombreProfesional.getText().toString(),
                                    rubroSeleccionado,
                                    inputDescripcion.getText().toString(),
                                    inputDireccion.getText().toString(),
                                    Integer.parseInt(inputTelefono.getText().toString()),
                                    email,
                                    mapHelper.getMarkerLatitude(),
                                    mapHelper.getMarkerLongitude(),
                                    inicioJornada.getText().toString(),
                                    finJornada.getText().toString(),
                                    getIntent().getStringExtra("documento"),
                                    imagenes);
                            nuevoProfesional(profesional);
                        }
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
        String[] permissions = new String[]{
                Manifest.permission.READ_MEDIA_IMAGES
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
        } else {
            openGallery();
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

                    Intent nuevaActividad = new Intent(CrearProfesional.this, VerProfesionales.class);

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

    private void getRubros(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SectorService sectorService = retrofit.create(SectorService.class);

        Call<List<Sector>> call = sectorService.getSectores(autenticacion);

        call.enqueue(new Callback<List<Sector>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sector>> call, @NonNull Response<List<Sector>> response) {
                if (response.code() == 200) {//este ok
                    System.out.println("EL RESULTADO ES:"+response.code());
                    assert response.body() != null;
                    List<Sector> sectores = response.body();
                    for (Sector sector : sectores) {
                        System.out.println(sector.getDescripcion());
                        rubros.add(sector.getDescripcion());
                    }
                    adapterRubros.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<List<Sector>> call, @NonNull Throwable t) {
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