package ar.edu.uade.municipio_frontend.Activities.Denuncia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import ar.edu.uade.municipio_frontend.Services.DenunciaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearDenuncia extends AppCompatActivity {
    private static final int PICK_FILES_REQUEST = 1;
    private List<Uri> fileUris = new ArrayList<>();

    private List<String> fileStrings = new ArrayList<>();

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri fileUri = data.getClipData().getItemAt(i).getUri();
                            fileUris.add(fileUri);
                            System.out.println(fileUri);
                        }
                    }else if (data.getData() != null) {
                        Uri fileUri = data.getData();
                        fileUris.add(fileUri);
                    }
                }
            }
    );
    private ImageButton botonVolver;
    private Spinner tipoDenuncia;
    private EditText insertNombre;
    private EditText insertApellido;
    private EditText insertNombreComercio;
    private EditText insertDireccion;
    private EditText insertDescripcion;
    private Button botonAdjuntarArchivos;
    private CheckBox confirmacionResponsabilidad;
    private Button botonGenerar;
    private Autenticacion autenticacion;

    /*
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
            });*/

    @SuppressLint("MissingInflatedId")
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

        tipoDenuncia = findViewById(R.id.tipoDenuncia);

        insertNombre = findViewById(R.id.insertNombre);

        insertApellido = findViewById(R.id.insertApellido);

        insertNombreComercio = findViewById(R.id.insertNombreComercio);

        insertNombreComercio.setVisibility(View.INVISIBLE);

        insertDireccion = findViewById(R.id.insertDireccion);

        insertDescripcion = findViewById(R.id.editTextDescripcion);

        botonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        confirmacionResponsabilidad = findViewById(R.id.checkBoxAcceptResponsibility);

        botonGenerar = findViewById(R.id.buttonGenerar);

        checkPermissions();

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO"));

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
                openFilePicker();

            }
        });


        botonGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                if(confirmacionResponsabilidad.isChecked()
                        && ((!insertNombre.getText().toString().isEmpty() && !insertApellido.getText().toString().isEmpty()) || !insertNombreComercio.getText().toString().isEmpty())
                        && !insertDireccion.getText().toString().isEmpty()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        for(Uri uri: fileUris){
                            System.out.println(uri);
                            fileStrings.add(convertFileToBase64(uri));
                        }

                        CompletableFuture.supplyAsync(() -> {
                            if (tipoDenuncia.getSelectedItem().equals("VECINO")) {
                                generarDenunciaVecino(new ContainerDenunciaVecino(
                                        new Denuncia(
                                                null,
                                                insertDescripcion.getText().toString(),
                                                "Nuevo",
                                                confirmacionResponsabilidad.isChecked(),
                                                getIntent().getStringExtra("documento"),
                                                fileStrings
                                        ),
                                        new VecinoDenunciado(
                                                null,
                                                null,
                                                insertDireccion.getText().toString(),
                                                insertNombre.getText().toString(),
                                                insertApellido.getText().toString()
                                        )));

                            } else if (tipoDenuncia.getSelectedItem().equals("COMERCIO")) {
                                System.out.println("GENERADO DE DENUNCIA DE COMERCIO");
                                generarDenunciaComercio(new ContainerDenunciaComercio(
                                        new Denuncia(
                                                null,
                                                insertDescripcion.getText().toString(),
                                                "Nuevo",
                                                confirmacionResponsabilidad.isChecked(),
                                                getIntent().getStringExtra("documento"),
                                                fileStrings
                                        ),
                                        new ComercioDenunciado(
                                                null,
                                                null,
                                                insertNombreComercio.getText().toString(),
                                                insertDireccion.getText().toString()
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
            }
        });

        tipoDenuncia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==1){
                    insertNombre.setVisibility(View.INVISIBLE);
                    insertApellido.setVisibility(View.INVISIBLE);
                    insertNombreComercio.setVisibility(View.VISIBLE);
                }else{
                    insertNombre.setVisibility(View.VISIBLE);
                    insertApellido.setVisibility(View.VISIBLE);
                    insertNombreComercio.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                insertNombre.setVisibility(View.VISIBLE);
                insertApellido.setVisibility(View.VISIBLE);
                insertNombreComercio.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Files"));

    }

    private void generarDenunciaVecino(ContainerDenunciaVecino containerDenunciaVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);

        Call<Denuncia> call = denunciaService.nuevaDenunciaVecino(new AutenticacionDenunciaVecino(autenticacion, containerDenunciaVecino));

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                System.out.println("DENUNCIA VECINO GENERADA");

                try {
                    assert response.body() != null;
                    String message = "El ID del reclamo es: " + response.body().getIdDenuncia();
                    showSnackbarAndWait(message,response.body().getIdDenuncia(), true, this::navigateToVerDenuncias);

                }catch (AssertionError ignored) {

                }

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

            private void navigateToVerDenuncias() {
                Intent nuevaActividad = new Intent(CrearDenuncia.this, VerDenuncias.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }

            @Override
            public void onFailure(@NonNull Call<Denuncia> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }

    private void showSnackbarAndWait(String message,int id, boolean isSuccess, Runnable onDismissed) {
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        if (isSuccess) {
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.grey));
        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.black));
        }

        snackbar.setAction("Copiar ID", v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("ID Reclamo", String.valueOf(id));
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, "ID copiado al portapapeles", Snackbar.LENGTH_SHORT).show();
        });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (onDismissed != null) {
                    onDismissed.run();
                }
            }
        });

        snackbar.show();
    }

    private void generarDenunciaComercio(ContainerDenunciaComercio containerDenunciaComercio) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        DenunciaService denunciaService = retrofit.create(DenunciaService.class);
        Call<Denuncia> call = denunciaService.nuevaDenunciaComercio(new AutenticacionDenunciaComercio(autenticacion, containerDenunciaComercio));

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                System.out.println("DENUNCIA COMERCIO GENERADA");

                assert response.body() != null;
                Toast.makeText(getApplicationContext(), "El ID de la denuncia es:"+ response.body().getIdDenuncia(), Toast.LENGTH_LONG).show();

                Intent nuevaActividad = new Intent(CrearDenuncia.this, VerDenuncias.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);

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

    /*
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
    */

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public String convertFileToBase64(Uri uri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream fileStream = contentResolver.openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                assert fileStream != null;
                if ((bytesRead = fileStream.read(buffer)) == -1) break;
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            System.out.println("Base 64: " + base64String);

            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}