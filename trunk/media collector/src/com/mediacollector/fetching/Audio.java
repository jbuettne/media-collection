package com.mediacollector.fetching;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


//import com.mediacollector.fetching.audio.BaseParser;

public class Audio {
	
	/**
	 * Der Barcode. Wird durch den Konstruktor gesetzt und darauf basierend die
	 * Daten von MusicBrainz und lastfm eingeholt.
	 */
	private String barcode;
	
	/**
	 * Die Id des Artists.
	 */
	private String artist_id;
	
	/**
	 * Der Name des Artists.
	 */
	private String artist;
	
	/**
	 * Der Name des Albums/Releases.
	 */
	private String title;
	
	/**
	 * Der Type des Albums/Releases.
	 */
	private String type;
	
	/**
	 * Das Erscheinungsjahr des Albums/Releases.
	 */
	private int year;
	
	/**
	 * Das Format vom Album/Release (CD,LM,MP,...).
	 */
	private int format;
	
	/**
	 * Die URL zum Cover zum Album/Release.
	 */
	private String	 cover_url;
	
	/*
	 * -------------------------------------------------------------------------
	 * GETTER- UND SETTER
	 * -------------------------------------------------------------------------
	 */
	
	public void setBarcode(String mediaBarcode) { this.barcode = mediaBarcode; }
	public String getBarcode() { return barcode; }
	
	public void setArtist(String artist) { this.artist = artist; }
	public String getArtist() { return artist; }
	
	public String getArtist_id() {return artist_id;}
	public void setArtist_id(String artistId) {artist_id = artistId;}

	public void setTitle(String title) { this.title = title; }
	public String getTitle() { return title; }
	
	public void setType(String type) { this.type = type; }
	public String getType() { return type; }

	public void setYear(int i) { this.year = i; }
	public int getYear() { return year; }

	public void setFormat(int format) { this.format = format; }
	public int getFormat() { return format; }
	
	public void setCoverUrl(String cover_url) { this.cover_url = cover_url; }
	public String getCoverUrl() { return cover_url; }
	
	/*
	 * -------------------------------------------------------------------------
	 * METHODEN DER KLASSE
	 * -------------------------------------------------------------------------
	 */
	
	/**
	 * Der Konstruktor.
	 * @param: int paramBarcode: Der Barcode zu dem Album/Release, zu dem 
	 * Informationen eingeholt werden sollen.
	 * @return 
	 */
	
	/*public Audio(final int paramBarcode) {
		this.barcode = paramBarcode;
		this.searchAudio();
	}*/
	
    // getters and setters omitted for brevity
    	
	//@SuppressWarnings("unused")
	
	
	
	public void searchAudio() {
		
		try {
			
			List<Audio> audiolist = new ArrayList<Audio>();
			
			URL url = new URL("http://musicbrainz.org/ws/1/release/?type=xml&query=barcode:" + this.barcode);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("release");
			
			//setArtist(String.valueOf(nodeList.getLength()));
			
			for (int i = 0; i < nodeList.getLength(); i++) {
			
				Audio audio = new Audio();
				
				Node node = nodeList.item(i);
				
				Element fstElmnt = (Element) node;
				
				NodeList releaseList = fstElmnt.getElementsByTagName("title");
				Element releaseElement = (Element) releaseList.item(0);
				releaseList = releaseElement.getChildNodes();
				audio.setTitle(releaseList.item(0).getNodeValue());
			
				NodeList artistList = fstElmnt.getElementsByTagName("artist");
				Element artistElement = (Element) artistList.item(0);
				artistList = artistElement.getChildNodes();
				audio.setArtist_id(artistElement.getAttribute("id"));
				
				NodeList artistNameList = fstElmnt.getElementsByTagName("name");
				Element artistNameElement = (Element) artistNameList.item(0);
				artistNameList = artistNameElement.getChildNodes();
				audio.setArtist(artistNameList.item(0).getNodeValue());
				
				NodeList eventList = fstElmnt.getElementsByTagName("event");
				Element eventElement = (Element) eventList.item(0);
				eventList = eventElement.getChildNodes();
				audio.setYear(this.changeDate(eventElement.getAttribute("date")));
								
				audiolist.add(audio);

			}
			
			Audio selectone = audiolist.get(0);

			setArtist(selectone.getArtist());
			setTitle(selectone.getTitle());
			setYear(selectone.getYear());
			setArtist_id(selectone.getArtist_id());
			
			setCoverUrl(searchForCoverImage(selectone));
			
		} catch (Exception e) {
			System.out.println("XML Pasing Excpetion = " + e);
		}

	}
	private int changeDate(String date){
	    Pattern datePattern = Pattern.compile("\\d{4}");
	    Matcher dateMatcher = datePattern.matcher(date);
	    dateMatcher.find();
	    int newDate = Integer.valueOf(dateMatcher.group(0));
		return newDate;
	}
	
	//@SuppressWarnings("unused")
	private String searchForCoverImage(Audio audio) {
		
		String cover_url = null;
		
		try {
			List<Audio> audiolist = new ArrayList<Audio>();

			String api_url = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo";
			String api_key = "&api_key=b25b959554ed76058ac220b7b2e0a026";
			String artist = "&artist=" + URLEncoder.encode(this.artist);
			String title = "&album=" + URLEncoder.encode(this.title);
			
			URL url = new URL( api_url + api_key + artist + title );

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("lfm");
			
			//nodes = String.valueOf(nodeList.getLength());
			
			for (int i = 0; i < nodeList.getLength(); i++) {
			
				Audio cover = new Audio();
				
				//cover.setCoverUrl(null);
				
				Node node = nodeList.item(i);
				
				Element fstElmnt = (Element) node;
				
				NodeList coverList = fstElmnt.getElementsByTagName("image");				
				Element coverElement = (Element) coverList.item(2);
				coverList = coverElement.getChildNodes();
				cover.setCoverUrl(coverList.item(0).getNodeValue());
			
				audiolist.add(cover);

			}
			
			Audio selectone = audiolist.get(0);
			
			cover_url = selectone.getCoverUrl();

			
		} catch (Exception ex) {
			// Fehler
			cover_url = "fehler";
		}
		
		return cover_url;
	}
	
	public String toString() {
		return this.title + " " + this.artist;
	}

}
