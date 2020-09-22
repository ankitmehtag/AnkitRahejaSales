package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildersVO extends BaseVO {

	private ArrayList<BuilderVO> arrBuilders;
	
	public BuildersVO(JSONObject o) {
		super(o);
		
		JSONArray arrJson;
		try {
			arrJson = o.getJSONArray("data");
			if(arrJson!= null ){
				arrBuilders = new ArrayList<BuilderVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					BuilderVO cityvo = new BuilderVO(arrJson.optJSONObject(i));
					arrBuilders.add(cityvo);
				}
				this.setArrBuilders(arrBuilders);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	public ArrayList<BuilderVO> getArrBuilders() {
		return arrBuilders;
	}

	public void setArrBuilders(ArrayList<BuilderVO> arrBuilders) {
		this.arrBuilders = arrBuilders;
	}


	
	
}
