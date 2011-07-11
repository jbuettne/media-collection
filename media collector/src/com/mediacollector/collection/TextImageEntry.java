package com.mediacollector.collection;

import java.util.HashMap;

/**
 * Klasse zur Erstellung von TextImageEntry-Objekten.
 * Diese werden für die Anzeige der Einträge in den ListViews und 
 * ExpandableListViews verwendet.
 * 
 * @author Jens Buettner
 * @version 0.1
 */
public class TextImageEntry {
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public TextImageEntry(String id, String text, String year, String image, 
			String table, String extra) {
		this.data.put("id", id);
		this.data.put("text", text);
		this.data.put("image", image);
		this.data.put("year", year);
		this.data.put("table", table);
		this.data.put("extra", extra);
		//this.data.put("trackCount", String.valueOf(trackCount));
	}
	
	public HashMap<String, Object>getMap() {
		return data;
	}
	
	public String getId() {
		return (String) this.data.get("id");
	}
	
	public String getText() {
		return (String) this.data.get("text");
	}
	
	public String getImage() {
		return (String) this.data.get("image");
	}
	
	public String getYear() {
		return (String) this.data.get("year");
	}
	
	public String getTable() {
		return (String) this.data.get("table");
	}
	
	public String getExtra() {
		return (String) this.data.get("extra");
	}

}
