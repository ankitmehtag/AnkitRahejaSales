package com.pwn;

import org.json.JSONException;
import org.json.JSONObject;

import com.exception.BMHException;
import com.helper.ContentLoader;

import android.os.AsyncTask;

public class RawSendAsync extends AsyncTask{
	
	private String url;
	private String params;
	private JSONObject jsonObj = null;
	private RawSendAsynchListener listener = null;
	
	public RawSendAsync(String url, String params,RawSendAsynchListener listener){
		this.url = url;
		this.params = params;
		this.listener = listener;
	}

	@Override
	protected Object doInBackground(Object... param) {
		// TODO Auto-generated method stub
		System.out.println("hs serverHit= " + url);
		System.out.println("hs value= " + params);
		String response = null;
		try {
			response = ContentLoader.getJsonUsingPost(url, params);
		} catch (BMHException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (response == null) {
			System.out.println("hs response null");
			jsonObj = null;
		}
		System.out.println("hs response= " + response);
		try {
			jsonObj = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(final Object unused) {
		if(!isCancelled()){
			if(listener != null){
				listener.onComplete(jsonObj);
			}
		}
	}
}

