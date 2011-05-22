package com.mediacollector;

import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.audio.Artist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TestDBDelete extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //try {
        Database db = new Database(this); 
            Artist fanta = new Artist(getBaseContext(), "Fanta 1");
        	showExDialog(fanta);
            fanta.deleteFromDb();
        //} catch (Throwable ex) {
        //}
    }
    private AlertDialog showExDialog(Artist artist) {
    	AlertDialog.Builder exDialog = new AlertDialog.Builder(this);
    	exDialog.setTitle(getString(R.string.BSNFD_title));
    	exDialog.setMessage(artist.getId() + " | " + artist.getName());
    	exDialog.setNegativeButton(getString(R.string.BSNFD_button_neg), 
    			new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {}
    	});
    	return exDialog.show();
    }
}