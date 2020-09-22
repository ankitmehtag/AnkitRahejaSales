package com.VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AmenitiesVO {

	private boolean Shopping_Area;
	private boolean Restaurent;
	private boolean Pre_School;
	private boolean Medical_Facility;
	private boolean Day_care_center;
	private boolean petArea;
	private boolean Driver_Area;
	private boolean Concierge;
	private boolean Business_Center;
	private boolean Mini_Theatre;
	private boolean party_hall;
	private boolean Library;
	private boolean Amphitheater;
	private boolean Observatoryater;
	//	private boolean Lift;
	private boolean Left;

	private ArrayList<Amenity> Amenities;

	public ArrayList<Amenity> getAmenities() {
		return Amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		Amenities = amenities;
	}

	public class Amenity{
		private String title;
		private String image;

		public Amenity(JSONObject jsonObj){
			if(jsonObj != null) {
				setTitle(jsonObj.optString("title"));
				setImage(jsonObj.optString("image"));
			}
		}
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}
	}

	public AmenitiesVO(JSONArray jobj)
	{
		if(jobj != null) {
			/*setShopping_Area(jobj.optBoolean("Shopping_Area"));
			setRestaurent(jobj.optBoolean("Restaurent"));
			setPre_School(jobj.optBoolean("Pre_School"));
			setMedical_Facility(jobj.optBoolean("Medical_Facility"));
			setDay_care_center(jobj.optBoolean("Day_care_center"));
			setPetArea(jobj.optBoolean("petArea"));
			setDriver_Area(jobj.optBoolean("Driver_Area"));
			setConcierge(jobj.optBoolean("Concierge"));
			setBusiness_Center(jobj.optBoolean("Mini_Theatre"));
			setMini_Theatre(jobj.optBoolean("Mini_Theatre"));
			setParty_hall(jobj.optBoolean("party_hall"));
			setLibrary(jobj.optBoolean("Library"));
			setAmphitheater(jobj.optBoolean("Amphitheater"));
			setObservatoryater(jobj.optBoolean("Observatoryater"));
			setLeft(jobj.optBoolean("Lift"));*/
			ArrayList<Amenity> amenities = new ArrayList<>();
			for (int i = 0; i < jobj.length(); i++){
				try {
					JSONObject obj = (JSONObject) jobj.get(i);
					amenities.add(new Amenity(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setAmenities(amenities);
		}

	}

	public boolean isShopping_Area() {
		return Shopping_Area;
	}
	public void setShopping_Area(boolean shopping_Area) {
		Shopping_Area = shopping_Area;
	}
	public boolean isRestaurent() {
		return Restaurent;
	}
	public void setRestaurent(boolean restaurent) {
		Restaurent = restaurent;
	}
	public boolean isPre_School() {
		return Pre_School;
	}
	public void setPre_School(boolean pre_School) {
		Pre_School = pre_School;
	}
	public boolean isMedical_Facility() {
		return Medical_Facility;
	}
	public void setMedical_Facility(boolean medical_Facility) {
		Medical_Facility = medical_Facility;
	}
	public boolean isDay_care_center() {
		return Day_care_center;
	}
	public void setDay_care_center(boolean day_care_center) {
		Day_care_center = day_care_center;
	}
	public boolean isPetArea() {
		return petArea;
	}
	public void setPetArea(boolean petArea) {
		this.petArea = petArea;
	}
	public boolean isDriver_Area() {
		return Driver_Area;
	}
	public void setDriver_Area(boolean driver_Area) {
		Driver_Area = driver_Area;
	}
	public boolean isConcierge() {
		return Concierge;
	}
	public void setConcierge(boolean concierge) {
		Concierge = concierge;
	}
	public boolean isBusiness_Center() {
		return Business_Center;
	}
	public void setBusiness_Center(boolean business_Center) {
		Business_Center = business_Center;
	}
	public boolean isMini_Theatre() {
		return Mini_Theatre;
	}
	public void setMini_Theatre(boolean mini_Theatre) {
		Mini_Theatre = mini_Theatre;
	}
	public boolean isParty_hall() {
		return party_hall;
	}
	public void setParty_hall(boolean party_hall) {
		this.party_hall = party_hall;
	}
	public boolean isLibrary() {
		return Library;
	}
	public void setLibrary(boolean library) {
		Library = library;
	}
	public boolean isAmphitheater() {
		return Amphitheater;
	}
	public void setAmphitheater(boolean amphitheater) {
		Amphitheater = amphitheater;
	}
	public boolean isObservatoryater() {
		return Observatoryater;
	}
	public void setObservatoryater(boolean observatoryater) {
		Observatoryater = observatoryater;
	}


	public boolean isLeft() {
		return Left;
	}
	public void setLeft(boolean left) {
		Left = left;
	}





}
