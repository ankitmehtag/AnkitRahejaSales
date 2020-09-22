package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class BoundariesVO implements Parcelable{

	private String lat;
	private String lon;
	
	public BoundariesVO (JSONObject obj){
		setLat(obj.optString("lat"));
		setLon(obj.optString("long"));
	}
	
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getLat());
		dest.writeString(getLon());
	}
	
	
	public static final Parcelable.Creator<BoundariesVO> CREATOR = new Parcelable.Creator<BoundariesVO>() {
		public BoundariesVO[] newArray(int size) {
			return new BoundariesVO[size];
		}

		public BoundariesVO createFromParcel(Parcel in) {
			return new BoundariesVO(in);
		}
	};
	
	private BoundariesVO(Parcel in){
		setLat(in.readString());
		setLon(in.readString());
		
	}
	
}
