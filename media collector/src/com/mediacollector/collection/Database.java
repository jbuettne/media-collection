package com.mediacollector.collection;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Die Datenbank-Klasse, die für die Erstellung, zur Verbindung zur Datenbank
 * und zum sauberen Ausführen von Anfragen zuständig ist.
 * Die Klasse ist als Singleton aufgebaut, es ist also nur eine Verbindung zur 
 * Datenbank möglich.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Database extends Activity {
	
	private final String DBNAME = "mcDataDB";
	private SQLiteDatabase dbHandle = null;
	
	private static Database instance = new Database();	
	public static Database getInstance() {
		return instance;
	}
	
	public Database() {}
	
	public void connectToDb() {
		DBHelper dbHelper = new DBHelper(getBaseContext(), DBNAME);
		this.dbHandle = dbHelper.getWritableDatabase();
		//this.dbHandle = this.openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
	}	
	public void executeStatement(final String statement) {}	
	public void executeStatements(final String[] statements) {}
	public void create() {}
	public void select() {}
	public void update() {}
	public void delete() {}
	public void createDb() {
		/*if (this.dbHandle == null) this.connectToDb();
		this.dbHandle.beginTransaction();
		this.dbHandle.execSQL(
				"CREATE TABLE Artist (										" +
				"	id		INTEGER			PRIMARY_KEY AUTO_INCREMENT,		" +
				"	name	VARCHAR(500)	NOT NULL,						" +
				"	imgPath	VARCHAR(500),									" +
				"	mbId	VARCHAR(500)									" +
				");"
		);
		this.dbHandle.execSQL(
				"CREATE TABLE Cd (											" +
				"	id		INTEGER			PRIMARY_KEY AUTO_INCREMENT,		" +
				"	name	VARCHAR(500)	NOT NULL,						" +
				"	artist	INTEGER 		REFERENCES Artist,				" +
				"	year	INTEGER,										" +
				"	imgPath	VARCHAR(500),									" +
				"	mbId	VARCHAR(500)									" +
				");"
		);
		this.dbHandle.execSQL(
				"CREATE TABLE Track (										" +
				"	id			INTEGER			PRIMARY_KEY AUTO_INCREMENT,	" +
				"	name		VARCHAR(500)	NOT NULL,					" +
				"	artist		INTEGER 		REFERENCES Artist,			" +
				"	cd			INTEGER 		REFERENCES Cd,				" +
				"	trackOnCd	INTEGER,									" +
				"	length		INTEGER,									" +
				"	imgPath		VARCHAR(500),								" +
				"	mbId		VARCHAR(500)								" +
				");"
		);
		this.dbHandle.execSQL(
				"CREATE TABLE Film (										" +
				"	id		INTEGER			PRIMARY_KEY AUTO_INCREMENT,		" +
				"	name	VARCHAR(500)	NOT NULL,						" +
				"	movie	TINYINT(1) 										" +
				"	length	INTEGER,										" +
				"	year	INTEGER,										" +
				"	imgPath	VARCHAR(500),									" +
				"	imdbId	VARCHAR(500)									" +
				");"
		);
		this.dbHandle.execSQL(
				"CREATE TABLE Series (										" +
				"	id		INTEGER			PRIMARY_KEY AUTO_INCREMENT,		" +
				"	name	VARCHAR(250)	NOT NULL,						" +
				"	imgPath	VARCHAR(500)									" +	
				");"
		);
		this.dbHandle.execSQL(
				"CREATE TABLE Episode (										" +
				"	id		INTEGER			PRIMARY_KEY AUTO_INCREMENT,		" +
				"	film	INTEGER			REFERENCES Film,				" +
				"	series	INTEGER			REFERENCES Series,				" +
				"	season	INTEGER,										" +
				"	episode	INTEGER											" +	
				");"
		);
		this.dbHandle.endTransaction();
	}*/
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		try {
			dbHelper.createDb();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		
		try {
			dbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
	}
}

class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context, String name) {
		super(context, name, null, 1);
	}
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
