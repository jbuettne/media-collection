package com.mediacollector.collection.books;

public interface BookTbl {

	public static final String COL_BOOK_ID = "id";
	public static final String COL_BOOK_NAME = "name";
	public static final String COL_BOOK_AUTHOR = "author";
	public static final String COL_BOOK_YEAR = "year";
	public static final String COL_BOOK_IMAGE = "imgPath";

	static final String TABLE_NAME = "Book";

	static final String SQL_CREATE = "CREATE TABLE Book (				"
			+ "	id			VARCHAR(63)		PRIMARY KEY ,					"
			+ "	name		VARCHAR(500)	NOT NULL,					"
			+ "	author		VARCHAR(63),								"
			+ "	year		VARCHAR(4),									"
			+ "	imgPath		VARCHAR(500)								"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Book (		"
			+ "	id, name, author, year, imgPath) "
			+ " 	values (?,?,?,?,?)";

	static final String STMT_DELETE = "DELETE FROM Book WHERE ?";
}
