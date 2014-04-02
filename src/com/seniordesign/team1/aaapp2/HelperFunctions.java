package com.seniordesign.team1.aaapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HelperFunctions {
	public static boolean isJSONValid(JSONArray jsonArray){
		boolean isValid = false;
		try{
			JSONObject validJson = jsonArray.getJSONObject(jsonArray.length()-1);
			isValid = validJson.getBoolean("valid");
		}
		catch(JSONException e){
			isValid = false;
		}
		return isValid;
	}
}
