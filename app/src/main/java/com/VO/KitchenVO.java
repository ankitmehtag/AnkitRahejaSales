package com.VO;

import org.json.JSONObject;

public class KitchenVO {
	
	private boolean ImportedModular;
	private boolean IndianModular;
	private boolean Microwave;
	private boolean InBuiltRefrigerator;
	private boolean Refrigerator;
	private boolean GraniteTop;
	private boolean ROFilter;
	private boolean none;

	public KitchenVO(JSONObject obj){
		setImportedModular(obj.optBoolean("ImportedModular"));
		setIndianModular(obj.optBoolean("IndianModular"));
		setMicrowave(obj.optBoolean("Microwave"));
		setInBuiltRefrigerator(obj.optBoolean("InBuiltRefrigerator"));
		setRefrigerator(obj.optBoolean("Refrigerator"));
		setGraniteTop(obj.optBoolean("GraniteTop"));
		setROFilter(obj.optBoolean("ROFilter"));
		setNone(obj.optBoolean("none"));
	}

	public boolean isImportedModular() {
		return ImportedModular;
	}

	public void setImportedModular(boolean importedModular) {
		ImportedModular = importedModular;
	}

	public boolean isIndianModular() {
		return IndianModular;
	}

	public void setIndianModular(boolean indianModular) {
		IndianModular = indianModular;
	}

	public boolean isMicrowave() {
		return Microwave;
	}

	public void setMicrowave(boolean microwave) {
		Microwave = microwave;
	}

	public boolean isInBuiltRefrigerator() {
		return InBuiltRefrigerator;
	}

	public void setInBuiltRefrigerator(boolean inBuiltRefrigerator) {
		InBuiltRefrigerator = inBuiltRefrigerator;
	}

	public boolean isRefrigerator() {
		return Refrigerator;
	}

	public void setRefrigerator(boolean refrigerator) {
		Refrigerator = refrigerator;
	}

	public boolean isGraniteTop() {
		return GraniteTop;
	}

	public void setGraniteTop(boolean graniteTop) {
		GraniteTop = graniteTop;
	}

	public boolean isROFilter() {
		return ROFilter;
	}

	public void setROFilter(boolean rOFilter) {
		ROFilter = rOFilter;
	}

	public boolean isNone() {
		return none;
	}

	public void setNone(boolean none) {
		this.none = none;
	}
}
