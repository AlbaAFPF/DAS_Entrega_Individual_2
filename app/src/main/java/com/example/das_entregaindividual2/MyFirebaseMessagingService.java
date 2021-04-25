package com.example.das_entregaindividual2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Método para la recepción de mensajes
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Al seleccionar la notificación recibida, se abre la actividad "Tareas".
        Intent intent = new Intent(this, Examenes.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "101")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                // Definir título del mensaje
                .setContentTitle(remoteMessage.getNotification().getTitle())
                // Definir texto del mensaje
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Marcar el intent que se activará cuando el usuario toque la notificación
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onNewToken(String s) {
    }

}
