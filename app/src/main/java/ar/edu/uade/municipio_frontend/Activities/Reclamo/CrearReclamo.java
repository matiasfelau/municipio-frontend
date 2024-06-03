package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import ar.edu.uade.municipio_frontend.R;

public class CrearReclamo extends AppCompatActivity {
    private Spinner spinnerSitio;
    private Spinner spinnerDesperfecto;
    private EditText editTextDescripcion;
    private Button buttonGenerar;
    private Button buttonAdjuntarArchivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reclamo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        spinnerSitio = findViewById(R.id.spinnerSitio);
        spinnerDesperfecto = findViewById(R.id.spinnerDesperfecto);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        buttonGenerar = findViewById(R.id.buttonGenerar);
        buttonAdjuntarArchivos = findViewById(R.id.buttonAdjuntarArchivos);

        // Configurar adaptadores para los Spinners
        ArrayAdapter<CharSequence> adapterSitio = ArrayAdapter.createFromResource(this,
                R.array.sitio_options, android.R.layout.simple_spinner_item);
        adapterSitio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSitio.setAdapter(adapterSitio);

        ArrayAdapter<CharSequence> adapterDesperfecto = ArrayAdapter.createFromResource(this,
                R.array.desperfecto_options, android.R.layout.simple_spinner_item);
        adapterDesperfecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesperfecto.setAdapter(adapterDesperfecto);

        // Configurar el botón de generar reclamo
        buttonGenerar.setOnClickListener(view -> {
            String sitioSeleccionado = spinnerSitio.getSelectedItem().toString();
            String desperfectoSeleccionado = spinnerDesperfecto.getSelectedItem().toString();
            String descripcion = editTextDescripcion.getText().toString();

            // Mostrar el popup de éxito
            FragmentManager fragmentManager = getSupportFragmentManager();
            ReclamoExitosoDialog dialog = new ReclamoExitosoDialog();
            dialog.show(fragmentManager, "ReclamoExitosoDialog");
        });
        /*
        // Configurar el botón de adjuntar archivos
        buttonAdjuntarArchivos.setOnClickListener(view -> {
            // Aquí puedes añadir la lógica para adjuntar archivos
            Toast.makeText(NuevoReclamo.this, "Adjuntar archivos no implementado", Toast.LENGTH_SHORT).show();
        });

         */
    }
}