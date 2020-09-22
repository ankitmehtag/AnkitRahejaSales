package com.utils;

import java.io.Serializable;

public class LocalityData implements Serializable{

	//title,subtitle,id,buildername,location_id,location_name

	private String id;
	private String title;
	private String subtitle;
	private String buildername;
	private String location_name;

	// Local variable i.e. following keys are not comming from server
	private String city;
	private String showLocation;
	private String header;
	private String localityType;

	public String getLocalityType() {
		return localityType;
	}

	public void setLocalityType(String localityType) {
		this.localityType = localityType;
	}

	public String getLocation_name() {
		return location_name;
	}


	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getId() {
		return id;
	}

	public void setshowLocation(String showLocation) {
		this.showLocation = showLocation;
	}
	
	public String getshowLocation() {
		return showLocation;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public void setBuildername(String buildername) {
		this.buildername = buildername;
	}

	public String getBuildername() {
		return buildername;
	}
	
	
	public void setLocation_Name(String location_name) {
		this.location_name = location_name;
	}

	public String getLocation_Name() {
		return location_name;
	}
	
	public String getCity(){
		return city;
	}
	public void setCity(String city){
		this.city = city;
	}
	
	
	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", title = " + title	+ ", subtitle = " + subtitle + " , builder = "
					+ buildername + " , location = " + location_name + "]";
	}
}
