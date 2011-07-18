package com.mediacollector.fetching.fetcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.WebParsing;

/**
 * Data-Fetcher, welcher Daten von tagtoad.com einholt. Siehe auch: DataFetcher.java.
 * @author David Pollehn
 */
public class Kaufkauf extends DataFetcher {
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/

	/**
	 * Die Grund-URL, über welche das Spiel - mittels des Barcodes - gesucht
	 * wird.
	 */
	private static final String BASE_URI = "http://openean.kaufkauf.net" +
			"/index.php?cmd=ean1&sid=&ean=";

	/**
	 * Das Pattern zum Suchen des Titels des Spiel-Datenträgers.
	 */
	private static final Pattern PATTERN_TITLE = Pattern
			.compile("<INPUT TYPE=HIDDEN NAME=\"fullname\" VALUE=\"([^\"]+)");

	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public Kaufkauf(final Context context, final String ean) {
		super(context, ean);
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
		Log.i("MediaCollector", "URL to parse: " + completeURI);
		String webContent	= WebParsing.getWebContent(completeURI);
		Matcher	matcher	= PATTERN_TITLE.matcher(webContent);
		if (matcher.find()) {
			this.set(TITLE_STRING, matcher.group(1));
			this.set(TITLE_ID_STRING, this.ean);
			this.set(ARTIST_STRING, "Spiel");
			this.set(YEAR_STRING, "");
			notifyObserver(true);
		} else notifyObserver(false);
	}

}