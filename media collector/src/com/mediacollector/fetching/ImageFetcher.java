package com.mediacollector.fetching;

//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;

import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.os.Environment;

abstract class ImageFetcher extends DataFetcher {
	
	/**
	 * Definiert den Schlüsselwert, unter welchem die URL des Covers des Videos
	 * in der Data-HashMap gespeichert wird.
	 */
	protected static final String COVER_STRING = "cover";
	
	/**
	 * Definiert den Schlüsselwert, unter welchem der lokale Pfad zum Cover des
	 * Videos in der Data-HashMap gespeichert wird.
	 */
	protected static final String COVER_PATH = "cover_path";
	
//	/**
//	 * Der Context, aus welchem der Fetcher aufgerufen wird. Wird für das 
//	 * Speichern des Bildes benötigt.
//	 */
//	private Context context = null;

	public ImageFetcher(final Context context, String ean) {
		super(ean);
//		this.context = context;
	}
	
	/**
	 * Wird noch auf Funktion geprüft und überarbeitet. 
	 * =========
	 * BITTE NICHT ÄNDERN!
	 * =========
	 * @throws IOException
	 */
	protected void getImage() 
	throws IOException {
//		Bitmap 	bmImg = null;
//		URL 	myFileUrl 	= null;
//	    try {
//	        myFileUrl = new URL(COVER_STRING);
//	    } catch (MalformedURLException e) {}
//	    try {
//	        HttpURLConnection conn = (HttpURLConnection) myFileUrl
//	        	.openConnection();
//	        conn.setDoInput(true);
//	        conn.connect();
//	        int length = conn.getContentLength();
//	        InputStream is = conn.getInputStream();
//	        bmImg = BitmapFactory.decodeStream(is);
//	    } catch (IOException e) {}
//	    try {
	        /* Speichern auf der SD-Card
	         * ---
	         * String filepath = Environment.getExternalStorageDirectory()
	         * 	.getAbsolutePath();*/
	    	// Speichern in der Sandbox
//	    	String filepath = this.context.getFilesDir().getAbsolutePath();
//	        FileOutputStream fos = new FileOutputStream(filepath + "/" 
//	        		+ "output.jpg"); 
//	        bmImg.compress(CompressFormat.JPEG, 75, fos);
//	        fos.flush();
//	        fos.close();
//	    } catch (Exception e) {}	    
	}
	
} 