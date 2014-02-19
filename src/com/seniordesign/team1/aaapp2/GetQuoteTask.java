package com.seniordesign.team1.aaapp2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

class GetQuoteTask extends AsyncTask<String,Void,String>{
	@Override
	protected String doInBackground(String... urls){
		String quote = "";
		for(String url : urls){
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
			String[] r1 = response.split("<div id=\"content\" align=\"center\">");
			String[] r2 = r1[1].split("<tr>");
			quote = r2[6].split("</?i>")[1];
		}
		return quote;
	}
}