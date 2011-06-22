package com.mediacollector.fetching;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.net.http.AndroidHttpClient;

public class WebParsing {
	
	/**
	 * Die Parsing-Methode. 
	 * Siehe http://code.google.com/p/zxing/source/browse/trunk/android/src/com/
	 * google/zxing/client/android/result/supplement/
	 * ProductResultInfoRetriever.java
	 * Methode consume()
	 * @param entity HttpEntity
	 * @return String Der Quellcode der entsprechenden Seite.
	 * @throws IOException 
	 */
	public static String getWebContent(final String uri) 
	throws IOException {
		HttpUriRequest		head 		= new HttpGet(uri);
		AndroidHttpClient 	client 		= AndroidHttpClient.newInstance(null);
		HttpResponse 		response 	= client.execute(head);
		
		int status = response.getStatusLine().getStatusCode();
		if (status != 200) return null;		
		
		HttpEntity			entity		= response.getEntity();		
	    ByteArrayOutputStream bAOS 	= new ByteArrayOutputStream();
	    InputStream 		iS 		= null;
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
