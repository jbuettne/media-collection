package com.mediacollector.collection;

import java.util.ArrayList;

import android.os.Bundle;

import com.mediacollector.EntryListing;
import com.mediacollector.collection.Database;

/**
 * 
 * @author Jens Buettner
 */
public class SearchListing extends EntryListing{

	Database db = null;
	
	@Override
	protected void setData() {
		ArrayList<TextImageEntry> searchResult;
		db = new Database(this);
		Bundle extras = getIntent().getExtras();
		if (extras.getString("searchText").equals("")) {
			searchResult = new ArrayList<TextImageEntry>();
		} else {
			searchResult = db.getSearchResult(
					extras.getString("searchText"));
		}
	    
		this.entries = searchResult;
		this.extras = extras;
	}
	@Override
	protected void onDestroy() {
		db.closeConnection();
		super.onDestroy();
	}
	
	@Override
	protected int getType() {
		return -1;
	}
		
}
