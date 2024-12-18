package ar.edu.uade.municipio_frontend.Activities.Profesional;

import static ar.edu.uade.municipio_frontend.Utilities.Container.ProfesionalStoraged.getProfesional;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.osmdroid.views.MapView;

import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Comercio.ComercioParticular;
import ar.edu.uade.municipio_frontend.Activities.Comercio.VerComercio;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Utilities.MapHelper;

public class ProfesionalParticular extends AppCompatActivity {
    TextView nombreProfesional;
    TextView rubroProfesional;
    TextView telefonoProfesional;
    TextView emailProfesional;
    TextView descripcionProfesional;
    TextView aperturaProfesional;
    TextView cierreProfesional;
    TextView direccionProfesional;
    ImageButton botonVolver;
    MapView mapa;
    LinearLayout contenedorFotos;
    MapHelper mapHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesional_particular);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nombreProfesional = findViewById(R.id.nombreProfesional);
        rubroProfesional = findViewById(R.id.rubroProfesional);
        telefonoProfesional = findViewById(R.id.nroTelefono);
        emailProfesional = findViewById(R.id.email);
        descripcionProfesional = findViewById(R.id.descripcionProfesionalParticular);
        aperturaProfesional = findViewById(R.id.apertura);
        cierreProfesional = findViewById(R.id.cierre);
        direccionProfesional = findViewById(R.id.direccion);
        botonVolver = findViewById(R.id.botonVolver);
        mapa = findViewById(R.id.map);
        contenedorFotos = findViewById(R.id.contenedorFotos);
        mapHelper = new MapHelper(this, this, mapa);
        Profesional profesional = getProfesional();
        nombreProfesional.setText(profesional.getNombre());
        rubroProfesional.setText(profesional.getRubro());
        telefonoProfesional.setText(String.valueOf(profesional.getTelefono()));
        emailProfesional.setText(profesional.getEmail());
        descripcionProfesional.setText(profesional.getDescripcion());
        aperturaProfesional.setText(profesional.getInicioJornada().substring(0, 5));
        cierreProfesional.setText(profesional.getFinJornada().substring(0, 5));
        direccionProfesional.setText(profesional.getDireccion());
        mapHelper.startService(false);
        mapHelper.setLocation(profesional.getLatitud(),
                profesional.getLongitud());
        for (String url : profesional.getImages()) {
            addImageToLayout(url);
        }

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(ProfesionalParticular.this, VerProfesionales.class);
                if (Objects.equals(getIntent().getStringExtra("USUARIO"), "VECINO")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                }

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapHelper.startService(true);
                mapHelper.setLocation(getProfesional().getLatitud(),
                        getProfesional().getLongitud());
            } else {
                Log.e("PermissionError", "Location permissions are required to use this feature.");
            }
        }
    }

    private void addImageToLayout(String url) {

        if (contenedorFotos == null) {
            System.out.println("El contenedor de fotos es nulo");
            return;
        }

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        contenedorFotos.addView(imageView);


        // Usar Glide para cargar la imagen desde la URL
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder) // Asegúrate de tener un recurso placeholder
                .error(R.drawable.error) // Asegúrate de tener un recurso de error
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            e.logRootCauses("Glide");
                        }
                        System.out.println("Error al cargar la imagen: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Imagen cargada exitosamente
                        return false;
                    }
                })
                .into(imageView);
        //Picasso.get().load(url).into(imageView);
    }
}