package com.VO;

import org.json.JSONObject;

public class BuilderVO {

	private String id;
	private String name;
	
	public BuilderVO(JSONObject o){
		setId(o.optString("id"));
		setName(o.optString("name"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
