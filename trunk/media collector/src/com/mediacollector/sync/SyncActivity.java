package com.mediacollector.sync;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.mediacollector.R;
import com.mediacollector.tools.Observer;

/**
 * Zeigt eine ProgressBar an und startet die Synchronisation (im Augenblick 
 * mittels Dropbox) im Hintergrund (in eigenem Thread).
 * @author Philipp Dermitzel
 */
public class SyncActivity extends Activity implements Observer {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sync);  
        
        Dropbox dropbox = new Dropbox(this);
        dropbox.addObserver(this);
        new Thread(dropbox).start();
	}

	/**
	 * Schließt die Activity wenn der Thread durch das Observable darüber 
	 * benachrichtigt wird, dass die Hintergrundaktion beendet ist.
	 */
	public void updateObserver(boolean statusOkay) {
		this.finish();
	}

}