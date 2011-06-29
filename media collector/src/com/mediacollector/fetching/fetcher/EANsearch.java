package com.mediacollector.fetching.fetcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.WebParsing;

/**
 * Data-Fetcher, welcher Daten von ean-search.org einholt. Siehe auch: DataFetcher.java.
 * @author David Pollehn
 */
public class EANsearch extends DataFetcher {
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/

	/**
	 * Die Grund-URL, über welche das Spiel - mittels des Barcodes - gesucht wird.
	 */
	private static final String BASE_URI = "http://www.ean-search.org/perl/ean-search.pl?q=";
	//http://www.ean-search.org/perl/ean-search.pl?q=5030930048365
	/**
	 * Das Pattern zum Suchen des Titels des Spiel-Datenträgers.
	 */
	private static final Pattern PATTERN_TITLE = Pattern.compile("/perl/ean-info.pl[^>]*>([^<]+)");
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public EANsearch(String ean) {
		super(ean);
	}	
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * Die Fetching-Methode. Siehe auch in DataFetcher.java
	 */
	protected void getData() throws IOException {
		String encPart		= URLEncoder.encode(this.ean, "UTF-8");
		String completeURI	= BASE_URI + encPart;
		//Log.i("Media Collector", "URL to parse: " + completeURI);
		String webContent	= WebParsing.getWebContent(completeURI);
		//webContent = "12";
		//this.set(TITLE_STRING, String.valueOf(webContent.length()));
		//this.set(TITLE_STRING, completeURI);
		//notifyObserver(true);
		Log.e("w", webContent);
		Matcher	matcher	= PATTERN_TITLE.matcher(webContent);
		if (matcher.find()) {
			this.set(TITLE_STRING, matcher.group(1));
			notifyObserver(true);
		} else notifyObserver(false);
	}

}