package com.VO;

import org.json.JSONObject;

public class FlooringFittingWallsVO extends BaseVO {
	
	private String price;
	private String type;
	private String img;
	private String name;
	

	public FlooringFittingWallsVO(JSONObject obj) {
		super(obj);
		
		setPrice(obj.optString("price"));
		setType(obj.optString("type"));
		setImg(obj.optString("img"));
		setName(obj.optString("name"));
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	

}
