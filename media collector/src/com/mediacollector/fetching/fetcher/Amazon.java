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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mediacollector.R;
import com.mediacollector.fetching.DataFetcher;
import com.mediacollector.tools.ImageResizer;
import com.mediacollector.tools.Exceptions.MCException;
import com.mediacollector.tools.Exceptions.MCFetchingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
     * All
     * Books
     * Music
     * VideoGames
     * Video
     * Toys (Brettspiele)
     * etc. 
     * http://docs.amazonwebservices.com/AWSECommerceService/2010-11-01/DG/index.html?CHAP_ResponseGroupsList.html
     */
    private static String SEARCH_INDEX = null;
	
	public static final int MUSIC 	= 0;
	public static final int VIDEO 	= 1;
	public static final int BOOKS 	= 2;
	public static final int GAMES 	= 3;
	public static final int ALL		= 4;
	
	public Amazon(final Context context, final String ean, 
			final int search) {
		super(context, ean, search);
	}
	
	protected void getData() throws IOException {
		
		SignedRequestsHelper helper;
		switch (this.search) {
		case MUSIC:
			SEARCH_INDEX = "Music"; 
			break;
		case VIDEO:
			SEARCH_INDEX = "Video"; 
			break;
		case BOOKS:
			SEARCH_INDEX = "Books"; 
			break;
		case GAMES:
			SEARCH_INDEX = "All"; 
			break;
		default:
			SEARCH_INDEX = "All";
			break;
		}
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
		if (ean.length() == 12) params.put("ItemId", 0 + ean);			
		else params.put("ItemId", ean);
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
			this.set(TITLE_STRING, doc.getElementsByTagName("Title").item(0)
					.getTextContent());
			this.set(COVER_STRING,
					((Element) doc.getElementsByTagName("MediumImage").item(0))
							.getElementsByTagName("URL").item(0)
							.getTextContent());
			this.set(MEDIUM_STRING, doc.getElementsByTagName("Binding").item(0)
					.getTextContent()); // Medium, Produktart
			this.set(TITLE_ID_STRING, this.genereateRandomID());
			this.set(ARTIST_ID_STRING, this.genereateRandomID());
			switch (this.search) {
			case MUSIC:
				this.set(ARTIST_STRING, doc.getElementsByTagName("Artist")
						.item(0).getTextContent());
				this.set(YEAR_STRING, doc.getElementsByTagName("ReleaseDate")
						.item(0).getTextContent().substring(0, 4));
				break;
			case VIDEO:
				this.set(ARTIST_STRING, doc.getElementsByTagName("Director")
						.item(0).getTextContent());
				this.set(YEAR_STRING, doc.getElementsByTagName("ReleaseDate")
						.item(0).getTextContent().substring(0, 4));
				break;
			case BOOKS:
				this.set(ARTIST_STRING, doc.getElementsByTagName("Author")
						.item(0).getTextContent());
				this.set(YEAR_STRING,
						doc.getElementsByTagName("PublicationDate").item(0)
								.getTextContent().substring(0, 4));
				break;
			case GAMES:
				this.set(ARTIST_STRING,
						doc.getElementsByTagName("Brand").item(0)
								.getTextContent());
				try {
					this.set(YEAR_STRING,
							doc.getElementsByTagName("ReleaseDate").item(0)
									.getTextContent().substring(0, 4));
				} catch (Exception ex) {
					this.set(YEAR_STRING,
							doc.getElementsByTagName("PublicationDate").item(0)
									.getTextContent().substring(0, 4));
				}
				break;
			default:
				SEARCH_INDEX = "All";
				break;
			}
			this.getImage(
					((Element) doc.getElementsByTagName("MediumImage").item(0))
					.getElementsByTagName("URL").item(0)
					.getTextContent());
			Log.i("AMAZON", (String) this.get(COVER_PATH));
			notifyObserver(true);
			notifyObserver(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
	/**
	 * Holt das Bild und speichert es auf der SD-Card im Ordner MediaCollector.
	 * Es wird auÃŸerdem auch eine verkleinerte Version mit max. 100px angelegt.
	 * Siehe dazu com.mediacollector.tools.ImageResizer
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws MCFetchingException 
	 */
	protected void getImage(final String url) throws ClientProtocolException, 
	IOException, MCFetchingException {
		//final String url = (String) this.get(COVER_STRING);
		final String name = (System.currentTimeMillis() / 1000) + "-" 
			+ url.substring(url.lastIndexOf("/") + 1);
		this.set(COVER_PATH, Environment.getExternalStorageDirectory() 
				+ "/MediaCollector/");
		
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);
        HttpResponse response = client.execute(getRequest);            
        HttpEntity entity = response.getEntity();
        if (entity != null){
        	this.getHTTPImage(entity, name);
        }else 
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
        		Log.i("AMAZON", "BIN DRIN");
            	this.set(COVER_PATH, Environment.getExternalStorageDirectory()
            			+ "/MediaCollector/" 
            			+ name.substring(0, name.lastIndexOf(".")));
            } else { 
            	this.set(COVER_PATH, null);
            	throw new MCFetchingException("");
            }
        } finally {
            if (inputStream != null) inputStream.close();
            entity.consumeContent();
        }
	}
	/**
	 * Erzeugt eine zufÃ¤llige ID mit 20-30 Stellen und dem Alphabet 0-9.
	 * @return String
	 */
	private String genereateRandomID() {
		StringBuffer randomID = new StringBuffer();
		Random rg = new Random();
		for (int i = 0; i < (20 + rg.nextInt(10)); i++) 
			randomID.append(rg.nextInt(9));
		return randomID.toString();
	}
}
