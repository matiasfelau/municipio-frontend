package ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado;

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

import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Publicacion.VerPublicacionesInvitado;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Models.Invitado;
import ar.edu.uade.municipio_frontend.R;

public class InvitadoIngreso extends AppCompatActivity {
    Button botonIngresar;

    ImageButton botonCambiarUsuarioIzquierda;

    InvitadoHelper helper;

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

        helper = new InvitadoHelper(this);

        boolean ingresado = getIntent().getBooleanExtra("ingresado", false);

        if (ingresado) {
            ingresar();

        }

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar();

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

    private void ingresar() {
        helper.saveInvitado(new Invitado(1, "John Doe"));

        Intent nuevaActividad = new Intent(InvitadoIngreso.this, VerPublicacionesInvitado.class);

        nuevaActividad.putExtra("from", "InvitadoIngreso");

        startActivity(nuevaActividad);

    }
}