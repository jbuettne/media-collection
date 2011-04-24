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
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public void setData(final HashMap<String, Object> dataMap) {}
	public void setValue(final String key, final Object value) {}
	public HashMap<String, Object> getData() { 
		return this.data;
	}
	public int getId() {
		return (Integer) this.data.get("id");
	}
	public String getName() {
		return (String) this.data.get("name");
	}
	public String getImgPath() {
		return (String) this.data.get("imgPath");
	}
	public String getMbId() {
		return (String) this.data.get("mbId");
	}
	public void getDataFromDb() {}
	public void insertIntoDb() {}
	public void deleteFromDb() {}
	public void getAlbums() {}
	public void getTracks() {}
	public void getAll() {}

}
