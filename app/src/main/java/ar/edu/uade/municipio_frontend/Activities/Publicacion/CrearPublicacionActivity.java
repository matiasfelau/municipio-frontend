package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.PublicacionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearPublicacionActivity extends AppCompatActivity {

    private EditText inputTitulo;
    private EditText inputDescripcion;
    private EditText inputAutor;
    private Button btnCrearPublicacion;
    private Button btnAdjuntarImagen;
    private TextView textCantidadImagenes;
    private TextView textFechaHora;
    private List<Uri> imageUris;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ImageButton botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_publicacion);

        inputTitulo = findViewById(R.id.inputTitulo);
        inputDescripcion = findViewById(R.id.inputDescripcion);
        inputAutor = findViewById(R.id.inputAutor);
        btnCrearPublicacion = findViewById(R.id.btnCrearPublicacion);
        btnAdjuntarImagen = findViewById(R.id.btnAdjuntarImagen);
        textCantidadImagenes = findViewById(R.id.textCantidadImagenes);
        textFechaHora = findViewById(R.id.textFechaHora);
        String currentDateTime = getCurrentDateTime();
        textFechaHora.setText(currentDateTime);
        botonVolver = findViewById(R.id.botonVolver);

        imageUris = new ArrayList<>();

        runnable = new Runnable() {
            @Override
            public void run() {
                actualizarFechaHora();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        btnCrearPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = inputTitulo.getText().toString();
                String descripcion = inputDescripcion.getText().toString();
                String autor = inputAutor.getText().toString();

                if (titulo.isEmpty() || descripcion.isEmpty() || autor.isEmpty()) {
                    Toast.makeText(CrearPublicacionActivity.this, "Todos los campos son obligatorios",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Publicacion nuevaPublicacion = new Publicacion(titulo, descripcion, autor, currentDateTime);
                    enviarNuevaPublicacion(nuevaPublicacion);
                }
            }
        });

        btnAdjuntarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearPublicacionActivity.this, VerPublicaciones.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ClipData clipData = result.getData().getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            imageUris.add(clipData.getItemAt(i).getUri());
                        }
                    } else {
                        Uri selectedImage = result.getData().getData();
                        imageUris.add(selectedImage);
                    }
                    textCantidadImagenes.setText("Imágenes subidas: " + imageUris.size());
                }
            });

    private void enviarNuevaPublicacion(Publicacion publicacion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PublicacionService service = retrofit.create(PublicacionService.class);
        Call<Publicacion> call = service.nuevaPublicacion(publicacion);

        call.enqueue(new Callback<Publicacion>() {
            @Override
            public void onResponse(Call<Publicacion> call, Response<Publicacion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearPublicacionActivity.this, "Publicación creada exitosamente", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(CrearPublicacionActivity.this, VerPublicaciones.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CrearPublicacionActivity.this, "Error al crear la publicación", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Publicacion> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CrearPublicacionActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la fecha y hora actual
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void actualizarFechaHora() {
        String fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        textFechaHora.setText(fechaHora);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
