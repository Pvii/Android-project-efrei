package efrei.android.project;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class founder {
	public static ArrayList<String> parse(String page){
		ArrayList<String> articles = new ArrayList<String>();
		while(page.indexOf("<item>")!=-1){
			int pos2 = page.indexOf("<item>");
			int pos3 = page.indexOf("</item>")+"</item>".length();
			String temp = page.substring(pos2, pos3);
			articles.add(temp);
			page = page.substring(pos3);
		}
		return articles;
	}
	
	public static ArrayList<String> found_articles(String link) throws IOException{
        //Log.d("hey", "yolo");
        URL url = new URL(link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/xml");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		String output;
		String resultat ="";
		while ((output = br.readLine()) != null) {
			resultat = resultat + new String(output.getBytes(), "UTF-8");
		}
		conn.disconnect();
		return founder.parse(resultat);
	}
}
