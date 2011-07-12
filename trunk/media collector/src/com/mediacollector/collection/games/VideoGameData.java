package com.mediacollector.collection.games;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.mediacollector.collection.DatabaseHelper;
import com.mediacollector.collection.TextImageEntry;


/**
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class VideoGameData{
	
	private static final String TAG = "VideoGameData";
	private DatabaseHelper dbHelper;
	private Context context;

	public VideoGameData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "VideoGamespeicher angelegt.");
	}

	public long insertVideoGame(String id, String name, String year,
			String imgPath, String imgPathHttp) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(VideoGameTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, year);
			if (imgPath == null) stmtInsert.bindNull(4);
			else stmtInsert.bindString(4, imgPath);
			if (imgPathHttp == null) stmtInsert.bindNull(5);
			else stmtInsert.bindString(5, imgPathHttp);
			long pos = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "PC-Spiel mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, "Spiel zur Datenbank hinzugefügt",
					Toast.LENGTH_LONG).show();
			return pos;
		} catch(Throwable ex) {
			Log.e(TAG, "PC-Spiel nicht hinzugefuegt! " + ex);
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertVideoGame(VideoGame game) {
		return insertVideoGame(game.id, game.name, game.year,
				game.imgPath, game.imgPathHttp);
	}
	
	public boolean deleteVideoGame(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File gameImage = new File(getVideoGame(id).imgPath
						+ ".jpg");
				File gameImageSmall = new File(getVideoGame(id).imgPath
						+ "_small.jpg");
				gameImage.delete();
				gameImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Spiel hat kein Cover " + ex);
			}
			deleteCount = db.delete(VideoGameTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "PC-Spiel id=" + id + " deleted.");
			Toast.makeText(this.context, "Spiel aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public boolean deleteVideoGameName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File gameImage = new File(getVideoGameName(name).imgPath
						+ ".jpg");
				File gameImageSmall = new File(getVideoGameName(name).imgPath
						+ "_small.jpg");
				gameImage.delete();
				gameImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Spiel hat kein Cover " + ex);
			}
			deleteCount = db.delete(VideoGameTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "PC-Spiel name=" + name + " deleted.");
			Toast.makeText(this.context, "Spiel aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public VideoGame getVideoGame(String id) {
		VideoGame game = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + VideoGameTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			game = getVideoGame(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return game;
	}

	public VideoGame getVideoGameName(String name) {
		VideoGame game = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + VideoGameTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			game = getVideoGame(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return game;
	}

	public VideoGame getVideoGame(Cursor dbCursor) {
		final VideoGame game = new VideoGame();

		game.id = dbCursor.getString(dbCursor
				.getColumnIndex(VideoGameTbl.COL_VIDEO_ID));
		game.name = dbCursor.getString(dbCursor
				.getColumnIndex(VideoGameTbl.COL_VIDEO_NAME));
		game.year = dbCursor.getString(dbCursor
				.getColumnIndex(VideoGameTbl.COL_VIDEO_YEAR));
		game.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(VideoGameTbl.COL_VIDEO_IMAGE));
		return game;
	}

	public Cursor getVideoGameDetailsName(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + VideoGameTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getVideoGameDetails(String id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + VideoGameTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
	}

	public ArrayList<VideoGame> getVideoGamesAll() {
		ArrayList<VideoGame> games = new ArrayList<VideoGame>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + VideoGameTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return games;
			}
			games.add(new VideoGame(dbCursor.getString(0),dbCursor.getString(1),
					dbCursor.getString(2),dbCursor.getString(3),
					dbCursor.getString(4)));
			while (dbCursor.moveToNext() == true) {
				games.add(new VideoGame(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3),
						dbCursor.getString(4)));
			}
		} catch(Throwable ex) {
			Log.e(TAG, "Konnte PC-Spiele nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return games;
	}
	
	public ArrayList<TextImageEntry> getVideoGamesTI() {
		ArrayList<TextImageEntry> games = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM "
							+ VideoGameTbl.TABLE_NAME + " ORDER by name", null);
			if (dbCursor.moveToFirst() == false) {
				return games;
			}
			games.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1), dbCursor.getString(2),
					dbCursor.getString(3), VideoGameTbl.TABLE_NAME, ""));
			while (dbCursor.moveToNext() == true) {
				games.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1), dbCursor.getString(2),
						dbCursor.getString(3), VideoGameTbl.TABLE_NAME, ""));
			}
		} catch (Throwable ex) {
			Log.e(TAG, "Konnte PC-Spiele nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return games;
	}

	public int gameCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + VideoGameTbl.TABLE_NAME, null);
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
