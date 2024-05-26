package ar.edu.uade.municipio_frontend.Activities.Start.Vecino;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.R;

public class VecinoRecupero extends AppCompatActivity {
    EditText inputEmail;
    TextView avisoDatosIncorrectos;
    Button botonEnviar;

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

        inputEmail = findViewById(R.id.inputEmailRecupero);

        avisoDatosIncorrectos = findViewById(R.id.textAvisoDatosIncorrectos); //TODO agregar al hacer la funcionalidad

        botonEnviar = findViewById(R.id.botonEnviarOlvido);

        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VecinoRecupero.this, VecinoIngreso.class);

                startActivity(nuevaActividad);

            }
        });

    }
}