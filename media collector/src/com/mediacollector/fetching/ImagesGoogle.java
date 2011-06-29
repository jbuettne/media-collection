package com.mediacollector.fetching;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Image-Fetcher, welcher Daten von Googles Bildersuche einholt. 
 * Siehe auch: DataFetcher.java
 * @author Philipp Dermitzel
 */
public class ImagesGoogle extends ImageFetcher {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/

	/**
	 * Die URL zu Googles Image-Search API-Seite. Die EAN wird an das Ende 
	 * dieser URL angehängt.
	 */
	private static final String BASE_URI = "https://ajax.googleapis.com/ajax/"
		+ "services/search/images?v=1.0&q=";
	
	/**
	 * Das anhängen des Suchbegriffes "cover" an die EAN erhöht die 
	 * Wahrscheinlichkeit, Cover und keine anderen, eintragsrelevanten Bilder 
	 * zu finden.
	 */
	private static final String BASE_URI_2 = "+cover";
	
	/**
	 * Das Regex-Pattern, mittels welchem die URL zum Bild/Cover gefunden und
	 * gespeichert werden kann.
	 */
	private static final Pattern PATTERN = Pattern.compile(
			"\\\"unescapedUrl\\\":\\\"([^\\\"]+)\\\"");
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public ImagesGoogle(String ean) {
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
		String encProductID = URLEncoder.encode(ean, "UTF-8");
		String completeURI	= BASE_URI + encProductID + BASE_URI_2;
		String webContent 	= WebParsing.getWebContent(completeURI);
		Matcher	matcher 	= PATTERN.matcher(webContent);
		if (matcher.find()) {
			this.set(COVER_STRING, matcher.group(1));
			this.getImage();
			notifyObserver(true);
		} else notifyObserver(false);
	}
}