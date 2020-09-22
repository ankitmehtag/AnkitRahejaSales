package com.VO;

import org.json.JSONObject;

public class FacitlitiesVO {
	
	private boolean wardrobs;
	private boolean servent_room;
	private boolean pooja_room;
	private boolean washing_area;
	private boolean piped_gas;
	private boolean lifts;
	private boolean stores;
	private boolean furnished;
	private boolean automation;
	private boolean centralwifi;
	private boolean highspeedinternet;
	
	
	public FacitlitiesVO(JSONObject obj) {
		setWardrobs(obj.optBoolean("wardrobes"));
		setServent_room(obj.optBoolean("servent_room"));
		setPooja_room(obj.optBoolean("pooja_room"));
		setWashing_area(obj.optBoolean("washing_area"));
		setPiped_gas(obj.optBoolean("piped_gas"));
		setLifts(obj.optBoolean("lifts"));
		setStores(obj.optBoolean("stores"));
		setFurnished(obj.optBoolean("furnished"));
		setAutomation(obj.optBoolean("automation"));
		setCentralwifi(obj.optBoolean("centralwifi"));
		setHighspeedinternet(obj.optBoolean("highspeedinternet"));
	}
	
	
	public boolean isWardrobs() {
		return wardrobs;
	}
	
	public void setWardrobs(boolean wardrobs) {
		this.wardrobs = wardrobs;
	}
	public boolean isServent_room() {
		return servent_room;
	}
	public void setServent_room(boolean servent_room) {
		this.servent_room = servent_room;
	}
	public boolean isPooja_room() {
		return pooja_room;
	}
	public void setPooja_room(boolean pooja_room) {
		this.pooja_room = pooja_room;
	}
	public boolean isWashing_area() {
		return washing_area;
	}
	public void setWashing_area(boolean washing_area) {
		this.washing_area = washing_area;
	}
	public boolean isPiped_gas() {
		return piped_gas;
	}
	public void setPiped_gas(boolean piped_gas) {
		this.piped_gas = piped_gas;
	}
	public boolean isLifts() {
		return lifts;
	}
	public void setLifts(boolean lifts) {
		this.lifts = lifts;
	}
	public boolean isStores() {
		return stores;
	}
	public void setStores(boolean stores) {
		this.stores = stores;
	}
	public boolean isFurnished() {
		return furnished;
	}
	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}
	public boolean isAutomation() {
		return automation;
	}
	public void setAutomation(boolean automation) {
		this.automation = automation;
	}
	public boolean isCentralwifi() {
		return centralwifi;
	}
	public void setCentralwifi(boolean centralwifi) {
		this.centralwifi = centralwifi;
	}
	public boolean isHighspeedinternet() {
		return highspeedinternet;
	}
	public void setHighspeedinternet(boolean highspeedinternet) {
		this.highspeedinternet = highspeedinternet;
	}
	

	
}
