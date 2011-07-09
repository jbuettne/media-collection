package com.mediacollector.collection.games.listings;

import java.util.ArrayList;

import com.mediacollector.EntryListing;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.games.BoardGameData;
import com.mediacollector.collection.games.VideoGameData;

public class GamesListing extends EntryListing{

	BoardGameData boardDB;
	VideoGameData videoDB;
	
	@Override
	protected void setData() {
		
		ArrayList<TextImageEntry> entries;
		boardDB = new BoardGameData(this);
		videoDB = new VideoGameData(this);
		
		entries = boardDB.getBoardGamesTI();
		entries.addAll(videoDB.getVideoGamesTI());
	    
		this.entries = entries;
	}
	
	@Override
	protected void onDestroy() {
		boardDB.close();
		videoDB.close();
		super.onDestroy();
	}
	
}
