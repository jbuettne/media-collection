package com.mediacollector.fetching;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mediacollector.tools.Observable;

import android.os.Looper;
import android.util.Log;

/**
 * 
 * @author Philipp Dermitzel
 */
public class Imdb extends Observable implements Runnable {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/
	
	private static final String BASE_URI_WEB = "http://ajax.googleapis.com/"
			+ "ajax/services/search/web?v=2.0&q=";
	private static final String SEARCH_ADD = "\"- IMDb\" site:imdb.com/title/";	
	private static final Pattern PATTERN_WEB = Pattern.compile(",\"url\":\""
			+ "http:\\/\\/www.imdb.com\\/title\\/tt([0-9]+)");
	
	private static final String BASE_URI_IMDB = "http://www.imdb.com/title/tt";
	private static final Pattern PATTERN_IMDB = Pattern.compile("<title>" 
			+ "([^\\(]+) \\(([0-9]{4})\\) - IMDb</title>");
	
	private static String imdbURL = null;
	private static String title = null;
	
	public static int year = -1;
	public static String correctTitle = null;
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	public Imdb(final String title) {
		Imdb.title = title;
	}

	public void run() {
		Looper.prepare();
		try {
			this.getWebData();
		} catch (IOException e) {
			Log.e("HAYAFEHLER", "FEHLER: " + e);
		}
		Looper.loop();
	}
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * @throws IOException 
	 */
	private void getWebData() 
	throws IOException {
		String webContent = WebParsing.getWebContent(BASE_URI_WEB + 
				URLEncoder.encode("\"" + title + "\"" + SEARCH_ADD));
		Matcher matcher = PATTERN_WEB.matcher(webContent);
		if (matcher.find())
			imdbURL =  BASE_URI_IMDB + matcher.group(1) + "/";
		this.getIMDbData();
	}
	
	private void getIMDbData() throws IOException {
		String imdbContent = WebParsing.getWebContent(imdbURL);
		Matcher imdbMatcher = PATTERN_IMDB.matcher(imdbContent);
		if (imdbMatcher.find()) {
			correctTitle = imdbMatcher.group(1);
			year = new Integer(imdbMatcher.group(2));
			notifyObserver();
		}
	}

}
