package com.mediacollector.collection.audio;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mediacollector.collection.DatabaseHelper;

/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class ArtistData {

	private static final String TAG = "ArtistData";
	private DatabaseHelper dbHelper;

	public ArtistData(Context context) {
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "Artistspeicher angelegt.");
	}
	
	public long insertArtist(String mbId, String name) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(ArtistTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, mbId);
			stmtInsert.bindString(2, name);
			long id = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Artist mit mbId=" + mbId + " erzeugt.");
			return id;
		} catch(Throwable ex) {
			Log.e("TAG", "Artist nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	  public long insertArtist(Artist artist) {
	      return insertArtist(
	          artist.name,
	          artist.imgPath);
	    }

	public boolean deleteArtist(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(ArtistTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "Artist id=" + id + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}
	
	public boolean deleteArtistName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(
					ArtistTbl.TABLE_NAME, "name = '" + name + "'", null);
			Log.i(TAG, "Artist name=" + name + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Artist getArtist(String value, String type) {
		Artist artist = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + ArtistTbl.TABLE_NAME
				+ " WHERE '" + type + "' = '" + value + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			artist = getArtist(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return artist;
	}

	  public Artist getArtist(Cursor dbCursor) {
	    final Artist artist = new Artist();

	    artist.name = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_NAME));
	    artist.id = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_ID));
	    return artist;
	  }

	public Cursor getArtistDetails(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + ArtistTbl.TABLE_NAME
				+ " WHERE name = '" + name + "'", null);
	}


	public Cursor getArtistDetailsMbId(String mbId) {
		if (mbId == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + ArtistTbl.TABLE_NAME
				+ " WHERE id = '" + mbId + "'", null);
	}	

	public ArrayList<String> getArtistsName() {
		ArrayList<String> artists = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name FROM " + ArtistTbl.TABLE_NAME +
					" ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return new ArrayList<String>();
			}	
	    	artists.add(dbCursor.getString(0));
		    while (dbCursor.moveToNext() == true) {
		    	artists.add(dbCursor.getString(0));
			}
  
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
	    return artists; 
	}
	
	public ArrayList<Artist> getArtistsAll() {
		ArrayList<Artist> artists = new ArrayList<Artist>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + ArtistTbl.TABLE_NAME +
					" ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return new ArrayList<Artist>();
			}	
	    	artists.add(new Artist(dbCursor.getString(0),
	    			dbCursor.getString(1)));
		    while (dbCursor.moveToNext() == true) {
		    	artists.add(new Artist(dbCursor.getString(0),
		    			dbCursor.getString(1)));
			}
  
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
	    return artists; 
	}	
	  
	public int artistCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + ArtistTbl.TABLE_NAME, null);
		if (dbCursor.moveToFirst() == false) {
			return 0;
		}
		return dbCursor.getInt(0);
	}

	public void close() {
		dbHelper.close();
		Log.d(TAG, "Database closed.");
	}

	public void open() {
		dbHelper.getReadableDatabase();
		Log.d(TAG, "Database opened.");
	}
}
