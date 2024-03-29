package com.mediacollector.collection.audio;

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
public class AlbumData{

	private static final String TAG = "AlbumData";
	private DatabaseHelper dbHelper;
	private Context context;

	public AlbumData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "Albenspeicher angelegt.");
	}

	public long insertAlbum(String id, String name, String artist, 
			String year, String imgPath, String imgPathHttp) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(AlbumTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, artist);
			stmtInsert.bindString(4, year);
			if (imgPath == null) stmtInsert.bindNull(5);
			else stmtInsert.bindString(5, imgPath);
			if (imgPathHttp == null) stmtInsert.bindNull(6);
			else stmtInsert.bindString(6, imgPathHttp);
			long index = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Album mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Album) + " " + context.getString(
					R.string.MEDIA_add),	Toast.LENGTH_LONG).show();
			return index;
		} catch(Throwable ex) {
			Log.e("TAG", "Album nicht hinzugefuegt!");
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertAlbum(Album album) {
		return insertAlbum(album.mbId, album.name, album.artist, album.year,
				album.imgPath, album.imgPathHttp);
	}

	public boolean deleteAlbum(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(AlbumTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "Album id=" + id + " deleted.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Album) + " " + context.getString(
					R.string.MEDIA_del),	Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public boolean deleteAlbumName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deleteCount = 0;
		try {
			try {
				File albumImage = new File(
						getAlbum(name).imgPath + ".jpg");
				File albumImageSmall = new File(
						getAlbum(name).imgPath + "_small.jpg");
				albumImage.delete();
				albumImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Album hat kein Cover " + ex);				
			}
			deleteCount = db.delete(AlbumTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Album name=" + name + " deleted.");
			Toast.makeText(this.context, context.getString(
					R.string.MEDIA_Album) + " " + context.getString(
					R.string.MEDIA_del),	Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Cursor getAlbumDetails(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getAlbumDetails(Integer id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE _id = '" + id + "'", null);
	}

	public Album getAlbum(String name) {
		Album album = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			album = getAlbum(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return album;
	}

	public Album getAlbum(Cursor dbCursor) {
		final Album album = new Album();

		album.mbId = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_ID));
		album.name = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_NAME));
		album.artist = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_ARTIST));
		album.year = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_YEAR));
		album.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_IMAGE));
		album.imgPathHttp = dbCursor.getString(dbCursor
				.getColumnIndex(AlbumTbl.COL_ALBUM_IMAGE_HTTP));
		return album;
	}

	public ArrayList<String> getAlbums(Artist artist) {
		ArrayList<String> albums = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT name, imgPath FROM " + AlbumTbl.TABLE_NAME + 
					" WHERE artist = '" + artist.name + "' " +
					"ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			albums.add(dbCursor.getString(0));
			while (dbCursor.moveToNext() == true) {
				albums.add(dbCursor.getString(0));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Alben nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return albums;
	}
	
	public ArrayList<TextImageEntry> getAlbumsTI(Artist artist) {
		ArrayList<TextImageEntry> albums = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM " + AlbumTbl.TABLE_NAME
					+ " WHERE artist = '" + artist.name + "' " +
					"ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			albums.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1), dbCursor.getString(2),
					dbCursor.getString(3), AlbumTbl.TABLE_NAME, artist.name));
			while (dbCursor.moveToNext() == true) {
				albums.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1), dbCursor.getString(2),
						dbCursor.getString(3), AlbumTbl.TABLE_NAME, artist.name));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Alben nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return albums;
	}	

	public int albumCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + AlbumTbl.TABLE_NAME, null);
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
