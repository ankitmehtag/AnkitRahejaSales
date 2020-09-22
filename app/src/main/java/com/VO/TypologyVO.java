package com.VO;

import org.json.JSONObject;

public class TypologyVO {

	private int id;
	private String prop_size_floor;
	private String prop_type_floor;
	
	public TypologyVO(JSONObject objJson){
		this.setId(objJson.optInt("id"));
		this.setProp_size_floor(objJson.optString("prop_size_floor"));
		this.setProp_type_floor(objJson.optString("prop_type_floor"));
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProp_size_floor() {
		return prop_size_floor;
	}
	public void setProp_size_floor(String prop_size_floor) {
		this.prop_size_floor = prop_size_floor;
	}
	public String getProp_type_floor() {
		return prop_type_floor;
	}
	public void setProp_type_floor(String prop_type_floor) {
		this.prop_type_floor = prop_type_floor;
	}

	
}
