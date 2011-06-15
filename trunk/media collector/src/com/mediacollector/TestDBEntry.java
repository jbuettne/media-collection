package com.mediacollector;

import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.collection.audio.Cd;

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
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artist = new ArtistData(this);
        
        for (Integer i = 0; i <= 3; i++) {
        	artist.insertArtist("Fanta " + i
        			, "bild.jpg"
        			, "kjjh8227");
        }
    }
    @Override
    protected void onDestroy() {
        artist.close();
        super.onDestroy();
    }
}