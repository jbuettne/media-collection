package com.mediacollector.collection.video.listings;

import java.util.ArrayList;

import com.mediacollector.EntryListing;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.video.FilmData;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class FilmListing extends EntryListing{

	FilmData filmDB;
	
	@Override
	protected void setData() {
		
		ArrayList<TextImageEntry> entries;
		filmDB = new FilmData(this);
		
		entries = filmDB.getFilmsTI();
	    
		this.entries = entries;
	}
	
	@Override
	protected void onDestroy() {
		filmDB.close();
		super.onDestroy();
	}

	@Override
	protected int getType() {
		return TYPE_FILM;
	}
}
