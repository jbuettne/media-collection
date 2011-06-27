package com.mediacollector;

import com.mediacollector.collection.SearchListing;
import com.mediacollector.collection.audio.listings.ArtistListing;
import com.mediacollector.sync.SyncActivity;
import com.mediacollector.tools.RegisteredActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
	
	/**
	 * Die verschiedenen Sammlungen, die das Programm verwaltet. Die letzte 
	 * steht hierbei für die (experimentelle) "Wishlist".
	 */
	
	private static String editText;
	private static AlertDialog alert;
	
    public static String getEditText() {
    	return editText;
	}

	public void setEditText(String editText) {
		Start.editText = editText;
	}
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        final String[] collections = {
        		getString(R.string.COLLECTION_Audio), 
        		getString(R.string.COLLECTION_Video), 
        		getString(R.string.COLLECTION_Books), 
        		getString(R.string.COLLECTION_Games), 
        		getString(R.string.COLLECTION_Wishlist) 
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.COLLECTION_Choose);
        builder.setItems(collections, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	Intent intent = new Intent(getApplicationContext(), 
            			ScanBarcode.class);
            	intent.putExtra("collection", collections[item]);
            	startActivity(intent);
            }
        });
        alert = builder.create();
        
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
        
        // Es war einmal in Amerika
        //new Fetching(this, "7321921200267", Fetching.SEARCH_ENGINE_OFDB);    
        // Geist - Galeere
        //new Fetching(this, "4039053403328", Fetching.SEARCH_ENGINE_THALIA);
        
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