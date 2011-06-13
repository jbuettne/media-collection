package com.mediacollector.collection.audio;

import java.util.ArrayList;
import java.util.HashMap;

import com.mediacollector.collection.Database;
import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.DatabaseHelper.T_Artist;
import com.mediacollector.collection.DatabaseHelper.T_Cd;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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
public class Artist{

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private Cursor dbCursor = null;
	private String sqlWhere;
	private DatabaseHelper dbHelper;
	private Context context;
	public Artist(Context context) {
		dbHelper = new DatabaseHelper(context);
		this.context = context;
	}

	public Artist(Context context, final int id) {
		dbHelper = new DatabaseHelper(context);
		this.data.put("id", id);
		this.getDataFromDb();
	}

	public Artist(Context context, final String name) {
		dbHelper = new DatabaseHelper(context);
		this.data.put("name", name);
		this.getDataFromDb();
	}

	/**
	 * Über diese Methode kann eine Menge von Werten gesetzt werden. Erlaubte
	 * (key-)Werte: id, name, imgPath, mbId
	 * 
	 * @param dataMap
	 *            HashMap<String, Object> Map mit Key-Value-Paaren.
	 */
	public void setData(final HashMap<String, Object> dataMap) {
		if (dataMap.containsKey("id"))
			this.data.put("id", dataMap.get("id"));
		if (dataMap.containsKey("name"))
			this.data.put("name", dataMap.get("name"));
		if (dataMap.containsKey("imgPath"))
			this.data.put("imgPath", dataMap.get("imgPath"));
		if (dataMap.containsKey("mbId"))
			this.data.put("mbId", dataMap.get("mbId"));
	}

	/**
	 * Über diese Methode kann ein einzelner Wert gesetzt werden. Erlaubte
	 * (key-)Werte: id, name, imgPath, mbId
	 * 
	 * @param key
	 *            String Der Key.
	 * @param value
	 *            Der zu setzende Wert.
	 */
	public void setValue(final String key, final Object value) {
		if (key.equals("id") || key.equals("name") || key.equals("imgPath")
				|| key.equals("mbId")) {
			this.data.put(key, value);
		}
	}

	/**
	 * Getter. Liefer die gesamten Daten des Objektes.
	 * 
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getData() {
		return this.data;
	}

	/**
	 * Getter. Liefert die ID des Artists in der Datenbank.
	 * 
	 * @return Integer
	 */
	public int getId() {
		return (Integer) this.data.get("id");
	}

	/**
	 * Getter. Liefert den Namen des Artists.
	 * 
	 * @return String
	 */
	public String getName() {
		return (String) this.data.get("name");
	}

	/**
	 * Getter. Liefert den Pfad zur Bilddatei zum Artist.
	 * 
	 * @return String
	 */
	public String getImgPath() {
		return (String) this.data.get("imgPath");
	}

	/**
	 * Getter. Liefert die MusicBrainz-ID des Artists.
	 * 
	 * @return String
	 */
	public String getMbId() {
		return (String) this.data.get("mbId");
	}

	public void getDataFromDb() {
		if (this.data.get("id") != null) {
			sqlWhere = "_id = '" + getId() + "'";
		} else if (this.data.get("name") != null) {
			sqlWhere = "name = '" + getName() + "'";
		} else
			throw new SQLException("Nicht alle Daten angegeben");

		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM Artist WHERE " + sqlWhere, null);
		    if (dbCursor == null || dbCursor.moveToFirst() == false) {
		    	Log.e("ArtistFromDB", "test");
		      }
			this.data.put("id", dbCursor.getInt(dbCursor
					.getColumnIndexOrThrow(T_Artist.COL_ARTIST_ID)));
			this.data.put("name", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Artist.COL_ARTIST_NAME)));
			this.data.put("imgPath", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Artist.COL_ARTIST_IMAGE)));
			this.data.put("mbId", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Artist.COL_ARTIST_MBID)));
			dbCursor.close();
		} catch (Throwable ex) {
			Log.e("Artist - getDataFromDb", "Konnte Daten nicht einfügen", ex);
			ex.printStackTrace();
		} finally {
			dbCursor.close();
		}

	}

	public void insertIntoDb() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(T_Artist.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, getName());
			stmtInsert.bindString(2, getImgPath());
			stmtInsert.bindString(3, getMbId());
			stmtInsert.executeInsert();
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			Log.e("Artist - insertIntoDb", "Konnte Daten nicht einfügen", ex);
			ex.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public void deleteFromDb() {
		if (this.data.get("id") != null) {
			sqlWhere = "_id = '" + getId() + "'";
		} else if (this.data.get("name") != null) {
			sqlWhere = "name = '" + getName() + "'";
		} else
			throw new SQLException("Nicht alle Daten angegeben");
		try {
			dbHelper.getWritableDatabase().delete("Artist", sqlWhere, null);
		} catch (Throwable ex) {
			Log.e("Artist - deleteFromDb", "Artist nicht gefunden", ex);
			ex.printStackTrace();
		}
	}

	public ArrayList<Cd> getAlbums() {
		if (this.data.get("id") != null) {
			sqlWhere = "artist = '" + getId() + "'";
		} else if (this.data.get("name") != null) {
			getDataFromDb();
			sqlWhere = "artist = '" + getId() + "'";
		} else
			throw new SQLException("Nicht alle Daten angegeben");

		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT _id FROM Cd WHERE " + sqlWhere, null);
			if (dbCursor == null || dbCursor.moveToFirst() == false) {}
			ArrayList<Cd> albumList = new ArrayList<Cd>();
			while (dbCursor.moveToNext()) {
				Cd curAlbum = new Cd(this.context, dbCursor.getInt(dbCursor
						.getColumnIndexOrThrow(T_Cd.COL_CD_ID)));
				albumList.add(curAlbum);
				dbCursor.moveToNext();
			}
			return albumList;
		} catch (Throwable ex) {
			Log.e("Artist - getAlbums", "Artist nicht gefunden", ex);
			ex.printStackTrace();
		} finally {
			dbCursor.close();
		}
		/*
		 * if id != null: sqlWhere = "artist = '" + id + "';"; else if name !=
		 * null: getDataFromDb(); sqlWhere = "artist = '" + id + "';"; else:
		 * throw new MCSQLException("Nicht alle Daten angegeben");
		 * 
		 * db = Database(); try: rows = db.select("id", "Album", sqlWhere);
		 * albumList = new ArrayList<Album>(); for row in rows: curAlbum = new
		 * Album(row[0]); albumList.add(curAlbum); return albumList
		 * 
		 * catch MCSQLException: throw new
		 * MCSQLException("Artist nicht gefunden");
		 */
		// return null;
		return null;
	}

	public void getTracks() {
	}

	public static void getAll() {
	}

}
