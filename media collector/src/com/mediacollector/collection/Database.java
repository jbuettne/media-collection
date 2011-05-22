package com.mediacollector.collection;

import java.io.IOException;

import com.mediacollector.Start;
import com.mediacollector.collection.DatabaseHelper.T_Artist;
import com.mediacollector.collection.audio.Artist;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Die Datenbank-Klasse, die für die Erstellung, zur Verbindung zur Datenbank
 * und zum sauberen Ausführen von Anfragen zuständig ist. Die Klasse ist als
 * Singleton aufgebaut, es ist also nur eine Verbindung zur Datenbank möglich.
 * 
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Database {

	private DatabaseHelper dbHelper;
	private SQLiteStatement stmtInsert;

	public Database(Context context) {
		dbHelper = new DatabaseHelper(context);

		// vernetzung mit anderen speichern...
		// ortSpeicher = new OrtSpeicher(context);

		// erzeugung von prepared statements
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		stmtInsert = db.compileStatement(
				DatabaseHelper.T_Artist.STMT_FULL_INSERT);
		stmtInsert = db.compileStatement(
				DatabaseHelper.T_Cd.STMT_FULL_INSERT);
		stmtInsert = db.compileStatement(
				DatabaseHelper.T_Track.STMT_FULL_INSERT);
	}

	/**
	 * schlie�e die Datenbankverbindung
	 */
	public void closeConnection() {
		dbHelper.close();
	}
	
	public void insertIntoDb(Artist artist) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(T_Artist.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, artist.getName());
			stmtInsert.bindString(2, artist.getImgPath());
			stmtInsert.bindString(3, artist.getMbId());
			stmtInsert.executeInsert();
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			throw new SQLException("Konnte Daten nicht einfuegen");
		} finally {
			db.endTransaction();
		}
	}
}
