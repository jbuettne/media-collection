package com.mediacollector.collection;

import java.util.HashMap;

import android.graphics.drawable.Drawable;

public class TextImageEntry {
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public TextImageEntry(String text, Drawable image, int year,
			int trackCount) {
		this.data.put("text", text);
		this.data.put("image", image);
		this.data.put("year", String.valueOf(year));
		this.data.put("trackCount", String.valueOf(trackCount));
	}
	
	public HashMap<String, Object>getMap() {
		return data;
	}
	
	public String getText() {
		return (String) this.data.get("text");
	}
	
	public Drawable getImage() {
		return (Drawable) this.data.get("image");
	}
	
	public String getYear() {
		return (String) this.data.get("year");
	}
	
	public String getTrackCount() {
		return (String) this.data.get("trackCount");
	}

}
