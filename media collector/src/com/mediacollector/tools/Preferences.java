package com.mediacollector.tools;

import com.mediacollector.R;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Philipp Dermitzel
 */
public class Preferences extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	    getListView().setBackgroundColor(Color.BLACK);
	}

}