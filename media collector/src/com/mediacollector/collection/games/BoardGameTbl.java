package com.mediacollector.collection.games;

public interface BoardGameTbl {

	public static final String COL_BOARD_ID = "id";
	public static final String COL_BOARD_NAME = "name";
	public static final String COL_BOARD_YEAR = "year";
	public static final String COL_BOARD_IMAGE = "imgPath";
	public static final String COL_BOARD_IMAGE_HTTP = "imgPathHttp";

	static final String TABLE_NAME = "BoardGame";

	static final String SQL_CREATE = "CREATE TABLE BoardGame ( 			"
			+ "id			VARCHAR(63)		PRIMARY KEY, 				"
			+ "name			VARCHAR(500)	NOT NULL, 					"
			+ "year			VARCHAR(4), 								"
			+ "imgPath		VARCHAR(500) 								"
			+ "imgPathHttp	VARCHAR(500) 								"
			+ ");";

	static final String STMT_FULL_INSERT = "INSERT INTO BoardGame ( 	"
			+ "id, name, year, imgPath, imgPathHttp)					"
			+ " 	values (?,?,?,?,?)";
	
}
