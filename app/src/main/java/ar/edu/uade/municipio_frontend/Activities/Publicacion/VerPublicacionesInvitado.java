package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;

public class VerPublicacionesInvitado extends AppCompatActivity {
    ImageButton boton;
    EmpleadoHelper empleadoHelper;
    InvitadoHelper invitadoHelper;
    VecinoHelper vecinoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_publicaciones_invitado);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boton = findViewById(R.id.botonLogout);

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        vecinoHelper = new VecinoHelper(this);

        String from = getIntent().getStringExtra("from");

        if (from != null) {
            if (from.equals("InvitadoIngreso")) {
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                    }
                });

            }
        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupSalir();
            }
        });
    }
    private void mostrarPopupSalir() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_confirmacion, null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        Button btnConfirmarSalir = view.findViewById(R.id.btn_confirmar_salir);
        Button btnCancelarsalir = view.findViewById(R.id.btn_cancelar_salir);

        btnConfirmarSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
                dialog.dismiss();
            }
        });

        btnCancelarsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Cerrar el popup sin hacer nada si se cancela
            }
        });

        dialog.show();
    }

    private void cerrarSesion() {
        try {
            Vecino vecino = vecinoHelper.getVecinos().get(0);
            vecinoHelper.deleteVecinos();
            Intent nuevaActividad = new Intent(VerPublicacionesInvitado.this, VecinoIngreso.class);
            nuevaActividad.putExtra("ingresado", false);
            nuevaActividad.putExtra("from", "VerReclamos");
            startActivity(nuevaActividad);
        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);
                empleadoHelper.deleteEmpleados();
                Intent nuevaActividad = new Intent(VerPublicacionesInvitado.this, VecinoIngreso.class);
                nuevaActividad.putExtra("ingresado", false);
                nuevaActividad.putExtra("from", "VerReclamos");
                startActivity(nuevaActividad);
            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);
                    invitadoHelper.deleteInvitados();
                    Intent nuevaActividad = new Intent(VerPublicacionesInvitado.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerReclamos");
                    startActivity(nuevaActividad);
                } catch (Exception e4) {
                    Intent nuevaActividad = new Intent(VerPublicacionesInvitado.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerReclamos");
                    startActivity(nuevaActividad);
                }
            }
        }
    }
}