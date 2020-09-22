package com.VO;

import org.json.JSONObject;

public class AcVO {

	private boolean central;
	private boolean vrv;
	private boolean split;
	private boolean none;
	
	public AcVO(JSONObject obj){
		setCentral(obj.optBoolean("central"));
		setVrv(obj.optBoolean("vrv"));
		setSplit(obj.optBoolean("split"));
		setNone(obj.optBoolean("none"));
	}

	public boolean isCentral() {
		return central;
	}

	public void setCentral(boolean central) {
		this.central = central;
	}

	public boolean isVrv() {
		return vrv;
	}

	public void setVrv(boolean vrv) {
		this.vrv = vrv;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public boolean isNone() {
		return none;
	}

	public void setNone(boolean none) {
		this.none = none;
	}
	
}
