package com.VO;

import java.io.Serializable;

import org.json.JSONObject;

public class AreaVO implements Serializable{
	private String name;
	private String Id;

	public AreaVO(JSONObject obj) {
		this.setName(obj.optString("area_name"));
		this.setId(obj.optString("area_id"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}


}
