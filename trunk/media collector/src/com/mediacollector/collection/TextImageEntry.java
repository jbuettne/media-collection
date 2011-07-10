package com.mediacollector.collection;

import java.util.HashMap;

public class TextImageEntry {
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public TextImageEntry(String id, String text, String image, String year) {
		this.data.put("id", id);
		this.data.put("text", text);
		this.data.put("image", image);
		this.data.put("year", year);
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
	
	public String getTrackCount() {
		return (String) this.data.get("trackCount");
	}

}
