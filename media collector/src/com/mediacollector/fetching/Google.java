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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Google extends DataFetcher {
	
	/***************************************************************************
	 * Klassenvariablen
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
	
	/***************************************************************************
	 * Konstruktor/On-Create-Methode
	 **************************************************************************/
	
	/**
	 * Der Konstruktor. 
	 * Setzt die EAN (Product-ID).
	 */
	public Google(String ean) {
		super(ean);
	}
	
	/***************************************************************************
	 * Klassenmethoden
	 **************************************************************************/
	
	/**
	 * Die Fetching-Methode. Siehe auch in DataFetcher.java
	 */
	protected void getData() 
	throws IOException {
		String encProductID = URLEncoder.encode(ean, "UTF-8");
		String completeURI	= BASE_URI + encProductID;
		String webContent 	= WebParsing.getWebContent(completeURI);
		Matcher	matcher 	= PATTERN.matcher(webContent);		
		if (matcher.find()) {
			this.set(TITLE_STRING, matcher.group(1));
			notifyObserver(true);
		} else notifyObserver(false);
	}

}