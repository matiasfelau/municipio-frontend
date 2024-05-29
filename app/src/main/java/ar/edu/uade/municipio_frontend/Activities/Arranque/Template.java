package ar.edu.uade.municipio_frontend.Activities.Arranque;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;

public class Template extends AppCompatActivity {
    private VecinoHelper vecinoHelper;

    private EmpleadoHelper empleadoHelper;

    private InvitadoHelper invitadoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO al template se le puede cambiar la apariencia y hacer que se borre con logout
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_template);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        

        vecinoHelper = new VecinoHelper(this);

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        Intent nuevaActividad;

        try {
            Vecino vecino = vecinoHelper.getVecinos().get(0);

            nuevaActividad = new Intent(Template.this, VecinoIngreso.class);

            nuevaActividad.putExtra("ingresado", true);

            nuevaActividad.putExtra("documento", vecino.getDocumento());

            startActivity(nuevaActividad);

        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);

                nuevaActividad = new Intent(Template.this, EmpleadoIngreso.class);

                nuevaActividad.putExtra("ingresado", true);

                nuevaActividad.putExtra("legajo", empleado.getLegajo());

                startActivity(nuevaActividad);

            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);

                    nuevaActividad = new Intent(Template.this, InvitadoIngreso.class);

                    nuevaActividad.putExtra("ingresado", true);

                    startActivity(nuevaActividad);

                } catch (Exception e4) {
                    nuevaActividad = new Intent(Template.this, VecinoIngreso.class);

                    nuevaActividad.putExtra("ingresado", false);

                    startActivity(nuevaActividad);

                }
            }
        }
    }
}