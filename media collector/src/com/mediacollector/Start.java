package com.mediacollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Der Start-Screen der Applikation. Sie zeigt die Hauptbuttons zum Scannen von
 * neuen Medien sowie zum Browsen und Synchronisieren der Sammlungen. 
 * @author Philipp Dermitzel
 */
public class Start extends Activity {
	
	/**
	 * Das Paket, über welches der BarcodeScanner erreichbar ist. Da der 
	 * MediaCollector auf diese App aufbaut, muss sie installiert sein. Sollte
	 * dies nicht der Fall sein, so fragt der MediaCollector, ob sie 
	 * installiert werden soll.
	 */
	public static final String BS_PACKAGE = "com.google.zxing.client.android";	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);        
        LinearLayout addField = (LinearLayout) findViewById(R.id.addField);
        addField.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
                intentScan.setPackage(BS_PACKAGE);
                intentScan.addCategory(Intent.CATEGORY_DEFAULT);
                try {
                	startActivityForResult(intentScan, 0);
                } catch (ActivityNotFoundException e) {
                	showDownloadDialog();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, 
    		Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Toast toast = Toast.makeText(Start.this, 
        				contents, Toast.LENGTH_SHORT);
            	toast.show();
            } else if (resultCode == RESULT_CANCELED) {
            	Toast toast = Toast.makeText(Start.this, 
        				"Sorry, no scan-result", Toast.LENGTH_SHORT);
            	toast.show();
            }
        }
    }
    
    /**
     * Zeigt einen Dialog, der darauf hinweist, dass der MediaCollector den 
     * BarcodeScanner benötigt und fragt nach, ob dieser installiert werden
     * soll. In diesem Falle wird der Market geöffnet.
     * @return AlertDialog: Der Dialog
     */
    private AlertDialog showDownloadDialog() {
    	AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
    	downloadDialog.setTitle(getString(R.string.BSNFD_title));
    	downloadDialog.setMessage(getString(R.string.BSNFD_message));
    	downloadDialog.setPositiveButton(getString(R.string.BSNFD_button_pos), 
    			new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {
    			Uri uri = Uri.parse("market://search?q=pname:" + BS_PACKAGE);
    			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    			startActivity(intent);
    		}
    	});
    	downloadDialog.setNegativeButton(getString(R.string.BSNFD_button_neg), 
    			new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {}
    	});
    	return downloadDialog.show();
    }
    
}