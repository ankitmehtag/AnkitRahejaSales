package com.filter;

import java.io.Serializable;

public class Builder implements Serializable {
	private String name = "";
	private String tag = "";
	private String builderId = "";
	private boolean checked = false;

	public Builder() {
	}

	public Builder(String builderId,String builderName,boolean checked){
		this.builderId = builderId;
		this.name = builderName;
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}

	public String getId(){
		return builderId+"";
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		return name;
	}

	public void toggleChecked() {
		checked = !checked;
	}
}