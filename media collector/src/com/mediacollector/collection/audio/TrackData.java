package com.mediacollector.collection.audio;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mediacollector.R;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.TextImageEntry;

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
public class TrackData{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private static final String TAG = "AlbumData";
	private DatabaseHelper dbHelper;
	private Context context;

	public TrackData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		open();
		Log.d(TAG, "Albenspeicher angelegt.");
	}

	private TrackData() {
	}

	public long insertTrack(String name, String artist, long cd, long trackOnCd,
			long length, String mbId) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(AlbumTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, name);
			stmtInsert.bindString(2, artist);
			stmtInsert.bindLong(3, cd);
			stmtInsert.bindLong(4, trackOnCd);
			stmtInsert.bindLong(5, length);
			stmtInsert.bindString(6, mbId);
			long id = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Track mit id=" + id + " erzeugt.");
			return id;
		} catch(Throwable ex) {
			Log.e("TAG", "Track nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertTrack(Track track) {
		// if (artist.istNeu()) {
		return insertTrack(track.name, track.artist, track.cd, track.trackOnCd,
				track.length, track.mbId);

		// };
		// } else {
		// updateArtist(
		// artist.id,
		// artist.name,
		// artist.imgPath,
		// artist.mbId);
		// return artist.id;
		// }
	}

	// public void aendereGeoKontakt(long id, String name,
	// String lookupKey, String mobilnummer,
	// String stichwort,
	// double laengengrad, double breitengrad, double hoehe,
	// long zeitstempel) {
	// if (id == 0) {
	// Log.w(TAG, "id == 0 => kein update möglich.");
	// return;
	// }
	//
	// final ContentValues daten = new ContentValues();
	// daten.put(GeoKontaktTbl.NAME, name);
	// daten.put(GeoKontaktTbl.LOOKUP_KEY, lookupKey);
	// daten.put(GeoKontaktTbl.MOBILNUMMER, mobilnummer);
	// if (stichwort != null) {
	// daten.put(GeoKontaktTbl.STICHWORT_POS, stichwort);
	// daten.put(GeoKontaktTbl.LAENGENGRAD, laengengrad);
	// daten.put(GeoKontaktTbl.BREITENGRAD, breitengrad);
	// daten.put(GeoKontaktTbl.HOEHE, hoehe);
	// daten.put(GeoKontaktTbl.ZEITSTEMPEL, zeitstempel);
	// }
	//
	// final SQLiteDatabase dbCon = mDb.getWritableDatabase();
	//
	// try {
	// dbCon.update(GeoKontaktTbl.TABLE_NAME, daten,
	// GeoKontaktTbl.WHERE_ID_EQUALS, new String[] {
	// String.valueOf(id) });
	// Log.i(TAG,
	// "Geokontakt id=" + id + " aktualisiert.");
	// } finally {
	// dbCon.close();
	// }
	// }

	// public void updateAlbum(long id,
	// double laengengrad, double breitengrad, double hoehe,
	// long zeitstempel) {
	// if (id == 0) {
	// Log.w(TAG, "id == 0 => kein update möglich.");
	// return;
	// }
	//
	// final ContentValues daten = new ContentValues();
	// daten.put(GeoKontaktTbl.LAENGENGRAD, laengengrad);
	// daten.put(GeoKontaktTbl.BREITENGRAD, breitengrad);
	// daten.put(GeoKontaktTbl.HOEHE, hoehe);
	// daten.put(GeoKontaktTbl.ZEITSTEMPEL, zeitstempel);
	//
	// final SQLiteDatabase dbCon = mDb.getWritableDatabase();
	//
	// try {
	// dbCon.update(GeoKontaktTbl.TABLE_NAME, daten,
	// GeoKontaktTbl.WHERE_ID_EQUALS,
	// new String[] { String.valueOf(id) });
	// Log.i(TAG,
	// "Geokontakt id=" + id + " aktualisiert.");
	// } finally {
	// dbCon.close();
	// }
	// }

	/**
	 * Entfernt einen Geokontakt aus der Datenbank.
	 * 
	 * @param name
	 *            Schlüssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean deleteTrack(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(TrackTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Track name=" + name + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Track getTrack(long id) {
		Track track = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + TrackTbl.TABLE_NAME 
				+ " WHERE _id = '" + id + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			track = getTrack(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return track;
	}
	
	public Track getTrack(String name) {
		Track track = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + TrackTbl.TABLE_NAME 
				+ " WHERE name = '" + name + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			track = getTrack(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return track;
	}	

	/**
	 * Lädt den Geo-Kontakt aus dem GeoKontaktTbl-Datensatz, auf dem der Cursor
	 * gerade steht.
	 * <p>
	 * Der Cursor wird anschließend deaktiviert, da er im GeoKontaktSpeicher nur
	 * intern als "letzter Aufruf" aufgerufen wird.
	 * 
	 * @param c
	 *            aktuelle Cursorposition != null
	 * @return Exemplar von GeoKontakt.
	 */
	public Track getTrack(Cursor dbCursor) {
		final Track track = new Track();

		track.id = dbCursor.getLong(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_ID));
		track.name = dbCursor.getString(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_NAME));
		track.artist = dbCursor.getString(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_ARTIST));
		track.cd = dbCursor.getLong(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_CD));
		track.trackOnCd = dbCursor.getLong(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_TRACKONCD));
		track.length = dbCursor.getLong(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_LENGTH));
		track.mbId = dbCursor.getString(dbCursor
				.getColumnIndex(TrackTbl.COL_TRACK_MBID));
		return track;
	}

	public ArrayList<String> getTracks(Artist artist) {
		ArrayList<String> tracks = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT name FROM " + TrackTbl.TABLE_NAME 
				+ " WHERE artist = '" + artist.getMbId() + "'", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			tracks.add(dbCursor.getString(0));
			while (dbCursor.moveToNext() == true) {
				tracks.add(dbCursor.getString(0));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Tracks nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return tracks;
	}
	


	public Cursor getTrackDetails(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + TrackTbl.TABLE_NAME 
				+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getTrackDetails(Integer id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + TrackTbl.TABLE_NAME 
				+ " WHERE _id = '" + id + "'", null);
	}

	/**
	 * Gibt die Anzahl der Alben in der Datenbank zurueck. <br>
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Kontakte.
	 */
	public int trackCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + TrackTbl.TABLE_NAME, null);
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
