package com.mediacollector.collection.audio;

import java.util.HashMap;

/**
 * OR-Mapper für 'Track'.
 * -----------------------
 * Die Track-Klasse repräsentiert einen Track mit allen in der Datenbank 
 * gespeicherten Attributen. Über ein Objekt dieser Klasse können neue Einträge
 * in die Datenbank eingefügt werden, im Gegenzug aber auch über die Id des 
 * Eintrags in der Datenbank die entsprechenden Informationen in das Objekt 
 * eingelesen werden.
 * @author Philipp Dermitzel
 * @version 0.1
 */
public class Track {
	
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
	public int getArtist() {
		return (Integer) this.data.get("artist");
	}
	public int getCd() {
		return (Integer) this.data.get("cd");
	}
	public int getTrackOnCd() {
		return (Integer) this.data.get("trackOnCd");
	}
	public int getLength() {
		return (Integer) this.data.get("length");
	}
	public void getDataFromDb() {}
	public void insertIntoDb() {}
	public void deleteFromDb() {}

}
