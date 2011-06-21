package com.mediacollector;

import com.mediacollector.collection.SearchListing;
import com.mediacollector.sync.Dropbox;
import com.mediacollector.tools.Identifier;
import com.mediacollector.tools.RegisteredActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Der Start-Screen der Applikation. Sie zeigt die Hauptbuttons zum Scannen von
 * neuen Medien sowie zum Browsen und Synchronisieren der Sammlungen. 
 * @author Philipp Dermitzel
 */
public class Start extends RegisteredActivity {
	
	private static String editText;	
	
    public static String getEditText() {
    	return editText;
	}

	public void setEditText(String editText) {
		this.editText = editText;
	}
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        LinearLayout addField = (LinearLayout) findViewById(R.id.addField);
        addField.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
               startActivity(new Intent(getBaseContext(), ScanBarcode.class));
            }
        });
        LinearLayout browseAudioField = (LinearLayout) findViewById(
           R.id.browseAudioField);
        browseAudioField.setOnClickListener(new OnClickListener() { 
        public void onClick(View v) {
        	startActivity(new Intent(getBaseContext(), 
        			com.mediacollector.collection.audio.listings.ArtistListing.class));
        	}
        });
        LinearLayout browseGamesField = (LinearLayout) findViewById(
        	R.id.browseGamesField);
        browseGamesField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), ScanResult.class));
        	}
        });
        LinearLayout browseBooksField = (LinearLayout) findViewById(
        		R.id.browseBooksField);
        browseBooksField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), TestDBEntry.class));
        	}
        });
        LinearLayout browseVideoField = (LinearLayout) findViewById(
        	R.id.browseVideoField);
        browseVideoField.setOnClickListener(new OnClickListener() { 
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), TestDBDelete.class));
        	}
        });
        LinearLayout syncField = (LinearLayout) findViewById(
        	R.id.syncField);
        syncField.setOnClickListener(new OnClickListener() {
		   	public void onClick(View v) {
		   		SharedPreferences prefs = 
		   			getSharedPreferences("com.mediacollector_preferences", 0);
		   		Intent intent = new Intent(getBaseContext(), Dropbox.class);
		   		intent.putExtra("email", prefs.getString("dropboxLogin", null));
		   		intent.putExtra("password",prefs.getString("dropboxPassword", null));
		   		startActivity(intent);
		   	}
        });

        Button searchButton = (Button) findViewById(
        		R.id.searchButton);
        final EditText searchText = (EditText) findViewById(
        		R.id.searchText);
        searchButton.setOnClickListener(new OnClickListener() {			
        	public void onClick(View v) {
        		setEditText(searchText.getText().toString());
        		startActivity(new Intent(getBaseContext(), SearchListing.class));
        	}
        });
        
//        Dropbox db = new Dropbox(this, null, null);
//        Toast toast = Toast.makeText(getBaseContext(), Identifier.getIdentifier(this), Toast.LENGTH_LONG);
//    	toast.show();
//        try {
//			db.sync();
//		} catch (Exception e) {
//			Log.v("HAYA", e.toString());
//			Toast toast = Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG);
//	    	toast.show();
//		}
    }
    
}