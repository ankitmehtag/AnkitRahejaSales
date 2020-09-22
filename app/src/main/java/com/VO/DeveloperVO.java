package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class DeveloperVO implements Parcelable{

	
	
	private String builder_id;
	private String email;
	private String logo;
	private String builder_name;
	private String establish_year;
	private String company_name;
	private String area_delevered;
	private String description;
	private String ready_to_move;
	private String new_launch;
	private String under_construction;
	private String name_builder;
	private String city_id;
	
	public DeveloperVO(JSONObject o){
		setBuilder_id(o.optString("builder_id"));
		setEmail(o.optString("email"));
		setLogo(o.optString("logo"));
		setBuilder_name(o.optString("builder_name"));
		setEstablish_year(o.optString("establish_year"));
		setCompany_name(o.optString("company_name"));
		setArea_delevered(o.optString("area_delevered"));
		setDescription(o.optString("description"));
		setReady_to_move(o.optString("ready_to_move"));
		setNew_launch(o.optString("new_launch"));
		setUnder_construction(o.optString("under_construction"));
		setName_builder(o.optString("name_builder"));
		setCity_id(o.optString("city_id"));
		
	}
	public String getBuilder_id() {
		return builder_id;
	}
	public void setBuilder_id(String builder_id) {
		this.builder_id = builder_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBuilder_name() {
		return builder_name;
	}
	public void setBuilder_name(String builder_name) {
		this.builder_name = builder_name;
	}
	public String getEstablish_year() {
		return establish_year;
	}
	public void setEstablish_year(String establish_year) {
		this.establish_year = establish_year;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getArea_delevered() {
		return area_delevered;
	}
	public void setArea_delevered(String area_delevered) {
		this.area_delevered = area_delevered;
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	//=============================
	public String getReady_to_move() {
		return ready_to_move;
	}
	
	public void setReady_to_move(String ready_to_move) {
		this.ready_to_move = ready_to_move;
	}
	
	
	public String getNew_launch() {
		return new_launch;
	}
	
	public void setNew_launch(String new_launch) {
		this.new_launch = new_launch;
	}
	
	public String getUnder_construction() {
		return under_construction;
	}
	
	public void setUnder_construction(String under_construction) {
		this.under_construction = under_construction;
	}
	
	public String getName_builder() {
		return name_builder;
	}
	
	public void setName_builder(String name_builder) {
		this.name_builder = name_builder;
	}
	
	public String getCity_id(){
		return city_id;
	}
	
	public void setCity_id(String city_id){
		this.city_id = city_id;
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getArea_delevered());
		dest.writeString(getBuilder_id());
		dest.writeString(getBuilder_name());
		dest.writeString(getCompany_name());
		dest.writeString(getDescription());
		dest.writeString(getEmail());
		dest.writeString(getEstablish_year());
		dest.writeString(getLogo());
		dest.writeString(getReady_to_move());
		dest.writeString(getNew_launch());
		dest.writeString(getUnder_construction());
		dest.writeString(getName_builder());
		dest.writeString(getCity_id());
		
	}
	
	public static final Parcelable.Creator<DeveloperVO> CREATOR = new Parcelable.Creator<DeveloperVO>() {
		public DeveloperVO[] newArray(int size) {
			return new DeveloperVO[size];
		}

		public DeveloperVO createFromParcel(Parcel in) {
			return new DeveloperVO(in);
		}
	};
	
	private DeveloperVO(Parcel in){
		setArea_delevered(in.readString());
		setBuilder_id(in.readString());
		setBuilder_name(in.readString());
		setCompany_name(in.readString());
		setDescription(in.readString());
		setEmail(in.readString());
		setEstablish_year(in.readString());
		setLogo(in.readString());
		setReady_to_move(in.readString());
		setNew_launch(in.readString());
		setUnder_construction(in.readString());
		setName_builder(in.readString());
		setCity_id(in.readString());
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
