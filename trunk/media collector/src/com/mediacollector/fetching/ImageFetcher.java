package com.mediacollector.fetching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mediacollector.R;
import com.mediacollector.tools.ImageResizer;
import com.mediacollector.tools.Exceptions.MCException;
import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Der allgemeine Image-Fetcher. Kann für alle Sammlungstypen genutzt werden.
 * @author Philipp Dermitzel
 */
public abstract class ImageFetcher extends DataFetcher {
	
	/**
	 * Definiert den Schlüsselwert, unter welchem die URL des Covers des Videos
	 * in der Data-HashMap gespeichert wird.
	 */
	public static final String COVER_STRING = "cover";
	
	/**
	 * Definiert den Schlüsselwert, unter welchem der lokale Pfad zum Cover des
	 * Videos in der Data-HashMap gespeichert wird.
	 */
	public static final String COVER_PATH = "cover_path";

	/**
	 * Der Konstruktor.
	 * Setzt die EAN (Product-ID).
	 * @param ean String Die EAN, zu welchem ein Cover gesucht werden soll.
	 */
	public ImageFetcher(final Context context, String ean) {
		super(context, ean);
	}
	
	/**
	 * Holt das Bild und speichert es auf der SD-Card im Ordner MediaCollector.
	 * Es wird außerdem auch eine verkleinerte Version mit max. 100px angelegt.
	 * Siehe dazu com.mediacollector.tools.ImageResizer
	 */
	protected void getImage() {
		final String url = (String) this.get(COVER_STRING);
		final String name = (System.currentTimeMillis() / 1000) + "-" 
			+ url.substring(url.lastIndexOf("/") + 1);
		this.set(COVER_PATH, Environment.getExternalStorageDirectory() 
				+ "/MediaCollector/");
		
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest 	= new HttpGet(url);
        final HttpGet getRequestEnc	= new HttpGet(URLEncoder.encode(url));

        try {
            HttpResponse response = client.execute(getRequest);            
            final int sc1 = response.getStatusLine().getStatusCode();            
            if (sc1 != HttpStatus.SC_OK) return;
            HttpEntity entity = response.getEntity();            
            if (entity != null)
            	this.getHTTPImage(entity, name);
            else new MCFetchingException(this.context, 
            		this.context.getString(R.string.EXCEPTION_Fechting_3), 
            		MCException.INFO);
        } catch (Exception e) {
        	try {
        		HttpResponse responseEnc 	= client.execute(getRequestEnc);
        		final int sc2 = responseEnc.getStatusLine().getStatusCode();
        		if (sc2 != HttpStatus.SC_OK) return;
        		HttpEntity entityEnc = responseEnc.getEntity();
        		if (entityEnc != null) 
        			this.getHTTPImage(entityEnc, name);
        		else new MCFetchingException(this.context, 
                		this.context.getString(R.string.EXCEPTION_Fechting_3), 
                		MCException.INFO);
        	} catch (Exception ex) {
        		new MCFetchingException(this.context, 
        				this.context.getString(R.string.EXCEPTION_Fechting_3), 
                		MCException.INFO);
        	}
        }
	}
	
	/**
	 * Holt das Bild - je nach dem, ob mittels encodierter oder nicht 
	 * encodierter URl.
	 * @param entity HttpEntity
	 * @param name Der Dateiname
	 * @throws IllegalStateException .
	 * @throws IOException .
	 */
	private void getHTTPImage(final HttpEntity entity, final String name) 
	throws IllegalStateException, IOException {
		final String nameSmall = name.substring(0, name.lastIndexOf(".")) 
			+ "_small" + name.substring(name.lastIndexOf("."));
		InputStream inputStream = null;
		try { 
    		inputStream = entity.getContent();            	
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            try {
            	// >>> Dieser Teil sollte noch entfernt werden
            	File cp = new File((String) this.get(COVER_PATH));
            	if (!cp.exists()) cp.mkdir();
            	// <<< Dieser Teil sollte noch entfernt werden
            	FileOutputStream out = 
            		new FileOutputStream(this.get(COVER_PATH) + name);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                new ImageResizer(bmp, 100, 100, nameSmall);
            } catch (Exception e) {}
            this.set(COVER_PATH, Environment.getExternalStorageDirectory() 
            		+ "/MediaCollector/" + name);
            return;
        } finally {
            if (inputStream != null) inputStream.close();
            entity.consumeContent();
        }
	}

} 