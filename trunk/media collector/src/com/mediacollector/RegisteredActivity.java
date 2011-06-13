package com.mediacollector;

import android.app.Activity;
import android.os.Bundle;

public class RegisteredActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegistry.register(this);
	}

}
