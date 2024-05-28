package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;

public class VerReclamos extends AppCompatActivity {
    Button boton;
    EmpleadoHelper empleadoHelper;
    InvitadoHelper invitadoHelper;
    VecinoHelper vecinoHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_reclamos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String from = getIntent().getStringExtra("from");

        if (from != null) {
            if (from.equals("VecinoIngreso") || from.equals("EmpleadoIngreso") || from.equals("PrimerIngreso")) {
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                    }
                });

            }
        }

        boton = findViewById(R.id.logout);

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        vecinoHelper = new VecinoHelper(this);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Vecino vecino = vecinoHelper.getVecinos().get(0);

                    vecinoHelper.deleteVecinos();

                } catch (Exception e2) {
                    try {
                        Empleado empleado = empleadoHelper.getEmpleados().get(0);

                        empleadoHelper.deleteEmpleados();

                    } catch (Exception e3) {
                        try {
                            invitadoHelper.getInvitados().get(0);

                            invitadoHelper.deleteInvitados();

                        } catch (Exception e4) {
                            Intent nuevaActividad = new Intent(VerReclamos.this, VecinoIngreso.class);

                            nuevaActividad.putExtra("ingresado", false);

                            nuevaActividad.putExtra("from", "VerReclamos");

                            startActivity(nuevaActividad);

                        }
                    }
                }
            }
        });
    }
}