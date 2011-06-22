/*
 * Copyright (C) 2011 Philipp Dermitzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mediacollector.fetching;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.mediacollector.tools.Observable;

import android.net.http.AndroidHttpClient;
import android.os.Looper;

/**
 * Diese Klasse dient dazu, zu einer gegebenen EAN einen Titel bzw. eine 
 * "Beschrebung" zu finden. Über diese können dann weitere benötigte Daten aus
 * anderen (Web-)Services gefetcht werden.
 * Die Suche muss als eigener Thread ausgeführt werden:
 * 		Google google = new Google("724352190003");
 * 		google.addObserver(this);
 * 		new Thread(google).start();
 * Die Daten können nun, nach Benachrichtigung des Observers, abgerufen werden:
 * 		public void updateObserver() {
 *			String productTitle = Google.product;
 *		}
 * Basierend auf ZXings ProductResultInfoRetriever.
 * @author Philipp Dermitzel
 */
public class Google extends Observable implements Runnable {
	
	/***************************************************************************
	 * KLASSENVARIABLEN
	 **************************************************************************/
	
	/**
	 * Die URL zu Googles Product-Seite, welche geparst und ausgelesen wird.
	 * Die EAN wird an das Ende dieser URL angehängt.
	 */
	private static final String BASE_URI = "http://www.google.de/m/products?ie=" 
		+ "utf8&oe=utf8&scoring=p&q=";
	
	/**
	 * Das Regex-Pattern, mittels welchem der entsprechende Teil im Quellcode 
	 * der Product-Seite (s.o.) gefunden und gespeichert werden kann.
	 */
	private static final Pattern PATTERN = Pattern.compile("owb63p\">([^<]+)");
	
	/**
	 * Die EAN des Produktes, zu welchem die "Beschreibung" gesucht werden soll.	
	 */
	private String productID = null;
	
	/**
	 * Die Produkt-"Beschreibung".
	 */
	public static String product = null;
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public Google(String productID) {
		this.productID = productID;
	}
	
	/**
	 * Holt - als Thread aufgerufen - die Daten von Google Product-Pages und 
	 * speichert die Beschreibung in der product-Klassenvariable.
	 */
	public void run() {
		Looper.prepare();
		try {
			this.getData();
		} catch (IOException e) {}
		Looper.loop();
	}
	
	/**
	 * Die Methode, welche das Parsen der Daten übernimmt.
	 * @throws IOException
	 */
	private void getData() 
	throws IOException {
		String encProductID = URLEncoder.encode(productID, "UTF-8");
		String completeURI	= BASE_URI + encProductID;
		
		HttpUriRequest		head 		= new HttpGet(completeURI);
		AndroidHttpClient 	client 		= AndroidHttpClient.newInstance(null);
		HttpResponse 		response 	= client.execute(head);		
		
		int status = response.getStatusLine().getStatusCode();
		if (status != 200) return;
				
		String 				webContent 	= getWebContent(response.getEntity());
		Matcher				matcher 	= PATTERN.matcher(webContent);
		
		if (matcher.find()) {
			product = matcher.group(1);
			notifyObserver();
		}
	}
	
	/**
	 * Die Parsing-Methode. 
	 * Siehe http://code.google.com/p/zxing/source/browse/trunk/android/src/com/
	 * google/zxing/client/android/result/supplement/
	 * ProductResultInfoRetriever.java
	 * Methode consume()
	 * @param entity HttpEntity
	 * @return String Der Quellcode der entsprechenden Seite.
	 */
	private static String getWebContent(HttpEntity entity) {
	    ByteArrayOutputStream 	bAOS 	= new ByteArrayOutputStream();
	    InputStream 			iS 		= null;
	    try {
	      iS = entity.getContent();
	      byte[] buffer = new byte[1024];
	      int bytesRead;
	      
	      while ((bytesRead = iS.read(buffer)) > 0) 
	    	  bAOS.write(buffer, 0, bytesRead);
	    } catch (IOException ioe) {	    	
	    } finally {
	      if (iS != null) {
	        try {
	          iS.close();
	        } catch (IOException ioe) {}
	      }
	    }
	    try {
	      return new String(bAOS.toByteArray(), "UTF-8");
	    } catch (UnsupportedEncodingException uee) {
	      throw new IllegalStateException(uee);
	    }
	  }

}
