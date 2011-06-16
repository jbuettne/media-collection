package com.mediacollector.collection.audio;

public class Track {

	public String mbId;
  
	public String name;

	public String artist;

	public long cd;
	
	public long trackOnCd;

	public long length;
	
	public Track(String mbId, String name, String artist, long cd, 
			long trackOnCd, long length){
		this.mbId = mbId;
		this.name = name;
		this.artist = artist;
		this.cd = cd;
		this.trackOnCd = trackOnCd;
		this.length = length;
	}
	public Track(){
		
	}
}

