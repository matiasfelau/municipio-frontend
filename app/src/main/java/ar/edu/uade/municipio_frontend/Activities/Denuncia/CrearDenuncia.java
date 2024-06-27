package ar.edu.uade.municipio_frontend.Activities.Denuncia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionDenunciaComercio;
import ar.edu.uade.municipio_frontend.Models.AutenticacionDenunciaVecino;
import ar.edu.uade.municipio_frontend.Models.ComercioDenunciado;
import ar.edu.uade.municipio_frontend.Models.ContainerDenunciaComercio;
import ar.edu.uade.municipio_frontend.Models.ContainerDenunciaVecino;
import ar.edu.uade.municipio_frontend.Models.Denuncia;
import ar.edu.uade.municipio_frontend.Models.VecinoDenunciado;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ApiService;
import ar.edu.uade.municipio_frontend.Services.DenunciaService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearDenuncia extends AppCompatActivity {
    ImageButton botonVolver;
    EditText insertNombre;
    EditText insertDireccion;
    EditText insertDescripcion;
    Button botonAdjuntarArchivos;
    CheckBox confirmacionResponsabilidad;
    Button botonGenerar;
    ImageButton botonCambiarTipoUsuarioIzquierda;
    TextView tipoDenunciado;
    ImageButton botonCambiarTipoUsuarioDerecha;
    Autenticacion autenticacion;
    List<Uri> imageUris = new ArrayList<>();
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
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_crear_denuncia);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        botonVolver = findViewById(R.id.botonVolver);

        insertNombre = findViewById(R.id.insertNombre);

        insertDireccion = findViewById(R.id.insertDireccion);

        insertDescripcion = findViewById(R.id.editTextDescripcion);

        botonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        confirmacionResponsabilidad = findViewById(R.id.checkBoxAcceptResponsibility);

        botonGenerar = findViewById(R.id.buttonGenerar);

        botonCambiarTipoUsuarioIzquierda = findViewById(R.id.botonCambiarUsuarioIzquierda);

        tipoDenunciado = findViewById(R.id.textTipoUsuario);

        botonCambiarTipoUsuarioDerecha = findViewById(R.id.botonCambiarUsuarioDerecha);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("USUARIO"),
                getIntent().getStringExtra("token"));

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(CrearDenuncia.this, VerDenuncias.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);

            }
        });

        botonAdjuntarArchivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        botonGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    CompletableFuture.supplyAsync(() -> {
                        if (tipoDenunciado.getText().toString().equals("Vecino")) {
                            generarDenunciaVecino(new ContainerDenunciaVecino(
                                    new Denuncia(
                                            null,
                                            insertDescripcion.getText().toString(),
                                            null,
                                            confirmacionResponsabilidad.isChecked(),
                                            getIntent().getStringExtra("documento")
                                    ),
                                    new VecinoDenunciado(
                                            null,
                                            null,
                                            insertDireccion.getText().toString(),
                                            insertNombre.getText().toString().split(" ")[0],
                                            insertNombre.getText().toString().split(" ")[1]
                                    )));

                        } else if (tipoDenunciado.getText().toString().equals("Comercio")) {
                            generarDenunciaComercio(new ContainerDenunciaComercio(
                                    new Denuncia(
                                            null,
                                            insertDescripcion.getText().toString(),
                                            null,
                                            confirmacionResponsabilidad.isChecked(),
                                            getIntent().getStringExtra("documento")
                                    ),
                                    new ComercioDenunciado(
                                            null,
                                            null,
                                            insertDireccion.getText().toString(),
                                            insertNombre.getText().toString()
                                    )));

                        }
                        return "Resultado de la primera tarea";
                    }, executor).thenApply(result1 -> {
                        // Puedes usar el resultado de la primera tarea aquí
                        System.out.println(result1);

                        // Segunda tarea
                        try {
                            Thread.sleep(1000); // Simulando una tarea de 1 segundo
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "Resultado de la segunda tarea";
                    }).thenAccept(result2 -> {
                        // Puedes usar el resultado de la segunda tarea aquí
                        System.out.println(result2);
                    }).exceptionally(ex -> {
                        // Manejo de errores
                        ex.printStackTrace();
                        return null;
                    });
                }

            }
        });

        botonCambiarTipoUsuarioIzquierda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tipoDenunciado.setText("Vecino");

                botonCambiarTipoUsuarioDerecha.setVisibility(View.VISIBLE);

                botonCambiarTipoUsuarioIzquierda.setVisibility(View.INVISIBLE);

            }
        });

        botonCambiarTipoUsuarioDerecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tipoDenunciado.setText("Comercio");

                botonCambiarTipoUsuarioIzquierda.setVisibility(View.VISIBLE);

                botonCambiarTipoUsuarioDerecha.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        galleryLauncher.launch(intent);

    }

    private void generarDenunciaVecino(ContainerDenunciaVecino containerDenunciaVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        Call<Denuncia> call = denunciaService.nuevaDenunciaVecino(new AutenticacionDenunciaVecino(autenticacion, containerDenunciaVecino));

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                System.out.println("GENERAR DENUNCIA VECINO");

                if (response.code() == 200) {
                    System.out.println(response.code());

                } else if (response.code() == 400) {
                    System.out.println(response.code());

                } else if (response.code() == 401) {
                    System.out.println(response.code());

                } else if (response.code() == 403) {
                    System.out.println(response.code());

                } else if (response.code() == 500) {
                    System.out.println(response.code());

                } else {
                    System.out.println(response.code());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Denuncia> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    private void generarDenunciaComercio(ContainerDenunciaComercio containerDenunciaComercio) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        Call<Denuncia> call = denunciaService.nuevaDenunciaComercio(new AutenticacionDenunciaComercio(autenticacion, containerDenunciaComercio));

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                System.out.println("GENERAR DENUNCIA COMERCIO");

                if (response.code() == 200) {
                    System.out.println(response.code());

                } else if (response.code() == 400) {
                    System.out.println(response.code());

                } else if (response.code() == 401) {
                    System.out.println(response.code());

                } else if (response.code() == 403) {
                    System.out.println(response.code());

                } else if (response.code() == 500) {
                    System.out.println(response.code());

                } else {
                    System.out.println(response.code());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Denuncia> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    private void uploadImages(Integer idReclamo, List<Uri> imageUris) {

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

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.uploadImages(idReclamo, parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Imágenes subidas exitosamente
                } else {
                    // Error al subir las imágenes
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();

        } else {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            return cursor.getString(idx);

        }
    }
}