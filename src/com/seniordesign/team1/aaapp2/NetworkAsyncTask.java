package com.seniordesign.team1.aaapp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class NetworkAsyncTask extends AsyncTask<String, Void, String> {
	private static final String quoteURL = "http://www.aa.org/lang/en/aareflections.cfm";
	private static final  String serverURL = "http://ec2-54-201-163-32.us-west-2.compute.amazonaws.com:80/";
	public static final String serverLit = "server::";
	public static final String quoteLit = "quote";
	public static final String errorLit = "--error--";
	
	private String ret;
	private Context context;
	
	public NetworkAsyncTask(Context context){
		super();
		ret = "";
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... urls){
		
		for(String url : urls){
			if(isNetworkAvailable()){
				/*
				 * WHY!!! JAVA 6!!!!!!!!
				 */
				if(url.equals(quoteLit)){
					String quote = "";
					String title = "";
					String secondHalf = "";
					String response = doNetworkCall(quoteURL);
					String[] r1 = response.split("<div id=\"content\" align=\"center\">");
					String[] r2 = r1[1].split("<tr>");
					String[] r3 = r2[10].split("</?p>"); //to deal with multiple paragraphs within the second half
					if(r3.length > 1){
						for(int i = 1; i < r3.length-1; i++){ //1 to get rid of "justify", -1 to get rid of /td and /tr
							secondHalf += r3[i] + "\n";	
						}
					}else{
						secondHalf = r3[0].split("</?td.+?>")[1];
					}
					title = r2[4].split("</?td.+?>")[1];
					quote = "\"" + r2[6].split("</?i>")[1] + "\"" + "\n\n" + r2[8].split("</?td.+?>")[1] + "\n\n" + secondHalf; //builds the multiple-paragraph quote
					quote += "+++" + title;
					retAppend(quote);
				}
				//if starts with serverLit, then it's a server command
				else if(url.length() >= serverLit.length() && url.substring(0, serverLit.length()).equals(serverLit)){
					String command = url.substring(serverLit.length());
					String[] split = command.split("\\?");
					String path = split[0];
					if(split.length > 1){
						String query = split[1];
						retAppend(doNetworkCall(serverURL + path,query));
					}
					else{
						retAppend(doNetworkCall(serverURL + path));
					}
					
				}
				else{
					retAppend(doNetworkCall(url));
				}
			}
			else{
				retAppend(errorLit);
			}
		}
		return ret;
	}
	private String doNetworkCall(String url){
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try{
			
			HttpResponse execute = client.execute(httpGet);
			response = receive(execute);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	private String doNetworkCall(String url, String body){
		String response = "";
		
		JSONObject json = new JSONObject();
		if(!body.isEmpty()){
			String[] elems = body.split("&");
			for(String elem : elems){
				String[] keyVal = elem.split("=");
				try {
					json.put(keyVal[0], keyVal[1]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try{
			httpPost.setHeader( "Content-Type", "application/json" );
			httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
			HttpResponse execute = client.execute(httpPost);
			response = receive(execute);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	private String receive(HttpResponse execute) throws IOException{
		String response = "";
		InputStream content = execute.getEntity().getContent();

        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(content));
        String s = "";
        while ((s = buffer.readLine()) != null) {
            response += s;
        }
        return response;
	}
	
	private void retAppend(String app){
		if(ret.isEmpty()){
			ret = app;
		}
		else{
			ret += "===" + app;
		}
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
