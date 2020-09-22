package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class BoundaryUnitVO implements Parcelable {

	private double lat;
	private double lon;
	
	
	public BoundaryUnitVO(JSONObject obj){
		String s = obj.optString("langitude");
		if(s!=null && s.length()>0){
			setLat(Double.parseDouble(s));
		}
		String s2 = obj.optString("longitude");
		if(s2!=null && s2.length()>0){
			setLon(Double.parseDouble(s2));
		}
	}

	protected BoundaryUnitVO(Parcel in) {
		lat = in.readDouble();
		lon = in.readDouble();
	}

	public static final Creator<BoundaryUnitVO> CREATOR = new Creator<BoundaryUnitVO>() {
		@Override
		public BoundaryUnitVO createFromParcel(Parcel in) {
			return new BoundaryUnitVO(in);
		}

		@Override
		public BoundaryUnitVO[] newArray(int size) {
			return new BoundaryUnitVO[size];
		}
	};

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


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(lat);
		dest.writeDouble(lon);
	}
}
