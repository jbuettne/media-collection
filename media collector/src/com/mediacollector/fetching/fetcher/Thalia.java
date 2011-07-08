package com.mediacollector.fetching.fetcher;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.Toast;

import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.fetching.WebParsing;

/**
 * Data-Fetcher, welcher Daten von Thalia einholt. Siehe auch: DataFetcher.java
 * und VideoDataFetching.java.
 * @author Philipp Dermitzel
 */
public class Thalia extends DataFetcher {
	
	public static final int BOOKS_ONLY = 1;
	public static final int AUDIO_ONLY = 2;
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/

	/**
	 * Die Grund-URL, über welche der Film - mittels der EAN - gesucht wird.
	 */
	private static final String BASE_URI_1 = "http://www.thalia.de/shop/tha_"
			+ "homestartseite/suche/?sswg=";	
	private static final String BASE_URI_20 = "ANY";
	private static final String BASE_URI_21 = "BUCH";
	private static final String BASE_URI_22 = "MUSIK";
	private static final String BASE_URI_3 = "&sq=";
	private static final String BASE_URI_4 = "&submit.x=0&submit.y=0";

	/**
	 * Das Pattern zum Suchen des Titels des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_TITLE = Pattern.compile("https://ssl."
			+ "buch.de/pagstract/textimage/\\?s=artikel-titel&t=([^&]+)"
			+ "\\&c=([^&]+)"); 
	
	/**
	 * Das Pattern zum Suchen des Artists des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_ARTIST = Pattern.compile("https://ssl."
			+ "buch.de/pagstract/textimage/\\?s=artikel-person-details&t=von"
			+ "\\+([^&]+)\\&c=([^&]+)");
	/**
	 * Alternatives Pattern zum Suchen des Artists des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_ARTIST_ALT_KOM = Pattern.compile(
			"<strong>Komponist: </strong>([^<]+)");
	
	/**
	 * Alternatives Pattern zum Suchen des Artists des Audio-Datenträgers.
	 */
	private static final Pattern PATTERN_ARTIST_ALT_SOL = Pattern.compile(
			"<strong>Solist:</strong>([^<]+)");
	
	/**
	 * Das Pattern zum Suchen des Erscheinungsjahres Audio-Datenträgers. Findet
	 * sowohl Daten der Form tt.mm.yyyy als auch yyyy.
	 */
	private static final Pattern PATTERN_YEAR = Pattern.compile("<li><strong>"
			+ "Erschienen:</strong>.+([0-9]{4})");	
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public Thalia(final Context context, final String ean) {
		super(context, ean);
	}
	
	public Thalia(final Context context, final String ean, final int search) {
		super(context, ean, search);
	}
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * Die Fetching-Methode. Siehe auch in DataFetcher.java
	 */
	protected void getData() 
	throws IOException {
		String encPart 		= URLEncoder.encode(this.ean, "UTF-8");
		String completeURI	= null;
		switch (this.search) {
		case BOOKS_ONLY:
			completeURI = BASE_URI_1 + BASE_URI_21 + BASE_URI_3 + encPart 
				+ BASE_URI_4; 
			break;
		case AUDIO_ONLY:
			completeURI = BASE_URI_1 + BASE_URI_22 + BASE_URI_3 + encPart 
				+ BASE_URI_4;
			break;
		default:
			completeURI = BASE_URI_1 + BASE_URI_20 + BASE_URI_3 + encPart 
				+ BASE_URI_4;
			break;
		}
		String webContent	= WebParsing.getWebContent(completeURI);
		Matcher	matcher_t	= PATTERN_TITLE.matcher(webContent);
		Matcher	matcher_a	= PATTERN_ARTIST.matcher(webContent);
		Matcher	matcher_ak	= PATTERN_ARTIST_ALT_KOM.matcher(webContent);
		Matcher	matcher_as	= PATTERN_ARTIST_ALT_SOL.matcher(webContent);
		Matcher matcher_y	= PATTERN_YEAR.matcher(webContent);
		if (matcher_t.find() && matcher_y.find()) {
			String artist = null;
			if (matcher_ak.find()) 
				artist = URLDecoder.decode(matcher_ak.group(1));
			else if (matcher_as.find())
				artist = URLDecoder.decode(matcher_as.group(1));
			else if (matcher_a.find()) 
				artist = URLDecoder.decode(matcher_a.group(1));
			else artist = "Unknown Artist";
			
			this.set(TITLE_STRING, URLDecoder.decode(matcher_t.group(1)));
			this.set(TITLE_ID_STRING, URLDecoder.decode(matcher_t.group(2)));
			//this.set(ARTIST_ID_STRING, URLDecoder.decode(matcher_a.group(2))); Diese Zeile macht Probleme! No succesful match so far ! u.A. getestet mit Barcode 5099749423862
			this.set(ARTIST_ID_STRING, this.ean);
			this.set(ARTIST_STRING, artist); // Ist teilweise immer noch nicht korrekt: z.B. Die Fantastischen Vier, Die Fantastischen Vier
			this.set(YEAR_STRING, URLDecoder.decode(matcher_y.group(1)));
			notifyObserver(true);
		} else notifyObserver(false);
	}

}