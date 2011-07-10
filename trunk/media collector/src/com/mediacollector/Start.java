package com.mediacollector;

import com.mediacollector.collection.SearchListing;
import com.mediacollector.collection.audio.listings.ArtistListing;
import com.mediacollector.collection.books.listings.BookListing;
import com.mediacollector.collection.games.listings.GamesListing;
import com.mediacollector.collection.video.listings.FilmListing;
import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.Fetching;
//import com.mediacollector.fetching.DataFetcher;
//import com.mediacollector.fetching.Fetching;
//import com.mediacollector.fetching.ImageFetcher;
import com.mediacollector.sync.SyncActivity;
//import com.mediacollector.tools.Observer;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.RegisteredActivity;
import com.mediacollector.tools.Exceptions.MCException;
import com.mediacollector.tools.Exceptions.MCFetchingException;
//import com.mediacollector.tools.Exceptions.MCException;
//import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.Toast;

/**
 * Der Start-Screen der Applikation. Sie zeigt die Hauptbuttons zum Scannen von
 * neuen Medien sowie zum Browsen und Synchronisieren der Sammlungen. 
 * @author Philipp Dermitzel
 */
public class Start extends RegisteredActivity implements Observer {
	
	// Zum schnellen Testen der Fetcher...
	Fetching f;
 
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
        		startActivity(new Intent(getBaseContext(), 
        				ArtistListing.class));
        	}
        });
        LinearLayout browseVideoField = (LinearLayout) findViewById(
            	R.id.browseVideoField);
        browseVideoField.setOnClickListener(new OnClickListener() { 
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), 
        				FilmListing.class));
            }
        });
        LinearLayout browseBooksField = (LinearLayout) findViewById(
        		R.id.browseBooksField);
        browseBooksField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), 
        				BookListing.class));
        	}
        });
        LinearLayout browseGamesField = (LinearLayout) findViewById(
            	R.id.browseGamesField);
        browseGamesField.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), 
        				GamesListing.class));
            }
        });

        LinearLayout syncField = (LinearLayout) findViewById(
        	R.id.syncField);
        syncField.setOnClickListener(new OnClickListener() {
		   	public void onClick(View v) {
		   		startActivity(new Intent(getBaseContext(), 
		   				SyncActivity.class));
		   	}
        });
        
        Button searchButton = (Button) findViewById(
        		R.id.searchButton);
        final EditText searchText = (EditText) findViewById(
        		R.id.searchText);
        searchButton.setOnClickListener(new OnClickListener() {			
        	public void onClick(View v) {
        		Intent searchIntent = new Intent(getBaseContext(), 
        				SearchListing.class);
        		searchIntent.putExtra("searchText", 
        				searchText.getText().toString());
        		startActivity(searchIntent);
        	}
        });
        
        // Zum schnellen Testen der Fetcher...
        f = new Fetching(this, "5099749423862", Fetching.SEARCH_ENGINE_THALIA);
        f.addObserver(this);
        try {
			f.fetchData();
		} catch (MCFetchingException e) {
			new MCFetchingException(this, "kacke", MCException.WARNING, false);
		}
    }

	public void updateObserver(boolean statusOkay) {
		Toast.makeText(this, (String) f.getDataFetcher().get(DataFetcher
				.ARTIST_STRING), Toast.LENGTH_LONG).show();
	}
    
}