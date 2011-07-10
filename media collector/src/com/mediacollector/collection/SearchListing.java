package com.mediacollector.collection;

import java.util.ArrayList;

import android.os.Bundle;

import com.mediacollector.SearchResult;
import com.mediacollector.collection.Database;

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
