package com.mediacollector.collection.audio;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mediacollector.collection.DatabaseHelper;

/**
 * OR-Mapper für 'Artist'. ----------------------- Die Artist-Klasse
 * repräsentiert einen Künstler mit allen in der Datenbank gespeicherten
 * Attributen. Über ein Objekt dieser Klasse können neue Einträge in die
 * Datenbank eingefügt werden, im Gegenzug aber auch über die Id des Eintrags
 * in der Datenbank die entsprechenden Informationen in das Objekt eingelesen
 * werden.
 * 
 * @author Philipp Dermitzel
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

	  /**
	   * Speichert einen Geokontakt. Ist dieser bereits in der
	   * Datenbank bekannt, wird der vorhandene Datensatz
	   * geändert.<br>
	   * Ansonsten wird ein neuer Datensatz erzeugt.
	   * 
	   * @param kontakt
	   *          Zu speichernder Geokontakt.
	   * @return id des persistenten Kontakts.
	   * @throws SQLException
	   *           falls Neuanlegen gefordert aber nicht
	   *           möglich.
	   */
	  public long insertArtist(Artist artist) {
	      return insertArtist(
	          artist.name,
	          artist.imgPath);
	    }
//	    } else {
//	      updateArtist(
//	          artist.id,
//	          artist.name,
//	          artist.imgPath,
//	          artist.mbId);
//	      return artist.id;
//	    }

//	  public void updateArtist(long id, String name,
//	      String imgPath, String mbId) {
//	    if (id == 0) {
//	      Log.w(TAG, "id == 0 => kein update möglich.");
//	      return;
//	    }
//
//	    final ContentValues daten = new ContentValues();
//	    daten.put(GeoKontaktTbl.NAME, name);
//	    daten.put(GeoKontaktTbl.LOOKUP_KEY, lookupKey);
//	    daten.put(GeoKontaktTbl.MOBILNUMMER, mobilnummer);
//	    final SQLiteDatabase dbCon = mDb.getWritableDatabase();
//
//	    try {
//	      dbCon.update(GeoKontaktTbl.TABLE_NAME, daten,
//	          GeoKontaktTbl.WHERE_ID_EQUALS, new String[] { 
//	          String.valueOf(id) });
//	      Log.i(TAG,
//	          "Geokontakt id=" + id + " aktualisiert.");
//	    } finally {
//	      dbCon.close();
//	    }
//	  }


//	  public void updateArtist(long id,
//	      double laengengrad, double breitengrad, double hoehe,
//	      long zeitstempel) {
//	    if (id == 0) {
//	      Log.w(TAG, "id == 0 => kein update möglich.");
//	      return;
//	    }
//
//	    final ContentValues daten = new ContentValues();
//	    daten.put(GeoKontaktTbl.LAENGENGRAD, laengengrad);
//	    daten.put(GeoKontaktTbl.BREITENGRAD, breitengrad);
//	    daten.put(GeoKontaktTbl.HOEHE, hoehe);
//	    daten.put(GeoKontaktTbl.ZEITSTEMPEL, zeitstempel);
//
//	    final SQLiteDatabase dbCon = mDb.getWritableDatabase();
//
//	    try {
//	      dbCon.update(GeoKontaktTbl.TABLE_NAME, daten,
//	          GeoKontaktTbl.WHERE_ID_EQUALS, 
//	          new String[] { String.valueOf(id) });
//	      Log.i(TAG,
//	          "Geokontakt id=" + id + " aktualisiert.");
//	    } finally {
//	      dbCon.close();
//	    }
//	  }
	  
	  /**
	   * Entfernt einen Geokontakt aus der Datenbank.
	   * 
	   * @param name
	   *          Schlüssel des gesuchten Kontakts
	   * @return true, wenn Datensatz geloescht wurde.
	   */
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

	  /**
	   * Lädt den Geo-Kontakt aus dem GeoKontaktTbl-Datensatz, 
	   * auf dem der Cursor gerade steht.
	   * <p>
	   * Der Cursor wird anschließend deaktiviert, da er im
	   * GeoKontaktSpeicher nur intern als "letzter Aufruf"
	   * aufgerufen wird.
	   * 
	   * @param c aktuelle Cursorposition != null
	   * @return Exemplar von GeoKontakt.
	   */
	  public Artist getArtist(Cursor dbCursor) {
	    final Artist artist = new Artist();

	    artist.name = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_NAME));
	    artist.imgPath = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_IMAGE));
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
	    	//artists.add(dbCursor.getString(0));
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
	  

	  /**
	   * Gibt die Anzahl der Geokontakte in der Datenbank
	   * zurueck.
	   * <br>Performanter als Cursor::getCount.
	   * 
	   * @return Anzahl der Kontakte.
	   */
	  public int artistCount() {
	    final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
	        "SELECT count(*) FROM " + ArtistTbl.TABLE_NAME,
	        null);
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
