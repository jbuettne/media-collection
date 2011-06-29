package com.mediacollector;

import com.mediacollector.fetching.Fetching;
import com.mediacollector.tools.RegisteredActivity;

import android.os.Bundle;

public class ScanResult extends RegisteredActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
        
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
        	if (searchEngine == -1) 
        		new Fetching(this, extras.getString("BARCODE"));
        	else new Fetching(this, extras.getString("BARCODE"), searchEngine);       	
        }
	}

}
