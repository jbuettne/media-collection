package com.mediacollector.collection.audio;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.DatabaseHelper.T_Artist;
import com.mediacollector.collection.DatabaseHelper.T_Cd;

/**
 * OR-Mapper für 'Cd'.
 * -----------------------
 * Die Cd-Klasse repräsentiert eine Cd mit allen in der Datenbank  
 * gespeicherten Attributen. Über ein Objekt dieser Klasse können neue Einträge
 * in die Datenbank eingefügt werden, im Gegenzug aber auch über die Id des 
 * Eintrags in der Datenbank die entsprechenden Informationen in das Objekt 
 * eingelesen werden.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Cd {
	
	private HashMap<String, Object> data   = new HashMap<String, Object>();
	private Cursor dbCursor  = null;
	private String sqlWhere;
	private DatabaseHelper dbHelper;

	public Cd(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public Cd(Context context, final int id) {
		dbHelper = new DatabaseHelper(context);
		this.data.put("id", id);
		this.getDataFromDb();
	}

	public Cd(Context context, final String name) {
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
		if (dataMap.containsKey("year"))
			this.data.put("year", dataMap.get("year"));
		if (dataMap.containsKey("imgPath"))
			this.data.put("imgPath", dataMap.get("imgPath"));
		if (dataMap.containsKey("mbId"))
			this.data.put("mbId", dataMap.get("mbId"));
	}
	public void setValue(final String key, final Object value) {
		if (key.equals("id") || key.equals("name") || key.equals("artist") 
				|| key.equals("year") || key.equals("imgPath")
				|| key.equals("mbId")) {
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
	public long getYear() {
		return (Long) this.data.get("year");
	}
	public String getImgPath() {
		return (String) this.data.get("imgPath");
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
					"SELECT * FROM Cd WHERE " + sqlWhere, null);
		    if (dbCursor == null || dbCursor.moveToFirst() == false) {
		    	Log.e("CdFromDB", "test");
		      }
			this.data.put("id", dbCursor.getInt(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_ID)));
			this.data.put("name", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_NAME)));
			this.data.put("artist", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_ARTIST)));
			this.data.put("year", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_YEAR)));
			this.data.put("imgPath", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_IMAGE)));
			this.data.put("mbId", dbCursor.getString(dbCursor
					.getColumnIndexOrThrow(T_Cd.COL_CD_MBID)));
			dbCursor.close();
		} catch (Throwable ex) {
			Log.e("Cd - getDataFromDb", "Konnte Daten nicht einfügen", ex);
			ex.printStackTrace();
		} finally {
			dbCursor.close();
		}
	}
	
	public void insertIntoDb() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(T_Cd.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, getName());
			stmtInsert.bindLong(2, getArtist());
			stmtInsert.bindLong(3, getYear());
			stmtInsert.bindString(4, getImgPath());
			stmtInsert.bindString(5, getMbId());
			stmtInsert.executeInsert();
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			Log.e("Cd - insertIntoDb", "Konnte Daten nicht einfügen", ex);
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
			dbHelper.getWritableDatabase().delete("Cd", sqlWhere, null);
		} catch (Throwable ex) {
			Log.e("Cd - deleteFromDb", "Cd nicht gefunden", ex);
			ex.printStackTrace();
		}
	}
	public void getTracks() {}
	public void getAll() {}

}
