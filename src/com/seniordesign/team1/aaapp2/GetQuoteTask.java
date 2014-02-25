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
			String title = "";
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
			//String[] r3 = r1[1].split("<tbody>");
			title = r2[4].split("</?td.+?>")[1];
			quote = r2[6].split("</?i>")[1] + "\n" + r2[8].split("</?td.+?>")[1] + "\n" + r2[10].split("</?td.+?>")[1]; //builds the multiple-paragraph quote
			quote += "+++" + title;
		}
		return quote;
	}
}