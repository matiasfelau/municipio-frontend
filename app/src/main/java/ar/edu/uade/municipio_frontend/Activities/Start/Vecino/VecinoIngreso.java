package ar.edu.uade.municipio_frontend.Activities.Start.Vecino;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Application.PrimerIngreso;
import ar.edu.uade.municipio_frontend.Activities.Start.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.VerReclamos;
import ar.edu.uade.municipio_frontend.POJOs.CredencialVecino;
import ar.edu.uade.municipio_frontend.POJOs.Email;
import ar.edu.uade.municipio_frontend.POJOs.Token;
import ar.edu.uade.municipio_frontend.POJOs.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import ar.edu.uade.municipio_frontend.Services.VecinoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ar.edu.uade.municipio_frontend.Utilities.*;

public class VecinoIngreso extends AppCompatActivity {
    private VecinoHelper helper;
    String email;
    EditText inputDocumento;
    EditText inputPassword;
    Button botonOlvidoPassword;
    TextView avisoDatosIncorrectos;
    Button botonIngresar;
    Button botonRegistro;
    ImageButton botonCambiarUsuarioDerecha;

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
                ingresar(new CredencialVecino(inputDocumento.getText().toString(), inputPassword.getText().toString(), ""));

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
        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);
        VecinoService vecinoService = retrofit.create(VecinoService.class);

        Call<Email> call0 =  credencialVecinoService.getEmail(documento);
        call0.enqueue(new Callback<Email>() {
            @Override
            public void onResponse(Call<Email> call, Response<Email> response) {
                if (response.body() != null){
                    email = response.body().getEmail();
                }
            }

            @Override
            public void onFailure(Call<Email> call, Throwable t) {
                System.out.println(t);
            }
        });
        Call<Vecino> call = vecinoService.getPerfil(documento);

        call.enqueue(new Callback<Vecino>() {
            @Override
            public void onResponse(@NonNull Call<Vecino> call, @NonNull Response<Vecino> response) {
                if (response.body() != null) {
                    Vecino body = response.body();
                    Vecino vecino = new Vecino(body.getNombre(),body.getApellido(),body.getDocumento(),email);
                    helper.saveVecino(body);
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
                Intent nuevaActividad;

                if (Boolean.TRUE.equals(response.body())) {
                    nuevaActividad = new Intent(VecinoIngreso.this, PrimerIngreso.class);

                    nuevaActividad.putExtra("documento", documento);

                } else {
                    nuevaActividad = new Intent(VecinoIngreso.this, VerReclamos.class);

                }
                nuevaActividad.putExtra("token", token);

                startActivity(nuevaActividad);

            }
            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }
}