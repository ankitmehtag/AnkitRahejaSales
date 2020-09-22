package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationVO implements Parcelable{
	
	private String name;
	private String Id;
	private String locationImage;
	private ArrayList<BoundariesVO> arrBoundries;
	private ArrayList<SectorVO> arrSectors;
	private String total_projects;

	private String avgPsfLocation;
	private String avgPsfLocation_unit;
	private String avg_rating;
	private String needs;
	private String lifeStyle;
	private String infra;
	private String returns;
	private String returnsval;

	public String getReturnsval() {
		return returnsval;
	}

	public void setReturnsval(String returnsval) {
		this.returnsval = returnsval;
	}

	public LocationVO(JSONObject obj) {
		setName(obj.optString("location_name"));
		setId(obj.optString("location_id"));
		setLocationImage(obj.optString("location_img"));
		setTotal_projects(obj.optString("total_projects"));
		setAvgPsfLocation(obj.optString("avgPsfLocation"));
		setAvgPsfLocation_unit(obj.optString("avgPsfLocation_unit"));
		setAvg_rating(obj.optString("avg_rating"));
		setNeeds(obj.optString("needs"));
		setLifeStyle(obj.optString("lifeStyle"));
		setInfra(obj.optString("infra"));
		setReturns(obj.optString("returns"));
		setReturnsval(obj.optString("returnsval"));
		
		ArrayList<BoundariesVO> arr = new ArrayList<BoundariesVO>();
		JSONArray arrjson =obj.optJSONArray("cords");
		if(arrjson!=null){
			for (int i = 0; i < arrjson.length(); i++) {
				JSONObject o = arrjson.optJSONObject(i);
				if(o!=null){
					BoundariesVO vo = new BoundariesVO(o);
					arr.add(vo);
				}
			}
			this.setArrBoundries(arr);
		}
		
		
		ArrayList<SectorVO> arrSec = new ArrayList<SectorVO>();
		JSONArray secDetails =obj.optJSONArray("sector_detail");
		if(secDetails!=null){
			for (int i = 0; i < secDetails.length(); i++) {
				JSONObject o = secDetails.optJSONObject(i);
				if(o!=null){
					SectorVO vo = new SectorVO(o);
					arrSec.add(vo);
				}
			}
			this.setArrSectors(arrSec);
		}
	}

	public String getAvgPsfLocation_unit() {
		return avgPsfLocation_unit;
	}

	public void setAvgPsfLocation_unit(String avgPsfLocation_unit) {
		this.avgPsfLocation_unit = avgPsfLocation_unit;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}

	public ArrayList<BoundariesVO> getArrBoundries() {
		return arrBoundries;
	}

	public void setArrBoundries(ArrayList<BoundariesVO> arrBoundries) {
		this.arrBoundries = arrBoundries;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getName());
		dest.writeString(getId());
		dest.writeString(getLocationImage());
		dest.writeTypedList(getArrBoundries());
		dest.writeString(getTotal_projects());
		dest.writeString(getAvgPsfLocation());
		dest.writeString(getAvgPsfLocation_unit());
		dest.writeTypedList(getArrSectors());
		
		dest.writeString(getAvg_rating());
		dest.writeString(getNeeds());
		dest.writeString(getLifeStyle());
		dest.writeString(getInfra());
		dest.writeString(getReturns());
		dest.writeString(getReturnsval());
	}
	
	public static final Parcelable.Creator<LocationVO> CREATOR = new Parcelable.Creator<LocationVO>() {
		public LocationVO[] newArray(int size) {
			return new LocationVO[size];
		}

		public LocationVO createFromParcel(Parcel in) {
			return new LocationVO(in);
		}
	};
	
	private LocationVO(Parcel in){
		setName(in.readString());
		setId(in.readString());
		setLocationImage(in.readString());
		arrBoundries = new ArrayList<BoundariesVO>();
		in.readTypedList(this.arrBoundries, BoundariesVO.CREATOR);
		setTotal_projects(in.readString());
		setAvgPsfLocation(in.readString());
		setAvgPsfLocation_unit(in.readString());
		
		arrSectors = new ArrayList<SectorVO>();
		in.readTypedList(this.arrSectors, SectorVO.CREATOR);
		setAvg_rating(in.readString());
		setNeeds(in.readString());
		setLifeStyle(in.readString());
		setInfra(in.readString());
		setReturns(in.readString());
		setReturnsval(in.readString());

		
		
	}

	public String getLocationImage() {
		return locationImage;
	}

	public void setLocationImage(String locationImage) {
		this.locationImage = locationImage;
	}

	public ArrayList<SectorVO> getArrSectors() {
		return arrSectors;
	}

	public void setArrSectors(ArrayList<SectorVO> arrSectors) {
		this.arrSectors = arrSectors;
	}

	public String getAvgPsfLocation() {
		return avgPsfLocation;
	}

	public void setAvgPsfLocation(String avgPsfLocation) {
		this.avgPsfLocation = avgPsfLocation;
	}

	public String getTotal_projects() {
		return total_projects;
	}

	public void setTotal_projects(String total_projects) {
		this.total_projects = total_projects;
	}

	public String getAvg_rating() {
		return avg_rating;
	}

	public void setAvg_rating(String avg_rating) {
		this.avg_rating = avg_rating;
	}

	public String getNeeds() {
		return needs;
	}

	public void setNeeds(String needs) {
		this.needs = needs;
	}

	public String getLifeStyle() {
		return lifeStyle;
	}

	public void setLifeStyle(String lifeStyle) {
		this.lifeStyle = lifeStyle;
	}

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	
	
	
	

	

}
