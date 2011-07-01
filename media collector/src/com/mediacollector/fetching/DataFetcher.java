package com.mediacollector.fetching;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.os.Looper;

import com.mediacollector.R;
import com.mediacollector.tools.Observable;
import com.mediacollector.tools.Exceptions.MCFetchingException;

/**
 * Der allgemeine Data-Fetcher. Ist gültig für alle verschiedenen Sammlungs-
 * Typen. Die entsprechenden Search-Engines und daraus abgeleitet die benötigten
 * Klassen werden in der aufrufenden Klasse definiert. Siehe hierfür 
 * beispielhaft die Klasse VideoDataFetching.
 * @author Philipp Dermitzel
 */
public abstract class DataFetcher extends Observable implements Runnable {
	
	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/
	
	/**
	 * Definiert den Schlüsselwert, unter welchem der Titel des Eintrags in der
	 * Data-HashMap gespeichert wird.
	 */
	public static final String TITLE_STRING = "title";
	
	/**
	 * Definiert den Schlüsselwert, unter welchem das Erscheinungsjahr des 
	 * Eintrags in der Data-HashMap gespeichert wird.
	 */
	public static final String YEAR_STRING = "year";
	
	/**
	 * Definiert den Schlüsselwert, unter welchem der Artists des Eintrags in
	 * der Data-HashMap gespeichert wird.
	 */
	public static final String ARTIST_STRING = "artist";
	
	/**
	 * Die EAN, zu der die entsprechenden Daten eingeholt werden. Sie wird
	 * mittels des Konstruktors gesetzt.
	 */
	protected String ean;
	
	/**
	 * Der Context, aus dem das Fetching aufgerufen wird. Wichtig für
	 * möglicherweise auftretende Fehler (-> Exceptions)
	 */
	protected Context context;
	
	/**
	 * Die Daten, die eingeholt werden. Sie können über die allgemeinen Getter-
	 * und Setter-Methoden ausgelesen und gesetzt werden.
	 */
	protected HashMap<String, Object> data = new HashMap<String, Object>();
	
	/***************************************************************************
	 * Getter und Setter
	 **************************************************************************/
	/**
	 * Liefert einen beliebigen Wert aus der Data-HashMap.
	 * @param key String Der Schlüsselwert.
	 * @return Object Der gewünschte Wert aus der Data-HashMap.
	 */
	public Object get(final String key) {
		return this.data.get(key);
	}
	
	/**
	 * Setzt einen Wert in der Data-HashMap.
	 * @param key String Der Schlüsselwert.
	 * @param value Object Der Wert in der Data-HashMap.
	 */
	protected void set(final String key, final Object value) {
		this.data.put(key, value);
	}
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor.
	 * Setzt nur die ean, führt keine weitere Aktion aus. Der eigentliche 
	 * Vorgang wird manuell als Thread gestartet.
	 * @param ean String Die im Barcode codierte EAN.
	 */
	public DataFetcher(final Context context, final String ean) {
		this.ean = ean;
		this.context = context;
	}
	
	/**
	 * Startet den Web-Fetching-Vorgang. Wird im Thread aufgerufen.
	 */
	public void run() {
		Looper.prepare();
		try {
			this.getData();
		} catch (IOException e) {
			new MCFetchingException(this.context, this.context.getString(
					R.string.EXCEPTION_Fetching));
		}
		Looper.loop();
	}
	
	/**
	 * Die eigentliche Fetching-Methode. Muss in jeder Fetching-Klasse vorhanden
	 * sein und wird daher an dieser Stelle abstrakt definiert.
	 * @throws IOException .
	 */
	protected abstract void getData() throws IOException;

}
