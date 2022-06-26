package com.example.vsmusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID ="vaibhav3";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotChan();

    }

    private  void createNotChan(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"forground", NotificationManager.IMPORTANCE_DEFAULT);
//
//            NotificationManagerCompat managerCompat =getSystemService(NotificationManagerCompat.class);
//            managerCompat.createNotificationChannel(channel);

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID,"forground", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }

    }
}
