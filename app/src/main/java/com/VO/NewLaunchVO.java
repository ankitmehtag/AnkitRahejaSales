package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewLaunchVO extends BaseVO{
	
	private ArrayList<NewLaunch> arrNewLaunch;

	public NewLaunchVO(JSONObject obj) {
		super(obj);
		
		ArrayList<NewLaunch> arrVO = null;
		
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("data");
			if(arrJson!= null ){
				arrVO = new ArrayList<NewLaunch>();
				for (int i = 0; i < arrJson.length(); i++) {
					NewLaunch launch = new NewLaunch(arrJson.optJSONObject(i));
					arrVO.add(launch);
				}
				this.setArrNewLaunch(arrVO);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	public ArrayList<NewLaunch> getArrNewLaunch() {
		return arrNewLaunch;
	}

	public void setArrNewLaunch(ArrayList<NewLaunch> arrNewLaunch) {
		this.arrNewLaunch = arrNewLaunch;
	}

}
