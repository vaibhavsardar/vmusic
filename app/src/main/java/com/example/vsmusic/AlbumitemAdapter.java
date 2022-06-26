package com.example.vsmusic;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.vsmusic.MainActivity.musicFilesArrayList;

public class AlbumitemAdapter extends RecyclerView.Adapter<AlbumitemAdapter.MyHolder> {

    public static ArrayList<MusicFiles> albumSongs ;
    Context mContext;
    Uri uria;

    public AlbumitemAdapter(Context mContext,ArrayList<MusicFiles> musicFiles1) {
        this.albumSongs = musicFiles1;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AlbumitemAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item,parent,false);
        return new AlbumitemAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumitemAdapter.MyHolder holder, final int position) {

        holder.textView.setText(albumSongs.get(position).getTitles());
        uria = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,albumSongs.get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,""+musicFiles.get(position).getDuration(),Toast.LENGTH_SHORT).show();
                Intent playerIntent = new Intent(mContext,PlayerActivity.class);
                playerIntent.putExtra("AlbumitemAdapter","FromAlbumitemAdapter");
                //playerIntent.putExtra("Songlist",albumSongs);
                playerIntent.putExtra("position",position);
                mContext.startActivity(playerIntent);
            }
        });

        byte[] art =MainActivity.getAlbumArt(mContext,uria);
        if (art != null){
            Glide.with(mContext).asBitmap()
                    .load(art)
                    .into(holder.album_art);
        } else {
            Glide.with(mContext).asDrawable()
                    .load(R.drawable.ic_baseline_music_note_24)
                    .into(holder.album_art);
        }
    }

    @Override
    public int getItemCount() {
        return albumSongs.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView album_art;

        public MyHolder(View itemView) {
            super(itemView);
            this.textView = (TextView)itemView.findViewById(R.id.tv_item);
            this.album_art = (ImageView) itemView.findViewById(R.id.imageItem);
        }
    }
}
