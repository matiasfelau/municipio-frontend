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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Models.CredencialVecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import ar.edu.uade.municipio_frontend.Utilities.EmailValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VecinoRecupero extends AppCompatActivity {
    private EditText inputDocumento;
    private EditText inputEmail;
    private TextView avisoDatosIncorrectos;
    private Button botonEnviar;
    private String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private Spinner spinnerTipoDocumentacion;
    private String tipoDocumentacion;
    private ImageButton botonVolver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_vecino_recupero);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputDocumento = findViewById(R.id.inputDocumentoRecupero);

        inputEmail = findViewById(R.id.inputEmailRecupero);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos); //TODO agregar al hacer la funcionalidad

        botonEnviar = findViewById(R.id.botonEnviarOlvido);

        spinnerTipoDocumentacion = findViewById(R.id.tipoDocumentacion);

        botonVolver = findViewById(R.id.botonVolver);

        tipoDocumentacion = "DNI";

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

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VecinoRecupero.this, VecinoIngreso.class);
                startActivity(nuevaActividad);
            }
        });

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (EmailValidation.patternMatches(email, regexPattern)) {
                    recuperar(new CredencialVecino(tipoDocumentacion+inputDocumento.getText().toString(), "", email));

                    Intent nuevaActividad = new Intent(VecinoRecupero.this, VecinoIngreso.class);

                    startActivity(nuevaActividad);

                } else {
                    avisoDatosIncorrectos.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private void recuperar(CredencialVecino credencialVecino) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        CredencialVecinoService credencialVecinoService = retrofit.create(CredencialVecinoService.class);

        Call<Boolean> call = credencialVecinoService.recuperarPassword(credencialVecino);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {

            }
            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }
}