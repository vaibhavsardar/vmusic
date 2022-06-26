package com.example.vsmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import static com.example.vsmusic.PlayerActivity.musicFiles;

public class MediaPlayerFunctions {

     //public static Intent onstartIntent;
     static RemoteViews notificationLayout;
     static NotificationTarget notificationTarget;
     static NotificationCompat.Builder notification;

     public static Notification showNotification(Context context){

          Intent intentBack =new Intent(context,ForgroundMusicService.class);
          intentBack.setAction("com.vaibhav.playBack");
          PendingIntent pendingIntentBack =PendingIntent.getService(context,100,intentBack,0);

          Intent intentPlaypause =new Intent(context,ForgroundMusicService.class);
          intentPlaypause.setAction("com.vaibhav.playpause");
          PendingIntent pendingIntentPlaypause =PendingIntent.getService(context,100,intentPlaypause,0);

          Intent intentNext =new Intent(context,ForgroundMusicService.class);
          intentNext.setAction("com.vaibhav.playNext");

          PendingIntent pendingIntentNext =PendingIntent.getService(context,100,intentNext,0);

           notificationLayout =new RemoteViews(context.getPackageName(),R.layout.layout_notification);
           notification = new NotificationCompat.Builder(context,App.CHANNEL_ID);
           notification.setContentText("running...")
                  //.setContentTitle(musicFiles.get(PlayerActivity.position).getTitles())
                  .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                  .setCustomContentView(notificationLayout)
                  .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Back",pendingIntentBack))
                  .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Pause",pendingIntentPlaypause))
                  .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,"Next",pendingIntentNext))
                  .setSmallIcon(R.drawable.ic_notification);

          //startForeground(1234,);
          Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());
          notificationLayout.setTextViewText(R.id.notification_title_tv,musicFiles.get(PlayerActivity.position).getTitles());
          byte[] art =MainActivity.getAlbumArt(context,uri);
          notificationTarget = new NotificationTarget(context, R.id.notification_iv,notificationLayout,notification.build(),123);
          Glide.with(context).asBitmap()
                  .load(art)
                  .into(notificationTarget);
          return notification.build();
     }

     public static void ongetIntent(Context context){
          Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());

          notificationLayout.setTextViewText(R.id.notification_title_tv,musicFiles.get(PlayerActivity.position).getTitles());
          byte[] art =MainActivity.getAlbumArt(context,uri);
          notificationTarget = new NotificationTarget(context, R.id.notification_iv,notificationLayout,notification.build(),123);
          Glide.with(context).asBitmap()
                  .load(art)
                  .into(notificationTarget);

          if(PlayerActivity.mediaPlayer != null){
               PlayerActivity.mediaPlayer.stop();
               PlayerActivity.mediaPlayer.release();
               PlayerActivity.mediaPlayer= MediaPlayer.create(context.getApplicationContext(),uri);
               PlayerActivity.mediaPlayer.start();
               PlayerActivity.playpause.setImageResource(R.drawable.ic_baseline_pause);

          } else {
               PlayerActivity.mediaPlayer=MediaPlayer.create(context.getApplicationContext(),uri);
               PlayerActivity.mediaPlayer.start();
               PlayerActivity.playpause.setImageResource(R.drawable.ic_baseline_pause);
          }
          //PlayerActivity.seekBar.setMax(PlayerActivity.mediaPlayer.getDuration()/1000);
          //PlayerActivity.duration_end.setText(PlayerActivity.formmtedTime(PlayerActivity.mediaPlayer.getDuration()/1000));
     }

     public static void setNotificationContent(Context context){
          Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFiles.get(PlayerActivity.position).getId());
          notificationLayout.setTextViewText(R.id.notification_title_tv,musicFiles.get(PlayerActivity.position).getTitles());
          byte[] art =MainActivity.getAlbumArt(context,uri);
          notificationTarget = new NotificationTarget(context, R.id.notification_iv,notificationLayout,notification.build(),123);
          Glide.with(context).asBitmap()
                  .load(art)
                  .into(notificationTarget);
     }
}
