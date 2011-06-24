package com.mediacollector.collection;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
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
	EditText searchText = null;
	@Override
	protected void setData() {
		ArrayList<Data> searchResult;
		db = new Database(this);
		if ("".equals(Start.getEditText())) {
			searchResult = new ArrayList<Data>();
		} else {
			searchResult = db.getSearchResult(
				Start.getEditText());
		}
	    
		this.searchResult = searchResult;
	}
	@Override
	protected void onDestroy() {
		db.closeConnection();
		super.onDestroy();
	}
}
