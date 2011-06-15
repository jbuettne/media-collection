package com.mediacollector.collection;

import java.io.IOException;

import com.mediacollector.Start;
import com.mediacollector.collection.audio.ArtistData;

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
public class Database{

	private ArtistData artist;

	private SQLiteStatement stmtInsert;

	public Database(Context context) {
		artist = new ArtistData(context);
	}

		// vernetzung mit anderen speichern...
		// ortSpeicher = new OrtSpeicher(context);


	/**
	 * schlie�e die Datenbankverbindung
	 */
	public void closeConnection() {
		//dbHelper.close();
	}

}
