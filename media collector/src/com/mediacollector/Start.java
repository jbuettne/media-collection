package com.mediacollector;

import com.mediacollector.collection.SearchListing;
import com.mediacollector.collection.audio.listings.ArtistListing;
import com.mediacollector.fetching.Fetching;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.RegisteredActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Der Start-Screen der Applikation. Sie zeigt die Hauptbuttons zum Scannen von
 * neuen Medien sowie zum Browsen und Synchronisieren der Sammlungen. 
 * @author Philipp Dermitzel
 */
public class Start extends RegisteredActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        LinearLayout addField = (LinearLayout) findViewById(R.id.addField);
        addField.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                alert.show();
            }
        });
        LinearLayout browseAudioField = (LinearLayout) findViewById(
           R.id.browseAudioField);
        browseAudioField.setOnClickListener(new OnClickListener() { 
        public void onClick(View v) {
        	startActivity(new Intent(getBaseContext(), ArtistListing.class));
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
		   		startActivity(new Intent(getBaseContext(), SyncActivity.class));
		   	}
        });
        
        // Es war einmal in Amerika	- ok
        //new Fetching(this, "7321921200267", Fetching.SEARCH_ENGINE_OFDB);    
        
        // Geist - Galeere			- ok
        //new Fetching(this, "4039053403328", Fetching.SEARCH_ENGINE_THALIA);
        // Phil Collins - Going Back- ok
        //new Fetching(this, "0075678924484", Fetching.SEARCH_ENGINE_THALIA);        
        // Half-Life 2				- ok
        //new Fetching(this, "5030930058937", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Gothic III				- fehler
        //new Fetching(this, "4020628081492", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Baldurs Gate Collection	- ok
        //new Fetching(this, "3546430126882", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Mafia					- ok
        //new Fetching(this, "5026555033862", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Commandos Dir. Cut		- ok
        //new Fetching(this, "5032921015196", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Red Faction				- ok
        //new Fetching(this, "4005209031196", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Anno 1503				- name schlecht
        //new Fetching(this, "4020628081881", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Tribes2					- name schlecht
        //new Fetching(this, "3348542128323", Fetching.SEARCH_ENGINE_TAGTOAD);
        // Star Wars Racer			- name schlecht
        //new Fetching(this, "4012160460965", Fetching.SEARCH_ENGINE_TAGTOAD);
        
        
        Button searchButton = (Button) findViewById(
        		R.id.searchButton);
        final EditText searchText = (EditText) findViewById(
        		R.id.searchText);
        searchButton.setOnClickListener(new OnClickListener() {			
        	public void onClick(View v) {
        		Intent searchIntent = new Intent(getBaseContext(), SearchListing.class);
        		searchIntent.putExtra("searchText", searchText.getText().toString());
        		startActivity(searchIntent);
        	}
        });
    }
    
}