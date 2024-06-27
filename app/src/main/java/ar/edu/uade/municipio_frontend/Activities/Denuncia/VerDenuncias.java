package ar.edu.uade.municipio_frontend.Activities.Denuncia;

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
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.uade.municipio_frontend.Activities.Reclamo.CrearReclamo;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.ReclamoParticular;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.Denuncia;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.DenunciaService;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerDenuncias extends AppCompatActivity {
    //inicializaciones
    EmpleadoHelper empleadoHelper;
    Button botonAgregar;
    InvitadoHelper invitadoHelper;
    VecinoHelper vecinoHelper;
    EditText inputId;
    Button botonFiltrar;
    ImageButton botonCambiarPaginaIzquierda;
    ImageButton botonCambiarPaginaDerecha;
    TextView textPaginaActual;
    ListView listDenuncias;
    Button boton;
    Integer pagina;
    Autenticacion autenticacion;
    ArrayList<IdDescripcion> p;
    List<Denuncia> denuncias;
    ArrayAdapter<IdDescripcion> prueba;
    Integer cantidadPaginas;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_denuncias);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //intancias

        inputId =findViewById(R.id.inputId);

        botonAgregar = findViewById(R.id.botonAgregar);

        botonFiltrar = findViewById(R.id.botonfiltrar);

        pagina = 1;

        textPaginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        listDenuncias = findViewById(R.id.listDenuncias);

        boton = findViewById(R.id.botonLogout);

        autenticacion = new Autenticacion();

        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacion.setTipo("Vecino");

        p = new ArrayList<>();

        prueba = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);

        listDenuncias.setAdapter(prueba);

        setUpListViewListener();

        denuncias = new ArrayList<>();

        getPaginas(autenticacion);

        getDenuncias(1, autenticacion);

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
            public void onClick(View v){

                p.clear();
                prueba.notifyDataSetChanged();
                pagina++;
                getDenuncias(pagina,autenticacion);
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
                    prueba.notifyDataSetChanged();
                    pagina-=1;
                    textPaginaActual.setText(pagina.toString());
                    getDenuncia(Integer.parseInt(inputId.getText().toString()), autenticacion);
                    cantidadPaginas=1;

                    if (pagina==1) {
                        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                    }

                    botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);
                }
            }
        });

        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                p.clear();
                prueba.notifyDataSetChanged();
                getDenuncias(Integer.parseInt(inputId.getText().toString()), autenticacion);
                cantidadPaginas=1;
            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerDenuncias.this, CrearDenuncia.class);
                nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                startActivity(nuevaActividad);
            }
        });
    }

    private void getDenuncia(int i, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        DenunciaService denunciaService = retrofit.create(DenunciaService.class);
        Call<Denuncia> call =denunciaService.getDenuncia(i,autenticacion);

        call.enqueue(new Callback<Denuncia>() {
            @Override
            public void onResponse(@NonNull Call<Denuncia> call, @NonNull Response<Denuncia> response) {
                //TODO COMPLETAR CUANDO ESTE LA VISTA
                if (response.code()==200){//este ok
                    assert response.body() != null;
                    addItem(response.body());
                    prueba.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<Denuncia> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void addItem(Denuncia denuncia){
        if (denuncia != null) {
            if (denuncia.getDescripcion() != null) {
                prueba.add(new IdDescripcion(String.valueOf(denuncia.getIdDenuncia()), denuncia.getDescripcion()));
            }
        }

    }

    private void getDenuncias(int i, Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        DenunciaService denunciaService = retrofit.create(DenunciaService.class);
        Call<List<Denuncia>> call = denunciaService.getDenuncias(pagina,autenticacion);

        call.enqueue(new Callback<List<Denuncia>>(){

            @Override
            public void onResponse(@NonNull Call<List<Denuncia>> call, @NonNull Response<List<Denuncia>> response) {
                if (response.code()==200){
                    assert response.body() != null;
                    for (Denuncia denuncia: response.body()) {
                        addItem(denuncia);
                    }
                    denuncias.clear();
                    denuncias.addAll(response.body());
                    prueba.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<List<Denuncia>> call, @NonNull Throwable t) {

            }
        });
    }

    private void getPaginas(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        DenunciaService denunciasService = retrofit.create(DenunciaService.class);

        Call<Integer> call = denunciasService.getPaginas(autenticacion);

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

    private void setUpListViewListener(){
        listDenuncias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idSeleccionado = Objects.requireNonNull(prueba.getItem(position)).getId();
                for (Denuncia denuncia : denuncias) {
                    if (String.valueOf(denuncia.getIdDenuncia()).equals(idSeleccionado)) {
                        Intent nuevaActividad = new Intent(VerDenuncias.this, DenunciaParticular.class);

                        nuevaActividad.putExtra("id", denuncia.getIdDenuncia());

                        nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                        nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                        nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                        startActivity(nuevaActividad);
                    }
                }
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
            Intent nuevaActividad = new Intent(VerDenuncias.this, VecinoIngreso.class);
            nuevaActividad.putExtra("ingresado", false);
            nuevaActividad.putExtra("from", "VerDenuncias");
            startActivity(nuevaActividad);
        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);
                empleadoHelper.deleteEmpleados();
                Intent nuevaActividad = new Intent(VerDenuncias.this, EmpleadoIngreso.class);
                nuevaActividad.putExtra("ingresado", false);
                nuevaActividad.putExtra("from", "VerDenuncias");
                startActivity(nuevaActividad);
            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);
                    invitadoHelper.deleteInvitados();
                    Intent nuevaActividad = new Intent(VerDenuncias.this, InvitadoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerDenuncias");
                    startActivity(nuevaActividad);
                } catch (Exception e4) {
                    Intent nuevaActividad = new Intent(VerDenuncias.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerDenuncias");
                    startActivity(nuevaActividad);
                }
            }
        }
    }
}
