package com.mediacollector;

import com.mediacollector.tools.RegisteredActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * @author Philipp Dermitzel
 */
public class ScanBarcode extends RegisteredActivity {
	
	/**
	 * Das Paket, über welches der BarcodeScanner erreichbar ist. Da der 
	 * MediaCollector auf diese App aufbaut, muss sie installiert sein. Sollte
	 * dies nicht der Fall sein, so fragt der MediaCollector, ob sie 
	 * installiert werden soll.
	 */
	public final String BS_PACKAGE = "com.google.zxing.client.android";	
	
	public String scanResult = null;
	
	public String getScanResult() {
		return this.scanResult;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanBarcode();
	}
	
	public void scanBarcode() {
		Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
        intentScan.setPackage(BS_PACKAGE);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
        try {
        	startActivityForResult(intentScan, 0);
        } catch (ActivityNotFoundException e) {
        	showDownloadDialog();
        } catch (Exception ex) {
        	System.out.println("HAJA" + ex);
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
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, 
    		Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Toast toast = Toast.makeText(getBaseContext(), 
        				contents, Toast.LENGTH_SHORT);
            	toast.show();
            	this.scanResult = contents;
            } else if (resultCode == RESULT_CANCELED) {
            	Toast toast = Toast.makeText(getBaseContext(), 
        				"Sorry, no scan-result", Toast.LENGTH_SHORT);
            	toast.show();
            }
        }
    }

}
