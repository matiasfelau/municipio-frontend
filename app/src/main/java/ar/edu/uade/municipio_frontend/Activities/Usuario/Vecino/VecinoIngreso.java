package ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Usuario.PrimerIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.CredencialVecino;
import ar.edu.uade.municipio_frontend.Models.Token;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import ar.edu.uade.municipio_frontend.Services.VecinoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VecinoIngreso extends AppCompatActivity {
    private VecinoHelper helper;
    EditText inputDocumento;
    EditText inputPassword;
    Button botonOlvidoPassword;
    TextView avisoDatosIncorrectos;
    Button botonIngresar;
    Button botonRegistro;
    ImageButton botonCambiarUsuarioDerecha;
    Spinner spinnerTipoDocumentacion;
    String tipoDocumentacion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_vecino_ingreso);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputDocumento = findViewById(R.id.inputDocumento);

        helper = new VecinoHelper(this);

        inputPassword = findViewById(R.id.inputPassword);

        botonOlvidoPassword = findViewById(R.id.botonOlvidoPassword);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos);

        botonIngresar = findViewById(R.id.botonIngresar);

        botonRegistro = findViewById(R.id.botonRegistro);

        botonCambiarUsuarioDerecha = findViewById(R.id.botonCambiarUsuarioDerecha);

        spinnerTipoDocumentacion = findViewById(R.id.tipoDocumentacion);

        tipoDocumentacion = "DNI";

        boolean ingresado = getIntent().getBooleanExtra("ingresado", false);

        if (ingresado) {
            Vecino vecino = helper.getVecinoByDocumento(getIntent().getStringExtra("documento"));
            System.out.println(vecino.toString());
            ingresar(new CredencialVecino(
                    vecino.getDocumento(),
                    vecino.getPassword(),
                    ""
            ));

        }

        String from = getIntent().getStringExtra("from");

        if (from != null) {
            if (from.equals("VerReclamos")) {
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                    }
                });

            }
        }

        spinnerTipoDocumentacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1) {
                    tipoDocumentacion = "DNI";
                } else if (position==2) {
                    tipoDocumentacion = "PAS";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonOlvidoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VecinoIngreso.this, VecinoRecupero.class);

                startActivity(nuevaActividad);

            }
        });

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar(new CredencialVecino(tipoDocumentacion+inputDocumento.getText().toString(), inputPassword.getText().toString(), ""));

            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VecinoIngreso.this, VecinoRegistro.class);

                startActivity(nuevaActividad);

            }
        });

        botonCambiarUsuarioDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VecinoIngreso.this, EmpleadoIngreso.class);

                startActivity(nuevaActividad);

            }
        });

    }
    private void ingresar(CredencialVecino credencialVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);

        Call<Token> call = credencialVecinoService.getToken(credencialVecino);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                if (response.body() != null) {
                    buscar(credencialVecino.getDocumento());
                    verificar(credencialVecino.getDocumento(), response.body().getToken());
                } else {
                    avisoDatosIncorrectos.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }

    private void buscar(String documento) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        VecinoService vecinoService = retrofit.create(VecinoService.class);

        Call<Vecino> call = vecinoService.getPerfil(documento);

        call.enqueue(new Callback<Vecino>() {
            @Override
            public void onResponse(@NonNull Call<Vecino> call, @NonNull Response<Vecino> response) {
                if (response.body() != null) {
                    helper.saveVecino(response.body());

                }
            }
            @Override
            public void onFailure(@NonNull Call<Vecino> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void verificar(String documento, String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);

        Call<Boolean> call = credencialVecinoService.getConfirmacion(documento);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                Intent nuevaActividad = null;

                if (response.code() == 200) {
                    nuevaActividad = new Intent(VecinoIngreso.this, PrimerIngreso.class);

                }
                else if (response.code() == 400) {
                    nuevaActividad = new Intent(VecinoIngreso.this, VerReclamos.class);

                }
                if (nuevaActividad != null) {
                    nuevaActividad.putExtra("documento", documento);

                    nuevaActividad.putExtra("token", token);

                    nuevaActividad.putExtra("from", "VecinoIngreso");

                    nuevaActividad.putExtra("USUARIO", "VECINO");

                    startActivity(nuevaActividad);
                }


            }
            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }
}