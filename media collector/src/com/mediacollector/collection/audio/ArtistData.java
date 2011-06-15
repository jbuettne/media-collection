package com.mediacollector.collection.audio;

import java.util.ArrayList;
import java.util.HashMap;

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
public class ArtistData{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private static final String TAG = "ArtistData";
	private DatabaseHelper dbHelper;
	private Context context;

	public ArtistData(Context context) {
		dbHelper = new DatabaseHelper(context);
		open();
		Log.d(TAG, "Artistspeicher angelegt.");
	}

	private ArtistData() {
	}

	public long insertArtist(String name, String imgPath, String mbId) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(ArtistTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, name);
			stmtInsert.bindString(2, imgPath);
			stmtInsert.bindString(3, mbId);
			long id = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Geokontakt mit id=" + id + " erzeugt.");
			return id;
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
	    //if (artist.istNeu()) {
	      return insertArtist(
	          artist.name,
	          artist.imgPath,
	          artist.mbId);
	          
	    //};
//	    } else {
//	      updateArtist(
//	          artist.id,
//	          artist.name,
//	          artist.imgPath,
//	          artist.mbId);
//	      return artist.id;
//	    }
	  }

	  /**
	   * Ändert einen vorhandenen Geokontakt in der Datenbank.
	   * Wenn die id nicht mitgegeben wird, wird keine Änderung
	   * durchgeführt. <br>
	   * Es werden bei der Änderung alle Parameter
	   * berücksichtigt. Wenn das <code>stichwort</code> gesetzt
	   * wird, werden auch die Positionsangaben gespeichert.
	   * 
	   * @param id
	   *          Schlüssel des DB-Datensatzes. 
	   * @param name Vollständiger Name (Pflichtfeld)
	   * @param lookupKey key des Telefonbuch-Kontakts
	   * @param mobilnummer Rufnummer des Kontakts.
	   * @param stichwort Stichwort der Geomarkierung.
	   *          Wenn == null, werden Positionsdaten nicht
	   *          berücksichtigt.
	   * @param laengengrad Längengrad, 0 wenn unbekannt
	   * @param breitengrad Breitengrad, 0 wenn unbekannt
	   * @param hoehe Höhe, 0 wenn unbekannt
	   * @param zeitstempel Zeitpunkt des Kontakts
	   */
//	  public void aendereGeoKontakt(long id, String name,
//	      String lookupKey, String mobilnummer,
//	      String stichwort,
//	      double laengengrad, double breitengrad, double hoehe,
//	      long zeitstempel) {
//	    if (id == 0) {
//	      Log.w(TAG, "id == 0 => kein update möglich.");
//	      return;
//	    }
//
//	    final ContentValues daten = new ContentValues();
//	    daten.put(GeoKontaktTbl.NAME, name);
//	    daten.put(GeoKontaktTbl.LOOKUP_KEY, lookupKey);
//	    daten.put(GeoKontaktTbl.MOBILNUMMER, mobilnummer);
//	    if (stichwort != null) {
//	      daten.put(GeoKontaktTbl.STICHWORT_POS, stichwort);
//	      daten.put(GeoKontaktTbl.LAENGENGRAD, laengengrad);
//	      daten.put(GeoKontaktTbl.BREITENGRAD, breitengrad);
//	      daten.put(GeoKontaktTbl.HOEHE, hoehe);
//	      daten.put(GeoKontaktTbl.ZEITSTEMPEL, zeitstempel);
//	    }
//
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

	  /**
	   * Ändert die Positionsdaten eines vorhandenen Geokontakts
	   * in der Datenbank. Wenn die id nicht mitgegeben wird,
	   * wird keine Änderung durchgeführt. <br>
	   * Es werden bei der Änderung alle Parameter
	   * berücksichtigt.
	   * 
	   * @param id
	   *          Schlüssel des gesuchten Kontakts
	   * @param laengengrad
	   *          Längengrad, 0 wenn unbekannt
	   * @param breitengrad
	   *          Breitengrad, 0 wenn unbekannt
	   * @param hoehe
	   *          Höhe, 0 wenn unbekannt
	   * @param zeitstempel
	   *          Zeitpunkt des Kontakts.
	   */
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
	   * @param id
	   *          Schlüssel des gesuchten Kontakts
	   * @return true, wenn Datensatz geloescht wurde.
	   */
	  public boolean deleteArtist(long id) {
	    final SQLiteDatabase db = dbHelper.getWritableDatabase();

	    int deleteCount = 0;
	    try {
	      deleteCount = 
	        db.delete(ArtistTbl.TABLE_NAME, 
	          "_id = '" + id + "'",
	          null);
	      Log.i(TAG,
	          "Artist id=" + id + " deleted.");
	    } finally {
	      db.close();
	    }
	    return deleteCount == 1;
	  }
	  
	  /**
	   * Entfernt einen Geokontakt aus der Datenbank.
	   * 
	   * @param name
	   *          Schlüssel des gesuchten Kontakts
	   * @return true, wenn Datensatz geloescht wurde.
	   */
	  public boolean deleteArtist(String name) {
	    final SQLiteDatabase db = dbHelper.getWritableDatabase();

	    int deleteCount = 0;
	    try {
	      deleteCount = 
	        db.delete(ArtistTbl.TABLE_NAME, 
	          "name = '" + name + "'",
	          null);
	      Log.i(TAG,
	          "Artist name=" + name + " deleted.");
	    } finally {
	      db.close();
	    }
	    return deleteCount == 1;
	  }

	public Artist getArtist(long id) {
		Artist artist = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM Artist WHERE _id = '" + id + "'", null);
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

	public Cursor getArtistDetails(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM Artist WHERE name = '" + name + "'", null);
	}

	public Cursor getArtistDetails(Integer id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM Artist WHERE _id = '" + id + "'", null);
	}

	public ArrayList<String> getArtists() {
		ArrayList<String> artists = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name FROM Artist GROUP BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
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

	    artist.id = dbCursor.getLong(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_ID));
	    artist.name = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_NAME));
	    artist.imgPath = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_IMAGE));
	    artist.mbId = dbCursor.getString(dbCursor
	        .getColumnIndex(ArtistTbl.COL_ARTIST_MBID));
	    return artist;
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
