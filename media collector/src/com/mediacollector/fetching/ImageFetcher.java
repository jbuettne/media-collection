package com.mediacollector.fetching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
	//public static final String COVER_STRING = "cover";
	
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
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws MCFetchingException 
	 */
	protected void getImage() throws ClientProtocolException, 
	IOException, MCFetchingException {
		final String url = (String) this.get(COVER_STRING);
		final String name = (System.currentTimeMillis() / 1000) + "-" 
			+ url.substring(url.lastIndexOf("/") + 1);
		this.set(COVER_PATH, Environment.getExternalStorageDirectory() 
				+ "/MediaCollector/");
		
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);
        HttpResponse response = client.execute(getRequest);            
        HttpEntity entity = response.getEntity();
        if (entity != null)
        	this.getHTTPImage(entity, name);
        else 
        	throw new MCFetchingException(this.context, 
        			this.context.getString(R.string.EXCEPTION_Fechting_3), 
        			MCException.INFO);
	}
	
	/**
	 * Holt das Bild.
	 * @param entity HttpEntity
	 * @param name Der Dateiname
	 * @throws MCFetchingException .
	 * @throws IllegalStateException .
	 * @throws IOException .
	 */
	private void getHTTPImage(final HttpEntity entity, final String name) 
	throws IllegalStateException, IOException, MCFetchingException {
		final String extension = name.substring(name.lastIndexOf("."));
		final String nameSmall = name.substring(0, name.lastIndexOf(".")) 
			+ "_small" + extension;
		InputStream inputStream = null;
		boolean cover = true;
		try { 
    		inputStream = entity.getContent();            	
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            try {
            	// >>> Dieser Teil sollte noch entfernt werden
            	File cp = new File((String) this.get(COVER_PATH));
            	if (!cp.exists()) cp.mkdir();
            	// <<< Dieser Teil sollte noch entfernt werden
            	if (!extension.equals(".jpg") && !extension.equals(".png")) {
            		this.set(COVER_PATH, null);
            		throw new MCFetchingException("");
            	}
            	FileOutputStream out = 
            		new FileOutputStream(this.get(COVER_PATH) + name);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                new ImageResizer(bmp, 100, 100, nameSmall);
            } catch (Exception e) { cover = false; }
            if(cover) {
            	this.set(COVER_PATH, Environment.getExternalStorageDirectory()
            			+ "/MediaCollector/" 
            			+ name.substring(0, name.lastIndexOf(".")));
            } else { 
            	this.set(COVER_PATH, null);
            	throw new MCFetchingException("");
            }
            return;
        } finally {
            if (inputStream != null) inputStream.close();
            entity.consumeContent();
        }
	}

} 