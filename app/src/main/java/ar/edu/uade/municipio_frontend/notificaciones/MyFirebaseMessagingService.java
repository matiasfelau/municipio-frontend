package ar.edu.uade.municipio_frontend.notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.uade.municipio_frontend.Activities.Arranque.Template;
import ar.edu.uade.municipio_frontend.Activities.Reclamo.VerReclamos;
import ar.edu.uade.municipio_frontend.Activities.Usuario.PrimerIngreso;
import ar.edu.uade.municipio_frontend.Activities.Usuario.Vecino.VecinoIngreso;
import ar.edu.uade.municipio_frontend.Database.Helpers.VecinoHelper;
import ar.edu.uade.municipio_frontend.Models.DocumentoToken;
import ar.edu.uade.municipio_frontend.R;
import ar.edu.uade.municipio_frontend.Services.CredencialVecinoService;
import ar.edu.uade.municipio_frontend.Services.NotificationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "default_channel";
    private final String TAG = "MyFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        System.out.println("ME LLEGO LLAMADA DEL FIREBASE");
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        System.out.println("Creó un nuevo token");
        sendTokenToServer(token);
    }
    private void sendTokenToServer(String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        NotificationService notificationService = retrofit.create(NotificationService.class);

        VecinoHelper vecinoHelper = new VecinoHelper(this);

        String documento = vecinoHelper.getVecinos().get(0).getDocumento();

        DocumentoToken documentoToken = new DocumentoToken(documento, token);

        Call<Void> call = notificationService.sendToken(documentoToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                System.out.println("Salió bien mandar el token del dispositivo");


            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println(t);

            }
        });
    }
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, Template.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)  // Icono de la notificación
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
