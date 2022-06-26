package com.example.vsmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.util.ArrayList;

import static com.example.vsmusic.ListAdapter.mFiles;
import static com.example.vsmusic.MainActivity.musicFilesArrayList;

public class PlayerActivity extends AppCompatActivity {

    static TextView songname ,artist,duration_end,getDuration_plyed;
    public static ImageView albumArt,playpause;
    static SeekBar seekBar;
    Intent musicIntent;
    public  static ArrayList<MusicFiles> musicFiles ;
    static MediaPlayer mediaPlayer;
    static Uri uri;
    private Handler handler = new Handler();
    static int  position;
    static int stopedTime;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //Intent intent =getIntent();
        //musicFiles = musicFilesArrayList;
        //Log.d("com.vaibhav","oncreate..."+getIntent().getStringExtra("ListAdapter"));
        //if(intent.getStringExtra("ListAdapter").equals("FromListAdapter"))
        initViews();



        try {
            String listFlag =getIntent().getStringExtra("AlbumitemAdapter");
            if(listFlag.equals("FromAlbumitemAdapter")){
                musicFiles = AlbumitemAdapter.albumSongs;
                Log.d("com.vaibhav","11111");
                listFlag ="ListAdapter";

            } else {
                Log.d("com.vaibhav","22221");
                musicFiles = mFiles;
            }

        } catch (Exception e){
            musicFiles = mFiles;
        }

        ongetIntent();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Toast.makeText(getApplicationContext(),"seekbar progress:"+i,Toast.LENGTH_SHORT).show();
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int currentSeekbarPosition =mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(currentSeekbarPosition);
                    getDuration_plyed.setText(formmtedTime(currentSeekbarPosition));

                }
                handler.postDelayed(this,1000);
            }
        });


    }

    private void ongetIntent(){
        musicIntent =getIntent();
        //musicFiles = (ArrayList<MusicFiles>) getIntent().getSerializableExtra("Songlist");

        position =musicIntent.getIntExtra("position",-1);
        songname.setText(musicFiles.get(position).getTitles());
        uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,musicFiles.get(position).getId());
        artist.setText(musicFiles.get(position).getArtiest());

        Intent intent = new Intent(getApplicationContext(),ForgroundMusicService.class);
        ContextCompat.startForegroundService(this,intent);

        byte[] art =MainActivity.getAlbumArt(this,uri);
        if (art != null){
            Glide.with(getApplicationContext()).asBitmap()
                    .load(art)
                    .into(albumArt);
        } else {
            Glide.with(getApplicationContext()).asDrawable()
                    .load(R.drawable.ic_baseline_music_note_24)
                    .into(albumArt);
        }
    }

    public static String formmtedTime(int currentTime){
        String totalOut;
        String totalOut0;

        String sec =String.valueOf(currentTime % 60);
        String min =String.valueOf(currentTime /60);
        totalOut = min +":" + sec;
        totalOut0 = min +":"+"0" + sec;
        if(sec.length() ==1){
            return totalOut0;
        }else{
            return totalOut;
        }
    }

    public void playNext(View viewbacknext){

        if(viewbacknext.getId() == R.id.id_btn_next){
            position = position + 1;
        } else
            position = position - 1;
        playBackNextLocal(this);
    }

    public void playPause(View view){
        playPauseLocal();
    }

    public static void playPauseLocal(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
            mediaPlayer.start();
            playpause.setImageResource(R.drawable.ic_baseline_pause);

        }else if (mediaPlayer.isPlaying()){
            stopedTime = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playpause.setImageResource(R.drawable.ic_baseline_play);
        }
    }

    public static void playBackNextLocal(Context context){

        uri =ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,musicFiles.get(position).getId());
        songname.setText(musicFiles.get(position).getTitles());
        artist.setText(musicFiles.get(position).getArtiest());

        byte[] art =MainActivity.getAlbumArt(context,uri);

        if (art != null){
            Glide.with(context).asBitmap()
                    .load(art)
                    .into(albumArt);
        } else {
            Glide.with(context).asDrawable()
                    .load(R.drawable.ic_baseline_music_note_24)
                    .into(albumArt);
        }



        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=MediaPlayer.create(context,uri);
            mediaPlayer.start();
            playpause.setImageResource(R.drawable.ic_baseline_pause);

        } else {
            mediaPlayer=MediaPlayer.create(context,uri);
            mediaPlayer.start();
            playpause.setImageResource(R.drawable.ic_baseline_pause);
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        duration_end.setText(formmtedTime(mediaPlayer.getDuration()/1000));

    }

    private void initViews(){
        songname =(TextView)findViewById(R.id.songname);
        artist =(TextView)findViewById(R.id.id_artist);
        duration_end =(TextView)findViewById(R.id.id_duration_end);
        getDuration_plyed =(TextView)findViewById(R.id.id_duration_start);
        seekBar =(SeekBar) findViewById(R.id.id_seekbar);
        albumArt =(ImageView) findViewById(R.id.imageView);
        playpause =(ImageView) findViewById(R.id.imageView2);
    }
}