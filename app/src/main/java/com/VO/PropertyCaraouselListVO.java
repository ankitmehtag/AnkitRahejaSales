package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PropertyCaraouselListVO extends BaseVO{

private ArrayList<PropertyCaraouselVO> propertiesArr;
	
	public PropertyCaraouselListVO(JSONObject obj) {
		super(obj);
		ArrayList<PropertyCaraouselVO> propertiesArr = new ArrayList<PropertyCaraouselVO>();
		JSONArray arrJson = obj.optJSONArray("data");
		if(arrJson!=null){
			for (int i = 0; i < arrJson.length(); i++) {
				try {
					PropertyCaraouselVO vo = new PropertyCaraouselVO(arrJson.getJSONObject(i));
					propertiesArr.add(vo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.setPropertiesArr(propertiesArr);
		}
	}

	public ArrayList<PropertyCaraouselVO> getPropertiesArr() {
		return propertiesArr;
	}

	public void setPropertiesArr(ArrayList<PropertyCaraouselVO> propertiesArr) {
		this.propertiesArr = propertiesArr;
	}

	
}
