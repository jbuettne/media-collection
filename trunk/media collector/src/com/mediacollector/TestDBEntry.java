package com.mediacollector;

import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.Cd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TestDBEntry extends ListActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //try {
        Database db = new Database(this);
        for (Integer i = 0; i <= 3; i++) {
            HashMap<String, Object> artist = new HashMap<String, Object>();
            artist.put("name", "Fanta " + i);
            artist.put("imgPath", "test.jpg");
            artist.put("mbId", "h72626ks787");
            Artist fanta = new Artist(getBaseContext());
            fanta.setData(artist);
            fanta.insertIntoDb();
        }
        for (Integer i = 0; i <= 3; i++) {
            HashMap<String, Object> cd = new HashMap<String, Object>();
            cd.put("name", "Lauschgift " + i);
            cd.put("artist", i);
            cd.put("year", 2011);
            cd.put("imgPath", "test.jpg");
            cd.put("mbId", "h72626ks787");
            Cd fanta = new Cd(getBaseContext());
            fanta.setData(cd);
            fanta.insertIntoDb();
        }
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