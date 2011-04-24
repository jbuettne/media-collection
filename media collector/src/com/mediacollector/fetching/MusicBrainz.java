package com.mediacollector.fetching;

public class MusicBrainz {
	
	/**
	 * Der Barcode. Wird durch den Konstruktor gesetzt und darauf basierend die
	 * Daten von MusicBrainz eingeholt.
	 */
	private int 	barcode;
	
	/**
	 * Der Name des Artists.
	 */
	private String 	artist;
	
	/**
	 * Der Name des Albums/Releases.
	 */
	private String	title;
	
	/**
	 * Das Erscheinungsjahr des Albums/Releases.
	 */
	private int		year;
	
	/**
	 * Die URL zum Cover zum Album/Release.
	 */
	private String	cover;
	
	/*
	 * -------------------------------------------------------------------------
	 * GETTER- UND SETTER
	 * -------------------------------------------------------------------------
	 */
	
	public void setBarcode(int mediaBarcode) { this.barcode = mediaBarcode; }
	public int getBarcode() { return barcode; }
	
	public void setArtist(String artist) { this.artist = artist; }
	public String getArtist() { return artist; }

	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return title; }

	public void setYear(int year) { this.year = year; }
	public int getYear() { return year; }

	public void setCover(String cover) { this.cover = cover; }
	public String getCover() { return cover; }
	
	/*
	 * -------------------------------------------------------------------------
	 * METHODEN DER KLASSE
	 * -------------------------------------------------------------------------
	 */
	
	/**
	 * Der Konstruktor.
	 * @param: int paramBarcode: Der Barcode zu dem Album/Release, zu dem 
	 * Informationen eingeholt werden sollen.
	 */
	public MusicBrainz(final int paramBarcode) {
		this.barcode = paramBarcode;
	}
	
	@SuppressWarnings("unused")
	private void searchMusicBrainz() {
		try {
			// http://musicbrainz.org/ws/1/release/?type=xml&query=barcode:1234
		} catch (Exception ex) {
			// Fehler
		}
	}
	
	@SuppressWarnings("unused")
	private void searchForCoverImage() {
		try {
			// last.fm?
		} catch (Exception ex) {
			// Fehler
		}
	}

}
