package com.mediacollector.collection.audio;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
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
public class AlbumData{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private static final String TAG = "AlbumData";
	private DatabaseHelper dbHelper;
	private Context context;

	public AlbumData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		//open();
		Log.d(TAG, "Albenspeicher angelegt.");
	}

	private AlbumData() {
	}

	public long insertAlbum(String mbId, String name, String artist, String year,
			String imgPath) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(AlbumTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, mbId);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, artist);
			stmtInsert.bindString(4, year);
			stmtInsert.bindString(5, imgPath);
			long id = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Album mit id=" + mbId + " erzeugt.");
			return id;
		} catch(Throwable ex) {
			Log.e("TAG", "Album nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertAlbum(Album album) {
		// if (artist.istNeu()) {
		return insertAlbum(album.mbId, album.name, album.artist, album.year,
				album.imgPath);

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
	 * @param id
	 *            Schlüssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean deleteAlbum(long id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(AlbumTbl.TABLE_NAME, "_id = '" + id + "'",
					null);
			Log.i(TAG, "Album id=" + id + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	/**
	 * Entfernt einen Geokontakt aus der Datenbank.
	 * 
	 * @param name
	 *            Schlüssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean deleteAlbum(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(AlbumTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Album name=" + name + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Cursor getAlbumDetails(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getAlbumDetails(Integer id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE _id = '" + id + "'", null);
	}

	public Album getAlbum(String mbId) {
		Album album = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE mbId = '" + mbId + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			album = getAlbum(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return album;
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
	public Album getAlbum(Cursor dbCursor) {
		final Album album = new Album();

		album.mbId = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_ID));
		album.name = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_NAME));
		album.artist = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_ARTIST));
		album.year = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_YEAR));
		album.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_IMAGE));
		return album;
	}

	public ArrayList<String> getAlbums(Artist artist) {
		ArrayList<String> albums = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name, imgPath FROM " + AlbumTbl.TABLE_NAME + 
					" WHERE artist = '" + artist.mbId + "' " +
					"ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			albums.add(dbCursor.getString(0));
			while (dbCursor.moveToNext() == true) {
				albums.add(dbCursor.getString(0));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Alben nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return albums;
	}
	
	public ArrayList<TextImageEntry> getAlbumsTI(Artist artist) {
		ArrayList<TextImageEntry> albums = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE artist = '" + artist.mbId + "' " +
					"ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			albums.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1),
					context.getResources().getDrawable(
							R.drawable.color_red), dbCursor.getString(2)));
			while (dbCursor.moveToNext() == true) {
//				tempAlbum = new TextImageEntry(dbCursor.getString(0),
//						getResources().getDrawable(dbCursor.getString(1)),
//						dbCursor.getInt(2),getTrackCount...));
				albums.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1),
						context.getResources().getDrawable(
    							R.drawable.color_red), dbCursor.getString(2)));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Alben nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return albums;
	}	

	/**
	 * Gibt die Anzahl der Alben in der Datenbank zurueck. <br>
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Kontakte.
	 */
	public int albumCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + AlbumTbl.TABLE_NAME, null);
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
