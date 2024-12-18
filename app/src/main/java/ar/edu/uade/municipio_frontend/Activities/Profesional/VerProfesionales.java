package ar.edu.uade.municipio_frontend.Activities.Profesional;

import static ar.edu.uade.municipio_frontend.Utilities.Container.ProfesionalStoraged.setProfesional;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Comercio.VerComercio;
import ar.edu.uade.municipio_frontend.Activities.Publicacion.VerPublicaciones;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Profesional;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ProfesionalService;
import ar.edu.uade.municipio_frontend.Utilities.Adapter.ProfesionalAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerProfesionales extends AppCompatActivity {
    ImageButton botonCerrarSesion;
    ListView listaProfesionales;
    ImageButton botonCambiarPaginaIzquierda;
    TextView paginaActual;
    ImageButton botonCambiarPaginaDerecha;
    ImageButton botonNuevoProfesional;
    ImageButton botonCambiarModuloIzquierda;
    ImageButton botonCambiarModuloDerecha;
    VecinoHelper helperVecino;
    List<Profesional> profesionales;
    ArrayAdapter<Profesional> adapterProfesionales;
    int cantidadPaginas;
    Autenticacion autenticacion;
    int paginaActualInt = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_profesionales);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;

        });

        botonCerrarSesion = findViewById(R.id.botonLogout);

        listaProfesionales = findViewById(R.id.listProfesionales);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        paginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        botonNuevoProfesional = findViewById(R.id.botonAgregar);

        botonCambiarModuloIzquierda = findViewById(R.id.botonCambiarPantallaIzquierda);

        botonCambiarModuloDerecha = findViewById(R.id.botonCambiarPantallaDerecha);

        helperVecino = new VecinoHelper(this);

        profesionales = new ArrayList<>();

        adapterProfesionales = new ProfesionalAdapter(this, profesionales);

        listaProfesionales.setAdapter(adapterProfesionales);

        autenticacion = new Autenticacion(
                getIntent().getStringExtra("token"),
                getIntent().getStringExtra("USUARIO"));

        getPaginas();

        getProfesionales();

        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupSalir();

            }
        });

        listaProfesionales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profesional profesional = adapterProfesionales.getItem(position);
                Intent nuevaActividad = new Intent(VerProfesionales.this, ProfesionalParticular.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));
                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));
                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                setProfesional(profesional);
                startActivity(nuevaActividad);
            }
        });

        botonCambiarPaginaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginaActualInt > 1) {
                    paginaActualInt--;
                    paginaActual.setText(String.valueOf(paginaActualInt));
                    getProfesionales();
                    botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);
                    if (paginaActualInt == 1) {
                        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginaActualInt < cantidadPaginas) {
                    paginaActualInt++;
                    paginaActual.setText(String.valueOf(paginaActualInt));
                    getProfesionales();
                    botonCambiarPaginaIzquierda.setVisibility(View.VISIBLE);
                    if (paginaActualInt == cantidadPaginas) {
                        botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        botonNuevoProfesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerProfesionales.this, CrearProfesional.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);

            }
        });

        botonCambiarModuloIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerProfesionales.this, VerComercio.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);

            }
        });

        botonCambiarModuloDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerProfesionales.this, VerPublicaciones.class);

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
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
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void cerrarSesion() {
        Vecino vecino = helperVecino.getVecinos().get(0);

        helperVecino.deleteVecinos();

        Intent nuevaActividad = new Intent(VerProfesionales.this, VecinoIngreso.class);

        nuevaActividad.putExtra("ingresado", false);

        nuevaActividad.putExtra("from", "VerProfesionales");

        startActivity(nuevaActividad);

    }

    private void getPaginas() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ProfesionalService profesionalService = retrofit.create(ProfesionalService.class);

        Call<Integer> call = profesionalService.getPaginas(autenticacion);

        call.enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.code() == 200) {

                    cantidadPaginas = response.body();

                    if (cantidadPaginas > 1) {
                        botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);

                    } else {
                        botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);

                    }

                } else if (response.code() == 400) {
                    System.out.println(response.code());

                } else if (response.code() == 401) {
                    System.out.println(response.code());

                } else if (response.code() == 403) {
                    System.out.println(response.code());

                } else if (response.code() == 500) {
                    System.out.println(response.code());

                } else {
                    System.out.println(response.code());

                }

            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

            }
        });
    }

    private void getProfesionales() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ProfesionalService profesionalService = retrofit.create(ProfesionalService.class);

        Call<List<Profesional>> call = profesionalService.getAll(
                paginaActualInt,
                autenticacion);

        call.enqueue(new Callback<List<Profesional>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profesional>> call,
                    @NonNull Response<List<Profesional>> response) {
                if (response.code() == 200) {
                    System.out.println("si");
                    for (Profesional profesional : response.body()) {
                        adapterProfesionales.add(profesional);

                    }
                    profesionales.clear();

                    profesionales.addAll(response.body());

                    adapterProfesionales.notifyDataSetChanged();

                } else if (response.code() == 400) {
                    System.out.println(response.code());

                } else if (response.code() == 401) {
                    System.out.println(response.code());

                } else if (response.code() == 403) {
                    System.out.println(response.code());

                } else if (response.code() == 500) {
                    System.out.println(response.code());

                } else {
                    System.out.println(response.code());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Profesional>> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });

    }

    private void irAVerPublicaciones() {
        Intent intent = new Intent(VerProfesionales.this, VerPublicaciones.class);
        startActivity(intent);
    }

}