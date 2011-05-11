package com.mediacollector.collection.audio;

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
	public void getDataFromDb() {}
	public void insertIntoDb() {}
	public void deleteFromDb() {}
	public void getAlbums() {}
	public void getTracks() {}
	public static void getAll() {}

}
