package com.mediacollector.collection.video;

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
public class FilmData {
	
	private static final String TAG = "FilmData";
	private DatabaseHelper dbHelper;
	private Context context;

	public FilmData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "Filmspeicher angelegt.");
	}

	public long insertFilm(String id, String name, String year,
			String imgPath, String imgPathHttp) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(FilmTbl.STMT_FULL_INSERT);
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
			Log.i(TAG, "Film mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, "Film zur Datenbank hinzugefügt",
					Toast.LENGTH_LONG).show();
			return pos;
		} catch(Throwable ex) {
			Log.e(TAG, "Film nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertFilm(Film film) {
		return insertFilm(film.id, film.name, film.year,
				film.imgPath, film.imgPathHttp);

	}


	public boolean deleteFilm(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File filmImage = new File(getFilm(id).imgPath
						+ ".jpg");
				File filmImageSmall = new File(getFilm(id).imgPath
						+ "_small.jpg");
				filmImage.delete();
				filmImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Film hat kein Cover " + ex);
			}
			deleteCount = db.delete(FilmTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "Film id=" + id + " deleted.");
			Toast.makeText(this.context, "Film aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public boolean deleteFilmName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File filmImage = new File(getFilmName(name).imgPath
						+ ".jpg");
				File filmImageSmall = new File(getFilmName(name).imgPath
						+ "_small.jpg");
				filmImage.delete();
				filmImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Spiel hat kein Cover " + ex);
			}
			deleteCount = db.delete(FilmTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Film name=" + name + " deleted.");
			Toast.makeText(this.context, "Film aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
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

	public Film getFilmName(String name) {
		Film film = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + FilmTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
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

	public Film getFilm(Cursor dbCursor) {
		final Film film = new Film();

		film.id = dbCursor.getString(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_ID));
		film.name = dbCursor.getString(dbCursor
				.getColumnIndex(FilmTbl.COL_FILM_NAME));
		film.year = dbCursor.getString(dbCursor
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

	public Cursor getFilmDetails(String id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + FilmTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
	}

	public ArrayList<Film> getFilmsAll() {
		ArrayList<Film> films = new ArrayList<Film>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + FilmTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return films;
			}
			films.add(new Film(dbCursor.getString(0),dbCursor.getString(1),
					dbCursor.getString(2),dbCursor.getString(3),
					dbCursor.getString(4)));
			while (dbCursor.moveToNext() == true) {
				films.add(new Film(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3),
						dbCursor.getString(4)));
			}
		} catch(Throwable ex) {
			Log.e(TAG, "Konnte Filme nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return films;
	}
	
	public ArrayList<TextImageEntry> getFilmsTI() {
		ArrayList<TextImageEntry> films = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM " + FilmTbl.TABLE_NAME
					+ " ORDER by name", null);
			if (dbCursor.moveToFirst() == false) {
				return films;
			}
			films.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1), dbCursor.getString(2),
					dbCursor.getString(3), FilmTbl.TABLE_NAME, ""));
			while (dbCursor.moveToNext() == true) {
				films.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1), dbCursor.getString(2),
						dbCursor.getString(3), FilmTbl.TABLE_NAME, ""));
			}
		} catch(Throwable ex) {
			Log.e(TAG, "Konnte Filme nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return films;
	}	

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
