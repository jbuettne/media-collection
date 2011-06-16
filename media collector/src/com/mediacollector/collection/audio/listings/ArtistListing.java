package com.mediacollector.collection.audio.listings;

import java.util.ArrayList;

import android.util.Log;

import com.mediacollector.EntryListing;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;

public class ArtistListing extends EntryListing {
	ArtistData artists;
	AlbumData albums;
	@Override
	protected void setData() {
		/*
		 * Beispieldaten.
		 * Diese sollten später aus der Datenbank geholt werden.
		 * Groups bezeichnet hier die Artists, children die zum Artist 
		 * gehörenden Alben, images die entsprechenden Alben-Cover.
		 */
		artists = new ArtistData(this);
		albums = new AlbumData(this);
		
		ArrayList<String> groups = artists.getArtistsName();
		ArrayList<Artist> groupsA = artists.getArtistsAll();
		//ArrayList<String> groups = new ArrayList<String>();
//		for (Artist artist : groupsA) {
//			ArrayList<TextImageEntry> tempGroup = albums.getAlbumsTI(artist);
//
//			Log.i("ArtistListing", String.valueOf(artist.getMbId()));
//			for (TextImageEntry name : tempGroup) {
//				 boolean addedGroup = groups.add(name.getText());
//			}
//		}
		ArrayList<TextImageEntry[]> groupsTI =
			new ArrayList<TextImageEntry[]>();
		for (Artist artist : groupsA) {
			ArrayList<TextImageEntry> tempGroup = albums.getAlbumsTI(artist);
//			boolean addedGroup = 
				groupsTI.add(tempGroup.toArray(new TextImageEntry[0]));
		}
			
		this.groups = groups.toArray(new String[0]);
		this.children = groupsTI.toArray(new TextImageEntry[0][0]); //children.toArray(new TextImageEntry[0][0]);
	}
	@Override
	protected void onDestroy() {
		artists.close();
		albums.close();
		super.onDestroy();
	}
}
