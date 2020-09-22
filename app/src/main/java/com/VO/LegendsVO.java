package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class LegendsVO implements Parcelable{

	private int min;
	private int max;
	
	public LegendsVO(JSONObject obj){
		setMin(obj.optInt("min"));
		setMax(obj.optInt("max"));
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(getMin());
		dest.writeInt(getMax());
	}
	
	
	public static final Parcelable.Creator<LegendsVO> CREATOR = new Parcelable.Creator<LegendsVO>() {
		public LegendsVO[] newArray(int size) {
			return new LegendsVO[size];
		}

		public LegendsVO createFromParcel(Parcel in) {
			return new LegendsVO(in);
		}
	};
	
	private LegendsVO(Parcel in){
		setMin(in.readInt());
		setMax(in.readInt());
	}
}
