package com.VO;

import java.io.Serializable;

import org.json.JSONObject;

public class CityVO implements Serializable {
	private String name;
	private String cityId;

	public CityVO(JSONObject obj) {
		this.setName(obj.optString("city_name"));
		this.setCityId(obj.optString("city_id"));
	}
	public CityVO(String cityname, String cityId) {
		this.setName(cityname);
		this.setCityId(cityId);
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

}
