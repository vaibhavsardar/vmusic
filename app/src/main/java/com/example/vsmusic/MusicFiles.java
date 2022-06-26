package com.example.vsmusic;

public class MusicFiles {

//    public static final int FLAG_MUSIC =1;
//    public static final int FLAG_ALBUM =2;


    private String path;
    private String titles;
    private String artist;
    private  String album;
    private  String duration;
    private long id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MusicFiles(String path, String titles, String artist, String album, String duration, long id) {
        this.path = path;
        this.titles = titles;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getArtiest() {
        return artist;
    }

    public void setArtiest(String artiest) {
        this.artist = artiest;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
