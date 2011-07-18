package com.mediacollector.fetching.fetcher;
/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mediacollector.fetching.DataFetcher;

import android.content.Context;
import android.util.Log;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class Amazon extends DataFetcher{
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJR2TJ3N3M4TGGONQ";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "n8q890ccqgVJNM59lM27K4xPNEEr25B1coG4tlAC";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.de";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    //private static final String ITEM_ID = "4010232027719"; // i, Robot

    /*
     * SearchIndex muss bei einem IdType, welcher nicht die ASIN ist,
     * gesetzt sein.
     * Alternativen für SearchIndex:
     * Books
     * Music
     * VideoGames
     * Video
     * Toys (Brettspiele)
     * etc. 
     * http://docs.amazonwebservices.com/AWSECommerceService/2010-11-01/DG/index.html?CHAP_ResponseGroupsList.html
     */
    private static final String SEARCH_INDEX = "Video";
    
	public Amazon(final Context context, final String ean) {
		super(context, ean);
	}
	
	protected void getData() throws IOException {
		SignedRequestsHelper helper;
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT,
					AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		String requestUrl = null;

		/* The helper can sign requests in two forms - map form and string form */

		/*
		 * Here is an example in map form, where the request parameters are
		 * stored in a map.
		 */
		System.out.println("Map form example:");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2010-11-01");
		params.put("Operation", "ItemLookup");
		params.put("IdType", "EAN");
		params.put("SearchIndex", SEARCH_INDEX);
		/*
		 * Unser Barcode
		 */
		params.put("ItemId", ean);
		/*
		 * Je nach ResponseGroup werden unterschiedlich viele Daten ausgegeben
		 * mit "Small,ItemAttributes,Images,Tracks" würde man u.A. auch noch die
		 * Track-Anzahl erhalten können
		 * http://docs.amazonwebservices.com/AWSECommerceService
		 * /latest/DG/index.html?DESearchIndexParamForItemsearch.html
		 */
		params.put("ResponseGroup", "Small,ItemAttributes,Images");

		requestUrl = helper.sign(params);
		
		this.getMovieData(requestUrl);
		
	}

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private void getMovieData(final String requestUrl) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
        	Log.i("AMAZON", requestUrl + " " + ((Element) doc.getElementsByTagName("MediumImage").item(0)).getElementsByTagName("URL").item(0).getTextContent());
            this.set(TITLE_STRING, doc.getElementsByTagName("Title").item(0).getTextContent());
            
            /*//Informationen für Bücher
            title.put("artist", doc.getElementsByTagName("Author").item(0).getTextContent());
            title.put("year", doc.getElementsByTagName("PublicationDate").item(0).getTextContent().substring(0, 4));*/
            /*//Informationen für Musik
            title.put("artist", doc.getElementsByTagName("Artist").item(0).getTextContent());
            title.put("year", doc.getElementsByTagName("ReleaseDate").item(0).getTextContent().substring(0, 4));*/
            //Informationen für Filme
            this.set(ARTIST_STRING, doc.getElementsByTagName("Director").item(0).getTextContent());
            this.set(MEDIUM_STRING, doc.getElementsByTagName("Binding").item(0).getTextContent()); //DVD, BluRay, etc
            this.set(YEAR_STRING, doc.getElementsByTagName("ReleaseDate").item(0).getTextContent().substring(0, 4));
            this.set(COVER_STRING, ((Element) doc.getElementsByTagName("MediumImage").item(0)).getElementsByTagName("URL").item(0).getTextContent());
            /*//Informationen für Spiele
            title.put("artist", doc.getElementsByTagName("Brand").item(0).getTextContent());
            title.put("year", doc.getElementsByTagName("ReleaseDate").item(0).getTextContent().substring(0, 4));*/
			notifyObserver(true);
        } catch (Exception e) {
        	notifyObserver(false);
            throw new RuntimeException(e);
        }
    }

}
