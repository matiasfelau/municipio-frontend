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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
import ar.edu.uade.municipio_frontend.Models.Reclamo;
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
    ListView listReclamos;
    ArrayList<String> p;
    ArrayAdapter<String> prueba;
    Integer c;
    ImageButton botonAgregar;

    @SuppressLint("MissingInflatedId")
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

        String from = getIntent().getStringExtra("from");

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.rubro_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        dropdownFiltro = findViewById(R.id.dropdownTipoFiltro);

        dropdownFiltro.setAdapter(adapter);

        p = new ArrayList<>();

        prueba = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, p);

        listReclamos = findViewById(R.id.listReclamos);

        listReclamos.setAdapter(prueba);

        setUpListViewListener();

        c = 0;

        botonAgregar = findViewById(R.id.botonAgregar);

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
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

    private void addItem(){
        prueba.add(c.toString());
        c++;
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

    private void getReclamos(String pagina, AutenticacionFiltro filtro){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        ReclamoService reclamosService = retrofit.create(ReclamoService.class);
        Call<List<Reclamo>> call = reclamosService.getReclamos(pagina,filtro);

        call.enqueue(new Callback<List<Reclamo>>(){

            @Override
            public void onResponse(Call<List<Reclamo>> call, Response<List<Reclamo>> response) {
                Intent nuevaActivdad;
                if (response.code()==200){//TODO COMPLETAR CUANDO ESTE LA VISTA

                }else if(response.code()==400){

                }else if(response.code()==401){

                }else if(response.code()==403){

                }else if(response.code()==500){

                }else{

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
                Intent nuevaActividad;
                if (response.code()==200){//TODO COMPLETAR CUANDO ESTE LA VISTA

                }else if(response.code()==400){

                }else if(response.code()==401){

                }else if(response.code()==403){

                }else if(response.code()==404){

                }else if(response.code()==500){

                }else{

                }
            }

            @Override
            public void onFailure(Call<Reclamo> call, Throwable t) {

            }
        });

    }
}
