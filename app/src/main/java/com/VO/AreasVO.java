package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AreasVO extends BaseVO{

	private ArrayList<AreaVO> arrArea;
	
	public AreasVO(JSONObject obj) {
		super(obj);
		ArrayList<AreaVO> arrCityVo = null;
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("area_list");
			if(arrJson!= null ){
				arrCityVo = new ArrayList<AreaVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					AreaVO cityvo = new AreaVO(arrJson.optJSONObject(i));
					arrCityVo.add(cityvo);
				}
				this.setArrArea(arrCityVo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<AreaVO> getArrArea() {
		return arrArea;
	}

	public void setArrArea(ArrayList<AreaVO> arrArea) {
		this.arrArea = arrArea;
	}
	
}
