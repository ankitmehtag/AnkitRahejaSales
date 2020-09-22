package com.VO;

public class PlaceVo {

	private String name;
	private String queryString;
	private String image;
	
	public PlaceVo(String name, String query){
		setName(name);
		setQueryString(query);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}
