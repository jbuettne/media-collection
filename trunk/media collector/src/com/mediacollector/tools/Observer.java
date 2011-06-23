package com.mediacollector.tools;

/**
 * Grundklasse für die Observer. Stellt sicher, dass jeder von dieser Klasse 
 * abgeleitete Observer die benötigte updateObserver-Methode implementiert.
 * @author Philipp Dermitzel
 */
public interface Observer {
	
	/**
	 * Diese Methode definiert, welche Aktion(en) der Observer nach 
	 * Benachrichtigung durch das Observable ausführen soll. Der Status gibt an,
	 * ob beim Ausführen Fehler aufgetreten sind (false) oder nicht (true).	 * 
	 */
	public void updateObserver(boolean statusOkay);

}
