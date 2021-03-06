package net.stormdev.MTA.SM.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SimpleHttpClient {
	public static String post(String site, String[] keys, String[] values){
		String data = "";
		if(keys.length > values.length){
			throw new RuntimeException("Invalid keys and values!");
		}
		
		try {	
			// Construct data
			for(int i=0;i<keys.length;i++){
				String key = keys[i];
				String value = values[i];
				if(data.length() < 1){
					data = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
					continue;
				}
				data += "&" + URLEncoder.encode(key, "UTF-8") + "=" +
                        URLEncoder.encode(value, "UTF-8");
			}
		    
		    // Send data
		    URL url = new URL(site);
		    URLConnection conn = url.openConnection();
		    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));

		    String response = "";
		    String line;
		    while ((line = rd.readLine()) != null) {
		        response += line;
		    }
		    wr.close();
		    rd.close();
		    return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String get(String site, String[] keys, String[] values){
		String data = site+"?";
		if(keys.length > values.length){
			throw new RuntimeException("Invalid keys and values!");
		}
		boolean first = true;
		try {	
			// Construct data
			for(int i=0;i<keys.length;i++){
				String key = keys[i];
				String value = values[i];
				if(first){
					data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
					first = false;
					continue;
				}
				data += "&" + URLEncoder.encode(key, "UTF-8") + "=" +
                        URLEncoder.encode(value, "UTF-8");
			}
		    
		    // Send data
		    URL url = new URL(data);
		    URLConnection conn = url.openConnection();
		    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");

		    // Get the response
		    BufferedReader rd = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));

		    String response = "";
		    String line;
		    while ((line = rd.readLine()) != null) {
		        response += line;
		    }
		    rd.close();
		    return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
