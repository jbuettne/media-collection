package com.mediacollector;

import com.mediacollector.tools.ActivityRegistry;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * 
 * @author Philipp Dermitzel
 */
public abstract class SearchResult extends ListActivity {
	
	protected	  String[] 				groups 		= null;
	protected abstract void setData();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActivityRegistry.registerActivity(this);
        setContentView(R.layout.search_result);
    }


}
