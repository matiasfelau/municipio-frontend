package ar.edu.uade.municipio_frontend.Activities.Reclamo;

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
import android.widget.Spinner;
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

import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Filtro;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Sector;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
import ar.edu.uade.municipio_frontend.Services.SectorService;
import ar.edu.uade.municipio_frontend.Utilities.IdDescripcion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerReclamos extends AppCompatActivity {
    ImageButton boton;
    EmpleadoHelper empleadoHelper;
    InvitadoHelper invitadoHelper;
    VecinoHelper vecinoHelper;
    Spinner dropdownFiltro;
    Spinner dropdownDatoSector;
    Spinner dropdownDatoPertenencia;
    ListView listReclamos;
    ArrayList<IdDescripcion> p;
    List<Reclamo> reclamos;
    ArrayAdapter<IdDescripcion> prueba;
    Integer c;
    ImageButton botonAgregar;
    Button botonFiltrar;
    Filtro filtro;
    EditText inputId;
    Autenticacion autenticacion;
    AutenticacionFiltro autenticacionFiltro;
    String dato;
    Integer pagina;
    ImageButton botonCambiarPaginaIzquierda;
    ImageButton botonCambiarPaginaDerecha;
    TextView textPaginaActual;
    List<Sector> listaSectores;
    Integer cantidadPaginas;
    EditText util;
    List<String> sectores;
    ArrayAdapter<String> adapterSector;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_ver_reclamos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pagina = 1;

        //inicializacion

        String from = getIntent().getStringExtra("from");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.rubro_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterPertenencia=ArrayAdapter.createFromResource(this, R.array.pertenencia_options, android.R.layout.simple_spinner_item);

        adapterPertenencia.setDropDownViewResource(android.R.layout.simple_spinner_item);

        textPaginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        util = findViewById(R.id.util);

        dropdownFiltro = findViewById(R.id.dropdownTipoFiltro);

        inputId =findViewById(R.id.inputId);

        listReclamos = findViewById(R.id.listReclamos);

        dropdownDatoPertenencia =  findViewById(R.id.dropdownDatoPertenencia);

        botonAgregar = findViewById(R.id.botonAgregar);

        botonFiltrar = findViewById(R.id.botonfiltrar);

        boton = findViewById(R.id.botonLogout);

        dropdownFiltro.setAdapter(adapter);

        filtro = new Filtro();

        p = new ArrayList<>();

        reclamos = new ArrayList<>();

        prueba = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);

        autenticacion = new Autenticacion();

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        vecinoHelper = new VecinoHelper(this);

        autenticacionFiltro = new AutenticacionFiltro();

        filtro = new Filtro();

        listReclamos.setAdapter(prueba);

        setUpListViewListener();

        String tipoUsuario = getIntent().getStringExtra("USUARIO");

        if (Objects.equals(tipoUsuario, "VECINO")) {
            autenticacion.setTipo("Vecino");
        } else if (Objects.equals(tipoUsuario, "EMPLEADO")) {
            autenticacion.setTipo("Empleado");
            dropdownFiltro.setVisibility(View.INVISIBLE);
            util.setVisibility(View.INVISIBLE);
            botonFiltrar.setVisibility(View.INVISIBLE);
        }

        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacionFiltro.setAutenticacion(autenticacion);

        cantidadPaginas = 0;

        sectores = new ArrayList<>();

        dropdownDatoSector = findViewById(R.id.dropdownDatoSector);

        adapterSector = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectores);

        adapterSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownDatoSector.setAdapter(adapterSector);

        getSectores(autenticacion);

        //segunda inicializacion y alguna automatizacion

        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);

        if (autenticacion.getTipo().equals("Vecino")) {
            filtro.setTipo("");

            filtro.setDato("");

            autenticacionFiltro.setFiltro(filtro);

            getPaginas(autenticacionFiltro);

            getReclamos(1, autenticacionFiltro);
        } else if (autenticacion.getTipo().equals("Empleado")) {
            filtro.setTipo("sector");

            filtro.setDato(empleadoHelper.getEmpleadoByLegajo(getIntent().getIntExtra("legajo", -1)).getSector());

            autenticacionFiltro.setFiltro(filtro);

            getPaginas(autenticacionFiltro);

            getReclamos(1, autenticacionFiltro);
        }

        //verificacion
        if (from != null) {
            if (from.equals("VecinoIngreso") || from.equals("EmpleadoIngreso") || from.equals("PrimerIngreso")) {
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                    }
                });

            }
        }

        //Listener
        botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v){

                p.clear();
                prueba.notifyDataSetChanged();
                pagina++;
                getReclamos(pagina,autenticacionFiltro);
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
                    getReclamos(pagina, autenticacionFiltro);

                    if (pagina==1) {
                        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                    }

                    botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);
                }
            }
        });


        dropdownFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    if (!Objects.equals(autenticacion.getTipo(), "Empleado")) {
                        util.setVisibility(View.VISIBLE);
                        inputId.setVisibility(View.INVISIBLE);
                        dropdownDatoSector.setVisibility(View.INVISIBLE);
                        dropdownDatoPertenencia.setVisibility(View.INVISIBLE);
                    }
                } else if(position==1){
                    util.setVisibility(View.INVISIBLE);
                    filtro.setTipo("id");
                    inputId.setVisibility(View.VISIBLE);
                    dropdownDatoSector.setVisibility(View.INVISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.INVISIBLE);
                }else if (position==2){
                    util.setVisibility(View.INVISIBLE);
                    filtro.setTipo("sector");
                    inputId.setVisibility(View.INVISIBLE);
                    dropdownDatoSector.setVisibility(View.VISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.INVISIBLE);
                }else if(position==3){
                    util.setVisibility(View.INVISIBLE);
                    filtro.setTipo("documento");
                    inputId.setVisibility(View.INVISIBLE);
                    dropdownDatoSector.setVisibility(View.INVISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdownDatoSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtro.setDato(sectores.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdownDatoPertenencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    filtro.setTipo("documento");
                    filtro.setDato(vecinoHelper.getVecinos().get(0).getDocumento());
                    System.out.println(filtro.toString());
                } else if (position == 2) {
                    filtro.setTipo("");
                    filtro.setDato("");
                    System.out.println(filtro.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                switch (filtro.getTipo()) {
                    case "id":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        getReclamo(Integer.parseInt(inputId.getText().toString()), autenticacion);
                        cantidadPaginas=1;
                        break;
                    case "sector":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        autenticacionFiltro.setFiltro(filtro);
                        getReclamos(1, autenticacionFiltro);
                        getPaginas(autenticacionFiltro);
                        break;
                    case "":
                    case "documento":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        autenticacionFiltro.setFiltro(filtro);

                        getReclamos(1, autenticacionFiltro);
                        p.clear();
                        prueba.notifyDataSetChanged();
                        getPaginas(autenticacionFiltro);
                        break;
                }

                textPaginaActual.setText(String.valueOf(1));
            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaActividad = new Intent(VerReclamos.this, CrearReclamo.class);

                if (Objects.equals(autenticacion.getTipo(), "Vecino")) {
                    nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                } else if (Objects.equals(autenticacion.getTipo(), "Empleado")) {
                    nuevaActividad.putExtra("legajo", getIntent().getStringExtra("legajo"));

                }

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

    }

    //endpoints y funciones

    private void setUpListViewListener(){
        listReclamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idSeleccionado = prueba.getItem(position).getId();
                for (Reclamo reclamo : reclamos) {
                    if (String.valueOf(reclamo.getIdReclamo()).equals(idSeleccionado)) {
                        Intent nuevaActividad = new Intent(VerReclamos.this, ReclamoParticular.class);

                        nuevaActividad.putExtra("id", reclamo.getIdReclamo());

                        if (Objects.equals(autenticacion.getTipo(), "Vecino")) {
                            nuevaActividad.putExtra("documento", getIntent().getStringExtra("documento"));

                        } else if (Objects.equals(autenticacion.getTipo(), "Empleado")) {
                            nuevaActividad.putExtra("legajo", getIntent().getStringExtra("legajo"));

                        }

                        nuevaActividad.putExtra("token", getIntent().getStringExtra("token"));

                        nuevaActividad.putExtra("USUARIO", getIntent().getStringExtra("USUARIO"));

                        startActivity(nuevaActividad);
                    }
                }
            }
        });

    }

    private void addItem(Reclamo reclamo){
        if (reclamo != null) {
            if (reclamo.getDescripcion() != null) {
                prueba.add(new IdDescripcion(String.valueOf(reclamo.getIdReclamo()), reclamo.getDescripcion()));
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
                dialog.dismiss(); // Cerrar el popup sin hacer nada si se cancela
            }
        });

        dialog.show();
    }

    private void cerrarSesion() {
        try {
            Vecino vecino = vecinoHelper.getVecinos().get(0);
            vecinoHelper.deleteVecinos();
            Intent nuevaActividad = new Intent(VerReclamos.this, VecinoIngreso.class);
            nuevaActividad.putExtra("ingresado", false);
            nuevaActividad.putExtra("from", "VerReclamos");
            startActivity(nuevaActividad);
        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);
                empleadoHelper.deleteEmpleados();
                Intent nuevaActividad = new Intent(VerReclamos.this, EmpleadoIngreso.class);
                nuevaActividad.putExtra("ingresado", false);
                nuevaActividad.putExtra("from", "VerReclamos");
                startActivity(nuevaActividad);
            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);
                    invitadoHelper.deleteInvitados();
                    Intent nuevaActividad = new Intent(VerReclamos.this, InvitadoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerReclamos");
                    startActivity(nuevaActividad);
                } catch (Exception e4) {
                    Intent nuevaActividad = new Intent(VerReclamos.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerReclamos");
                    startActivity(nuevaActividad);
                }
            }
        }
    }

    private void getPaginas(AutenticacionFiltro filtro){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        ReclamoService reclamosService = retrofit.create(ReclamoService.class);

        Call<Integer> call = reclamosService.getPaginas(filtro);

        call.enqueue(new Callback<Integer>(){

            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.code()==200){

                    cantidadPaginas = response.body();

                    if (cantidadPaginas>1){
                        botonCambiarPaginaDerecha.setVisibility(View.VISIBLE);
                    }else{
                        botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                    }

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
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                System.out.println("FALLO GETPAGINAS");
            }
        });
    }

    private void getReclamos(int pagina, AutenticacionFiltro filtro){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamosService = retrofit.create(ReclamoService.class);
        Call<List<Reclamo>> call = reclamosService.getReclamos(pagina,filtro);

        call.enqueue(new Callback<List<Reclamo>>(){

            @Override
            public void onResponse(@NonNull Call<List<Reclamo>> call, @NonNull Response<List<Reclamo>> response) {
                if (response.code()==200){
                    assert response.body() != null;
                    for (Reclamo reclamo: response.body()) {
                        addItem(reclamo);
                    }
                    reclamos.clear();
                    reclamos.addAll(response.body());
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
            public void onFailure(@NonNull Call<List<Reclamo>> call, @NonNull Throwable t) {

            }
        });

    }

    private void getReclamo(int id, Autenticacion autenticacion){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        ReclamoService reclamoService = retrofit.create(ReclamoService.class);
        Call<Reclamo> call =reclamoService.getReclamo(id,autenticacion);

        call.enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(@NonNull Call<Reclamo> call, @NonNull Response<Reclamo> response) {
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
            public void onFailure(@NonNull Call<Reclamo> call, @NonNull Throwable t) {
                System.out.println(t);
            }
        });


    }
    private void getSectores(Autenticacion autenticacion) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        SectorService sectorService = retrofit.create(SectorService.class);

        Call<List<Sector>> call = sectorService.getSectores(autenticacion);

        call.enqueue(new Callback<List<Sector>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sector>> call, @NonNull Response<List<Sector>> response) {
                if (response.code()==200){//este ok
                    System.out.println(response.code());
                    assert response.body() != null;
                    List<Sector> ss = response.body();
                    for (Sector s: ss) {
                        String c = s.getDescripcion().substring(0,1).toUpperCase();
                        sectores.add(c+s.getDescripcion().substring(1));
                    }
                    adapterSector.notifyDataSetChanged();
                } else if(response.code()==400){//este? badrequest?
                    System.out.println(response.code());
                } else if(response.code()==401){//este? unauthorized?
                    System.out.println(response.code());
                } else if(response.code()==403){//este forbbiden
                    System.out.println(response.code());
                } else if(response.code()==404){//not found?
                    System.out.println(response.code());
                } else if(response.code()==500){//este internal error server
                    System.out.println(response.code());
                } else{
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sector>> call, @NonNull Throwable t) {

            }
        });
    }
}
