package com.VO;

import org.json.JSONObject;

import java.io.Serializable;

public class UnitType  implements Serializable{
	
	private String total_units;
	private String flat_typology;
	private String flat_unit_prop_type;
	private String unit_ids;
	private String total_types;
	private String area_range;
	private String price_range;
	private String per_square_price_range;
	private String std_unit_size;
	private String per_square_price_range_unit;
	private String static_unit; //YES or No Value will come from backend
	private String isLotteryProject;

	public String getIsLotteryProject() {
		return isLotteryProject;
	}

	public void setIsLotteryProject(String isLotteryProject) {
		this.isLotteryProject = isLotteryProject;
	}

	public UnitType(JSONObject obj){
		setTotal_units(obj.optString("total_units"));
		setFlat_typology(obj.optString("wpcf_flat_typology"));
		setFlat_unit_prop_type(obj.optString("wpcf_flat_unit_prop_type"));
		setUnit_ids(obj.optString("unit_ids"));
		setTotal_types(obj.optString("total_types"));
		setArea_range(obj.optString("area_range"));
		setStd_unit_size(obj.optString("std_unit_size"));
		setPer_square_price_range_unit(obj.optString("per_square_price_range_unit"));
		setPrice_range(obj.optString("price_range"));
		setPer_square_price_range(obj.optString("per_square_price_range"));
		setStatic_unit(obj.optString("static_unit"));
		setIsLotteryProject(obj.optString("isLotteryProject"));
	}

	public String getStatic_unit() {
		return static_unit;
	}

	public void setStatic_unit(String static_unit) {
		this.static_unit = static_unit;
	}

	public String getPer_square_price_range_unit() {
		return per_square_price_range_unit;
	}

	public void setPer_square_price_range_unit(String per_square_price_range_unit) {
		this.per_square_price_range_unit = per_square_price_range_unit;
	}

	public String getStd_unit_size() {
		return std_unit_size;
	}

	public void setStd_unit_size(String std_unit_size) {
		this.std_unit_size = std_unit_size;
	}

	public String getTotal_units() {
		return total_units;
	}

	public void setTotal_units(String total_units) {
		this.total_units = total_units;
	}


	public String getUnit_ids() {
		return unit_ids;
	}

	public void setUnit_ids(String unit_ids) {
		this.unit_ids = unit_ids;
	}

	public String getTotal_types() {
		return total_types;
	}

	public void setTotal_types(String total_types) {
		this.total_types = total_types;
	}

	public String getArea_range() {
		return area_range;
	}

	public void setArea_range(String area_range) {
		this.area_range = area_range;
	}

	public String getPrice_range() {
		return price_range;
	}

	public void setPrice_range(String price_range) {
		this.price_range = price_range;
	}

	public String getFlat_typology() {
		return flat_typology;
	}

	public void setFlat_typology(String flat_typology) {
		this.flat_typology = flat_typology;
	}

	public String getFlat_unit_prop_type() {
		return flat_unit_prop_type;
	}

	public void setFlat_unit_prop_type(String flat_unit_prop_type) {
		this.flat_unit_prop_type = flat_unit_prop_type;
	}

	public String getPer_square_price_range() {
		return per_square_price_range;
	}

	public void setPer_square_price_range(String per_square_price_range) {
		this.per_square_price_range = per_square_price_range;
	}
	
	
}
