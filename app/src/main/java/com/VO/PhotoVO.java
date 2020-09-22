package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoVO implements Parcelable {

//	private String id;
	private String url;
	
	private ArrayList<PhotoVO> data;

	public PhotoVO(JSONObject obj) {
		setUrl(obj.optString("url"));
//		setId(obj.optString("id"));
		
		
		JSONArray arrjsonGellary = obj.optJSONArray("data");
		ArrayList<PhotoVO> arrDataGallery = new ArrayList<PhotoVO>();
		if (arrjsonGellary != null) {
			for (int i = 0; i < arrjsonGellary.length(); i++) {
				JSONObject obj1 = arrjsonGellary.optJSONObject(i);
				if (obj != null) {
					PhotoVO vo = new PhotoVO(obj);
					arrDataGallery.add(vo);
				}
			}
			this.setDataGellary(arrDataGallery);
		}
		
		
	}

//	public String geId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public ArrayList<PhotoVO> getDataGellary() {
		return data;
	}

	public void setDataGellary(ArrayList<PhotoVO> data) {
		this.data = data;
	}
	
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getUrl());

	}

	public static final Parcelable.Creator<PhotoVO> CREATOR = new Parcelable.Creator<PhotoVO>() {
		public PhotoVO[] newArray(int size) {
			return new PhotoVO[size];
		}

		public PhotoVO createFromParcel(Parcel in) {
			return new PhotoVO(in);
		}
	};

	private PhotoVO(Parcel in) {
		setUrl(in.readString());
	}

}
