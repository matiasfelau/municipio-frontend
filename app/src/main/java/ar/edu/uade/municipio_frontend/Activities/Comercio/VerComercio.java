package ar.edu.uade.municipio_frontend.Activities.Comercio;

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
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Denuncia.VerDenuncias;
import ar.edu.uade.municipio_frontend.Activities.Profesional.VerProfesionales;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Comercio;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.ComercioService;
import ar.edu.uade.municipio_frontend.Utilities.AdapterCormercios;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerComercio extends AppCompatActivity {
    //inicializaciones
    EmpleadoHelper empleadoHelper;
    InvitadoHelper invitadoHelper;
    VecinoHelper vecinoHelper;
    ImageButton botonAgregar;
    ImageButton boton;
    Autenticacion autenticacion;
    Integer pagina;
    Integer cantidadPaginas;
    ImageButton botonCambiarPaginaIzquierda;
    ImageButton botonCambiarPaginaDerecha;
    TextView textPaginaActual;
    ListView listComercios;
    ImageButton botonCambiarPantallaDerecha;
    ImageButton botonCambiarPantallaIzquierda;
    ArrayList<IdDescripcion> p;
    List<Comercio> comercios;
    ArrayAdapter<Comercio> adapterComercio;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_comercios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //instancias
        pagina = 1;

        cantidadPaginas = 0;

        botonAgregar = findViewById(R.id.botonAgregar);

        boton = findViewById(R.id.botonLogout);

        botonCambiarPantallaDerecha = findViewById(R.id.botonCambiarPantallaDerecha);

        botonCambiarPantallaIzquierda = findViewById(R.id.botonCambiarPantallaIzquierda);

        textPaginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        listComercios = findViewById(R.id.listComercios);

        comercios = new ArrayList<>();

        autenticacion = new Autenticacion();

        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacion.setTipo("VECINO");

        adapterComercio = new AdapterCormercios(this, comercios);

        listComercios.setAdapter(adapterComercio);

        getPaginas();

        getComercios();

        //Listener

        listComercios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//TODO REVISAR

                Comercio comercio = adapterComercio.getItem(position);

                Intent nuevaActividad = new Intent(VerComercio.this, ComercioParticular.class);

                nuevaActividad.putExtra("id", comercio.getIdComercio());

                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }


        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupSalir();
            }
        });

        botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v){

                p.clear();
                adapterComercio.notifyDataSetChanged();
                pagina++;
                getComercios();
                textPaginaActual.setText(pagina.toString());

                botonCambiarPaginaIzquierda.setVisibility(View.VISIBLE);

                if (Objects.equals(cantidadPaginas, pagina)){

                    botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                }
            }
        });

        botonCambiarPaginaIzquierda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(pagina>1) {
                    p.clear();
                    adapterComercio.notifyDataSetChanged();
                    pagina--;
                    textPaginaActual.setText(pagina.toString());

                    getComercios();
                    cantidadPaginas=1;

                    if (pagina==1) {
                        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                    }

                    botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);
                }
            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerComercio.this, CrearComercio.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });

        botonCambiarPantallaDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerComercio.this, VerProfesionales.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("from", "VerDenuncias");

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                startActivity(nuevaActividad);
            }
        });

        botonCambiarPantallaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerComercio.this, VerDenuncias.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("from", "VerDenuncias");

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));
                startActivity(nuevaActividad);
            }
        });

    }
    //funciones
    private void getComercio(int i, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        ComercioService comercioService = retrofit.create(ComercioService.class);
        Call<Comercio> call =comercioService.getComercio(i,autenticacion);

        call.enqueue(new Callback<Comercio>() {
            @Override
            public void onResponse(@NonNull Call<Comercio> call, @NonNull Response<Comercio> response) {
                //TODO COMPLETAR CUANDO ESTE LA VISTA
                if (response.code()==200){//este ok
                    assert response.body() != null;
                    addItem(response.body());
                    adapterComercio.notifyDataSetChanged();
                }else if(response.code()==400){//este? badrequest?
                    System.out.println(response.code());
                }else if(response.code()==401){//este? unauthorized?
                    System.out.println(response.code());
                }else if(response.code()==403){//este forbbiden
                    System.out.println(response.code());
                }else if(response.code()==404){//not found?
                    System.out.println(response.code());
                }else if(response.code()==500){//este internal error server
                    System.out.println(response.code());
                }else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comercio> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void addItem(Comercio comercio){
        if (comercio != null) {
            if (comercio.getDescripcion() != null) {
                adapterComercio.add(comercio);
            }
        }

    }

    private void getPaginas() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        ComercioService comercioService = retrofit.create(ComercioService.class);

        Call<Integer> call = comercioService.getPaginas(autenticacion);

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

                } else if (response.code() == 400) {//este? badrequest?
                    System.out.println(response.code());
                } else if (response.code() == 401) {//este? unauthorized?
                    System.out.println(response.code());
                } else if (response.code() == 403) {//este forbbiden
                    System.out.println(response.code());
                } else if (response.code() == 500) {//este internal error server
                    System.out.println(response.code());
                } else {
                    System.out.println(response.code());
                }

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void getComercios() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ComercioService comercioService = retrofit.create(ComercioService.class);
        Call<List<Comercio>> call = comercioService.getComercios(Integer.parseInt(textPaginaActual .getText().toString()),
                autenticacion);

        call.enqueue(new Callback<List<Comercio>>(){

            @Override
            public void onResponse(@NonNull Call<List<Comercio>> call, @NonNull Response<List<Comercio>> response) {
                if (response.code()==200){
                    System.out.println("si");
                    assert response.body() != null;
                    for (Comercio comercio: response.body()) {
                        adapterComercio.add(comercio);
                    }
                    comercios.clear();
                    comercios.addAll(response.body());
                    adapterComercio.notifyDataSetChanged();
                }else if(response.code()==400){//este? badrequest?
                    System.out.println(response.code());
                }else if(response.code()==401){//este? unauthorized?
                    System.out.println(response.code());
                }else if(response.code()==403){//este forbbiden
                    System.out.println(response.code());
                }else if(response.code()==500){//este internal error server
                    System.out.println(response.code());
                }else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comercio>> call, @NonNull Throwable t) {
                System.out.println(t);
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
            Intent nuevaActividad = new Intent(VerComercio.this, VecinoIngreso.class);
            nuevaActividad.putExtra("ingresado", false);
            nuevaActividad.putExtra("from", "VerComercio");
            startActivity(nuevaActividad);
        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);
                empleadoHelper.deleteEmpleados();
                Intent nuevaActividad = new Intent(VerComercio.this, EmpleadoIngreso.class);
                nuevaActividad.putExtra("ingresado", false);
                nuevaActividad.putExtra("from", "VerDenuncias");
                startActivity(nuevaActividad);
            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);
                    invitadoHelper.deleteInvitados();
                    Intent nuevaActividad = new Intent(VerComercio.this, InvitadoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerDenuncias");
                    startActivity(nuevaActividad);
                } catch (Exception e4) {
                    Intent nuevaActividad = new Intent(VerComercio.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerDenuncias");
                    startActivity(nuevaActividad);
                }
            }
        }
    }

}
