package ar.edu.uade.municipio_frontend.Activities.Usuario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.CredencialVecino;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrimerIngreso extends AppCompatActivity {
    EditText inputPassword;
    EditText inputRepetirPassword;
    TextView avisoDatosIncorrectos;
    Button botonEnviar;
    Button botonMantener;
    VecinoHelper vecinoHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_primer_ingreso);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        inputPassword = findViewById(R.id.inputPrimerPassword);

        inputRepetirPassword = findViewById(R.id.inputRepetirPrimerPassword);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos);

        botonEnviar = findViewById(R.id.botonEnviarPassword);

        botonMantener = findViewById(R.id.botonMismaPassword);

        vecinoHelper = new VecinoHelper(this);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputPassword.getText().toString().isEmpty() && inputPassword.getText().toString().equals(inputRepetirPassword.getText().toString())) {
                    modificar(new CredencialVecino(
                            getIntent().getStringExtra("documento"),
                            inputPassword.getText().toString(),
                            ""
                    ));

                }
            }
        });

        botonMantener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(new CredencialVecino(
                        getIntent().getStringExtra("documento"),
                        "",
                        ""
                ));

            }
        });

    }

    private void modificar(CredencialVecino credencialVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);

        Call<Boolean> call = credencialVecinoService.modificarPassword(credencialVecino);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {

                if (response.code() == 200) {
                    if (!Objects.equals(credencialVecino.getPassword(), "")) {
                        Vecino vecino = vecinoHelper.getVecinos().get(0);
                        vecinoHelper.deleteVecinos();
                        vecino.setPassword(inputPassword.getText().toString());
                        vecinoHelper.saveVecino(vecino);
                    }

                    Intent nuevaActividad = new Intent(PrimerIngreso.this, VerReclamos.class);

                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                    nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                    nuevaActividad.putExtra("from", "PrimerIngreso");

                    nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                    startActivity(nuevaActividad);

                }
                else if (response.code() == 400) {
                    avisoDatosIncorrectos.setVisibility(View.VISIBLE);

                }

            }
            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }
}