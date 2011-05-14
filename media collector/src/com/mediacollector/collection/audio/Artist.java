package com.mediacollector.collection.audio;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * OR-Mapper für 'Artist'.
 * -----------------------
 * Die Artist-Klasse repräsentiert einen Künstler mit allen in der Datenbank 
 * gespeicherten Attributen. Über ein Objekt dieser Klasse können neue Einträge
 * in die Datenbank eingefügt werden, im Gegenzug aber auch über die Id des 
 * Eintrags in der Datenbank die entsprechenden Informationen in das Objekt 
 * eingelesen werden.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Artist {
	
	/**
	 * Enthält alle benötigten Daten für die Objekte.
	 * Diese können über die entsprechenden getter und setter gelesen/gesetzt 
	 * werden.
	 */
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public Artist() {}
	public Artist(final int id) {
		this.data.put("id", id);
		this.getDataFromDb();
	}
	public Artist(final String name) {
		this.data.put("name", name);
		this.getDataFromDb();
	}
	
	/**
	 * Über diese Methode kann eine Menge von Werten gesetzt werden.
	 * Erlaubte (key-)Werte: id, name, imgPath, mbId
	 * @param dataMap HashMap<String, Object> Map mit Key-Value-Paaren.
	 */
	public void setData(final HashMap<String, Object> dataMap) {		
		if (dataMap.containsKey("id")) 
			this.data.put("id", dataMap.get("id"));
		if (dataMap.containsKey("name")) 
			this.data.put("name", dataMap.get("name"));
		if (dataMap.containsKey("imgPath")) 
			this.data.put("imgPath", dataMap.get("imgPath"));
		if (dataMap.containsKey("mbId")) 
			this.data.put("mbId", dataMap.get("mbId"));		
	}
	
	/**
	 * Über diese Methode kann ein einzelner Wert gesetzt werden.
	 * Erlaubte (key-)Werte: id, name, imgPath, mbId
	 * @param key String Der Key.
	 * @param value Der zu setzende Wert.
	 */
	public void setValue(final String key, final Object value) {
		if (key.equals("id") || key.equals("name") || key.equals("imgPath")
				|| key.equals("mbId")) {
			this.data.put(key, value);
		}
	}
	
	/**
	 * Getter.
	 * Liefer die gesamten Daten des Objektes.
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getData() { 
		return this.data;
	}
	
	/**
	 * Getter.
	 * Liefert die ID des Artists in der Datenbank.
	 * @return Integer
	 */
	public int getId() {
		return (Integer) this.data.get("id");
	}
	
	/**
	 * Getter.
	 * Liefert den Namen des Artists.
	 * @return String
	 */
	public String getName() {
		return (String) this.data.get("name");
	}
	
	/**
	 * Getter.
	 * Liefert den Pfad zur Bilddatei zum Artist.
	 * @return String
	 */
	public String getImgPath() {
		return (String) this.data.get("imgPath");
	}
	
	/**
	 * Getter.
	 * Liefert die MusicBrainz-ID des Artists.
	 * @return String
	 */
	public String getMbId() {
		return (String) this.data.get("mbId");
	}
	public void getDataFromDb() {
		/*	if id != null:
		 * 		sqlWhere = "id = '" + id + "';"; 
		 * 	else if name != null:
		 * 		sqlWhere = "name = '" + name + "';";
		 * 	else:
		 * 		throw new MCSQLException("Nicht alle Daten angegeben");
		 *  
		 *  db = Database();
		 *  try:
		 *  	row = db.select("*", "Artist", sqlWhere);
		 *  	id = row[0];
		 *  	name = row[1]
		 *  	// etc...
		 *  catch MCSQLException:
		 *  	throw new MCSQLException("Artist nicht gefunden");
		 */
	}
	public void insertIntoDb() {
		/*
		 * 	if id == null || name == null:
		 * 		throw new MCSQLException("Nicht alle Daten angegeben");
		 * 	else:
		 * 		db = Database();
		 * 		HashMap<String, ?> values = new HashMap<String, ?>();
		 * 		values.put("id", id);
		 * 		values.put("name", name");
		 * 		// etc...
		 * 		try: 
		 * 			db.insert("Artist", values);
		 *		catch MCSQLException:
		 *			throw new MCSQLException("Konnte Daten nicht einfügen");
		 */
	}
	public void deleteFromDb() {
		/*	if id != null:
		 * 		sqlWhere = "id = '" + id + "';"; 
		 * 	else if name != null:
		 * 		sqlWhere = "name = '" + name + "';";
		 * 	else:
		 * 		throw new MCSQLException("Nicht alle Daten angegeben");
		 *  
		 *  db = Database();
		 *  try:
		 *  	row = db.delete("Artist", sqlWhere);
		 *  catch MCSQLException:
		 *  	throw new MCSQLException("Artist nicht gefunden");
		 */
	}
	public ArrayList getAlbums() {
		/*	if id != null:
		 * 		sqlWhere = "artist = '" + id + "';"; 
		 * 	else if name != null:
		 * 		getDataFromDb();
		 * 		sqlWhere = "artist = '" + id + "';"; 
		 * 	else:
		 * 		throw new MCSQLException("Nicht alle Daten angegeben");
		 *  
		 *  db = Database();
		 *  try:
		 *  	rows = db.select("id", "Album", sqlWhere);
		 *  	albumList = new ArrayList<Album>();
		 *  	for row in rows:
		 *  		curAlbum = new Album(row[0]);
		 *  		albumList.add(curAlbum);
		 *  	return albumList
		 *  		
		 *  catch MCSQLException:
		 *  	throw new MCSQLException("Artist nicht gefunden");
		 */
		return null;
	}
	public void getTracks() {}
	public static void getAll() {}

}
