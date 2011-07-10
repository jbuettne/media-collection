package com.mediacollector.collection.audio.listings;

import java.util.ArrayList;

import com.mediacollector.EntryListingExp;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;

public class ArtistListing extends EntryListingExp {
	
	ArtistData artistDB;
	AlbumData albumDB;
	
	@Override
	protected void setData() {
		/*
		 * Beispieldaten.
		 * Diese sollten später aus der Datenbank geholt werden.
		 * Groups bezeichnet hier die Artists, children die zum Artist 
		 * gehörenden Alben, images die entsprechenden Alben-Cover.
		 */
		artistDB = new ArtistData(this);
		albumDB = new AlbumData(this);
		
		ArrayList<String> groups = artistDB.getArtistsName();
		ArrayList<Artist> groupsA = artistDB.getArtistsAll();
		
		ArrayList<TextImageEntry[]> groupsTI =
			new ArrayList<TextImageEntry[]>();
		for (Artist artist : groupsA) {
			ArrayList<TextImageEntry> tempGroup = albumDB.getAlbumsTI(artist);
//			boolean addedGroup = 
				groupsTI.add(tempGroup.toArray(new TextImageEntry[0]));
		}
			
		this.groups = groups.toArray(new String[0]);
		this.children = groupsTI.toArray(new TextImageEntry[0][0]);
	}
	
	@Override
	protected void onDestroy() {
		artistDB.close();
		albumDB.close();
		super.onDestroy();
	}

	@Override
	protected int getType() {
		return TYPE_AUDIO;
	}
}
