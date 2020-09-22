package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnitCaraouselListVO extends BaseVO{

	private ArrayList<UnitCaraouselVO> arrUnits;
	public UnitCaraouselListVO(JSONObject obj) {
		super(obj);
		
		ArrayList<UnitCaraouselVO> propertiesArr = new ArrayList<UnitCaraouselVO>();
		JSONArray arrJson = obj.optJSONArray("data");
		if(arrJson!=null){
			for (int i = 0; i < arrJson.length(); i++) {
				try {
					UnitCaraouselVO vo = new UnitCaraouselVO(arrJson.getJSONObject(i));
					propertiesArr.add(vo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.setArrUnits(propertiesArr);
		}
	}
	
	public ArrayList<UnitCaraouselVO> getArrUnits() {
		return arrUnits;
	}
	public void setArrUnits(ArrayList<UnitCaraouselVO> arrUnits) {
		this.arrUnits = arrUnits;
	}

}
