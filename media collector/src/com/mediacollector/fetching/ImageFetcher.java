package com.mediacollector.fetching;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mediacollector.tools.ImageResizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Der allgemeine Image-Fetcher. Kann für alle Sammlungstypen genutzt werden.
 * @author Philipp Dermitzel
 */
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
		final String nameSmall = name.substring(0, name.lastIndexOf(".")) 
			+ "_small" + name.substring(name.lastIndexOf("."));
		this.set(COVER_PATH, Environment.getExternalStorageDirectory() 
				+ "/MediaCollector/");
		this.set("test", this.get(COVER_PATH) + nameSmall);
		
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(URLEncoder.encode(url));

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) return;

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
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
                    this.set(COVER_PATH, Environment
                    		.getExternalStorageDirectory() + "/MediaCollector/"
                    		+ name);
                    return;
                } finally {
                    if (inputStream != null) inputStream.close();
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {}
        return;
	}

} 