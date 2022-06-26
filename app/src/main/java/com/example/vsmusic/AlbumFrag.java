package com.example.vsmusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.vsmusic.MainActivity.musicFilesArrayList;


public class AlbumFrag extends Fragment {
    RecyclerView recyclerViewAlbum;
    AlbumAdapter albumAdapter;

    public AlbumFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerViewAlbum = view.findViewById(R.id.recyeview_album);

        //Log.d("com.vaibav","arrry cvif size.."+musicFilesArrayList.size());
        if (musicFilesArrayList.size() >1) {
            albumAdapter = new AlbumAdapter(getContext());
            recyclerViewAlbum.setHasFixedSize(true);
            recyclerViewAlbum.setLayoutManager(new GridLayoutManager(getContext(),2));
            recyclerViewAlbum.setAdapter(albumAdapter);
        }
        return view;
    }
}