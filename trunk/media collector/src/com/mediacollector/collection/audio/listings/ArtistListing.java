package com.mediacollector.collection.audio.listings;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.mediacollector.EntryListing;
import com.mediacollector.R;
import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.TextImageEntry;
import com.mediacollector.collection.DatabaseHelper.T_Cd;
import com.mediacollector.collection.audio.Cd;

public class ArtistListing extends EntryListing {

	@Override
	protected void setData() {
		/*
		 * Beispieldaten.
		 * Diese sollten später aus der Datenbank geholt werden.
		 * Groups bezeichnet hier die Artists, children die zum Artist 
		 * gehörenden Alben, images die entsprechenden Alben-Cover.
		 */
		final DatabaseHelper dbHelper = new DatabaseHelper(this);
		Cursor dbCursor = null;
		ArrayList<String> groups = new ArrayList<String>();
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name FROM Artist", null);
			if (dbCursor == null || dbCursor.moveToFirst() == false) {}
	    	groups.add(dbCursor.getString(0));
		    while (dbCursor.moveToNext() == true) {
		    	groups.add(dbCursor.getString(0));
			}
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name, imgPath FROM Cd", null);
		} catch (Throwable ex) {
			Log.e("Artist-Liste", "Artist kaputt", ex);
			ex.printStackTrace();
		} finally {

			dbCursor.close();
		}
		
		TextImageEntry[][] children = {
				{ new TextImageEntry("Mit Raben und Wölfen", 
						getResources().getDrawable(R.drawable.color_red))
				},
				{ new TextImageEntry("Stiff Upper Lip", 
						getResources().getDrawable(R.drawable.color_yellow))
				},
				{ new TextImageEntry("19", 
						getResources().getDrawable(R.drawable.color_green)), 
				  new TextImageEntry("21",
						getResources().getDrawable(R.drawable.color_yellow))
				},
				{},{},{}

		};
//		String[] groups = {
//				"Aaskereia", "AC/DC", "Adele", "Aggaloch", 
//				"Agrypnie", "Air", "Al Green", "Alice Cooper", "Alida", 
//				"Amity in Fame", "Amon Amarth", "Amos Lee", "Amy Macdonald", 
//				"Amy Winehouse", "Anthrax", "Apocalyptica", "Audio"
////		};
//		TextImageEntry[][] children = {
//				{ new TextImageEntry("Mit Raben und Wölfen", 
//						getResources().getDrawable(R.drawable.color_red))
//				},
//				{ new TextImageEntry("Stiff Upper Lip", 
//						getResources().getDrawable(R.drawable.color_yellow))
//				},
//				{ new TextImageEntry("19", 
//						getResources().getDrawable(R.drawable.color_green)), 
//				  new TextImageEntry("21",
//						getResources().getDrawable(R.drawable.color_yellow))
//				},
//				{ new TextImageEntry("Ashes Against the Grain", 
//						getResources().getDrawable(R.drawable.color_yellow)),
//				  new TextImageEntry("The Mantle", 
//						getResources().getDrawable(R.drawable.color_red))
//				},
//				{ new TextImageEntry("F51.4", 
//						getResources().getDrawable(R.drawable.color_green))
//				},
//				{ new TextImageEntry("Moon Safari",
//						getResources().getDrawable(R.drawable.color_red))
//				},
//				{},{},{},{},
//				{ new TextImageEntry("Avanger, The", 
//						getResources().getDrawable(R.drawable.color_red)),
//				  new TextImageEntry("Crusher, The",
//						  getResources().getDrawable(R.drawable.color_yellow)),
//				  new TextImageEntry("Fate of Norns", 
//						  getResources().getDrawable(R.drawable.color_red)),
//				  new TextImageEntry("Twilight of the Thunder God",  
//						  getResources().getDrawable(R.drawable.color_yellow)),
//				  new TextImageEntry("Once Sent from the Golden Hall", 
//						  getResources().getDrawable(R.drawable.color_yellow)),
//			      new TextImageEntry("Sutur Rising",
//						  getResources().getDrawable(R.drawable.color_red)),
//				  new TextImageEntry("Versus the World", 
//						  getResources().getDrawable(R.drawable.color_red)),
//			      new TextImageEntry("With Oden on Our Side",
//						  getResources().getDrawable(R.drawable.color_red))
//				},
//				{},{},{},{},{},{}
//		};
	    
		this.groups = groups.toArray(new String[0]);
		this.children = children;
	}
}
