package org.stormdev.authlib.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.stormdev.authlib.json.GsonUtil;

/**
 * Used internally by StormAuthLib, but you could use it too
 *
 */
public class HttpClient {
	
	/**
	 * Sends a POST request to the URL given with the request body as the serializable object provided transformed into JSON.
	 * 
	 * @param targetURL The URL to make the request to
	 * @param elem The object to transform into JSON (Using GSON)
	 * @return The server's response, or null if no response
	 */
	public static String executeJSONPost(String targetURL, Object elem){
		return executePost(targetURL, GsonUtil.gson.toJson(elem));
	}
	
	/**
	 * Sends a POST request to the URL given with the request body as the string provided
	 * 
	 * @param targetURL The URL to make the request to
	 * @param body The request body
	 * @return The server's response, or null if no response
	 */
	public static String executePost(String targetURL, String body)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(body.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (body);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      //e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
}
