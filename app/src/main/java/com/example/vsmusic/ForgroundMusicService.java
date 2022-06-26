package com.example.vsmusic;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.sql.RowId;

import static com.example.vsmusic.PlayerActivity.musicFiles;

public class ForgroundMusicService extends Service {

    public static Intent onstartIntent;
    RemoteViews notificationLayout;
    NotificationTarget notificationTarget;
    NotificationCompat.Builder notification;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        onstartIntent = intent;
        //Toast.makeText(getApplicationContext(),"vvv "+intent.getAction(),Toast.LENGTH_SHORT).show();

        if (intent.getAction() =="com.vaibhav.playpause"){
            PlayerActivity.playPauseLocal();
        } else if (intent.getAction() =="com.vaibhav.playBack"){
            PlayerActivity.position = PlayerActivity.position - 1;
            PlayerActivity.playBackNextLocal(this);
            MediaPlayerFunctions.setNotificationContent(this);

        } else if (intent.getAction() =="com.vaibhav.playNext"){
            PlayerActivity.position = PlayerActivity.position + 1;
            PlayerActivity.playBackNextLocal(this);
            MediaPlayerFunctions.setNotificationContent(this);

        } else {
            startForeground(123,MediaPlayerFunctions.showNotification(this));
            MediaPlayerFunctions.ongetIntent(this);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void showNotification(){

        Intent intentBack =new Intent(this,ForgroundMusicService.class);
        intentBack.setAction("com.vaibhav.playBack");
        PendingIntent pendingIntentBack =PendingIntent.getService(this,100,intentBack,0);

        Intent intentPlaypause =new Intent(this,ForgroundMusicService.class);
        intentPlaypause.setAction("com.vaibhav.playpause");
        PendingIntent pendingIntentPlaypause =PendingIntent.getService(this,100,intentPlaypause,0);

        Intent intentNext =new Intent(this,ForgroundMusicService.class);
        intentNext.setAction("com.vaibhav.playNext");

        PendingIntent pendingIntentNext =PendingIntent.getService(this,100,intentNext,0);

        notificationLayout =new RemoteViews(getPackageName(),R.layout.layout_notification);

        notification = new NotificationCompat.Builder(this,App.CHANNEL_ID);
        notification.setContentText("running...")
                //.setContentTitle(musicFiles.get(PlayerActivity.position).getTitles())
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Back",pendingIntentBack))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Pause",pendingIntentPlaypause))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Next",pendingIntentNext))
                .setSmallIcon(R.drawable.ic_notification);

        startForeground(1234,notification.build());
        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());
        notificationLayout.setTextViewText(R.id.notification_title_tv,musicFiles.get(PlayerActivity.position).getTitles());
        byte[] art =MainActivity.getAlbumArt(this,uri);
        notificationTarget = new NotificationTarget(this, R.id.notification_iv,notificationLayout,notification.build(),1234);
        Glide.with(this).asBitmap()
                .load(art)
                .into(notificationTarget);
    }

//     void ongetIntent(){
//        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());
//
//         //notificationLayout.setImageViewUri(R.id.notification_iv,uri);
//        if(PlayerActivity.mediaPlayer != null){
//            PlayerActivity.mediaPlayer.stop();
//            PlayerActivity.mediaPlayer.release();
//            PlayerActivity.mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
//            PlayerActivity.mediaPlayer.start();
//            PlayerActivity.playpause.setImageResource(R.drawable.ic_baseline_pause);
//
//        } else {
//            PlayerActivity.mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
//            PlayerActivity.mediaPlayer.start();
//            PlayerActivity.playpause.setImageResource(R.drawable.ic_baseline_pause);
//        }
//         //PlayerActivity.seekBar.setMax(PlayerActivity.mediaPlayer.getDuration()/1000);
//         //PlayerActivity.duration_end.setText(PlayerActivity.formmtedTime(PlayerActivity.mediaPlayer.getDuration()/1000));
//    }

    public void setNotificationContent(){
        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());
        notificationLayout.setTextViewText(R.id.notification_title_tv,musicFiles.get(PlayerActivity.position).getTitles());
        byte[] art =MainActivity.getAlbumArt(this,uri);
        notificationTarget = new NotificationTarget(this, R.id.notification_iv,notificationLayout,notification.build(),1234);
        Glide.with(this).asBitmap()
                .load(art)
                .into(notificationTarget);
    }



}
