package ar.edu.uade.municipio_frontend.Activities.Reclamo;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Models.Autenticacion;
import ar.edu.uade.municipio_frontend.Models.AutenticacionFiltro;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Filtro;
import ar.edu.uade.municipio_frontend.Models.Reclamo;
import ar.edu.uade.municipio_frontend.Models.Sectores;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Services.ReclamoService;
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
    ArrayList<String> p;
    ArrayAdapter<String> prueba;
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
    List<Sectores> listaSectores;


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



        String from = getIntent().getStringExtra("from");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.rubro_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        textPaginaActual = findViewById(R.id.textPaginaActual);

        botonCambiarPaginaDerecha = findViewById(R.id.botonCambiarPaginaDerecha);



        botonCambiarPaginaDerecha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                p.clear();
                prueba.notifyDataSetChanged();
                pagina+=2;
                getReclamos(Integer.parseInt(textPaginaActual.getText().toString()),autenticacionFiltro);
                if (p.isEmpty()) {
                    botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
                }
                p.clear();
                prueba.notifyDataSetChanged();
                pagina--;
                getReclamos(Integer.parseInt(textPaginaActual.getText().toString()),autenticacionFiltro);
                textPaginaActual.setText(pagina.toString());
                if (pagina>1) {
                    botonCambiarPaginaIzquierda.setVisibility(View.VISIBLE);
                }
            }
        });

        botonCambiarPaginaIzquierda = findViewById(R.id.botonCambiarPaginaIzquierda);

        botonCambiarPaginaIzquierda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(pagina>1) {
                    p.clear();
                    prueba.notifyDataSetChanged();
                    pagina-=1;
                    textPaginaActual.setText(pagina.toString());
                    getReclamos(Integer.parseInt(textPaginaActual.getText().toString()),autenticacionFiltro);

                    if (pagina==1) {
                        botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        dropdownFiltro = findViewById(R.id.dropdownTipoFiltro);

        inputId =findViewById(R.id.inputId);

        dropdownDatoSector = findViewById(R.id.dropdownDatoSector);

        dropdownDatoPertenencia =  findViewById(R.id.dropdownDatoPertenencia);

        dropdownFiltro.setAdapter(adapter);

        filtro = new Filtro();

        dropdownFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    filtro.setTipo("id");
                    inputId.setVisibility(View.VISIBLE);
                    dropdownDatoSector.setVisibility(View.INVISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.INVISIBLE);
                }else if (position==1){
                    filtro.setTipo("sector");
                    inputId.setVisibility(View.INVISIBLE);
                    dropdownDatoSector.setVisibility(View.VISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.INVISIBLE);
                }else if(position==2){
                    filtro.setTipo("");
                    inputId.setVisibility(View.INVISIBLE);
                    dropdownDatoSector.setVisibility(View.INVISIBLE);
                    dropdownDatoPertenencia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        p = new ArrayList<>();

        prueba = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);

        listReclamos = findViewById(R.id.listReclamos);

        listReclamos.setAdapter(prueba);

        setUpListViewListener();





        c = 0;

        botonAgregar = findViewById(R.id.botonAgregar);

        botonFiltrar = findViewById(R.id.botonfiltrar);

        autenticacion = new Autenticacion();
        autenticacion.setTipo("Vecino");//Todo arreglar
        autenticacion.setToken(getIntent().getStringExtra("token"));

        autenticacionFiltro = new AutenticacionFiltro();
        autenticacionFiltro.setAutenticacion(autenticacion);

        filtro = new Filtro();

        filtro.setTipo("");
        filtro.setDato("");

        autenticacionFiltro.setFiltro(filtro);

        dropdownDatoPertenencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    filtro.setTipo("documento");
                    filtro.setDato(vecinoHelper.getVecinos().get(0).getDocumento());
                    System.out.println(filtro.toString());
                } else if (position == 1) {
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
                switch (filtro.getTipo()) {
                    case "id":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        getReclamo(1, autenticacion);
                        break;
                    case "sector":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        dropdownDatoSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                dato = listaSectores.get(position).getDescripcion();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        filtro.setDato(dato);
                        autenticacionFiltro.setFiltro(filtro);
                        getReclamos(1, autenticacionFiltro);
                        break;
                    case "":
                    case "documento":
                        p.clear();
                        prueba.notifyDataSetChanged();
                        autenticacionFiltro.setFiltro(filtro);

                        getReclamos(1, autenticacionFiltro);
                        p.clear();
                        prueba.notifyDataSetChanged();
                        break;
                }


            }
        });

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (from != null) {
            if (from.equals("VecinoIngreso") || from.equals("EmpleadoIngreso") || from.equals("PrimerIngreso")) {
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                    }
                });

            }
        }

        if (pagina==1) {
            botonCambiarPaginaIzquierda.setVisibility(View.INVISIBLE);
        }

        getReclamos(1,autenticacionFiltro);

        if (p.isEmpty()) {
            botonCambiarPaginaDerecha.setVisibility(View.INVISIBLE);
        }

        boton = findViewById(R.id.botonLogout);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupSalir();
            }
        });

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        vecinoHelper = new VecinoHelper(this);
    }



    private void setUpListViewListener(){
        listReclamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
                p.remove(position);
                prueba.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(Reclamo reclamo){
        prueba.add(reclamo.getDescripcion()+"\n"+reclamo.getEstado());
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
        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);
                empleadoHelper.deleteEmpleados();
            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);
                    invitadoHelper.deleteInvitados();
                } catch (Exception e4) {
                    Intent nuevaActividad = new Intent(VerReclamos.this, VecinoIngreso.class);
                    nuevaActividad.putExtra("ingresado", false);
                    nuevaActividad.putExtra("from", "VerReclamos");
                    startActivity(nuevaActividad);
                }
            }
        }
    }

    private void getReclamos(int pagina, AutenticacionFiltro filtro){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamosService = retrofit.create(ReclamoService.class);
        Call<List<Reclamo>> call = reclamosService.getReclamos(pagina,filtro);

        call.enqueue(new Callback<List<Reclamo>>(){

            @Override
            public void onResponse(Call<List<Reclamo>> call, Response<List<Reclamo>> response) {
                //TODO COMPLETAR CUANDO ESTE LA VISTA
                if (response.code()==200){
                    for (Reclamo reclamo: response.body()) {
                        addItem(reclamo);
                        System.out.println(reclamo.getDocumento());
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
            public void onFailure(Call<List<Reclamo>> call, Throwable t) {

            }
        });

    }

    private void getReclamo(int id, Autenticacion autenticacion){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        ReclamoService reclamoService = retrofit.create(ReclamoService.class);
        Call<Reclamo> call =reclamoService.getReclamo(id,autenticacion);

        call.enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(Call<Reclamo> call, Response<Reclamo> response) {
                //TODO COMPLETAR CUANDO ESTE LA VISTA
                if (response.code()==200){//este ok
                    addItem(response.body());
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
            public void onFailure(Call<Reclamo> call, Throwable t) {
                System.out.println(t);
            }
        });

    }
}
