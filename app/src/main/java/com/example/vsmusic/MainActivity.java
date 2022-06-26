package com.example.vsmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ViewPager2 viewPager;
    TabLayout tabLayout;
    private String My_SHARED_PREFERANCES ="short_songs";

    static ArrayList<MusicFiles> musicFilesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        permission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem =menu.findItem(R.id.mysearcharch);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor =getSharedPreferences(My_SHARED_PREFERANCES,MODE_PRIVATE).edit();
        switch (item.getItemId()){
            case R.id.id_name:
                editor.putString("sorting","Byname");
                editor.apply();
                this.recreate();
                break;
            case R.id.id_date:
                editor.putString("sorting","Bydate");
                editor.apply();
                this.recreate();
                break;
            case R.id.id_size:
                editor.putString("sorting","Bysize");
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==1){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicFilesArrayList = getSongs(this);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }

        }
    }

    private void permission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            musicFilesArrayList = getSongs(this);

        }
    }

    public  ArrayList<MusicFiles> getSongs(Context context){
        ArrayList<MusicFiles> musicTemp = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        SharedPreferences preferences =getSharedPreferences(My_SHARED_PREFERANCES,MODE_PRIVATE);
        String sortBy =preferences.getString("sorting","Byname");
        String order =null;

        switch (sortBy){
            case "Byname":
                order = MediaStore.MediaColumns.DISPLAY_NAME;
                break;
            case "Bydate":
                order = MediaStore.MediaColumns.DATE_ADDED;
                break;
            case "Bysize":
                order = MediaStore.MediaColumns.SIZE;
                break;
        }





        String[] projection ={
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID
        };

        Cursor cursor =context.getContentResolver().query(uri,projection,null,null,order);

        if (cursor != null){
            Log.d("com.vaibhav","not null cursor..");

            while(cursor.moveToNext()){
                String path = cursor.getString(4);
                String album = cursor.getString(0);
                String artist = cursor.getString(1);
                String duration = cursor.getString(2);
                String title = cursor.getString(3);
                long id = cursor.getLong(5);
                //long albumId = cursor.getLong(6);

                //Log.d("com.vaibhav",album+"=="+albumId);
//                Log.d("com.vaibhav","not null artist.."+artist);
//                //Log.d("com.vaibhav","not null path.."+path);
//                Log.d("com.vaibhav","not null album.."+album);

                MusicFiles musicFiles = new MusicFiles(path,title,artist,album,duration,id);
                musicTemp.add(musicFiles);
                //Log.d("com.vaibhav","not null album.."+musicFiles.getTitles());
            }
            cursor.close();
        }
        //Log.d("com.vaibhav","not null teplist."+musicTemp.get(2).getTitles());
        return musicTemp;
    }

    public static byte[] getAlbumArt(Context context,Uri uria){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(context,uria);
        byte[] art =metadataRetriever.getEmbeddedPicture();
        metadataRetriever.release();
        return art;
    }

    private void initView(){
        viewPager =(ViewPager2)findViewById(R.id.viewpager);
        tabLayout =(TabLayout) findViewById(R.id.tablayout);

        viewPager.setAdapter(new MyViewPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if( position ==0)
                    tab.setText("Music");
                else if (position ==1)
                    tab.setText("Album");

            }
        }).attach();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String input = newText.toLowerCase();
        ArrayList<MusicFiles> files =new ArrayList<>();
        for (MusicFiles songFile : musicFilesArrayList){

            if(songFile.getTitles().toLowerCase().contains(input)){
                files.add(songFile);
            }
        }

        MusicFrag.listAdapter.searchList(files);

        return true;
    }
}

class MyViewPagerAdapter extends FragmentStateAdapter{

    //ArrayList<Fragment> fragmentArrayList;

    public MyViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        //this.fragmentArrayList =fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if( position == 0){
            fragment =new MusicFrag();
        }
        else if (position == 1){
            fragment = new AlbumFrag();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
