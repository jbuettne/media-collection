package com.mediacollector.fetching;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mediacollector.tools.Observable;

import android.os.Looper;
import android.util.Log;

/**
 * Data-Fetcher, welcher Daten von OFDb einholt. Siehe auch: DataFetcher.java
 * und VideoDataFetching.java.
 * @author Philipp Dermitzel
 */
public class OFDb extends DataFetcher {
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/

	/**
	 * Die Grund-URL, über welche der Film - mittels der EAN - gesucht wird.
	 */
	private static final String BASE_URI_1 = "http://www.ofdb.de/view.php?"
			+ "page=suchergebnis&Kat=EAN&SText=";
	
	/**
	 * Die Grund-URL des Filmes. Über sie werden der korrekte Titel und das
	 * Erscheinungsjahr geholt.
	 */
	private static final String BASE_URI_2 = "http://www.ofdb.de/film/";

	/**
	 * Das Pattern zum Suchen des Links zum Film.
	 */
	private static final Pattern PATTERN = Pattern.compile(
			"<a href=\\\"film/([0-9]+,[^\\\"]+)\\\""); 
	
	/**
	 * Das Pattern zum Suchen des (deutschen) Titels des Filmes.
	 */
	private static final Pattern PATTERN_DT = Pattern.compile(
			"<h2><font face[^>]+><b>([^<]+)");
	
	/**
	 * Das Pattern zum Suchen des Erscheinungsjahres des  Filmes.
	 */
	private static final Pattern PATTERN_YEAR = Pattern.compile(
			"view\\.php\\?page=blaettern&Kat=Jahr&Text=([0-9]{4})");	
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public OFDb(String ean) {
		super(ean);
	}	

	/**
	 * Die Fetching-Methode. Siehe auch in DataFetcher.java
	 */
	protected void getData() 
	throws IOException {
		String encProductID	= URLEncoder.encode(ean, "UTF-8");
		String completeURI	= BASE_URI_1 + encProductID;
		String webContent	= WebParsing.getWebContent(completeURI);
		Matcher	matcher		= PATTERN.matcher(webContent);
		if (matcher.find()) 
			this.getMovieData(matcher.group(1));
	}
	
	/**
	 * Die zweite Fetching-Methode. Sie holt die eigentlichen Film-Daten.
	 * @param urlPart Die an BASE_URI_2 angehängte und durch getData() 
	 * 	ermittelte Film-ID.
	 * @throws IOException .
	 */
	private void getMovieData(final String urlPart) 
	throws IOException {
		String encPart		= URLEncoder.encode(urlPart, "UTF-8");
		String completeURI	= BASE_URI_2 + encPart;
		String webContent	= WebParsing.getWebContent(completeURI);
		Matcher	matcher_t	= PATTERN_DT.matcher(webContent);
		Matcher matcher_y	= PATTERN_YEAR.matcher(webContent);
		if (matcher_t.find() && matcher_y.find()) {
			this.set("title", matcher_t.group(1));
			this.set("year", matcher_y.group(1));
			notifyObserver();
		}
	}

}