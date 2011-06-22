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

import com.mediacollector.tools.RegisteredActivity;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class FetchingTest extends RegisteredActivity {
	
	private static final String BASE_PRODUCT_URI =
	      "http://www.google.de/m/products?ie=utf8&oe=utf8&scoring=p&source=zxing&q=";
	private static final Pattern PRODUCT_NAME_PRICE_PATTERN = 
		Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)");
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new FTTest()).start();
	}
	
	public void getData()
	throws IOException {
		
		String	ean			= "803341219427";
		
		 String encodedProductID = URLEncoder.encode(ean, "UTF-8");
		    String uri = BASE_PRODUCT_URI + encodedProductID;
		    Log.e("HAYA", uri);

		    HttpUriRequest head = new HttpGet(uri);
		    AndroidHttpClient client = AndroidHttpClient.newInstance(null);
		    HttpResponse response = client.execute(head);
		    int status = response.getStatusLine().getStatusCode();
		    if (status != 200) {
		      return;
		    }
		    
		    String content = consume(response.getEntity());
		    Log.e("HAYA", content);
		    Matcher matcher = PRODUCT_NAME_PRICE_PATTERN.matcher(content);
		    if (matcher.find()) {
		    	Toast toast9 = Toast.makeText(getBaseContext(), matcher.group(1), 
		    			Toast.LENGTH_LONG);
		    	toast9.show();
		    	Toast toast1 = Toast.makeText(getBaseContext(), matcher.group(2), 
		    			Toast.LENGTH_LONG);
		    	toast1.show();
		      Log.e("HAYA1", matcher.group(1));
		      Log.e("HAYA2", matcher.group(2));
		    } else {
		    	Toast toast1 = Toast.makeText(getBaseContext(), "nix da", 
		    			Toast.LENGTH_LONG);
		    	toast1.show();
		    }
    	this.finish();
	}
	
	private static String consume(HttpEntity entity) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    InputStream in = null;
	    try {
	      in = entity.getContent();
	      byte[] buffer = new byte[1024];
	      int bytesRead;
	      while ((bytesRead = in.read(buffer)) > 0) {
	        out.write(buffer, 0, bytesRead);
	      }
	    } catch (IOException ioe) {
	    } finally {
	      if (in != null) {
	        try {
	          in.close();
	        } catch (IOException ioe) {
	        }
	      }
	    }
	    try {
	      return new String(out.toByteArray(), "UTF-8");
	    } catch (UnsupportedEncodingException uee) {
	      throw new IllegalStateException(uee);
	    }
	  }
	
	class FTTest implements Runnable {

		public void run() {
			Looper.prepare();
			try {
				getData();
			} catch (IOException e) {
				Toast toast = Toast.makeText(getBaseContext(), "FEHLER", 
			    		Toast.LENGTH_LONG);
		    	toast.show();
			}
			Looper.loop();
		}
		
	}
}