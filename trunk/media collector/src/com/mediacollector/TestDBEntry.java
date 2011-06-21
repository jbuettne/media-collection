package com.mediacollector;

import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.tools.RegisteredActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

public class TestDBEntry extends RegisteredActivity {
	ArtistData artist;
	AlbumData album;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist = new ArtistData(this);
        album = new AlbumData(this);

//        for (Integer i = 0; i <= 9; i++) {
//        	artist.insertArtist("kjjh800" + i, "Fanta 00" + i, "bild.jpg");
//        }
//        for (Integer i = 0; i <= 9; i++) {
//        	artist.insertArtist("kjjh850" + i, "Fanta 50" + i, "bild.jpg");
//        }
//        
//        for (Integer i = 0; i <= 9; i++) {
//        	album.insertAlbum("kjjh700" + i, "Lauschgift 00" + i, "kjjh800" + i,
//        			2011, "bild.jpg");
//        }
//        for (Integer i = 0; i <= 9; i++) {
//        	album.insertAlbum("kjjh750" + i, "Lauschgift 50" + i, "kjjh850" + i,
//        			2011, "bild.jpg");
//        }
        artist.insertArtist("123456", "The Baseballs", "bild.jpg");
        artist.insertArtist("567890", "Volbeat", "bild.jpg");
        album.insertAlbum("abcdef", "Strike", "123456", 2009, "bild.jpg");
        album.insertAlbum("ghijkl", "Strings N Stripes", "123456", 2011, "bild.jpg");
        album.insertAlbum("fdgfdgas", "Beyond Hell - Above Heaven", "567890", 2010, "bild.jpg");
        album.insertAlbum("bjgjhgjk", "Rock the Rebel - Metal the Devil", "567890", 2007, "bild.jpg");
        
        
        
    }
    @Override
    protected void onDestroy() {
        artist.close();
        album.close();
        super.onDestroy();
    }
}