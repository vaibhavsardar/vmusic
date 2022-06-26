package com.example.vsmusic;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyHolder> {

    static ArrayList<MusicFiles> mFiles ;
    Context mContext;
    Uri uria;
    //int mflag;

    public ListAdapter(Context mContext,ArrayList<MusicFiles> musicFiles1) {
        this.mFiles = musicFiles1;
        this.mContext = mContext;
        //this.mflag = flag;

//        if (flag == 1){
//            Log.d("com.vaibhav","music");
//        } else {
//            Log.d("com.vaibhav","album////");
//        }
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.textView.setText(mFiles.get(position).getTitles());

        uria = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mFiles.get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playerIntent = new Intent(mContext,PlayerActivity.class);
                playerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                playerIntent.putExtra("position",position);
                //playerIntent.putExtra("Songlist",musicFilesArrayList);
                playerIntent.putExtra("ListAdapter","FromListAdapter");
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
//        if (mflag ==1){
//            itemSize = musicFiles.size();
//        } else if (mflag == 2){
//            itemSize = musicFiles.size();
//        }
        return mFiles.size();
    }

    void  searchList(ArrayList<MusicFiles> musicFiles){
        mFiles =new ArrayList<>();
        mFiles.addAll(musicFiles);
        notifyDataSetChanged();
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
