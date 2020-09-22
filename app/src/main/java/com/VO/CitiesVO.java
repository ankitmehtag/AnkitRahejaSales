package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CitiesVO extends BaseVO{

	private ArrayList<CityVO> arrCity;
	
	public CitiesVO(JSONObject obj) {
		super(obj);
		ArrayList<CityVO> arrCityVo = null;
		
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("city_list");
			if(arrJson!= null ){
				arrCityVo = new ArrayList<CityVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					CityVO cityvo = new CityVO(arrJson.optJSONObject(i));
					arrCityVo.add(cityvo);
				}
				this.setArrCity(arrCityVo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
	}
	public ArrayList<CityVO> getArrCity() {
		return arrCity;
	}
	public void setArrCity(ArrayList<CityVO> arrCity) {
		this.arrCity = arrCity;
	}
	
}
