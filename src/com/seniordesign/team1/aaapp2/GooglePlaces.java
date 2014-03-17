package com.seniordesign.team1.aaapp2;

import java.net.MalformedURLException;
import android.location.Location;

/*
 * I have serious reservations about doing this. It really should be implemented on the server side instead of client side. Implementing it client side exposes the API key.
 */
public class GooglePlaces {
	private Location loc;
	private int radius;
	private String privKey;
	private boolean sensor;
	
	private static String googleJSONUrlString = "https://maps.google.com/maps/api/place/search/json";
	

	public GooglePlaces(Location loc, int radius,
			String privKey, boolean sensor) throws MalformedURLException {
		super();
		this.loc = loc;
		this.radius = radius;
		this.privKey = privKey;
		this.sensor = sensor;
	}


	public Location getLoc() {
		return loc;
	}


	public void setLoc(Location loc) {
		this.loc = loc;
	}


	public int getRadius() {
		return radius;
	}


	public void setRadius(int radius) {
		this.radius = radius;
	}


	public String getPrivKey() {
		return privKey;
	}


	public void setPrivKey(String privKey) {
		this.privKey = privKey;
	}


	public boolean isSensor() {
		return sensor;
	}


	public void setSensor(boolean sensor) {
		this.sensor = sensor;
	}
	
	public String sendRequest(){
		String urlString = new String(GooglePlaces.googleJSONUrlString);
		urlString += "?location=" + this.loc.getLatitude() + "," + this.loc.getLongitude();
		urlString += "&radius=" + this.radius;
		urlString += "&sensor=" + this.sensor;
		urlString += "&key=" + this.privKey;
		//TODO: use the web call class to call the api
		return "OMGWTF";
	}
}
