package com.example.vsmusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import static com.example.vsmusic.MainActivity.musicFilesArrayList;

public class MusicFrag extends Fragment {
    RecyclerView recyclerView;
    static ListAdapter listAdapter;

    public MusicFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView =(RecyclerView) view.findViewById(R.id.recycleview);

        if (musicFilesArrayList.size() >1){
            //Log.d("com.vaibav","arrry cvif size.."+musicFilesArrayList.size());
            listAdapter = new ListAdapter(getContext(),musicFilesArrayList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
        }

        return view;
    }
}