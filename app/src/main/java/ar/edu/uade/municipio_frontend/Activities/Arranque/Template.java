package ar.edu.uade.municipio_frontend.Activities.Arranque;


import static com.google.android.gms.tasks.Tasks.await;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.edu.uade.municipio_frontend.Activities.Usuario.Empleado.EmpleadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Invitado.InvitadoIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.EmpleadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.InvitadoHelper;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.DocumentoToken;
import ar.edu.uade.municipio_frontend.Models.Empleado;
import ar.edu.uade.municipio_frontend.Models.Vecino;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.NotificationService;
import ar.edu.uade.municipio_frontend.notificaciones.MyFirebaseMessagingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Template extends AppCompatActivity {
    private VecinoHelper vecinoHelper;
    private EmpleadoHelper empleadoHelper;
    private InvitadoHelper invitadoHelper;
    private final String TAG = "MyFirebaseMessagingService";

    private MyFirebaseMessagingService firebaseMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO al template se le puede cambiar la apariencia y hacer que se borre con logout
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_template);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*
        FirebaseApp.initializeApp(this);

        requestNotificationPermission();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and send token to your server
                        Log.d(TAG, "FCM Token: " + token);
                        sendTokenToServer(token);
                    }
                });

         */


        vecinoHelper = new VecinoHelper(this);

        empleadoHelper = new EmpleadoHelper(this);

        invitadoHelper = new InvitadoHelper(this);

        Intent nuevaActividad;

        try {
            Vecino vecino = vecinoHelper.getVecinos().get(0);

            nuevaActividad = new Intent(Template.this, VecinoIngreso.class);

            nuevaActividad.putExtra("ingresado", true);

            nuevaActividad.putExtra("documento", vecino.getDocumento());

            startActivity(nuevaActividad);

        } catch (Exception e2) {
            try {
                Empleado empleado = empleadoHelper.getEmpleados().get(0);

                nuevaActividad = new Intent(Template.this, EmpleadoIngreso.class);

                nuevaActividad.putExtra("ingresado", true);

                nuevaActividad.putExtra("legajo", empleado.getLegajo());

                startActivity(nuevaActividad);

            } catch (Exception e3) {
                try {
                    invitadoHelper.getInvitados().get(0);

                    nuevaActividad = new Intent(Template.this, InvitadoIngreso.class);

                    nuevaActividad.putExtra("ingresado", true);

                    startActivity(nuevaActividad);

                } catch (Exception e4) {
                    nuevaActividad = new Intent(Template.this, VecinoIngreso.class);

                    nuevaActividad.putExtra("ingresado", false);

                    startActivity(nuevaActividad);

                }
            }
        }

    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean hasPermission = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        0
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted");
            } else {
                Log.d(TAG, "Notification permission denied");
            }
        }
    }
    public void sendTokenToServer(String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        NotificationService notificationService = retrofit.create(NotificationService.class);

        VecinoHelper vecinoHelper = new VecinoHelper(this);

        String documento = vecinoHelper.getVecinos().get(0).getDocumento();

        DocumentoToken documentoToken = new DocumentoToken(documento, token);

        Call<Void> call = notificationService.sendToken(documentoToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<Void> call, @androidx.annotation.NonNull Response<Void> response) {
                System.out.println("Salió bien mandar el token del dispositivo");


            }
            @Override
            public void onFailure(@androidx.annotation.NonNull Call<Void> call, @androidx.annotation.NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }
}
