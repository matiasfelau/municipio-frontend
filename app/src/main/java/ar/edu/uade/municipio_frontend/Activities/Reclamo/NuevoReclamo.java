package com.example.tuapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NuevoReclamo extends AppCompatActivity {

    private Spinner spinnerSitio;
    private Spinner spinnerDesperfecto;
    private EditText editTextDescripcion;
    private ImageButton buttonAdjuntarArchivos;
    private Button buttonGenerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_reclamo);

        // elementos de la vista
        spinnerSitio = findViewById(R.id.spinnerSitio);
        spinnerDesperfecto = findViewById(R.id.spinnerDesperfecto);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        buttonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);
        buttonGenerar = findViewById(R.id.buttonGenerar);

        // spinner para sitio
        ArrayAdapter<CharSequence> adapterSitio = ArrayAdapter.createFromResource(this,
                R.array.sitio_options, android.R.layout.simple_spinner_item);
        adapterSitio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSitio.setAdapter(adapterSitio);

        // spinner para desperfecto
        ArrayAdapter<CharSequence> adapterDesperfecto = ArrayAdapter.createFromResource(this,
                R.array.desperfecto_options, android.R.layout.simple_spinner_item);
        adapterDesperfecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesperfecto.setAdapter(adapterDesperfecto);

        // botón de generar reclamo
        buttonGenerar.setOnClickListener(view -> {
            // Obtener los valores seleccionados
            String sitioSeleccionado = spinnerSitio.getSelectedItem().toString();
            String desperfectoSeleccionado = spinnerDesperfecto.getSelectedItem().toString();
            String descripcion = editTextDescripcion.getText().toString();

            // Aquí puedes añadir la lógica para manejar el reclamo
            Toast.makeText(NuevoReclamo.this, "Reclamo generado:\n" +
                    "Sitio: " + sitioSeleccionado + "\nDesperfecto: " + desperfectoSeleccionado +
                    "\nDescripción: " + descripcion, Toast.LENGTH_LONG).show();
        });

        // botón de adjuntar archivos
        buttonAdjuntarArchivos.setOnClickListener(view -> {
            // Aquí puedes añadir la lógica para adjuntar archivos
            Toast.makeText(NuevoReclamo.this, "Adjuntar archivos no implementado", Toast.LENGTH_SHORT).show();
        });
    }
}
