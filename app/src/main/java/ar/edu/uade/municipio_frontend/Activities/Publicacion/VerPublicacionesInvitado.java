package ar.edu.uade.municipio_frontend.Activities.Publicacion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Profesional.VerProfesionales;
import ar.edu.uade.municipio_frontend.Models.Publicacion;
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
    int paginaActual = 1;
    int totalPaginas = 1;
    final int ITEMS_POR_PAGINA = 10;

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

        // Configurar visibilidad inicial de los botones de navegación
        botonCambiarPantallaIzquierda.setVisibility(paginaActual > 1 ? View.VISIBLE : View.INVISIBLE);
        botonCambiarPantallaDerecha.setVisibility(paginaActual < totalPaginas ? View.VISIBLE : View.INVISIBLE);

        // Cargar datos desde la base de datos
        new CargarPublicacionesTask().execute(paginaActual);

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
                // TODO Lógica para agregar una nueva publicación
            }
        });

        botonCambiarPantallaDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginaActual < totalPaginas) {
                    paginaActual++;
                    new CargarPublicacionesTask().execute(paginaActual);
                }
            }
        });

        botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginaActual > 1) {
                    paginaActual--;
                    new CargarPublicacionesTask().execute(paginaActual);
                } else {
                    // Volver a la actividad de profesionales
                    Intent intent = new Intent(VerPublicacionesInvitado.this, VerProfesionales.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void mostrarPopupSalir() {
        // TODO agregar el popup
    }

    private class CargarPublicacionesTask extends AsyncTask<Integer, Void, List<Publicacion>> {
        @Override
        protected List<Publicacion> doInBackground(Integer... params) {
            int pagina = params[0];
            List<Publicacion> publicaciones = new ArrayList<>();
            try {
                // Conexión a la base de datos
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://<TU_HOST>:<TU_PUERTO>/<TU_BASE_DE_DATOS>", "<TU_USUARIO>", "<TU_CONTRASEÑA>");
                Statement statement = connection.createStatement();
                int offset = (pagina - 1) * ITEMS_POR_PAGINA;
                ResultSet resultSet = statement
                        .executeQuery("SELECT * FROM publicaciones LIMIT " + ITEMS_POR_PAGINA + " OFFSET " + offset);

                // Procesar resultados
                while (resultSet.next()) {
                    String titulo = resultSet.getString("titulo");
                    publicaciones.add(new Publicacion(titulo));
                }

                // Obtener el total de publicaciones para calcular el número de páginas
                ResultSet countResultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM publicaciones");
                if (countResultSet.next()) {
                    int totalPublicaciones = countResultSet.getInt("total");
                    totalPaginas = (int) Math.ceil((double) totalPublicaciones / ITEMS_POR_PAGINA);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return publicaciones;
        }

        @Override
        protected void onPostExecute(List<Publicacion> publicaciones) {
            mPublicaciones.clear();
            mPublicaciones.addAll(publicaciones);
            mAdapter.notifyDataSetChanged();
            textPaginaActual.setText(String.valueOf(paginaActual));

            // Mostrar u ocultar botones de navegación
            botonCambiarPantallaIzquierda.setVisibility(View.VISIBLE);
            botonCambiarPantallaDerecha.setVisibility(View.VISIBLE);
        }
    }
}
