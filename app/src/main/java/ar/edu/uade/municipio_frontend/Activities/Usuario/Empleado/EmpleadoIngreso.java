package ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado;

import android.content.Intent;
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

import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Token;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.EmpleadoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmpleadoIngreso extends AppCompatActivity {
    EditText inputLegajo;

    EditText inputPassword;

    TextView avisoDatosIncorrectos;

    Button botonIngresar;

    ImageButton botonCambiarUsuarioIzquierda;

    ImageButton botonCambiarUsuarioDerecha;

    private EmpleadoHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_empleado_ingreso);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputLegajo = findViewById(R.id.inputLegajoInspector);

        inputPassword = findViewById(R.id.inputPasswordInspector);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos);

        botonIngresar = findViewById(R.id.botonIngresoInspector);

        botonCambiarUsuarioIzquierda = findViewById(R.id.botonCambiarUsuarioIzquierda);

        botonCambiarUsuarioDerecha = findViewById(R.id.botonCambiarUsuarioDerecha);

        helper = new EmpleadoHelper(this);

        boolean ingresado = getIntent().getBooleanExtra("ingresado", false);

        if (ingresado) {
            Empleado empleado = helper.getEmpleadoByLegajo(getIntent().getIntExtra("legajo", 0));

            ingresar(new Empleado(
                    empleado.getLegajo(),
                    null,
                    null,
                    empleado.getPassword(),
                    null
            ));

        }

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar(new Empleado(Integer.parseInt(inputLegajo.getText().toString()),
                        null,
                        null,
                        inputPassword.getText().toString(),
                        null
                ));

            }
        });

        botonCambiarUsuarioIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(EmpleadoIngreso.this, VecinoIngreso.class);

                startActivity(nuevaActividad);

            }
        });

        botonCambiarUsuarioDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(EmpleadoIngreso.this, InvitadoIngreso.class);

                startActivity(nuevaActividad);

            }
        });

    }
    private void ingresar(Empleado empleado) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        EmpleadoService empleadoService = retrofit.create(EmpleadoService.class);

        Call<Token> call = empleadoService.getToken(empleado);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                if (response.body() != null) {
                    buscar(empleado.getLegajo());

                    Intent nuevaActividad = new Intent(EmpleadoIngreso.this, VerReclamos.class);

                    nuevaActividad.putExtra("legajo", empleado.getLegajo());

                    nuevaActividad.putExtra("token", response.body().getToken());

                    nuevaActividad.putExtra("from", "EmpleadoIngreso");

                    startActivity(nuevaActividad);

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

    private void buscar(int legajo) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        EmpleadoService empleadoService = retrofit.create(EmpleadoService.class);

        Call<Empleado> call = empleadoService.getPerfil(legajo);

        call.enqueue(new Callback<Empleado>() {
            @Override
            public void onResponse(@NonNull Call<Empleado> call, @NonNull Response<Empleado> response) {
                if (response.body() != null) {
                    helper.saveEmpleado(response.body());

                }
            }
            @Override
            public void onFailure(@NonNull Call<Empleado> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }
}