package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ar.edu.uade.municipio_frontend.Activities.Comercio.CrearComercio;
import ar.edu.uade.municipio_frontend.Activities.Comercio.VerComercio;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionPublicacion;
import ar.edu.uade.municipio_frontend.Models.Comercio;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ComercioService;
import ar.edu.uade.municipio_frontend.Services.PublicacionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearPublicacion extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titulo;
    private EditText descripcion;
    private Spinner inputAutor;
    private Button botonCrear;
    private ImageButton botonVolver;
    private Autenticacion autenticacion;
    private TextView textFechaHora;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<String> comercios;
    private  ArrayAdapter<String> adapter;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_publicacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });


        titulo = findViewById(R.id.inputTitulo);
        descripcion = findViewById(R.id.inputDescripcion);
        inputAutor = findViewById(R.id.inputAutor);
        botonCrear = findViewById(R.id.btnCrearPublicacion);
        botonVolver = findViewById(R.id.botonVolver);
        textFechaHora = findViewById(R.id.textFechaHora);

        autenticacion = new Autenticacion();
        autenticacion.setToken(getIntent().getStringExtra("token"));
        autenticacion.setTipo("Vecino");

        comercios = new ArrayList<>();
        adapter = new ArrayAdapter<>(CrearPublicacion.this,
                android.R.layout.simple_spinner_item, comercios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAutor.setAdapter(adapter);
        adapter.add("Seleccione un negocio...");
        adapter.notifyDataSetChanged();

        obtenerComerciosDelVecino();

        inputAutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    botonCrear.setEnabled(false);
                } else {
                    botonCrear.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar el caso en que no se selecciona nada
                botonCrear.setEnabled(false);
            }
        });

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!titulo.getText().toString().isEmpty()) {
                    String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            .format(new Date());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        List<String> imagenes = new ArrayList<>();
                        for (Uri uri : imageUris) {
                            System.out.println(uri);
                            imagenes.add(convertImageToBase64(uri));
                        }

                        Publicacion publicacion = new Publicacion(
                                null,
                                titulo.getText().toString(),
                                descripcion.getText().toString(),
                                inputAutor.getSelectedItem().toString(),
                                fechaActual,
                                imagenes);
                        crearPublicacion(publicacion, autenticacion);
                    }
                }
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnAdjuntarImagen = findViewById(R.id.btnAdjuntarImagen);
        btnAdjuntarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Iniciar la actualización de la fecha y hora
        startUpdatingDateTime();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        galleryLauncher.launch(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // para manejar las imágenes
            ClipData clipData = data.getClipData();
            int imageCount = 0;
            if (clipData != null) {
                imageCount = clipData.getItemCount();
                for (int i = 0; i < imageCount; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                }
            } else {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imageCount = 1;
                }
            }
            TextView textCantidadImagenes = findViewById(R.id.textCantidadImagenes);
            textCantidadImagenes.setText(imageCount + " imágenes subidas");
        }
    }

    private void obtenerComerciosDelVecino() {
        // Simulación de llamada a una API para obtener los comercios
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ComercioService comercioService = retrofit.create(ComercioService.class);

        Call<List<Comercio>> call = comercioService.obtenerComercios(getIntent().getStringExtra("documento"), autenticacion);
        call.enqueue(new Callback<List<Comercio>>() {
            @Override
            public void onResponse(Call<List<Comercio>> call, Response<List<Comercio>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    for (Comercio comercio : response.body()) {
                        adapter.add(comercio.getNombre());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    // manejar el error
                    Snackbar.make(findViewById(android.R.id.content), "Error al obtener los comercios",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comercio>> call, Throwable t) {
                // manejar el error
                Snackbar.make(findViewById(android.R.id.content), "Error de red", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void crearPublicacion(Publicacion publicacion, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        PublicacionService publicacionService = retrofit.create(PublicacionService.class);
        AutenticacionPublicacion autenticacionPublicacion = new AutenticacionPublicacion(publicacion, autenticacion);
        System.out.println(autenticacionPublicacion.toString());
        Call<Publicacion> call = publicacionService.nuevaPublicacion(autenticacionPublicacion);
        call.enqueue(new Callback<Publicacion>() {
            @Override
            public void onResponse(Call<Publicacion> call, Response<Publicacion> response) {
                if (response.code() == 201) {
                    Publicacion p = response.body();

                    Intent nuevaActividad = new Intent(CrearPublicacion.this, VerPublicaciones.class);

                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                    nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                    nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                    startActivity(nuevaActividad);
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Publicacion> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void startUpdatingDateTime() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .format(new Date());
                textFechaHora.setText(currentDateTime);
                handler.postDelayed(this, 1000);
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
