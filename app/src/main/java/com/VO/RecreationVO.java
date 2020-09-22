package com.VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecreationVO {

	private boolean Squash;
	private boolean tennis;
	private boolean indoorSwimmingpPool;
	private boolean Badminton;
	private boolean Kids_Play_Area;
	private boolean basketball;
	private boolean yoga;
	private boolean Kids_Pool;
	private boolean tableTennis;
	private boolean billiards;
	private boolean Soccer;
	private boolean Cricket;
	private boolean Cycling_Track;
	private boolean Go_Carting;
	private boolean Skating_Rink;
	private boolean minigolf;
	private boolean spa;
	private boolean Sanna_Bath;
	private boolean Steam;
	private boolean Jacuzziam;
	private boolean cardRoom;
	private boolean danceRoom;
	private boolean Indoor_Games;
	private boolean Bowling;
	private boolean Jogging_Running_Track;

	private ArrayList<Recreation> recreations;

	public ArrayList<Recreation> getRecreations() {
		return recreations;
	}

	public void setRecreations(ArrayList<Recreation> recreations) {
		this.recreations = recreations;
	}

	public class Recreation{
		private String title;
		private String image;

		public Recreation(JSONObject jsonObj){
			setTitle(jsonObj.optString("title"));
			setImage(jsonObj.optString("image"));
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
	public RecreationVO(JSONArray jobj)
	{
		/*setSquash(jobj.optBoolean("Squash"));
		setTennis(jobj.optBoolean("tennis"));
		setIndoor_Games(jobj.optBoolean("indoorSwimmingpPool"));
		setBadminton(jobj.optBoolean("Badminton"));
		setKids_Play_Area(jobj.optBoolean("Kids_Play_Area"));
		setBasketball(jobj.optBoolean("basketball"));
		setYoga(jobj.optBoolean("yoga"));
		setKids_Pool(jobj.optBoolean("Kids_Pool"));
		setTableTennis(jobj.optBoolean("tableTennis"));
		setBilliards(jobj.optBoolean("billiards"));
		setSoccer(jobj.optBoolean("Soccer"));
		setCricket(jobj.optBoolean("Cricket"));
		setCycling_Track(jobj.optBoolean("Cycling_Track"));
		setGo_Carting(jobj.optBoolean("Go_Carting"));
		setSkating_Rink(jobj.optBoolean("Skating_Rink"));
		setMinigolf(jobj.optBoolean("minigolf"));
		setSpa(jobj.optBoolean("spa"));
		setSanna_Bath(jobj.optBoolean("Sanna_Bath"));
		setSteam(jobj.optBoolean("Steam"));
		setJacuzziam(jobj.optBoolean("Jacuzziam"));
		setCardRoom(jobj.optBoolean("cardRoom"));
		setDanceRoom(jobj.optBoolean("danceRoom"));
		setIndoor_Games(jobj.optBoolean("Indoor_Games"));
		setBowling(jobj.optBoolean("Bowling"));
		setJogging_Running_Track(jobj.optBoolean("Jogging_Running_Track"));
		*/
		if(jobj != null) {
			ArrayList<Recreation> recreations = new ArrayList<>();
			for (int i = 0; i < jobj.length(); i++) {
				try {
					JSONObject obj = (JSONObject) jobj.get(i);
					recreations.add(new Recreation(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setRecreations(recreations);
		}

	}


	public boolean isSquash() {
		return Squash;
	}
	public void setSquash(boolean squash) {
		Squash = squash;
	}
	public boolean isTennis() {
		return tennis;
	}
	public void setTennis(boolean tennis) {
		this.tennis = tennis;
	}
	public boolean isIndoorSwimmingpPool() {
		return indoorSwimmingpPool;
	}
	public void setIndoorSwimmingpPool(boolean indoorSwimmingpPool) {
		this.indoorSwimmingpPool = indoorSwimmingpPool;
	}
	public boolean isBadminton() {
		return Badminton;
	}
	public void setBadminton(boolean badminton) {
		Badminton = badminton;
	}
	public boolean isKids_Play_Area() {
		return Kids_Play_Area;
	}
	public void setKids_Play_Area(boolean kids_Play_Area) {
		Kids_Play_Area = kids_Play_Area;
	}
	public boolean isBasketball() {
		return basketball;
	}
	public void setBasketball(boolean basketball) {
		this.basketball = basketball;
	}
	public boolean isYoga() {
		return yoga;
	}
	public void setYoga(boolean yoga) {
		this.yoga = yoga;
	}
	public boolean isKids_Pool() {
		return Kids_Pool;
	}
	public void setKids_Pool(boolean kids_Pool) {
		Kids_Pool = kids_Pool;
	}
	public boolean isTableTennis() {
		return tableTennis;
	}
	public void setTableTennis(boolean tableTennis) {
		this.tableTennis = tableTennis;
	}
	public boolean isBilliards() {
		return billiards;
	}
	public void setBilliards(boolean billiards) {
		this.billiards = billiards;
	}
	public boolean isSoccer() {
		return Soccer;
	}
	public void setSoccer(boolean soccer) {
		Soccer = soccer;
	}
	public boolean isCricket() {
		return Cricket;
	}
	public void setCricket(boolean cricket) {
		Cricket = cricket;
	}
	public boolean isCycling_Track() {
		return Cycling_Track;
	}
	public void setCycling_Track(boolean cycling_Track) {
		Cycling_Track = cycling_Track;
	}
	public boolean isGo_Carting() {
		return Go_Carting;
	}
	public void setGo_Carting(boolean go_Carting) {
		Go_Carting = go_Carting;
	}
	public boolean isSkating_Rink() {
		return Skating_Rink;
	}
	public void setSkating_Rink(boolean skating_Rink) {
		Skating_Rink = skating_Rink;
	}
	public boolean isMinigolf() {
		return minigolf;
	}
	public void setMinigolf(boolean minigolf) {
		this.minigolf = minigolf;
	}
	public boolean isSpa() {
		return spa;
	}
	public void setSpa(boolean spa) {
		this.spa = spa;
	}
	public boolean isSanna_Bath() {
		return Sanna_Bath;
	}
	public void setSanna_Bath(boolean sanna_Bath) {
		Sanna_Bath = sanna_Bath;
	}
	public boolean isSteam() {
		return Steam;
	}
	public void setSteam(boolean steam) {
		Steam = steam;
	}
	public boolean isJacuzziam() {
		return Jacuzziam;
	}
	public void setJacuzziam(boolean jacuzziam) {
		Jacuzziam = jacuzziam;
	}
	public boolean isCardRoom() {
		return cardRoom;
	}
	public void setCardRoom(boolean cardRoom) {
		this.cardRoom = cardRoom;
	}
	public boolean isDanceRoom() {
		return danceRoom;
	}
	public void setDanceRoom(boolean danceRoom) {
		this.danceRoom = danceRoom;
	}
	public boolean isIndoor_Games() {
		return Indoor_Games;
	}
	public void setIndoor_Games(boolean indoor_Games) {
		Indoor_Games = indoor_Games;
	}
	public boolean isBowling() {
		return Bowling;
	}
	public void setBowling(boolean bowling) {
		Bowling = bowling;
	}
	public boolean isJogging_Running_Track() {
		return Jogging_Running_Track;
	}
	public void setJogging_Running_Track(boolean jogging_Running_Track) {
		Jogging_Running_Track = jogging_Running_Track;
	}


}
