package com.mediacollector.collection.games.listings;

import java.util.ArrayList;

import com.mediacollector.EntryListing;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.games.BoardGameData;
import com.mediacollector.collection.video.FilmData;

public class GamesListing extends EntryListing{

	BoardGameData boardDB;
	
	@Override
	protected void setData() {
		
		ArrayList<TextImageEntry> entries;
		boardDB = new BoardGameData(this);
		
		entries = boardDB.getBoardGamesTI();
	    
		this.entries = entries;
	}
	
	@Override
	protected void onDestroy() {
		boardDB.close();
		super.onDestroy();
	}
	
}
