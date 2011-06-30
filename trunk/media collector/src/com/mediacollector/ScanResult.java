package com.mediacollector;

import com.mediacollector.fetching.Fetching;
import com.mediacollector.tools.RegisteredActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ScanResult extends RegisteredActivity {
	
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
        	if (searchEngine == -1) 
        		new Fetching(this, extras.getString("BARCODE"));
        	else new Fetching(this, extras.getString("BARCODE"), searchEngine);       	
        }
        
        LinearLayout addToCollection = (LinearLayout) findViewById(
            	R.id.add_to_collection);
        addToCollection.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivity(new Intent(getBaseContext(), Start.class));
        	}
        });
        
	}

}
