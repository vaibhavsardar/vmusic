package com.example.vsmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.vsmusic.MainActivity.musicFilesArrayList;

public class Albumactivity extends AppCompatActivity {

    ImageView album_iv;
    RecyclerView recyclerView;
    ArrayList<MusicFiles> albumList = new ArrayList<>();
    AlbumitemAdapter albumitemAdapter;
    int j =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumactivity);
        getSupportActionBar().setTitle("Album");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        album_iv =(ImageView) findViewById(R.id.id_albumActivity_iv);
        recyclerView =(RecyclerView)findViewById(R.id.recyeview_album_item);

        Intent intent =getIntent();

        for(int i = 0; musicFilesArrayList.size() > i; i++){
            if(getIntent().getStringExtra("album").regionMatches(0,musicFilesArrayList.get(i).getAlbum(),0,6)){
                albumList.add(j,musicFilesArrayList.get(i));
                j++;

               // Log.d("com.vaibhav","### "+j+musicFilesArrayList.get(i).getAlbum());
            }
        }

        if (musicFilesArrayList.size() >1){
            albumitemAdapter = new AlbumitemAdapter(this,albumList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(albumitemAdapter);
        }

        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicFilesArrayList.get(intent.getIntExtra("position",0)).getId());

        byte[] art =MainActivity.getAlbumArt(this,uri);
        if (art != null){
            Glide.with(this).asBitmap()
                    .load(art)
                    .into(album_iv);
        } else {
            Glide.with(this).asDrawable()
                    .load(R.drawable.ic_baseline_music_note_24)
                    .into(album_iv);
        }
    }

}