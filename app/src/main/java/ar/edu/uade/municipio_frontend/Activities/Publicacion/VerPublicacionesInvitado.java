package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Profesional.VerProfesionales;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Adapters.ImagenPagerAdapter;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.PublicacionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerPublicacionesInvitado extends AppCompatActivity {

    ImageButton botonLogout;
    ImageButton botonCambiarPantallaDerecha;
    ImageButton botonCambiarPantallaIzquierda;
    ImageButton botonCambiarPaginaDerecha;
    ImageButton botonCambiarPaginaIzquierda;
    TextView textPaginaActual;
    VecinoHelper helperVecino;
    ListView listPublicaciones;
    ViewPager viewPagerImagenes;
    ImageButton botonFlechaAtras;

    ArrayAdapter<Publicacion> mAdapter;
    List<Publicacion> mPublicaciones;
    int paginaActual = 1;
    int totalPaginas = 1;
    final int ITEMS_POR_PAGINA = 10;
    private Autenticacion autenticacion;

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

        // Inicialización de vistas
        botonLogout = findViewById(R.id.botonLogout);
        botonCambiarPantallaIzquierda = findViewById(R.id.botonCambiarPantallaIzquierda);
        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);
        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);
        textPaginaActual = findViewById(R.id.textPaginaActual);
        listPublicaciones = findViewById(R.id.listPublicaciones);
        botonFlechaAtras = findViewById(R.id.botonFlechaAtras);

        autenticacion = new Autenticacion();
        autenticacion.setToken(getIntent().getStringExtra("token"));
        autenticacion.setTipo("Invitado");

        // Verificación de null y asignación de OnClickListener
        if (botonCambiarPaginaDerecha != null) {
            botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al hacer clic
                }
            });
        } else {
            Log.e("VerPublicacionesInvitado", "botonCambiarPaginaDerecha es null. Verifica el ID en el layout.");
        }

        if (botonLogout != null) {
            botonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPopupSalir();
                }
            });
        } else {
            Log.e("VerPublicacionesInvitado", "botonLogout es null. Verifica el ID en el layout.");
        }

        if (botonCambiarPaginaIzquierda != null) {
            botonCambiarPaginaIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al hacer clic
                }
            });
        } else {
            Log.e("VerPublicacionesInvitado", "botonCambiarPaginaIzquierda es null. Verifica el ID en el layout.");
        }

        if (botonCambiarPantallaIzquierda != null) {
            botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción al hacer clic
                }
            });
        } else {
            Log.e("VerPublicacionesInvitado", "botonCambiarPantallaIzquierda es null. Verifica el ID en el layout.");
        }

        // Configuración de la lista de publicaciones
        mPublicaciones = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mPublicaciones);
        listPublicaciones.setAdapter(mAdapter);

        // Configurar visibilidad inicial de los botones de navegación
        actualizarBotones();

        // Cargar datos desde la base de datos
        getPublicaciones(paginaActual);

        // Listeners
        if (botonLogout != null) {
            botonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPopupSalir();
                }
            });
        }

        if (botonCambiarPantallaDerecha != null) {
            botonCambiarPantallaDerecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paginaActual < totalPaginas) {
                        paginaActual++;
                        getPublicaciones(paginaActual);
                    }
                }
            });
        }

        if (botonCambiarPantallaIzquierda != null) {
            botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paginaActual > 1) {
                        paginaActual--;
                        getPublicaciones(paginaActual);
                    } else {
                        // Volver a la actividad de profesionales
                        Intent intent = new Intent(VerPublicacionesInvitado.this, VerProfesionales.class);
                        startActivity(intent);
                    }
                }
            });
        }

        // Listener para el botón de flecha atrás
        botonFlechaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerPublicacionesInvitado.this, InvitadoIngreso.class);
                startActivity(intent);
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

        Intent nuevaActividad = new Intent(VerPublicacionesInvitado.this, VecinoIngreso.class);

        nuevaActividad.putExtra("ingresado", false);

        nuevaActividad.putExtra("from", "VerPublicacionesInvitado");

        startActivity(nuevaActividad);
    }

    private void getPublicaciones(int pagina) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PublicacionService service = retrofit.create(PublicacionService.class);
        Call<List<Publicacion>> call = service.getPublicaciones(pagina, autenticacion);

        call.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful()) {
                    mPublicaciones.clear();
                    mPublicaciones.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    totalPaginas = (int) Math.ceil((double) response.body().size() / ITEMS_POR_PAGINA);
                    actualizarBotones();
                } else {
                    // Manejar error en la respuesta
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                // Manejar fallo en la llamada
            }
        });
    }

    private void actualizarBotones() {
        botonCambiarPaginaIzquierda.setVisibility(paginaActual > 1 ? View.VISIBLE : View.GONE);
        botonCambiarPaginaDerecha.setVisibility(paginaActual < totalPaginas ? View.VISIBLE : View.GONE);
    }
}
