package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class SectorVO implements Parcelable{
	
	private String sector_name;
	private int infra;
	private int needs;
	private int lifestyle;
	private int returns;
	private int price;
	private ArrayList<BoundariesVO> arrSectorCords;
	
	public SectorVO(JSONObject obj){
		
		this.setSector_name(obj.optString("sector_name"));
		this.setInfra(obj.optInt("infra"));
		this.setNeeds(obj.optInt("needs"));
		this.setLifestyle(obj.optInt("lifestyle"));
		this.setReturns(obj.optInt("return"));
		this.setPrice(obj.optInt("psf_average"));
		
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
			this.setArrSectorCords(arr);
		}
	}


	public String getSector_name() {
		return sector_name;
	}


	public void setSector_name(String sector_name) {
		this.sector_name = sector_name;
	}


	public ArrayList<BoundariesVO> getArrSectorCords() {
		return arrSectorCords;
	}


	public void setArrSectorCords(ArrayList<BoundariesVO> arrSectorCords) {
		this.arrSectorCords = arrSectorCords;
	}


	public int getInfra() {
		return infra;
	}


	public void setInfra(int infra) {
		this.infra = infra;
	}


	public int getNeeds() {
		return needs;
	}


	public void setNeeds(int needs) {
		this.needs = needs;
	}


	public int getLifestyle() {
		return lifestyle;
	}


	public void setLifestyle(int lifestyle) {
		this.lifestyle = lifestyle;
	}


	public int getReturns() {
		return returns;
	}


	public void setReturns(int returns) {
		this.returns = returns;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getInfra());
		dest.writeInt(getNeeds());
		dest.writeInt(getLifestyle());
		dest.writeInt(getReturns());
		dest.writeInt(getPrice());
		dest.writeTypedList(getArrSectorCords());
	}
	
	public static final Parcelable.Creator<SectorVO> CREATOR = new Parcelable.Creator<SectorVO>() {
		public SectorVO[] newArray(int size) {
			return new SectorVO[size];
		}

		public SectorVO createFromParcel(Parcel in) {
			return new SectorVO(in);
		}
	};
	
	private SectorVO(Parcel in){
		setInfra(in.readInt());
		setNeeds(in.readInt());
		setLifestyle(in.readInt());
		setReturns(in.readInt());
		setPrice(in.readInt());
		arrSectorCords = new ArrayList<BoundariesVO>();
		in.readTypedList(this.arrSectorCords, BoundariesVO.CREATOR);
		
	}

}
