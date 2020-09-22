package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


public class PlacesLocationVO implements Parcelable{

	private double lat;
	private double lon;
	private String name;
	private String vicinity;
	private String project_name;
	
	public PlacesLocationVO(){
		
	}
	public PlacesLocationVO(JSONObject obj){
		setLat(obj.optDouble("lat"));
		setLon(obj.optDouble("long"));
		setName(obj.optString("location_name"));
		setName(obj.optString(project_name));
		setVicinity("");
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	
	public String getproject_name() {
		return project_name;
	}

	public void setproject_name(String project_name) {
		this.project_name = project_name;
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(getLat());
		dest.writeDouble(getLon());
		dest.writeString(getName());
		dest.writeString(getVicinity());
		dest.writeString(getproject_name());
	}
	
	public static final Parcelable.Creator<PlacesLocationVO> CREATOR = new Parcelable.Creator<PlacesLocationVO>() {
		public PlacesLocationVO[] newArray(int size) {
			return new PlacesLocationVO[size];
		}

		public PlacesLocationVO createFromParcel(Parcel in) {
			return new PlacesLocationVO(in);
		}
	};
	
	public PlacesLocationVO(Parcel in){
		setLat(in.readDouble());
		setLon(in.readDouble());
		setName(in.readString());
		setVicinity(in.readString());
		setproject_name(in.readString());
	}
	
}
