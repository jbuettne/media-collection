package com.mediacollector.collection.audio;

public class Album {

	public long id;
  
	public String name;

	public String artist;

	public long year;

	public String imgPath;

	public String mbId;

	public boolean istNeu() {
		return id == 0;
	}
	
	public long getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public long getYear(){
		return year;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public String getMbId() {
		return mbId;
	}
	
	public Album(String name, String artist, long year, 
			String imgPath, String mbId){
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.imgPath = imgPath;
		this.mbId = mbId;
	}
	public Album(){
		
	}
}

