package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotosVO extends BaseVO{

	private ArrayList<PropertyVO> arrPhoto;
	
	public PhotosVO(JSONObject obj) {
		super(obj);
		ArrayList<PropertyVO> arrPhotoVo = null;
		
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("data");
			if(arrJson!= null ){
				arrPhotoVo = new ArrayList<PropertyVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					PropertyVO photovo = new PropertyVO(arrJson.optJSONObject(i));
					arrPhotoVo.add(photovo);
				}
				this.setArrPhoto(arrPhotoVo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
	}
	public ArrayList<PropertyVO> getArrPhoto() {
		return arrPhoto;
	}
	public void setArrPhoto(ArrayList<PropertyVO> arrPhoto) {
		this.arrPhoto = arrPhoto;
	}
	
}
