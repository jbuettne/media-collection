package com.mediacollector.collection;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.util.Log;

import com.mediacollector.R;
import com.mediacollector.SearchResult;
import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;

public class SearchListing extends SearchResult{

	Database db = null;
	@Override
	protected void setData() {

		db = new Database(this);
		
		ArrayList<String> searchResult = db.getArtist().getArtistsName();
	    
		this.searchResult = searchResult;
	}
	@Override
	protected void onDestroy() {
		db.closeConnection();
		super.onDestroy();
	}
}
