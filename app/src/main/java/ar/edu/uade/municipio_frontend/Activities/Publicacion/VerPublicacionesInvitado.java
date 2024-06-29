    package ar.edu.uade.municipio_frontend.Activities.Publicacion;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageButton;

    import androidx.activity.EdgeToEdge;
    import androidx.activity.OnBackPressedCallback;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import java.util.ArrayList;

    import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
    import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
    import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
    import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
    import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
    import ar.edu.uade.municipio_frontend.Models.Empleado;
    import ar.edu.uade.municipio_frontend.Models.Vecino;
    import ar.edu.uade.municipio_frontend.R;

    public class VerPublicacionesInvitado extends AppCompatActivity {

        ImageButton botonAgregar;
        ImageButton botonLogout;
        ImageButton botonCambiarPantallaDerecha;
        ImageButton botonCambiarPantallaIzquierda;
        TextView textPaginaActual;
        ListView listPublicaciones;
        ArrayAdapter<Publicacion> mAdapter;
        List<Publicacion> mPublicaciones;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_publicaciones_invitado);

            // Inicialización de vistas
            botonAgregar = findViewById(R.id.botonAgregar);
            botonLogout = findViewById(R.id.botonLogout);
            botonCambiarPantallaDerecha = findViewById(R.id.botonCambiarPantallaDerecha);
            botonCambiarPantallaIzquierda = findViewById(R.id.botonCambiarPantallaIzquierda);
            textPaginaActual = findViewById(R.id.textPaginaActual);
            listPublicaciones = findViewById(R.id.listPublicaciones);

            // Configuración de la lista de publicaciones
            mPublicaciones = new ArrayList<>();
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mPublicaciones);
            listPublicaciones.setAdapter(mAdapter);

            // Simulación de datos (puedes reemplazarlo con tu lógica de obtención de datos)
            cargarDatosSimulados();

            // Listeners
            botonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarPopupSalir();
                }
            });

            botonAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Lógica para agregar una nueva publicación
                }
            });

            botonCambiarPantallaDerecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Lógica para cambiar a la pantalla siguiente
                    cambiarPantallaSiguiente();
                }
            });

            botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Lógica para cambiar a la pantalla anterior
                    cambiarPantallaAnterior();
                }
            });
        }

        private void cargarDatosSimulados() {
            // Simulación de datos para la lista de publicaciones
            mPublicaciones.add(new Publicacion("Publicación 1"));
            mPublicaciones.add(new Publicacion("Publicación 2"));
            mPublicaciones.add(new Publicacion("Publicación 3"));

            // Notificar al adaptador que los datos han cambiado
            mAdapter.notifyDataSetChanged();
        }

        private void mostrarPopupSalir() {
            //TODO agregar el popup
        }

        private void cambiarPantallaSiguiente() {
            //TODO
        }

        private void cambiarPantallaAnterior() {
            //TODO
        }
    }
