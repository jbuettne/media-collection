package com.mediacollector.fetching.Audio;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.WebParsing;

/**
 * Data-Fetcher, welcher Daten von Thalia einholt. Siehe auch: DataFetcher.java
 * und VideoDataFetching.java.
 * @author Philipp Dermitzel
 */
public class Thalia extends DataFetcher {
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/

	/**
	 * Die Grund-URL, über welche der Film - mittels der EAN - gesucht wird.
	 */
	private static final String BASE_URI = "http://www.thalia.de/shop/tha_"
			+ "homestartseite/suche/?sswg=MUSIK&sq=";
	
	/**
	 * Anhang an die URL nach der EAN.
	 */
	private static final String BASE_URI_2 = "&submit.x=0&submit.y=0";

	/**
	 * Das Pattern zum Suchen des Titels des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_TITLE = Pattern.compile("https://ssl."
			+ "buch.de/pagstract/textimage/\\?s=artikel-titel&t=([^&]+)"); 
	
	/**
	 * Das Pattern zum Suchen des Artists des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_ARTIST = Pattern.compile("https://ssl."
			+ "buch.de/pagstract/textimage/\\?s=artikel-person-details&t=von"
			+ "\\+([^&]+)");
	
	/**
	 * Das Pattern zum Suchen des Erscheinungsjahres Audio-Datenträgers. Findet
	 * sowohl Daten der Form tt.mm.yyyy als auch yyyy.
	 */
	private static final Pattern PATTERN_YEAR = Pattern.compile("<li><strong>"
			+ "Erschienen:</strong> [0-9]{0,2}\\.*[0-9]{0,2}\\.*([0-9]{4})");	
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public Thalia(String ean) {
		super(ean);
	}	
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * Die Fetching-Methode. Siehe auch in DataFetcher.java
	 */
	protected void getData() 
	throws IOException {
		String encPart		= URLEncoder.encode(this.ean, "UTF-8");
		String completeURI	= BASE_URI + encPart + BASE_URI_2;
		String webContent	= WebParsing.getWebContent(completeURI);
		Matcher	matcher_t	= PATTERN_TITLE.matcher(webContent);
		Matcher	matcher_a	= PATTERN_ARTIST.matcher(webContent);
		Matcher matcher_y	= PATTERN_YEAR.matcher(webContent);
		if (matcher_t.find() && matcher_y.find() && matcher_a.find()) {
			this.set(TITLE_STRING, matcher_t.group(1));
			this.set(ARTIST_STRING, matcher_a.group(1));
			this.set(YEAR_STRING, matcher_y.group(1));
			notifyObserver(true);
		} else notifyObserver(false);
	}

}