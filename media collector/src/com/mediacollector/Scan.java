package com.mediacollector;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.Fetching;
import com.mediacollector.fetching.ImageFetcher;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.RegisteredActivity;
import com.mediacollector.tools.ScanBarcode;
import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.app.Activity;
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
public class Scan extends RegisteredActivity implements Observer {
	
	private Handler guiHandler = new Handler();
	
	private Fetching fetching;
	
	public int collection;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
        final Bundle extras = getIntent().getExtras();  
        if (extras != null) 
        	this.collection = extras.getInt("collection");
    	startActivityForResult(new Intent(this, ScanBarcode.class), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, Intent data) {	
		switch (resultCode) {
			case Activity.RESULT_OK:
    			int searchEngine;
	        	switch (this.collection) {
	        	case R.string.COLLECTION_Audio:
	        	case R.string.COLLECTION_Books:        	
	        		searchEngine = Fetching.SEARCH_ENGINE_THALIA; 
	        		break;
	        	case R.string.COLLECTION_Video:
	        		searchEngine = Fetching.SEARCH_ENGINE_OFDB; 
	        		break;
	        	case R.string.COLLECTION_Games:
	        		searchEngine = Fetching.SEARCH_ENGINE_TAGTOAD; 
	        		break;
	        	default:
	        		searchEngine = Fetching.SEARCH_ENGINE_GOOGLE; 
	        		break;
	        	}
	        	try {
	        		this.fetching = new Fetching(this, data.getExtras()
	        				.getString("BARCODE"), searchEngine);
	        		this.fetching.addObserver(this);
        			this.fetching.fetchData();
	        	} catch (MCFetchingException e) {
					new MCFetchingException(this, e.getMessage());
				}
				break;
			case Activity.RESULT_CANCELED:
				finish();
				break;
			default: break;
		}
		
		LinearLayout backToStart = 
        	(LinearLayout) findViewById(R.id.back_to_start);
    	backToStart.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) { finish(); }
        });
    	
        LinearLayout addToCollection = 
        	(LinearLayout) findViewById(R.id.add_to_collection);
        addToCollection.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), Start.class));
        	}
        });
        
        super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void updateObserver(boolean statusOkay) {
		guiHandler.post(new Runnable() { public void run() {			
			((ImageView) findViewById(R.id.cover)).setImageBitmap(BitmapFactory
					.decodeFile((String) fetching.getImageFetcher()
					.get(ImageFetcher.COVER_PATH)));			
			((TextView) findViewById(R.id.artist)).setText((String) 
					fetching.getDataFetcher().get(DataFetcher.ARTIST_STRING));
			((TextView) findViewById(R.id.release)).setText((String) 
					fetching.getDataFetcher().get(DataFetcher.TITLE_STRING));
			((TextView) findViewById(R.id.year)).setText((String) 
					fetching.getDataFetcher().get(DataFetcher.YEAR_STRING));
			}
		});
	}

}
