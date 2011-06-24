package com.mediacollector.collection.video;

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
public class FilmData{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private static final String TAG = "FilmData";
	private DatabaseHelper dbHelper;
	private Context context;

	public FilmData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		//open();
		Log.d(TAG, "Filmspeicher angelegt.");
	}

	private FilmData() {
	}

	public long insertFilm(String id, String name, long year,
			String imgPath) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(FilmTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindLong(3, year);
			stmtInsert.bindString(4, imgPath);
			long pos = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Film mit id=" + id + " erzeugt.");
			return pos;
		} catch(Throwable ex) {
			Log.e("TAG", "Film nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertFilm(Film film) {
		// if (artist.istNeu()) {
		return insertFilm(film.id, film.name, film.year,
				film.imgPath);

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
	public boolean deleteFilm(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(FilmTbl.TABLE_NAME, "_id = '" + id + "'",
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
	public boolean deleteFilmName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(FilmTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Film name=" + name + " deleted.");
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Film getFilm(String id) {
		Film film = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + FilmTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			film = getFilm(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return film;
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
	public Film getFilm(Cursor dbCursor) {
		final Film film = new Film();

		film.id = dbCursor.getString(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_ID));
		film.name = dbCursor.getString(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_NAME));
		film.year = dbCursor.getLong(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_YEAR));
		film.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_IMAGE));
		return film;
	}

	public Cursor getFilmDetailsName(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + FilmTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getAlbumDetails(String id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + FilmTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
	}

//	public ArrayList<String> getFilms(String firstLetter) {
//		ArrayList<String> albums = new ArrayList<String>();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = dbHelper.getReadableDatabase().rawQuery(
//					"SELECT name, imgPath FROM " + FilmTbl.TABLE_NAME
//					+ " WHERE name = '" + artist.mbId + "'", null);
//			if (dbCursor.moveToFirst() == false) {
//				return null;
//			}
//			albums.add(dbCursor.getString(0));
//			while (dbCursor.moveToNext() == true) {
//				albums.add(dbCursor.getString(0));
//			}
//		} catch(Throwable ex) {
//			Log.e("TAG", "Konnte Alben nicht lesen", ex);
//		} finally {
//			if (dbCursor != null) {
//				dbCursor.close();
//			}
//		}
//		return albums;
//	}	
	public ArrayList<Film> getFilmsAll() {
		ArrayList<Film> films = new ArrayList<Film>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + FilmTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			films.add(new Film(dbCursor.getString(0),dbCursor.getString(1),
					dbCursor.getLong(2),dbCursor.getString(3)));
			while (dbCursor.moveToNext() == true) {
				films.add(new Film(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getLong(2),dbCursor.getString(3)));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Filme nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return films;
	}
	
//	public ArrayList<TextImageEntry> getAlbumsTI(Artist artist) {
//		ArrayList<TextImageEntry> albums = new ArrayList<TextImageEntry>();
//		Cursor dbCursor = null;
//		try {
//			dbCursor = dbHelper.getReadableDatabase().rawQuery(
//					"SELECT name, year, imgPath FROM " + AlbumTbl.TABLE_NAME
//					+ " WHERE artist = '" + artist.mbId + "'", null);
//			if (dbCursor.moveToFirst() == false) {
//				return null;
//			}
//			albums.add(new TextImageEntry(dbCursor.getString(0),
//					context.getResources().getDrawable(
//							R.drawable.color_red), dbCursor.getInt(1),10));
//			while (dbCursor.moveToNext() == true) {
////				tempAlbum = new TextImageEntry(dbCursor.getString(0),
////						getResources().getDrawable(dbCursor.getString(1)),
////						dbCursor.getInt(2),getTrackCount...));
//				albums.add(new TextImageEntry(dbCursor.getString(0),
//						context.getResources().getDrawable(
//    							R.drawable.color_red), dbCursor.getInt(1),10));
//			}
//		} catch(Throwable ex) {
//			Log.e("TAG", "Konnte Alben nicht lesen", ex);
//		} finally {
//			if (dbCursor != null) {
//				dbCursor.close();
//			}
//		}
//		return albums;
//	}	

	/**
	 * Gibt die Anzahl der Alben in der Datenbank zurueck. <br>
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Kontakte.
	 */
	public int filmCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + FilmTbl.TABLE_NAME, null);
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
