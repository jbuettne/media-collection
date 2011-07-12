package com.mediacollector.collection.books;

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
public class BookData {

	private static final String TAG = "BookData";
	private DatabaseHelper dbHelper;
	private Context context;

	public BookData(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		Log.d(TAG, "Bookspeicher angelegt.");
	}

	public long insertBook(String id, String name, String author, String year,
			String imgPath, String imgPathHttp) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		SQLiteStatement stmtInsert = db
				.compileStatement(BookTbl.STMT_FULL_INSERT);
		db.beginTransaction();
		try {
			stmtInsert.bindString(1, id);
			stmtInsert.bindString(2, name);
			stmtInsert.bindString(3, author);
			stmtInsert.bindString(4, year);
			if (imgPath == null) stmtInsert.bindNull(5);
			else stmtInsert.bindString(5, imgPath);
			if (imgPathHttp == null) stmtInsert.bindNull(6);
			else stmtInsert.bindString(6, imgPathHttp);
			long pos = stmtInsert.executeInsert();
			db.setTransactionSuccessful();
			Log.i(TAG, "Book mit id=" + id + " erzeugt.");
			Toast.makeText(this.context, R.string.MEDIA_Book + " " 
					+ R.string.MEDIA_add,	Toast.LENGTH_LONG).show();
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
				book.imgPath, book.imgPathHttp);
	}

	public boolean deleteBook(String id) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File bookImage = new File(getBook(id).imgPath + ".jpg");
				File bookImageSmall = new File(getBook(id).imgPath
						+ "_small.jpg");
				bookImage.delete();
				bookImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Buch hat kein Cover " + ex);
			}
			deleteCount = db.delete(BookTbl.TABLE_NAME, "id = '" + id + "'",
					null);
			Log.i(TAG, "Book id=" + id + " deleted.");
			Toast.makeText(this.context, R.string.MEDIA_Book + " " 
					+ R.string.MEDIA_del,	Toast.LENGTH_LONG).show();
		} finally {
			db.close();
		}
		return deleteCount == 1;
	}

	public boolean deleteBookName(String name) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();

		int deleteCount = 0;
		try {
			try {
				File bookImage = new File(
						getBookName(name).imgPath + ".jpg");
				File bookImageSmall = new File(
						getBookName(name).imgPath + "_small.jpg");
				bookImage.delete();
				bookImageSmall.delete();
			} catch (Exception ex) {
				Log.e(TAG, "Buch hat kein Cover " + ex);				
			}
			deleteCount = db.delete(BookTbl.TABLE_NAME, "name = '" + name
					+ "'", null);
			Log.i(TAG, "Book name=" + name + " deleted.");
			Toast.makeText(this.context, R.string.MEDIA_Book + " " 
					+ R.string.MEDIA_del,	Toast.LENGTH_LONG).show();
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
	
	public Book getBookName(String name) {
		Book book = null;
		Cursor c = null;
		try {
			c = dbHelper.getReadableDatabase().rawQuery(
					"SELECT * FROM " + BookTbl.TABLE_NAME
					+ " WHERE name = '" + name + "'", null);
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
					dbCursor.getString(4), dbCursor.getString(5)));
			while (dbCursor.moveToNext() == true) {
				books.add(new Book(dbCursor.getString(0),dbCursor.getString(1),
						dbCursor.getString(2),dbCursor.getString(3),
						dbCursor.getString(4), dbCursor.getString(5)));
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
					dbCursor.getString(1), dbCursor.getString(2),
					dbCursor.getString(3), BookTbl.TABLE_NAME, author));
			while (dbCursor.moveToNext() == true) {
				books.add(new TextImageEntry(dbCursor.getString(0),
						dbCursor.getString(1), dbCursor.getString(2),
						dbCursor.getString(3), BookTbl.TABLE_NAME, author));
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
