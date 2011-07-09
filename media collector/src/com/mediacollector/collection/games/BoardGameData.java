package com.mediacollector.collection.games;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

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
public class BoardGameData{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	private static final String TAG = "BoardGameData";
	private DatabaseHelper dbHelper;
	private Context context;

	public BoardGameData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "BoardGamespeicher angelegt.");
	}

	public long insertBoardGame(String id, String name, String year,
			String imgPath) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(BoardGameTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, year);
			stmtInsert.bindString(4, imgPath);
			long pos = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "BoardGame mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, "Brettspiel zur Datenbank hinzugefügt",
					Toast.LENGTH_LONG).show();
			return pos;
		} catch(Throwable ex) {
			Log.e(TAG, "BoardGame nicht hinzugefuegt! " + ex);
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertBoardGame(BoardGame game) {
		// if (artist.istNeu()) {
		return insertBoardGame(game.id, game.name, game.year,
				game.imgPath);

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
	public boolean deleteBoardGame(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(BoardGameTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "BoardGame id=" + id + " deleted.");
			Toast.makeText(this.context, "Brettspiel aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
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
	public boolean deleteBoardGameName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(BoardGameTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "BoardGame name=" + name + " deleted.");
			Toast.makeText(this.context, "Brettspiel aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public BoardGame getBoardGame(String id) {
		BoardGame game = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BoardGameTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			game = getBoardGame(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return game;
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
	public BoardGame getBoardGame(Cursor dbCursor) {
		final BoardGame game = new BoardGame();

		game.id = dbCursor.getString(dbCursor
				.getColumnIndex(BoardGameTbl.COL_BOARD_ID));
		game.name = dbCursor.getString(dbCursor
				.getColumnIndex(BoardGameTbl.COL_BOARD_NAME));
		game.year = dbCursor.getString(dbCursor
				.getColumnIndex(BoardGameTbl.COL_BOARD_YEAR));
		game.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(BoardGameTbl.COL_BOARD_IMAGE));
		return game;
	}

	public Cursor getBoardGameDetailsName(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + BoardGameTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getBoardGameDetails(String id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + BoardGameTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
	}

	public ArrayList<BoardGame> getBoardGamesAll() {
		ArrayList<BoardGame> games = new ArrayList<BoardGame>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BoardGameTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return games;
			}
			games.add(new BoardGame(dbCursor.getString(0),dbCursor.getString(1),
					dbCursor.getString(2),dbCursor.getString(3)));
			while (dbCursor.moveToNext() == true) {
				games.add(new BoardGame(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3)));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte BoardGamee nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return games;
	}
	
	public ArrayList<TextImageEntry> getBoardGamesTI() {
		ArrayList<TextImageEntry> games = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM "
							+ BoardGameTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return games;
			}
			games.add(new TextImageEntry(dbCursor.getString(0), dbCursor
					.getString(1), context.getResources().getDrawable(
					R.drawable.color_red), dbCursor.getString(2)));
			while (dbCursor.moveToNext() == true) {
				// tempAlbum = new TextImageEntry(dbCursor.getString(0),
				// getResources().getDrawable(dbCursor.getString(1)),
				// dbCursor.getInt(2)));
				games.add(new TextImageEntry(dbCursor.getString(0), dbCursor
						.getString(1), context.getResources().getDrawable(
								R.drawable.color_red), dbCursor.getString(2)));
			}
		} catch (Throwable ex) {
			Log.e("TAG", "Konnte Spiele nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return games;
	}

	/**
	 * Gibt die Anzahl der Alben in der Datenbank zurueck. <br>
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Kontakte.
	 */
	public int gameCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + BoardGameTbl.TABLE_NAME, null);
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
