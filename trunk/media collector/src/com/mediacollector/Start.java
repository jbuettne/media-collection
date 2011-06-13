package com.mediacollector;

import com.mediacollector.collection.Database;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Der Start-Screen der Applikation. Sie zeigt die Hauptbuttons zum Scannen von
 * neuen Medien sowie zum Browsen und Synchronisieren der Sammlungen. 
 * @author Philipp Dermitzel
 */
public class Start extends RegisteredActivity {
	
	private Database dbHandle;
	
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
    			startActivity(new Intent(getBaseContext(), com.mediacollector
    					.collection.audio.listings.ArtistListing.class));
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
        dbHandle = new Database(this);
    }
    
    @Override
    protected void onDestroy() {
        dbHandle.closeConnection();
        super.onDestroy();
    }
    
}