package com.mediacollector.fetching;

import android.content.Context;

import com.mediacollector.R;
import com.mediacollector.fetching.fetcher.ImagesGoogle;
import com.mediacollector.fetching.fetcher.Amazon;
import com.mediacollector.tools.Observable;
import com.mediacollector.tools.Observer;
import com.mediacollector.tools.Exceptions.MCFetchingException;

/**
 * Data-Fetching-Factory. Diese Klasse definiert die vorhandenen Search-Enginges
 * und startet den Vorgang des Fetchings. Diese Klasse übernimmt auch das 
 * Callback-Handling mittels der updateObserver-Methode.
 * @author Philipp Dermitzel
 */
public class Fetching extends Observable implements Observer {
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Google Product Search;
	 * http://www.google.com/m/products  -- Google.java
	 */
	public static final int SEARCH_INDEX_MUSIC 			= 0;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier OFDb;
	 * http://www.ofdb.de  -- OFDb.java
	 */
	public static final int SEARCH_INDEX_VIDEO 			= 1;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Thalia;
	 * http://www.thalia.de  -- Thalia.java
	 */
	public static final int SEARCH_INDEX_BOOKS 			= 2;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Thalia - Audio only;
	 * http://www.thalia.de  -- Thalia.java
	 */
	public static final int SEARCH_INDEX_GAMES 			= 3;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Thalia - Bücher only;
	 * http://www.thalia.de  -- Thalia.java
	 */
	public static final int SEARCH_INDEX_VIDEOGAMES 	= 31;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Thalia - Bücher only;
	 * http://www.thalia.de  -- Thalia.java
	 */
	public static final int SEARCH_INDEX_ALL 	= 4;
	
	/**
	 * Der Context, aus welchem das Fetching aufgerufen wurde. Wird nur für das
	 * Callback-Handling benötigt.
	 */
	private Context context = null;
	
	/**
	 * Die zu nutzenden Search-Engine. Wird über den Kosntruktor gesetzt.
	 */
	private int searchEngine = -1;
	
	/**
	 * Die EAN, zu welcher die Daten eingeholt werden.
	 */
	private String ean = null;
	
	/**
	 * Der arbeitenden Fetcher. Welche Klasse genutzt wird, wird mittels des
	 * Konstruktors definiert.
	 */
	private DataFetcher fetcher = null;
	
	/**
	 * Der Image-Fetcher. Holt ein zum Video passendes Cover.
	 */
	private ImageFetcher imgFetcher = null;
	
	/**
	 * Ein Counter, der sicherstellt, dass beide Fetcher gelaufen sind, bevor
	 * die Callback-Aktion ausgeführt wird.
	 */
	private int fetcherCounter = 0;
	
	public DataFetcher getDataFetcher() {
		return this.fetcher;
	}
	
	public ImageFetcher getImageFetcher() {
		return this.imgFetcher;
	}
	
	/**
	 * Setzt den Standard-Fetcher (Google Product Search).
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 * @throws MCFetchingException 
	 */
	public Fetching(final Context context, final String ean) {
		this(context, ean, SEARCH_INDEX_ALL);
	}
	
	/**
	 * Der komplette Konstruktor. 
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 * @param searchEngine Die Konstante beschreibt die verschiedenen Fetching-
	 * 	Dienste und somit -Klassen.
	 */
	public Fetching(final Context context, final String ean, 
			final int searchEngine) {
		this.ean = ean;
		this.context = context;
		this.searchEngine = searchEngine;
	}
	
	/**
	 * Die Methode, welche das Fetching mit dem ausgewählten Fetching-Dienst 
	 * endgültig startet.
	 * @throws MCFetchingException .
	 */
	public void fetchData() 
	throws MCFetchingException {
		switch (searchEngine) {
		case SEARCH_INDEX_MUSIC:
			this.fetcher = new Amazon(context, this.ean, SEARCH_INDEX_MUSIC);
			break;
		case SEARCH_INDEX_VIDEO:
			this.fetcher = new Amazon(context, this.ean, SEARCH_INDEX_VIDEO);
			break;		
		case SEARCH_INDEX_BOOKS:
			this.fetcher = new Amazon(context, this.ean, SEARCH_INDEX_BOOKS);
			break;
		case SEARCH_INDEX_GAMES:
			this.fetcher = new Amazon(context, this.ean, SEARCH_INDEX_GAMES); 
			break;
		case SEARCH_INDEX_ALL:
			this.fetcher = new Amazon(context, this.ean, SEARCH_INDEX_ALL); 
			break;
		default: 
			throw new MCFetchingException(this.context.getString(
					R.string.EXCEPTION_Fetching_2_1) + " '" + this.searchEngine 
					+ "' " + this.context.getString(
					R.string.EXCEPTION_Fetching_2_2));
		}
		this.fetcher.addObserver(this);
        new Thread(this.fetcher).start();
        
        this.imgFetcher = new ImagesGoogle(context, this.ean);
        this.imgFetcher.addObserver(this);
        new Thread(this.imgFetcher).start();
	}
	
	/**
	 * Die Callback-Methode. 
	 * Sie wird nach dem erfolgreichen Holen der Daten aufgerufen und kann das
	 * weitere Vorgehen definieren.
	 * Im Augenblick wird bei erfolgreichem Holen von Daten und Cover der 
	 * eingesetzte Observer benachrichtigt, um in der entsprechenden Activity
	 * eben diese darzustellen. 
	 */
	public void updateObserver(boolean statusOkay) {
		this.fetcherCounter = this.fetcherCounter + 1;
		if (this.fetcherCounter == 2) this.notifyObserver(statusOkay);
	}

}
