package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnitCaraouselVO implements Parcelable{

	private String id;
	private String wpcf_flat_unit_with;
	private String unit_title;
	private String flat_tower;
	private String flat_typology;
	private String flat_floor;
	private String flat_price;
	private String flat_price_SqFt;
	private String image_unit;
	private String wpcf_flat_unit_category;
	private String wpcf_flat_unit_type;
	private String wpcf_flat_size;
	private String wpcf_flat_size_unit;
	private String sold_status;
	
	private String unit_no;
	private String lifestyle;
	private String total_floor;
	private String price_SqFt;
	private String price_SqFt_unit;
	private ArrayList<BoundaryUnitVO> arrUnitBoundary;
	private String wpcf_flat_floor;
	private String wpcf_flat_price_plc;
	private String wpcf_flat_price_SqFt;
	private String wpcf_prop_price_persq;
	private String total_types;
	private boolean user_favourite = false;

	public String getWpcf_flat_size_unit() {
		return wpcf_flat_size_unit;
	}

	public void setWpcf_flat_size_unit(String wpcf_flat_size_unit) {
		this.wpcf_flat_size_unit = wpcf_flat_size_unit;
	}

	public UnitCaraouselVO(JSONObject objJson){
		if(objJson == null || objJson.toString().isEmpty())return;
		this.setUnit_id(objJson.optString("unit_id"));
		this.setWpcf_flat_unit_with(objJson.optString("wpcf_flat_unit_with"));
		this.setUnit_title(objJson.optString("unit_title"));
		this.setFlat_tower(objJson.optString("wpcf_flat_tower"));
		this.setFlat_typology(objJson.optString("wpcf_flat_typology"));
		this.setFlat_floor(objJson.optString("wpcf_flat_floor"));
		this.setFlat_price(objJson.optString("wpcf_flat_price"));
		this.setFlat_price_SqFt(objJson.optString("wpcf_flat_price_SqFt"));
		this.setImage_unit(objJson.optString("image_unit"));
		this.setWpcf_flat_unit_category(objJson.optString("wpcf_flat_unit_category"));
		this.setWpcf_flat_size(objJson.optString("wpcf_flat_size"));
		this.setWpcf_flat_size_unit(objJson.optString("wpcf_flat_size_unit"));
		this.setWpcf_flat_unit_type(objJson.optString("wpcf_flat_unit_type"));
		this.setSold_status(objJson.optString("sold_status"));
		
		this.setUnit_no(objJson.optString("wpcf_flat_unit_no"));
		this.setFloor_value(objJson.optString("wpcf_flat_floor"));
		this.setPlc_value(objJson.optString("wpcf_flat_price_plc"));
		this.setSq_ft(objJson.optString("wpcf_flat_price_SqFt"));
		this.setP_sf(objJson.optString("wpcf_prop_price_persq"));
		this.setLife_style(objJson.optString("lifestyle"));
		
		this.setPrice_SqFt(objJson.optString("price_SqFt"));
		this.setPrice_SqFt_unit(objJson.optString("price_SqFt_unit"));
		
		this.setTotal_floor(objJson.optString("total_floor"));
		
		this.setUser_favourite(objJson.optBoolean("user_favourite"));
		
		this.setTotal_types(objJson.optString("total_types"));
		
		JSONArray arrUnitCords = null;
		try {
			arrUnitCords = objJson.getJSONArray("unit_cords");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(arrUnitCords!=null && arrUnitCords.length()>0){
			ArrayList<BoundaryUnitVO> a = new ArrayList<BoundaryUnitVO>();
			for (int i = 0; i < arrUnitCords.length(); i++) {
				a.add(new BoundaryUnitVO(arrUnitCords.optJSONObject(i)));
			}
			setArrUnitBoundary(a);
		}
	}

	public String getPrice_SqFt_unit() {
		return price_SqFt_unit;
	}

	public void setPrice_SqFt_unit(String price_SqFt_unit) {
		this.price_SqFt_unit = price_SqFt_unit;
	}

	public String getUnit_id() {
		return id;
	}

	public void setUnit_id(String id) {
		this.id = id;
	}

	public String getUnit_title() {
		return unit_title;
	}

	public void setUnit_title(String unit_title) {
		this.unit_title = unit_title;
	}

	public String getFlat_tower() {
		return flat_tower;
	}

	public void setFlat_tower(String flat_tower) {
		this.flat_tower = flat_tower;
	}

	public String getFlat_typology() {
		return flat_typology;
	}

	public void setFlat_typology(String flat_typology) {
		this.flat_typology = flat_typology;
	}

	public String getFlat_floor() {
		return flat_floor;
	}

	public void setFlat_floor(String flat_floor) {
		this.flat_floor = flat_floor;
	}

	public String getFlat_price() {
		return flat_price;
	}

	public void setFlat_price(String flat_price) {
		this.flat_price = flat_price;
	}

	public String getFlat_price_SqFt() {
		return flat_price_SqFt;
	}

	public void setFlat_price_SqFt(String flat_price_SqFt) {
		this.flat_price_SqFt = flat_price_SqFt;
	}
	
	
	public String getPsf(){
		return wpcf_prop_price_persq;
	}
	
	public void setP_sf(String wpcf_prop_price_persq){
		this.wpcf_prop_price_persq = wpcf_prop_price_persq;
	}
	
	public String getUnit_no(){
		return unit_no;
	}
	
	public void setUnit_no(String unit_no){
		this.unit_no = unit_no;
	}
//	
	public String getFloor_value(){
		return wpcf_flat_floor;
	}
	
	public void setFloor_value(String wpcf_flat_floor){
		this.wpcf_flat_floor = wpcf_flat_floor;
	}
	
	public String getPlc_value(){
		return wpcf_flat_price_plc;
	}
	
	public void setPlc_value(String wpcf_flat_price_plc){
		this.wpcf_flat_price_plc = wpcf_flat_price_plc;
	}
	
	
	public String getSq_ft(){
		return wpcf_flat_price_SqFt;
	}
	
	public void setSq_ft(String wpcf_flat_price_SqFt){
		this.wpcf_flat_price_SqFt = wpcf_flat_price_SqFt;
	}
	
	
	public String getLife_style(){
	return lifestyle;	
	}
	
	public void setLife_style(String lifestyle){
		this.lifestyle=lifestyle;
	}
	
	public boolean isUser_favourite() {
		return user_favourite;
	}

	public void setUser_favourite(boolean user_favourite) {
		this.user_favourite = user_favourite;
	}
	
	
	
	
	
	public String getTotal_floor(){
		return total_floor;	
		}
		
		public void setTotal_floor(String total_floor){
			this.total_floor=total_floor;
		}
	
		
		
		public String getTotal_types() {
			return total_types;
		}

		public void setTotal_types(String total_types) {
			this.total_types = total_types;
		}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getUnit_id());
		dest.writeString(getWpcf_flat_unit_with());
		dest.writeString(getUnit_title());
		dest.writeString(getFlat_tower());
		dest.writeString(getFlat_typology());
		dest.writeString(getFlat_floor());
		dest.writeString(getFlat_price());
		dest.writeString(getFlat_price_SqFt());
		dest.writeString(getImage_unit());
		dest.writeString(getWpcf_flat_unit_category());
		dest.writeString(getWpcf_flat_size());
		dest.writeString(getWpcf_flat_size_unit());
		dest.writeString(getSold_status());
		dest.writeString(getUnit_no());
		dest.writeString(getLife_style());
		dest.writeString(getPlc_value());
		dest.writeString(getPsf());
		dest.writeString(getTotal_floor());
		dest.writeString(getPrice_SqFt());
		dest.writeString(getPrice_SqFt_unit());
		dest.writeInt((isUser_favourite()) ? 1 : 0);
	}
	
	public static final Parcelable.Creator<UnitCaraouselVO> CREATOR = new Parcelable.Creator<UnitCaraouselVO>() {
		public UnitCaraouselVO[] newArray(int size) {
			return new UnitCaraouselVO[size];
		}

		public UnitCaraouselVO createFromParcel(Parcel in) {
			return new UnitCaraouselVO(in);
		}
	};
	
	private UnitCaraouselVO(Parcel in){
		this.setUnit_id(in.readString());
		this.setWpcf_flat_unit_with(in.readString());
		this.setUnit_title(in.readString());
		this.setFlat_tower(in.readString());
		this.setFlat_typology(in.readString());
		this.setFlat_floor(in.readString());
		this.setFlat_price(in.readString());
		this.setFlat_price_SqFt(in.readString());
		this.setImage_unit(in.readString());
		this.setWpcf_flat_unit_category(in.readString());
		this.setWpcf_flat_size(in.readString());
		this.setWpcf_flat_size_unit(in.readString());
		this.setSold_status(in.readString());
		this.setUnit_no(in.readString());
		this.setLife_style(in.readString());
		this.setPlc_value(in.readString());
		this.setP_sf(in.readString());
		this.setTotal_floor(in.readString());
		this.setPrice_SqFt(in.readString());
		this.setPrice_SqFt_unit(in.readString());
		this.setUser_favourite( (in.readInt() == 0) ? false : true);
		}

	public String getImage_unit() {
		return image_unit;
	}

	public void setImage_unit(String image_unit) {
		this.image_unit = image_unit;
	}

	public String getWpcf_flat_unit_category() {
		return wpcf_flat_unit_category;
	}

	public void setWpcf_flat_unit_category(String wpcf_flat_unit_category) {
		this.wpcf_flat_unit_category = wpcf_flat_unit_category;
	}

	public String getWpcf_flat_size() {
		return wpcf_flat_size;
	}

	public void setWpcf_flat_size(String wpcf_flat_size) {
		this.wpcf_flat_size = wpcf_flat_size;
	}

	public ArrayList<BoundaryUnitVO> getArrUnitBoundary() {
		return arrUnitBoundary;
	}

	public void setArrUnitBoundary(ArrayList<BoundaryUnitVO> arrUnitBoundary) {
		this.arrUnitBoundary = arrUnitBoundary;
	}

	public String getWpcf_flat_unit_type() {
		return wpcf_flat_unit_type;
	}

	public void setWpcf_flat_unit_type(String wpcf_flat_unit_type) {
		this.wpcf_flat_unit_type = wpcf_flat_unit_type;
	}

	public String getWpcf_flat_unit_with() {
		return wpcf_flat_unit_with;
	}

	public void setWpcf_flat_unit_with(String wpcf_flat_unit_with) {
		this.wpcf_flat_unit_with = wpcf_flat_unit_with;
	}

	public String getSold_status() {
		return sold_status;
	}

	public void setSold_status(String sold_status) {
		this.sold_status = sold_status;
	}
	
	public String getPrice_SqFt() {
		return price_SqFt;
	}

	public void setPrice_SqFt(String price_SqFt) {
		this.price_SqFt = price_SqFt;
	}

	public UnitCaraouselVO getDecimalFormatedPrice(float intPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	


	
}
