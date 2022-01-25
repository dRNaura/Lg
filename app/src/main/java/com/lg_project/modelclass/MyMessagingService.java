package com.lg_project.modelclass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lg_project.R;
import com.lg_project.Splash;
import com.lg_project.activity.MainActivity;
import com.lg_project.activity.MainActivity2;

import org.json.JSONObject;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try{
            Log.e("FirebaseREmote", String.valueOf(remoteMessage));
            Map<String, String> params =remoteMessage.getData();
            JSONObject object = new JSONObject(params);

            Log.e("Firebase",object.toString());

            String id="",user="",action="";
            id=object.getString("id");
            user=object.getString("user");
            action=object.getString("action");
            Log.d("IDNOTIFICATION",remoteMessage.getData().get("id"));
            showNOtification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody()
                    ,id,user,action);
        }catch (Exception e){
            Log.e("Firebaseee","");
        }
    }

    public void showNOtification(String title , String message,String id, String user,String action){
        Intent intent=new Intent(getApplicationContext(), Splash.class);
        intent.putExtra("id",id);
        intent.putExtra("user",user);
        intent.putExtra("action",action);
        intent.putExtra("from","fromPush");
        PendingIntent pendingIntent= PendingIntent.getActivity(this,100,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"My Notifications")
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel("My Notifications", "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            mBuilder.setChannelId("My Notifications") ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(999,mBuilder.build());


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
