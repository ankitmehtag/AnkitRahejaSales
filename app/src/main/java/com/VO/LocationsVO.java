package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationsVO extends BaseVO implements Parcelable{

	private ArrayList<LocationVO> arrLocation;
	private ArrayList<LegendsVO> arrReturnsConditions;
	private ArrayList<LegendsVO> arrPriceConditions;

	public LocationsVO(JSONObject obj) {
		super(obj);

		ArrayList<LocationVO> arrLocations = null;
		JSONArray arrJson;
		try {
			arrJson = obj.getJSONArray("location_list");
			if (arrJson != null) {
				arrLocations = new ArrayList<LocationVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					LocationVO cityvo = new LocationVO(arrJson.optJSONObject(i));
					arrLocations.add(cityvo);
				}
				this.setArrLocation(arrLocations);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ArrayList<LegendsVO> arrreturns = null;
		JSONArray arrReturns;
		try {
			arrReturns = obj.getJSONArray("return_condition");
			if (arrReturns != null) {
				arrreturns = new ArrayList<LegendsVO>();
				for (int i = 0; i < arrReturns.length(); i++) {
					LegendsVO cityvo = new LegendsVO(arrReturns.optJSONObject(i));
					arrreturns.add(cityvo);
				}
				this.setArrReturnsConditions(arrreturns);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ArrayList<LegendsVO> arrPrice = null;
		JSONArray arrp;
		try {
			arrp = obj.getJSONArray("price_condition");
			if (arrp != null) {
				arrPrice = new ArrayList<LegendsVO>();
				for (int i = 0; i < arrp.length(); i++) {
					LegendsVO cityvo = new LegendsVO(arrp.optJSONObject(i));
					arrPrice.add(cityvo);
				}
				this.setArrPriceConditions(arrPrice);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<LocationVO> getArrLocation() {
		return arrLocation;
	}

	public void setArrLocation(ArrayList<LocationVO> arrLocation) {
		this.arrLocation = arrLocation;
	}

	public ArrayList<LegendsVO> getArrReturnsConditions() {
		return arrReturnsConditions;
	}

	public void setArrReturnsConditions(ArrayList<LegendsVO> arrReturnsConditions) {
		this.arrReturnsConditions = arrReturnsConditions;
	}

	public ArrayList<LegendsVO> getArrPriceConditions() {
		return arrPriceConditions;
	}

	public void setArrPriceConditions(ArrayList<LegendsVO> arrPriceConditions) {
		this.arrPriceConditions = arrPriceConditions;
	}

	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(getArrLocation());
		dest.writeTypedList(getArrPriceConditions());
		dest.writeTypedList(getArrReturnsConditions());
	}
	
	public static final Parcelable.Creator<LocationsVO> CREATOR = new Parcelable.Creator<LocationsVO>() {
		public LocationsVO[] newArray(int size) {
			return new LocationsVO[size];
		}

		public LocationsVO createFromParcel(Parcel in) {
			return new LocationsVO(in);
		}
	};
	
	private LocationsVO(Parcel in){
		super(in);
		arrLocation = new ArrayList<LocationVO>();
		in.readTypedList(this.arrLocation, LocationVO.CREATOR);
		
		arrPriceConditions = new ArrayList<LegendsVO>();
		in.readTypedList(this.arrPriceConditions, LegendsVO.CREATOR);
		
		arrReturnsConditions = new ArrayList<LegendsVO>();
		in.readTypedList(this.arrReturnsConditions, LegendsVO.CREATOR);
	}

}
