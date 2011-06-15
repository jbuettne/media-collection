package com.mediacollector;

import com.mediacollector.tools.RegisteredActivity;

import android.os.Bundle;

/**
 * 
 * @author Philipp Dermitzel
 */
public class ScanResult extends RegisteredActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result);
    }

}
