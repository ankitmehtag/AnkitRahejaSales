package com.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.exception.BMHException;
import com.helper.ContentLoader;

public class ForgotModel {
	public JSONObject send(String url,String param){
		
		System.out.println("hs serverHit= " + url);
		System.out.println("hs value= " + param);
		String response = null;
		try {
			response = ContentLoader.getJsonUsingPost(url, param);
		} catch (BMHException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}