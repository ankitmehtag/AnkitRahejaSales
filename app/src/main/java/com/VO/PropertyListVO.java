package com.VO;

import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PropertyListVO extends BaseVO implements Parcelable{

	private ArrayList<PropertyVO> propertiesArr;
	
	public PropertyListVO(JSONObject obj) {
		super(obj);
		ArrayList<PropertyVO> propertiesArr = new ArrayList<PropertyVO>();
		JSONArray arrJson = obj.optJSONArray("data");
		if(arrJson!=null){
			for (int i = 0; i < arrJson.length(); i++) {
				try {
					PropertyVO vo = new PropertyVO(arrJson.getJSONObject(i));
					propertiesArr.add(vo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			this.setPropertiesArr(propertiesArr);
		}
	}


	public ArrayList<PropertyVO> getPropertiesArr() {
		return propertiesArr;
	}


	public void setPropertiesArr(ArrayList<PropertyVO> propertiesArr) {
		this.propertiesArr = propertiesArr;
	}

}
