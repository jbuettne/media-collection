package com.mediacollector.collection.games;

import java.io.File;
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
 * 
 * @author Jens Buettner
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
			String imgPath, String imgPathHttp) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(BoardGameTbl.STMT_FULL_INSERT);
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
			Log.i(TAG, "BoardGame mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Game) + " " + context.getString(
					R.string.MEDIA_add),	Toast.LENGTH_LONG).show();
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
		return insertBoardGame(game.id, game.name, game.year,
				game.imgPath, game.imgPathHttp);
	}

	public boolean deleteBoardGame(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File gameImage = new File(getBoardGame(id).imgPath
						+ ".jpg");
				File gameImageSmall = new File(getBoardGame(id).imgPath
						+ "_small.jpg");
				gameImage.delete();
				gameImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Spiel hat kein Cover " + ex);
			}
			deleteCount = db.delete(BoardGameTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "BoardGame id=" + id + " deleted.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Game) + " " + context.getString(
					R.string.MEDIA_del),	Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public boolean deleteBoardGameName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File gameImage = new File(getBoardGameName(name).imgPath
						+ ".jpg");
				File gameImageSmall = new File(getBoardGameName(name).imgPath
						+ "_small.jpg");
				gameImage.delete();
				gameImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Spiel hat kein Cover " + ex);
			}
			deleteCount = db.delete(BoardGameTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "BoardGame name=" + name + " deleted.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Game) + " " + context.getString(
					R.string.MEDIA_del),	Toast.LENGTH_LONG).show();
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

	public BoardGame getBoardGameName(String name) {
		BoardGame game = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BoardGameTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
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
					dbCursor.getString(2),dbCursor.getString(3),
					dbCursor.getString(4)));
			while (dbCursor.moveToNext() == true) {
				games.add(new BoardGame(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3),
						dbCursor.getString(4)));
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
			games.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1), dbCursor.getString(2),
					dbCursor.getString(3), BoardGameTbl.TABLE_NAME, ""));
			while (dbCursor.moveToNext() == true) {
				games.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1), dbCursor.getString(2),
						dbCursor.getString(3), BoardGameTbl.TABLE_NAME, ""));
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
