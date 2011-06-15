package com.mediacollector.collection.audio;

public class Track {

	public long id;
  
	public String name;

	public String artist;

	public long cd;
	
	public long trackOnCd;

	public long length;

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
	
	public long getCd(){
		return cd;
	}
	
	public long getTrackOnCd() {
		return trackOnCd;
	}
	
	public long getLength() {
		return length;
	}
	
	public String getMbId() {
		return mbId;
	}
	
	public Track(String name, String artist, long cd, 
			long trackOnCd, long length, String mbId){
		this.name = name;
		this.artist = artist;
		this.cd = cd;
		this.trackOnCd = trackOnCd;
		this.length = length;
		this.mbId = mbId;
	}
	public Track(){
		
	}
}

