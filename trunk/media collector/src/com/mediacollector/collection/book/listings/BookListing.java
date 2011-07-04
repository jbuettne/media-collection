package com.mediacollector.collection.book.listings;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.mediacollector.EntryListingExp;
import com.mediacollector.R;
import com.mediacollector.SearchResult;
import com.mediacollector.Start;
import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.audio.AlbumData;
import com.mediacollector.collection.audio.Artist;
import com.mediacollector.collection.audio.ArtistData;
import com.mediacollector.collection.books.Book;
import com.mediacollector.collection.books.BookData;

/**
 * 
 * @author Jens Buettner
 */
public class BookListing extends EntryListingExp{

	BookData bookDB = null;
	EditText searchText = null;
	@Override
	protected void setData() {
		bookDB = new BookData(this);
		
		ArrayList<String> groups = bookDB.getAuthorName();
		
		ArrayList<TextImageEntry[]> groupsTI =
			new ArrayList<TextImageEntry[]>();
		for (String author : groups) {
			ArrayList<TextImageEntry> tempGroup = bookDB.getBooksTI(author);
//			boolean addedGroup = 
				groupsTI.add(tempGroup.toArray(new TextImageEntry[0]));
		}
			
		this.groups = groups.toArray(new String[0]);
		this.children = groupsTI.toArray(new TextImageEntry[0][0]);
	}
	@Override
	protected void onDestroy() {
		bookDB.close();
		super.onDestroy();
	}
}
