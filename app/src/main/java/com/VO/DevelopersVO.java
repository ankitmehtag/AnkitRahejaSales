package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DevelopersVO extends BaseVO{
	
	private ArrayList<DeveloperVO> developers;
	
	public DevelopersVO(JSONObject o){
		super(o);
		ArrayList<DeveloperVO> arrCityVo = null;
		JSONArray arrJson;
		try {
			arrJson = o.getJSONArray("data");
			if(arrJson!= null ){
				arrCityVo = new ArrayList<DeveloperVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					DeveloperVO cityvo = new DeveloperVO(arrJson.optJSONObject(i));
					arrCityVo.add(cityvo);
				}
				this.setDevelopers(arrCityVo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<DeveloperVO> getDevelopers() {
		return developers;
	}

	public void setDevelopers(ArrayList<DeveloperVO> developers) {
		this.developers = developers;
	}
}
