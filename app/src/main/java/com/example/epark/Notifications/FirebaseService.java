package com.example.epark.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.epark.R;
import com.example.epark.UserMainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {

    //to receive message

    private final String CHANNEL_ID = "Ayush";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.e("TAG", "b17");
        super.onMessageReceived(message);
        Intent intent = new Intent(this, UserMainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        Log.e("TAG", "b18");
        //if device is greater then version 2 then notification channel we have to create
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(manager);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent intent1 = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
        Log.e("TAG", "b19");
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(message.getData().get("title"))
                    .setContentText(message.getData().get("message"))
                    .setSmallIcon(R.drawable.carbooking)
                    .setAutoCancel(true)
                    .setContentIntent(intent1)
                    .build();
            Log.e("TAG", "b20");
        }
        Log.e("TAG", "b21");
        manager.notify(notificationId, notification);
        Log.e("TAG", "b22");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channelName", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My description");
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
        Log.e("TAG", "b23");
            manager.createNotificationChannel(channel);


    }
}
