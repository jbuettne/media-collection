package com.mediacollector.collection;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.mediacollector.R;
import com.mediacollector.SearchResult;
import com.mediacollector.Start;
import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;

/**
 * 
 * @author Jens Buettner
 */
public class SearchListing extends SearchResult{

	Database db = null;
	
	@Override
	protected void setData() {
		ArrayList<Data> searchResult;
		db = new Database(this);
		Bundle extras = getIntent().getExtras();
		if (extras.getString("searchText").equals("")) {
			searchResult = new ArrayList<Data>();
		} else {
			searchResult = db.getSearchResult(
					extras.getString("searchText"));
		}
	    
		this.searchResult = searchResult;
	}
	@Override
	protected void onDestroy() {
		db.closeConnection();
		super.onDestroy();
	}
}
