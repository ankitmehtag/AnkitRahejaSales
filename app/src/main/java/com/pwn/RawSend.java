package com.pwn;

import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.helper.ContentLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class RawSend {
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
	
	public JSONObject sendThroughThread(String url,String param){
		
		CustomAsyncTask loadingTask = new CustomAsyncTask(null, new AsyncListner() {
			JSONObject jsonObj = null;
			@Override
			public void DoBackgroundTask(String[] url) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnBackgroundTaskCompleted() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnPreExec() {
				// TODO Auto-generated method stub
				
			}
			
		
		});
		loadingTask.execute();
		return null;
	}
}
