package com.mediacollector;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.Fetching;
import com.mediacollector.fetching.ImageFetcher;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.RegisteredActivity;
import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Philipp Dermitzel
 */
public class ScanResult extends RegisteredActivity implements Observer {
	
	private Handler guiHandler = new Handler();
	
	private Fetching fetching;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
        LinearLayout bachToStart = (LinearLayout) findViewById(
            	R.id.back_to_start);
    	bachToStart.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		finish();
        		startActivity(new Intent(getBaseContext(), Start.class));
        	}
        });
        
        final Bundle extras = getIntent().getExtras();  
        if (extras != null) {        	
        	int searchEngine = -1;
        	switch (extras.getInt("collection")) {
        	case R.string.COLLECTION_Audio:
        	case R.string.COLLECTION_Books:        	
        		searchEngine = Fetching.SEARCH_ENGINE_THALIA; break;
        	case R.string.COLLECTION_Video:
        		searchEngine = Fetching.SEARCH_ENGINE_OFDB; break;
        	case R.string.COLLECTION_Games:
        		searchEngine = Fetching.SEARCH_ENGINE_TAGTOAD; break;
        	}
        	if (searchEngine == -1) {
        		try { 
        			this.fetching = new Fetching(this, 
        					extras.getString("BARCODE"));
        			this.fetching.addObserver(this);
        			this.fetching.fetchData();
        		} catch (MCFetchingException e) {
        			new MCFetchingException(this, e.getMessage());
        		}
        	} else {
        		try {
        			this.fetching = new Fetching(this, 
        					extras.getString("BARCODE"), searchEngine);
        			this.fetching.addObserver(this);
        			this.fetching.fetchData();
        		} catch (MCFetchingException e) {
        			new MCFetchingException(this, e.getMessage());
        		}       	
        	}
        }
        
        LinearLayout addToCollection = (LinearLayout) findViewById(
            	R.id.add_to_collection);
        addToCollection.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), Start.class));
        	}
        });
        
	}

	public void updateObserver(boolean statusOkay) {
		guiHandler.post(new Runnable() {			
			public void run() {
				ImageView cover = (ImageView) findViewById(R.id.cover);
				cover.setImageBitmap(BitmapFactory.decodeFile((String) 
						fetching.getImageFetcher().get(ImageFetcher.COVER_PATH)));
				TextView artist = (TextView) findViewById(R.id.artist);
				artist.setText((String) 
						fetching.getDataFetcher().get(DataFetcher.ARTIST_STRING));
				TextView release = (TextView) findViewById(R.id.release);
				release.setText((String) 
						fetching.getDataFetcher().get(DataFetcher.TITLE_STRING));
				TextView year = (TextView) findViewById(R.id.year);
				year.setText((String) 
						fetching.getDataFetcher().get(DataFetcher.YEAR_STRING));
			}
		});
	}

}
