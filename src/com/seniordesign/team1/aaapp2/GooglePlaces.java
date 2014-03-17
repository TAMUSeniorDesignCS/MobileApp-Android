package com.seniordesign.team1.aaapp2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.location.Location;
import android.util.Base64;

/*
 * I have serious reservations about doing this. It really should be implemented on the server side instead of client side. Implementing it client side exposes the API key.
 */
public class GooglePlaces {
	private Location loc;
	private int radius;
	private String privKey;
	private boolean sensor;
	
	private static String googleJSONUrlString = "https://maps.google.com/maps/api/place/search/json";
	
	/*
	 * http://blog.brianbuikema.com/2010/08/android-development-part-1-using-googles-places-api-to-develop-compelling-location-based-mobile-applications/
	 */
	private class UrlSigner{
		//aparently, we won't need this because the places api does not use a signature...
		private byte[] key;
		public UrlSigner(String keyString) throws IOException {
			// Convert the key from 'web safe' base 64 to binary
			keyString = keyString.replace('-', '+');
			keyString = keyString.replace('_', '/');
			System.out.println("Key: " + keyString);
			this.key = Base64.decode(keyString, Base64.DEFAULT);
		}
		public String signRequest(String path, String query) throws NoSuchAlgorithmException,
		 InvalidKeyException, UnsupportedEncodingException, URISyntaxException {

			// Retrieve the proper URL components to sign
			String resource = path + '?' + query;

			// Get an HMAC-SHA1 signing key from the raw key bytes
			SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

			// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(sha1Key);

			// compute the binary signature for the request
			byte[] sigBytes = mac.doFinal(resource.getBytes());

			// base 64 encode the binary signature
			String signature = Base64.encodeToString(sigBytes,Base64.DEFAULT);

			// convert the signature to 'web safe' base 64
			signature = signature.replace('+', '-');
			signature = signature.replace('/', '_');

			return resource + "&signature=" + signature;
		 }
	}
	
	

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
