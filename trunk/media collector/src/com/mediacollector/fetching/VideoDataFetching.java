package com.mediacollector.fetching;

import android.content.Context;
import android.widget.Toast;

import com.mediacollector.tools.Observer;

/**
 * Data-Fetching-Factory für Videos. Diese Klasse definiert die vorhandenen 
 * Search-Enginges und startet den Vorgang des Fetchings. Diese Klasse übernimmt
 * auch das Callback-Handling mittels der updateObserver-Methode.
 * @author Philipp Dermitzel
 */
public class VideoDataFetching implements Observer {
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier OFDb;
	 * http://www.ofdb.de  -- OFDb.java
	 */
	public static final int SEARCH_ENGINE_OFDB 		= 1;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Google Product Search;
	 * http://www.google.com/m/products  -- Google.java
	 */
	public static final int SEARCH_ENGINE_GOOGLE 	= 3;
	
	/**
	 * Der Context, aus welchem das Fetching aufgerufen wurde. Wird nur für das
	 * Callback-Handling benötigt.
	 */
	private Context context = null;
	
	/**
	 * Der arbeitenden Fetcher. Welche Klasse genutzt wird, wird mittels des
	 * Konstruktors definiert.
	 */
	private DataFetcher fetcher = null;
	
	/**
	 * Der Image-Fetcher. Holt ein zum Video passendes Cover.
	 */
	private DataFetcher imgFetcher = null;
	
	/**
	 * Ein Counter, der sicherstellt, dass beide Fetcher gelaufen sind, bevor
	 * die Callback-Aktion ausgeführt wird.
	 */
	private int fetcherCounter = 0;
	
	/**
	 * Startet das Fetching mit dem Standard-Fetcher.
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 */
	public VideoDataFetching(final Context context, final String ean) {
		this(context, ean, SEARCH_ENGINE_OFDB);
	}
	
	/**
	 * Der komplette Konstruktor. Startet das Fetching mit dem ausgewählten
	 * Fetching-Dienst.
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 * @param searchEnginge Die Konstante beschreibt die verschiedenen Fetching-
	 * 	Dienste und somit -Klassen.
	 */
	public VideoDataFetching(final Context context, 
			final String ean, final int searchEnginge) {
		this.context = context;
		switch (searchEnginge) {
		case SEARCH_ENGINE_OFDB:
			this.fetcher = new OFDb(ean); break;
		case SEARCH_ENGINE_GOOGLE:
			this.fetcher = new Google(ean); break;
		}
		this.fetcher.addObserver(this);
        new Thread(this.fetcher).start();
        
        this.imgFetcher = new ImagesGoogle(context, ean);
        this.imgFetcher.addObserver(this);
        new Thread(this.imgFetcher).start();
	}
	
	/**
	 * Die Callback-Methode. 
	 * Sie wird nach dem erfolgreichen Holen der Daten aufgerufen und kann das
	 * weitere Vorgehen definieren.
	 */
	public void updateObserver(boolean statusOkay) {
		this.fetcherCounter = this.fetcherCounter + 1;
		if (this.fetcherCounter == 2) {
			Toast.makeText(this.context, this.fetcher.get("title") + " ("
					+ this.fetcher.get("year") + ")", Toast.LENGTH_LONG).show();
			Toast.makeText(this.context, "" + this.imgFetcher.get("cover"), 
					Toast.LENGTH_LONG).show();
		}
	}

}
