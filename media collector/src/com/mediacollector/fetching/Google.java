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

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.Looper;
import android.util.Log;

/**
 * 
 * @author Philipp Dermitzel
 */
public class Google extends Activity {
	
	private static final String 	BASE_URI = 
		"http://www.google.de/m/products?ie=utf8&oe=utf8&scoring=p&q=";
	private static final Pattern 	PRODUCT_PATTERN = 
		Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)");
	
	private static String product	 	= null;
	
	private String productID 	= null;
	
	public static String getProduct() {
		return Google.product;
	}
	
	public String startSearch(String productID) {
		this.productID = productID;
		GoogleThread gt = new GoogleThread();
		new Thread(gt).start();
		return gt.getProduct();
	}
	
	private String getData() 
	throws IOException {
		String encProductID = URLEncoder.encode(productID, "UTF-8");
		String completeURI	= BASE_URI + encProductID;
		
		HttpUriRequest		head 		= new HttpGet(completeURI);
		AndroidHttpClient 	client 		= AndroidHttpClient.newInstance(null);
		HttpResponse 		response 	= client.execute(head);		
		
		int status = response.getStatusLine().getStatusCode();
		if (status != 200) return null;
				
		String webContent = getWebContent(response.getEntity());
		Log.e("bbb", webContent);
		Matcher matcher = PRODUCT_PATTERN.matcher(webContent);
		
		if (matcher.find()) {
			return matcher.group(1);
		} else return null;
		
	}
	
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
	    	Log.e("HAYA", "ioe1");
	    } finally {
	      if (iS != null) {
	        try {
	          iS.close();
	        } catch (IOException ioe) {
	        	Log.e("HAYA", "io2e");
	        }
	      }
	    }
	    try {
	      return new String(bAOS.toByteArray(), "UTF-8");
	    } catch (UnsupportedEncodingException uee) {
	      throw new IllegalStateException(uee);
	    }
	  }

	class GoogleThread implements Runnable {
		
		private String product;
		
		public String getProduct() {
			return product;
		}

		public void run() {
			Looper.prepare();
			try {
				product = getData();
			} catch (IOException e) {}
			Looper.loop();
		}
		
	}

}
