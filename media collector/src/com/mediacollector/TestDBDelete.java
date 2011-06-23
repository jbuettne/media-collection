package com.mediacollector;

import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.tools.RegisteredActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class TestDBDelete extends RegisteredActivity {
	ArtistData artist;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //try {
        artist = new ArtistData(this);
        Artist art = new Artist();
        art.name = "Fanta 007";
            artist.deleteArtist(art.name, "name");
        	showExDialog(art);
            //} catch (Throwable ex) {
        //}
    }
    private AlertDialog showExDialog(Artist artist) {
    	AlertDialog.Builder exDialog = new AlertDialog.Builder(this);
    	exDialog.setTitle("Artist deleted!");
    	exDialog.setMessage(artist.name + " | " + artist.mbId + " deleted.");
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