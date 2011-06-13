package com.mediacollector;

import java.util.ArrayList;

import android.app.Activity;

/**
 * Diese Klasse dient dazu, die App komplett beenden zu können. Dazu werden 
 * alle aktiven Activities mittels Registry-Pattern gespeichert. Diese können
 * dann über den (statischen) Aufruf der Methode ActivityRegistry.closeAll() 
 * geschlossen werden.  
 * @author Philipp Dermitzel
 */
public class ActivityRegistry {
	
	/**
	 * Eine ArrayList mit allen aktiven Activities der App.
	 */
	private static ArrayList<Activity> activities = new ArrayList<Activity>();
	
	/**
	 * Registriert die übergebene Activity in der Registry.
	 * @param activity
	 */
	public static void register(final Activity activity) {
		activities.add(activity);
	}
	
	/**
	 * Schließt alle aktiven Activities der App.
	 */
	public static void closeAll() {
		for (Object obj : activities) {
			Activity activitiy = (Activity) obj;
			activitiy.finish();
		}
	}

}
