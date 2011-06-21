package com.mediacollector.tools;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * Erzeugt eine eindeutige ID des Geräts. Mittels dieser kann die 
 * Synchronisation erfolgen. Hierfür wird auf die ANDROID_ID zurückgegriffen.
 * Da jedoch ein Bug in Android 2.2 - der momentan meistgenutzten 
 * Android-Version - existiert, wird noch eine weitere Methode zur Verfügung 
 * gestellt, welche aus DeviceID und SIM-Serial eine weitesgehend eindeutige
 * Identifikationsnummer hasht.
 * @author Philipp Dermitzel
 */
public class Identifier {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/
	
	/**
	 * Der Schlüssel in den Preferences, mittels welchem auf die ID zugegriffen
	 * werden kann.
	 */
	public 	static 	String				ID_STRING	= "dev_identifier";
	private	static	SharedPreferences 	prefs		= null;

	/***************************************************************************
	 * Getter und Setter
	 **************************************************************************/
	
	public static String getIdentifier(Context context) {		
		prefs = context.getSharedPreferences(
				"com.mediacollector_preferences", 0);
		String prefSaved = prefs.getString(ID_STRING, null);
		if (prefSaved == null)
			return createAndStore(context);
		else return prefSaved;
	}

	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	public static String createAndStore(Context context) {	
		final String aId = Secure.getString(context.getContentResolver(), 
				Secure.ANDROID_ID);
		String identifier = null;
		if (!"9774d56d682e549c".equals(aId)) {
			identifier = UUID.nameUUIDFromBytes(aId.getBytes()).toString();
		} else {
			final TelephonyManager tm = (TelephonyManager) 
				context.getSystemService(Context.TELEPHONY_SERVICE);
			final String tmDevice = (String) tm.getDeviceId();
			final String tmSerial = (String) tm.getSimSerialNumber();
			identifier = new UUID(aId.hashCode(), ((long) tmDevice.hashCode() 
					<< 32) | tmSerial.hashCode()).toString();
		}
		Editor prefsEditor = prefs.edit();	    
	    prefsEditor.putString(ID_STRING, identifier);
	    prefsEditor.commit();
	    return identifier;
	}

}
