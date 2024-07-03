package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Profesional.VerProfesionales;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.PublicacionService;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerPublicaciones extends AppCompatActivity {
    private VecinoHelper vecinoHelper;
    private ImageButton botonAgregar;
    private EditText inputId;
    private ImageButton botonCambiarPaginaIzquierda;
    private ImageButton botonCambiarPaginaDerecha;
    private TextView textPaginaActual;
    private ListView listPublicaciones;
    private ImageButton boton;
    private Integer pagina;
    private Autenticacion autenticacion;
    private ArrayList<IdDescripcion> p;
    private List<Publicacion> publicaciones;
    private ArrayAdapter<IdDescripcion> prueba;
    private Integer cantidadPaginas;
    private ImageButton botonCambiarPantallaIzquierda;
    private int paginaActual;
    private int totalPaginas;

    @SuppressLint({ "MissingInflatedId", "WrongViewCast" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_publicaciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vecinoHelper = new VecinoHelper(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputId = findViewById(R.id.inputId);
        botonAgregar = findViewById(R.id.botonAgregar);
        botonCambiarPantallaIzquierda = findViewById(R.id.botonCambiarPantallaIzquierda);
        pagina = 1;
        textPaginaActual = findViewById(R.id.textPaginaActual);
        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);
        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);
        listPublicaciones = findViewById(R.id.listPublicaciones);
        boton = findViewById(R.id.botonLogout);

        autenticacion = new Autenticacion();
        autenticacion.setToken(getIntent().getStringExtra("token"));
        autenticacion.setTipo("Vecino");

        p = new ArrayList<>();
        prueba = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);
        listPublicaciones.setAdapter(prueba);

        publicaciones = new ArrayList<>();
        getPaginas(autenticacion);
        getPublicaciones(1, autenticacion);

        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupSalir();
            }
        });

        botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (cantidadPaginas != null && pagina < cantidadPaginas) {
                    pagina++;
                    getPublicaciones(pagina, autenticacion);
                    textPaginaActual.setText(pagina.toString());
                }
            }
        });

        botonCambiarPaginaIzquierda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (pagina > 1) {
                    pagina--;
                    getPublicaciones(pagina, autenticacion);
                    textPaginaActual.setText(pagina.toString());
                }
            }
        });

        botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerPublicaciones.this, VerProfesionales.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));
                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));
                nuevaActividad.putExtra("from", "VerPublicaciones");
                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                startActivity(nuevaActividad);
            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerPublicaciones.this, CrearPublicacion.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));
                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));
                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                startActivity(nuevaActividad);
            }
        });
    }

    private void getPaginas(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PublicacionService publicacionService = retrofit.create(PublicacionService.class);

        Call<Integer> call = publicacionService.getPaginas(autenticacion);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.code() == 200 && response.body() != null) {
                    cantidadPaginas = response.body();
                    actualizarBotones();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void getPublicaciones(int pagina, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PublicacionService publicacionService = retrofit.create(PublicacionService.class);

        Call<List<Publicacion>> call = publicacionService.getPublicaciones(pagina, autenticacion);

        call.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(@NonNull Call<List<Publicacion>> call,
                    @NonNull Response<List<Publicacion>> response) {
                if (response.code() == 200) {
                    publicaciones.clear();
                    publicaciones.addAll(response.body());
                    prueba.notifyDataSetChanged();
                    actualizarBotones();
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Publicacion>> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void addItem(Publicacion publicacion) {
        if (publicacion != null) {
            if (publicacion.getDescripcion() != null) {
                prueba.add(new IdDescripcion(String.valueOf(publicacion.getIdPublicacion()),
                        publicacion.getDescripcion()));
            }
        }
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
        Vecino vecino = vecinoHelper.getVecinos().get(0);

        vecinoHelper.deleteVecinos();

        Intent nuevaActividad = new Intent(VerPublicaciones.this, VecinoIngreso.class);

        nuevaActividad.putExtra("ingresado", false);

        nuevaActividad.putExtra("from", "VerPublicaciones");

        startActivity(nuevaActividad);

    }

    private void actualizarBotones() {
        botonCambiarPaginaIzquierda.setVisibility(pagina > 1 ? View.VISIBLE : View.INVISIBLE);
        botonCambiarPaginaDerecha.setVisibility(pagina < cantidadPaginas ? View.VISIBLE : View.INVISIBLE);
    }
}
