package com.mediacollector.tools;

import java.util.ArrayList;

/**
 * Observer-Pattern.
 * Diese Klasse kann als Elternklasse zu beobachtender Methoden hinzugefügt 
 * werden. Diesen können dann (auch mehrere) Observer hinzugefügt werden und 
 * diese bei Bedarf benachrichtigt.
 * Die Aktion nach Benachrichtigung ist in den entsprechenden Observern 
 * definiert. Diese können/müssen ihre updateObserver()-Methode überschreiben.
 * @author Philipp Dermitzel
 */
public abstract class Observable {
	
	/**
	 * Die Liste mit allen aktiven Observern.
	 */
	protected ArrayList<Observer> observer = new ArrayList<Observer>();
	
	/**
	 * Fügt einen Observer hinzu. Dieser wird abgeleitet von der Observer-
	 * Klasse des Media Collectors (com.mediacollector.tools.Observer).
	 * @param Observer Der hinzuzufügende Observer.
	 */
	public void addObserver(Observer observer) {
		this.observer.add(observer);
	}
	
	/**
	 * Löscht einen Observer aus der Liste. Dieser wird abgeleitet von der 
	 * Observer-Klasse des Media Collectors (com.mediacollector.tools.Observer).
	 * @param Observer Der zu löschende Observer.
	 */
	public void removeObserver(Observer Observer) {
		this.observer.remove(this.observer.indexOf(Observer));
	}
	
	/**
	 * Benachrichtigt alle aktiven (hinzugefügten) Observer.
	 */
	public void notifyObserver() {
		for (Object obj : this.observer) ((Observer) obj).updateObserver();			
	}

}
