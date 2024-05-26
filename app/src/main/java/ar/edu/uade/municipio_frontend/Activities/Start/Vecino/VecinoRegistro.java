package ar.edu.uade.municipio_frontend.Activities.Start.Vecino;

import android.annotation.SuppressLint;
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

import ar.edu.uade.municipio_frontend.Activities.VerReclamos;
import ar.edu.uade.municipio_frontend.POJOs.CredencialVecino;
import ar.edu.uade.municipio_frontend.POJOs.Token;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VecinoRegistro extends AppCompatActivity {

    EditText inputDocumento;
    EditText inputEmail;
    TextView avisoDatosIncorrectos;
    Button botonEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_vecino_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputDocumento = findViewById(R.id.inputDocumentoRegistro);

        inputEmail = findViewById(R.id.inputEmailRegistro);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos);

        botonEnviar = findViewById(R.id.botonEnviar);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar(new CredencialVecino(inputDocumento.getText().toString(), "", inputEmail.getText().toString()));

            }
        });

    }
    private void registrar(CredencialVecino credencialVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);

        Call<Boolean> call = credencialVecinoService.postVecino(credencialVecino);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.body() != null) {
                    if (response.body()) {
                        Intent nuevaActividad = new Intent(VecinoRegistro.this, VecinoIngreso.class);

                        startActivity(nuevaActividad);

                    } else {
                        avisoDatosIncorrectos.setVisibility(View.VISIBLE);

                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }
}