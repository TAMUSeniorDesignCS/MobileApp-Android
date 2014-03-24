package com.seniordesign.team1.aaapp2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class NetworkAsyncTask extends AsyncTask<String, Void, String> {
	private static final String quoteURL = "http://www.aa.org/lang/en/aareflections.cfm";
	private static final  String serverURL = "http://ec2-54-201-163-32.us-west-2.compute.amazonaws.com:80/";
	public static final String serverLit = "server::";
	public static final String quoteLit = "quote";
	
	private String ret;
	
	public NetworkAsyncTask(){
		super();
		ret = "";
	}
	
	@Override
	protected String doInBackground(String... urls){
		
		for(String url : urls){
			/*
			 * WHY!!! JAVA 6!!!!!!!!
			 */
			if(url.equals(quoteLit)){
				String quote = "";
				String title = "";
				String response = doNetworkCall(quoteURL);
				String[] r1 = response.split("<div id=\"content\" align=\"center\">");
				String[] r2 = r1[1].split("<tr>");
				title = r2[4].split("</?td.+?>")[1];
				quote = "\"" + r2[6].split("</?i>")[1] + "\"" + "\n\n" + r2[8].split("</?td.+?>")[1] + "\n\n" + r2[10].split("</?td.+?>")[1]; //builds the multiple-paragraph quote
				quote += "+++" + title;
				retAppend(quote);
			}
			//if starts with serverLit, then it's a server command
			else if(url.length() > serverLit.length() && url.substring(0, serverLit.length()).equals(serverLit)){
				String command = url.substring(serverLit.length());
				retAppend(doNetworkCall(serverURL + command));
			}
			else{
				retAppend(doNetworkCall(url));
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
			InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
		} 
		catch(Exception e){
			e.printStackTrace();
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
}
