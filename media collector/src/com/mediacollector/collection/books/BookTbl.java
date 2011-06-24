package com.mediacollector.collection.books;

public interface BookTbl {

	public static final String COL_BOOK_ID = "_id";
	public static final String COL_BOOK_NAME = "name";
	public static final String COL_BOOK_AUTHOR = "author";
	public static final String COL_BOOK_YEAR = "year";
	public static final String COL_BOOK_IMGPATH = "imgPath";

	static final String TABLE_NAME = "Book";

	static final String SQL_CREATE = "CREATE TABLE Book (				"
			+ "	_id			INTEGER		PRIMARY KEY AUTO INCREMENT,		"
			+ "	name		VARCHAR(500)	NOT NULL,					"
			+ "	author		VARCHAR(63),								"
			+ "	year		INTEGER(4),									"
			+ "	imgPath		VARCHAR(500)								"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO Book (		"
			+ "	name, author, year, imgPath) "
			+ " 	values (?,?,?,?)";
	
}
