package com.mediacollector.collection.books;

import java.util.ArrayList;
import java.util.HashMap;

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
public class BookData {

	/**
	 * Enthält alle benötigten Daten für die Objekte. Diese können über die
	 * entsprechenden getter und setter gelesen/gesetzt werden.
	 */
	
	@SuppressWarnings("unused")
	private HashMap<String, Object> data = new HashMap<String, Object>();
	private static final String TAG = "BookData";
	private DatabaseHelper dbHelper;
	private Context context;

	public BookData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "Bookspeicher angelegt.");
	}

	public long insertBook(String id, String name, String author, String year,
			String imgPath) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(BookTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, author);
			stmtInsert.bindString(4, year);
			stmtInsert.bindString(5, imgPath);
			long pos = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Book mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, "Buch zur Datenbank hinzugefügt",
					Toast.LENGTH_LONG).show();
			return pos;
		} catch(Throwable ex) {
			Log.e(TAG, "Book nicht hinzugefuegt! " + ex);
			return -1;
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public long insertBook(Book book) {
		return insertBook(book.id, book.name, book.author, book.year,
				book.imgPath);
	}

	/**
	 * Entfernt einen Geokontakt aus der Datenbank.
	 * 
	 * @param id
	 *            Schlüssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean deleteBook(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(BookTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "Book id=" + id + " deleted.");
			Toast.makeText(this.context, "Buch aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	/**
	 * Entfernt einen Geokontakt aus der Datenbank.
	 * 
	 * @param name
	 *            Schlüssel des gesuchten Kontakts
	 * @return true, wenn Datensatz geloescht wurde.
	 */
	public boolean deleteBookName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			deleteCount = db.delete(BookTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Book name=" + name + " deleted.");
			Toast.makeText(this.context, "Buch aus der Datenbank gelöscht",
					Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public Book getBook(String id) {
		Book book = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BookTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
			if (c.moveToFirst() == false) {
				return null;
			}
			book = getBook(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return book;
	}

	/**
	 * Lädt den Geo-Kontakt aus dem GeoKontaktTbl-Datensatz, auf dem der Cursor
	 * gerade steht.
	 * <p>
	 * Der Cursor wird anschließend deaktiviert, da er im GeoKontaktSpeicher nur
	 * intern als "letzter Aufruf" aufgerufen wird.
	 * 
	 * @param c
	 *            aktuelle Cursorposition != null
	 * @return Exemplar von GeoKontakt.
	 */
	public Book getBook(Cursor dbCursor) {
		final Book book = new Book();

		book.id = dbCursor.getString(dbCursor
				.getColumnIndex(BookTbl.COL_BOOK_ID));
		book.name = dbCursor.getString(dbCursor
				.getColumnIndex(BookTbl.COL_BOOK_NAME));
		book.author = dbCursor.getString(dbCursor
				.getColumnIndex(BookTbl.COL_BOOK_AUTHOR));
		book.year = dbCursor.getString(dbCursor
				.getColumnIndex(BookTbl.COL_BOOK_YEAR));
		book.imgPath = dbCursor.getString(dbCursor
				.getColumnIndex(BookTbl.COL_BOOK_IMAGE));
		return book;
	}

	public Cursor getBookDetailsName(String name) {
		if (name == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + BookTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
	}

	public Cursor getBookDetails(String id) {
		if (id == null) {
			return null;
		}
		return dbHelper.getReadableDatabase().rawQuery(
				"SELECT * FROM " + BookTbl.TABLE_NAME
					+ " WHERE id = '" + id + "'", null);
	}
	
	public ArrayList<Book> getBooksAll() {
		ArrayList<Book> books = new ArrayList<Book>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BookTbl.TABLE_NAME, null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			books.add(new Book(dbCursor.getString(0),dbCursor.getString(1),
					dbCursor.getString(2),dbCursor.getString(3),
					dbCursor.getString(4)));
			while (dbCursor.moveToNext() == true) {
				books.add(new Book(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3),
						dbCursor.getString(4)));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Books nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return books;
	}
	
	public ArrayList<String> getAuthorName(){
		ArrayList<String> author = new ArrayList<String>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT author FROM " + BookTbl.TABLE_NAME +
					" GROUP BY author ORDER BY author", null);
			if (dbCursor.moveToFirst() == false) {
				return new ArrayList<String>();
			}	
			author.add(dbCursor.getString(0));
		    while (dbCursor.moveToNext() == true) {
		    	author.add(dbCursor.getString(0));
			}
  
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
	    return author; 
		
	}	

	public ArrayList<TextImageEntry> getBooksTI(String author) {
		ArrayList<TextImageEntry> books = new ArrayList<TextImageEntry>();
		Cursor dbCursor = null;
		try {
			dbCursor = dbHelper.getReadableDatabase().rawQuery(
					"SELECT id, name, year, imgPath FROM " + BookTbl.TABLE_NAME
					+ " WHERE author = '" + author + "' " +
					"ORDER BY name", null);
			if (dbCursor.moveToFirst() == false) {
				return null;
			}
			books.add(new TextImageEntry(dbCursor.getString(0),
					dbCursor.getString(1),
					context.getResources().getDrawable(
							R.drawable.color_red), dbCursor.getString(2)));
			while (dbCursor.moveToNext() == true) {
//				tempAlbum = new TextImageEntry(dbCursor.getString(0),
//						getResources().getDrawable(dbCursor.getString(1)),
//						dbCursor.getInt(2),getTrackCount...));
				books.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1),
						context.getResources().getDrawable(
    							R.drawable.color_red), dbCursor.getString(2)));
			}
		} catch(Throwable ex) {
			Log.e("TAG", "Konnte Buecher nicht lesen", ex);
		} finally {
			if (dbCursor != null) {
				dbCursor.close();
			}
		}
		return books;
	}	

	/**
	 * Gibt die Anzahl der Alben in der Datenbank zurueck. <br>
	 * Performanter als Cursor::getCount.
	 * 
	 * @return Anzahl der Kontakte.
	 */
	public int bookCount() {
		final Cursor dbCursor = dbHelper.getReadableDatabase().rawQuery(
				"SELECT count(*) FROM " + BookTbl.TABLE_NAME, null);
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
