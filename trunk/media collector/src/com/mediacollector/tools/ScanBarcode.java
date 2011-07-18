package com.mediacollector.tools;

import com.mediacollector.R;
import com.mediacollector.tools.Exceptions.MCException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
	
	private boolean noScanner = false;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanBarcode();
	}
	
	public void scanBarcode() {
		Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
        intentScan.setPackage(BS_PACKAGE);
        intentScan.putExtra("SCAN_MODE", "PRODUCT_MODE");
        intentScan.putExtra("ENCODE_DATA","ENCODE_FORMATE");
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
        try {
        	startActivityForResult(intentScan, 2);
        } catch (ActivityNotFoundException e) {
        	this.noScanner = true;
        	showDownloadDialog();
        } catch (Exception ex) {
        	new MCException(this, getString(R.string.EXCEPTION_unknown), 
        			MCException.WARNING, true);
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
    			finish();
    		}
    	});
    	downloadDialog.setNegativeButton(getString(R.string.BSNFD_button_neg), 
    			new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialogInterface, int i) {
    			finish();
    		}
    	});
    	return downloadDialog.show();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, 
    		Intent intent) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
            	String contents = intent.getStringExtra("SCAN_RESULT");
            	String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            	final Intent scanresult = new Intent();
                scanresult.putExtra("BARCODE", contents);
                scanresult.putExtra("FORMAT", format);
                setResult(Activity.RESULT_OK, scanresult);
            } else {
            	setResult(Activity.RESULT_CANCELED);
            }
        	if (!this.noScanner)
        		finish();
        }
    }

}