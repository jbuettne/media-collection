package com.mediacollector.tools;

import com.mediacollector.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Philipp Dermitzel
 */
public class Preferences extends PreferenceActivity {
	
	public static final String PREF_FILE = "MCPrefs";  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	    getListView().setBackgroundColor(Color.BLACK);
	}
	
	public static final SharedPreferences getPreferences(final Context con) {
		return con.getSharedPreferences(con.getPackageName() + "_preferences",
				MODE_PRIVATE);
	}

}