package com.seniordesign.team1.aaapp2;

import java.util.ArrayList;
import java.util.List;

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
	
	//http://stackoverflow.com/questions/3639031/why-isnt-there-a-removeint-position-method-in-androids-jsonarray
	public static JSONArray remove(final int idx, final JSONArray from) {
	    final List<JSONObject> objs = asList(from);
	    objs.remove(idx);

	    final JSONArray ja = new JSONArray();
	    for (final JSONObject obj : objs) {
	        ja.put(obj);
	    }

	    return ja;
	}
	
	//http://stackoverflow.com/questions/3639031/why-isnt-there-a-removeint-position-method-in-androids-jsonarray
	public static List<JSONObject> asList(final JSONArray ja) {
	    final int len = ja.length();
	    final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
	    for (int i = 0; i < len; i++) {
	        final JSONObject obj = ja.optJSONObject(i);
	        if (obj != null) {
	            result.add(obj);
	        }
	    }
	    return result;
	}
	
	public static int boolToInt(boolean Bool){
		int value;
		if(Bool){
			value = 1;
		}else{
			value = 0;
		}
		return value;
	}
}
