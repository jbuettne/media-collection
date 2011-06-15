package com.mediacollector;

import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TestDBDelete extends RegisteredActivity {
	ArtistData artist;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //try {
        artist = new ArtistData(this);
        Artist art = new Artist();
        art.name = "Fanta 2";
            artist.deleteArtist(art.name);
        	showExDialog(art);
            //} catch (Throwable ex) {
        //}
    }
    private AlertDialog showExDialog(Artist artist) {
    	AlertDialog.Builder exDialog = new AlertDialog.Builder(this);
    	exDialog.setTitle(getString(R.string.BSNFD_title));
    	exDialog.setMessage(artist.getName() + " delteted.");
    	exDialog.setNegativeButton(getString(R.string.BSNFD_button_neg), 
    			new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {}
    	});
    	return exDialog.show();
    }
    @Override
    protected void onDestroy() {
        artist.close();
        super.onDestroy();
    }
}