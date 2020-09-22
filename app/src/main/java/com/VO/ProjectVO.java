package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectVO implements Parcelable {

	private String id;
	private String name;
	private String builder_name;
	private String banner;
	private String address;
	private String price;
	private String unit_type;
	private boolean user_favourite = false;
	private String city_id;
	// private String city_id;
	private String exactlocation;

	public ProjectVO(JSONObject o) {
		setId(o.optString("id"));
		setName(o.optString("name"));
		setBuilder_name(o.optString("builder_name"));
		setBanner(o.optString("banner"));
		setAddress(o.optString("address"));
		setPrice(o.optString("price"));
		setUnit_type(o.optString("unit_type"));
		setUser_favourite(o.optBoolean("favorite"));
		setCity(o.optString("city_id"));
		
		setExactlocation(o.optString("exactlocation"));

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuilder_name() {
		return builder_name;
	}

	public void setBuilder_name(String builder_name) {
		this.builder_name = builder_name;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;

	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;

	}

	public boolean isUser_favourite() {
		return user_favourite;
	}

	public void setUser_favourite(boolean user_favourite) {
		this.user_favourite = user_favourite;
	}

	public String getCity() {
		return city_id;
	}

	public void setCity(String city_id) {
		this.city_id = city_id;

	}
	
	public String getExactlocation(){
		return exactlocation;
	}
	
	public void setExactlocation(String exactlocation){
		this.exactlocation = exactlocation;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getAddress());
		dest.writeString(getBanner());
		dest.writeString(getBuilder_name());
		dest.writeString(getId());
		dest.writeString(getName());
		dest.writeString(getPrice());
		dest.writeString(getUnit_type());
		dest.writeString(getExactlocation());

	}

	public static final Parcelable.Creator<ProjectVO> CREATOR = new Parcelable.Creator<ProjectVO>() {
		public ProjectVO[] newArray(int size) {
			return new ProjectVO[size];
		}

		public ProjectVO createFromParcel(Parcel in) {
			return new ProjectVO(in);
		}
	};

	private ProjectVO(Parcel in) {
		setAddress(in.readString());
		setBanner(in.readString());
		setBuilder_name(in.readString());
		setId(in.readString());
		setName(in.readString());
		setPrice(in.readString());
		setUnit_type(in.readString());
		setExactlocation(in.readString());
		

	}

	@Override
	public int describeContents() {
		return 0;
	}
}
