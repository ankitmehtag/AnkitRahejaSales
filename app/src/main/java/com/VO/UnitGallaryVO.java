package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class UnitGallaryVO implements Parcelable {

	private String url;
	private int type;
	private int subType;
	private String image_name;

	public UnitGallaryVO(JSONObject obj) {
		setUrl(obj.optString("url"));
		setType(obj.optInt("type"));
		setSubType(obj.optInt("subtype"));
		setImage_name(obj.optString("image_name"));

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getUrl());
		dest.writeInt(getType());
		dest.writeInt(getSubType());
		dest.writeString(getImage_name());

	}

	public static final Parcelable.Creator<UnitGallaryVO> CREATOR = new Parcelable.Creator<UnitGallaryVO>() {
		public UnitGallaryVO[] newArray(int size) {
			return new UnitGallaryVO[size];
		}

		public UnitGallaryVO createFromParcel(Parcel in) {
			return new UnitGallaryVO(in);
		}
	};

	private UnitGallaryVO(Parcel in) {
		setUrl(in.readString());
		setType(in.readInt());
		setSubType(in.readInt());
		setImage_name(in.readString());

	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getUnitGellary() {
		// TODO Auto-generated method stub
		return image_name;
	}

	

}
