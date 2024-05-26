package ar.edu.uade.municipio_frontend.Activities.Start.Invitado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Start.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.VerPublicacionesInvitado;
import ar.edu.uade.municipio_frontend.R;

public class InvitadoIngreso extends AppCompatActivity {
    Button botonIngresar;
    ImageButton botonCambiarUsuarioIzquierda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_invitado_ingreso);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonIngresar = findViewById(R.id.botonIngresoInvitado);

        botonCambiarUsuarioIzquierda = findViewById(R.id.botonCambiarUsuarioIzquierda);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(InvitadoIngreso.this, VerPublicacionesInvitado.class);

                startActivity(nuevaActividad);

            }
        });

        botonCambiarUsuarioIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(InvitadoIngreso.this, EmpleadoIngreso.class);

                startActivity(nuevaActividad);

            }
        });

    }
}