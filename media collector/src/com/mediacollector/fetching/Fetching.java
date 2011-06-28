package com.mediacollector.fetching;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mediacollector.fetching.Audio.Thalia;
import com.mediacollector.fetching.Game.EANsearch;
import com.mediacollector.fetching.Video.OFDb;
import com.mediacollector.tools.Observer;

/**
 * Data-Fetching-Factory. Diese Klasse definiert die vorhandenen Search-Enginges
 * und startet den Vorgang des Fetchings. Diese Klasse übernimmt auch das 
 * Callback-Handling mittels der updateObserver-Methode.
 * @author Philipp Dermitzel
 */
public class Fetching implements Observer {
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Google Product Search;
	 * http://www.google.com/m/products  -- Google.java
	 */
	public static final int SEARCH_ENGINE_GOOGLE 	= 0;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier OFDb;
	 * http://www.ofdb.de  -- OFDb.java
	 */
	public static final int SEARCH_ENGINE_OFDB 		= 1;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Thalia;
	 * http://www.thalia.de  -- Thalia.java
	 */
	public static final int SEARCH_ENGINE_THALIA 	= 24;
	
	/**
	 * Die verschiedenen Search-Engine-Klassen. Hier Gameseek;
	 * http://www.gamesseek.co.uk  -- Gameseek.java
	 */
	public static final int SEARCH_ENGINE_EANSEARCH 	= 25;
	
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
	 * Startet das Fetching mit dem Standard-Fetcher (Google Product Search).
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 */
	public Fetching(final Context context, final String ean) {
		this(context, ean, SEARCH_ENGINE_GOOGLE);
	}
	
	/**
	 * Der komplette Konstruktor. Startet das Fetching mit dem ausgewählten
	 * Fetching-Dienst.
	 * @param context Context Der Context, aus dem das Fetching gestartet wurde.
	 * @param ean String Die EAN, zu welcher Daten eingeholt werden.
	 * @param searchEnginge Die Konstante beschreibt die verschiedenen Fetching-
	 * 	Dienste und somit -Klassen.
	 */
	public Fetching(final Context context, 
			final String ean, final int searchEnginge) {
		this.context = context;
		switch (searchEnginge) {
		case SEARCH_ENGINE_GOOGLE:
			this.fetcher = new Google(ean); break;
		case SEARCH_ENGINE_OFDB:
			this.fetcher = new OFDb(ean); break;		
		case SEARCH_ENGINE_THALIA:
			this.fetcher = new Thalia(ean); break;
		case SEARCH_ENGINE_EANSEARCH:
			this.fetcher = new EANsearch(ean); break;
		}
		this.fetcher.addObserver(this);
        new Thread(this.fetcher).start();
        
        this.imgFetcher = new ImagesGoogle(ean);
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
			String test = null;
			if (this.fetcher.get("artist") != null) {
				test = this.fetcher.get("artist") + " - " 
					+ this.fetcher.get("title") + " (" 
					+ this.fetcher.get("year") + ")";
			} else {
				test = this.fetcher.get("title") + " ("
				+ this.fetcher.get("year") + ")";
			}
			Toast.makeText(this.context, test, Toast.LENGTH_LONG).show();
			Toast.makeText(this.context, "" + this.imgFetcher
					.get(ImageFetcher.COVER_STRING), Toast.LENGTH_LONG).show();
		}
	}

}
