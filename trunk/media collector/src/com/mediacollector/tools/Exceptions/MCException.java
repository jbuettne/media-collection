package com.mediacollector.tools.Exceptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.mediacollector.R;

import android.content.Context;
import android.widget.Toast;

/**
 * Die Grund-Exception-Klasse des MediaCollectors. Über diese können Fehler und
 * Benachrichtigungen abgesetzt und geloggt werden. Alle Exceptions der Stufe
 * "CRITICAL" werden standardmäßig geloggt, bei allen anderen Stufen kann dies
 * optional über den entsprechenden Parameter des Konstruktors aktiviert werden.
 * @author Philipp Dermitzel
 */
public class MCException extends Exception {

	/***************************************************************************
	 * Klassenvariablen
	 **************************************************************************/
	
	/**
	 * Stufe 1: Eine Information für den User. Diese Stufe hat keine 
	 * Auswirkungen auf den weiteren stabilen Programmverlauf.
	 */
	public static final int INFO = 1;
	
	/**
	 * Stufe 2: Eine Warnung. Diese Stufe kann bereits Auswirkungen auf den 
	 * weiteren stabilen stabilen Betrieb des Programms haben.
	 */
	public static final int WARNING = 2;
	
	/**
	 * Stufe 3: Ein kritischer Fehler. Diese Stufe führt zum Programmabbruch.
	 * In der Log-File wird gespeichert, zu welcher Zeit und in welcher Klasse
	 * der entsprechende Fehler ausgelöst wurde.
	 */
	public static final int CRITICAL = 3;
	
	/**
	 * Erkennungs-Tag für die Exception-Klasse. Dies sollte von Kind-Klassen
	 * überschrieben werden.
	 */
	protected String excTag = "STANDARD";
	
	/**
	 * Der Context, in welchem die Exception ausgelöst wurde.
	 */
	private Context	context = null;
	
	/**
	 * Die Fehler-Nachricht der Exception.
	 */
	private String	errorMsg	= null;
	
	/**
	 * Generierte ID für das Serialisieren.
	 */
	private static final long serialVersionUID = -4943270582922408295L;
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der einfachste Konstruktor. Gibt eine Warnung mit der Fehlerbeschreibung
	 * "Unbekannter Fehler" aus.
	 * @param context Context Der Context, in dem die Exception ausgelöst wurde.
	 */
	public MCException(Context context) {
		this(context, context.getString(R.string.EXCEPTION_unknown), WARNING, 
				false);
	}
	
	/**
	 * Ein einfacher Konstruktor, welcher eine Warnung mit definierter Fehler-
	 * beschreibung ausgibt.
	 * @param context Context Der Context, in dem die Exception ausgelöst wurde.
	 * @param errorMsg String Die Fehlerbeschreibung.
	 */
	public MCException(Context context, final String errorMsg) {
		this(context, errorMsg, WARNING, false);
	}
	
	/**
	 * Ein einfacher Konstruktor, welcher eine Exception mit definiertem Status
	 * und "unbekannter" Fehlerbeschreibung ausgibt.
	 * @param context Context Der Context, in dem die Exception ausgelöst wurde.
	 * @param status int Der Status der Exception.
	 */
	public MCException(Context context, final int status) {
		this(context, context.getString(R.string.EXCEPTION_unknown), status, 
				false);
	}
	
	/**
	 * Der Standard-Konstruktor. Über diesen können Status und Fehler-
	 * beschreibung definiert werden. Kritische Fehler werden geloggt.
	 * @param context Context Der Context, in dem die Exception ausgelöst wurde.
	 * @param errorMsg String Die Fehlerbeschreibung.
	 * @param status int Der Status der Exception.
	 */
	public MCException(Context context, final String errorMsg, 
			final int status) {		
		this(context, errorMsg, status, false);
	}
	
	/**
	 * Der Konstruktor zum Erzwingen des Loggings. Über den Parameter log kann
	 * das Logging auch bei nicht-kritischen Fehlern erzwungen werden.
	 * @param context Context Der Context, in dem die Exception ausgelöst wurde.
	 * @param errorMsg String Die Fehlerbeschreibung.
	 * @param status int Der Status der Exception.
	 * @param log boolean Logging auch bei nicht-kritischen Fehlern aktiviert.
	 */
	public MCException(Context context, final String errorMsg, 
			final int status, boolean log) {		
		this.context 	= context;
		this.errorMsg 	= errorMsg;
		switch (status) {
		case INFO:
			Toast.makeText(context, "Info: \"" + errorMsg + "\"", 
					Toast.LENGTH_LONG).show(); break;
		case WARNING:
			// Dialog...
			break;
		case CRITICAL:
			// Dialog + Beenden
			log = true;
			break;
		}
		if (log) this.logException();
	}
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * Schreibt eine Log-Meldung der nachfolgenden Form in die LOG-File, welche
	 * im Sandbox-Verzeichnis liegt.
	 * 	1234567890 STANDARD in com.mediacollector.Start: "Fehlerbeschreibung"
	 * 	1234567890 SQL in com.mediacollector.collection.Database: "Fehler"
	 * 	...
	 */
	protected void logException() {
		try {
			String filePath = this.context.getFilesDir() + "/LOG";
			File logFile = new File(filePath);
			if (!logFile.exists()) logFile.createNewFile();			
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(System.currentTimeMillis() / 1000 + " " + this.excTag 
					+ " in " + this.context.getClass() + ": \"" 
					+ this.errorMsg + "\"");
			bw.close();
		} catch (Exception e) {}
	}

}
