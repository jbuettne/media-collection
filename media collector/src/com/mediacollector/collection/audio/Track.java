package com.mediacollector.collection.audio;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.DatabaseHelper.T_Cd;
import com.mediacollector.collection.DatabaseHelper.T_Track;

/**
 * OR-Mapper für 'Track'.
 * -----------------------
 * Die Track-Klasse repräsentiert einen Track mit allen in der Datenbank 
 * gespeicherten Attributen. Über ein Objekt dieser Klasse können neue Einträge
 * in die Datenbank eingefügt werden, im Gegenzug aber auch über die Id des 
 * Eintrags in der Datenbank die entsprechenden Informationen in das Objekt 
 * eingelesen werden.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Track {
	
	private HashMap<String, Object> data   = new HashMap<String, Object>();
	private Cursor dbCursor;
	private String sqlWhere;
	private DatabaseHelper dbHelper;

	public Track(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public Track(Context context, final int id) {
		dbHelper = new DatabaseHelper(context);
		this.data.put("id", id);
		this.getDataFromDb();
	}

	public Track(Context context, final String name) {
		dbHelper = new DatabaseHelper(context);
		this.data.put("name", name);
		this.getDataFromDb();
	}
	
	public void setData(final HashMap<String, Object> dataMap) {
		if (dataMap.containsKey("id"))
			this.data.put("id", dataMap.get("id"));
		if (dataMap.containsKey("name"))
			this.data.put("name", dataMap.get("name"));
		if (dataMap.containsKey("artist"))
			this.data.put("artist", dataMap.get("artist"));
		if (dataMap.containsKey("cd"))
			this.data.put("cd", dataMap.get("cd"));
		if (dataMap.containsKey("trackOnCd"))
			this.data.put("trackOnCd", dataMap.get("trackOnCd"));
		if (dataMap.containsKey("length"))
			this.data.put("length", dataMap.get("length"));
		if (dataMap.containsKey("mbId"))
			this.data.put("mbId", dataMap.get("mbId"));
	}
	public void setValue(final String key, final Object value) {
		if (key.equals("id") || key.equals("name") || key.equals("artist")
				|| key.equals("cd") || key.equals("trackOnCd")
				|| key.equals("length") || key.equals("mbId")) {
			this.data.put(key, value);
		}
	}
	public HashMap<String, Object> getData() { 
		return this.data;
	}
	public int getId() {
		return (Integer) this.data.get("id");
	}
	public String getName() {
		return (String) this.data.get("name");
	}
	public int getArtist() {
		return (Integer) this.data.get("artist");
	}
	public int getCd() {
		return (Integer) this.data.get("cd");
	}
	public long getTrackOnCd() {
		return (Long) this.data.get("trackOnCd");
	}
	public double getLength() {
		return (Double) this.data.get("length");
	}
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
					"SELECT * FROM Track WHERE " + sqlWhere, null);
		    if (dbCursor == null || dbCursor.moveToFirst() == false) {
		    	Log.e("TrackFromDB", "test");
		      }
			this.data.put("id", dbCursor.getInt(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_ID)));
			this.data.put("name", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_NAME)));
			this.data.put("artist", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_ARTIST)));
			this.data.put("cd", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_CD)));
			this.data.put("trackOnCd", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_TRACKONCD)));
			this.data.put("length", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_LENGTH)));
			this.data.put("mbId", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Track.COL_TRACK_MBID)));
			dbCursor.close();
		} catch (Throwable ex) {
			Log.e("Track - getDataFromDb", "Konnte Daten nicht einfügen", ex);
			ex.printStackTrace();
		}
	}
	public void insertIntoDb() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(T_Track.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, getName());
			stmtInsert.bindLong(2, getArtist());
			stmtInsert.bindLong(3, getCd());
			stmtInsert.bindLong(4, getTrackOnCd());
			stmtInsert.bindDouble(5, getLength());
			stmtInsert.bindString(6, getMbId());
			stmtInsert.executeInsert();
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			Log.e("Track - insertIntoDb", "Konnte Daten nicht einfügen", ex);
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
			dbHelper.getWritableDatabase().delete("Track", sqlWhere, null);
		} catch (Throwable ex) {
			Log.e("Track - deleteFromDb", "Track nicht gefunden", ex);
			ex.printStackTrace();
		}
	}

}
