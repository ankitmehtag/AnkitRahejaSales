package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnitTypesVO extends BaseVO{

	private ArrayList<UnitType> arrUnitType;
	
	public UnitTypesVO(JSONObject obj) {
		super(obj);
		
		
		ArrayList<UnitType> arrVO = null;
		
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("data");
			if(arrJson!= null ){
				arrVO = new ArrayList<UnitType>();
				for (int i = 0; i < arrJson.length(); i++) {
					UnitType launch = new UnitType(arrJson.optJSONObject(i));
					arrVO.add(launch);
				}
				this.setArrUnitType(arrVO);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<UnitType> getArrUnitType() {
		return arrUnitType;
	}

	public void setArrUnitType(ArrayList<UnitType> arrUnitType) {
		this.arrUnitType = arrUnitType;
	}
	
	

}
