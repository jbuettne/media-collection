package com.mediacollector.collection;

import java.util.HashMap;

import android.graphics.drawable.Drawable;

public class TextImageEntry {
	
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	public TextImageEntry(String text, Drawable image) {
		this.data.put("text", text);
		this.data.put("image", image);
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

}
