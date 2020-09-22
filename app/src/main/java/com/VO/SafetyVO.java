package com.VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SafetyVO {
	
	private boolean Fire_Fighting_Systems;
	private boolean CCTV_Surveillance;
	private boolean Smoke_Sensors;
	private boolean Smart_Card_Access;
	private boolean Security;
	private boolean Video_Door_Phone;
	private boolean Public_Address_System;
	private boolean Emergency_alarm;
	private boolean Burglar_Alarm;
	private boolean Boom_Barrier_Access;
	private ArrayList<Sefty>sefties;

	public ArrayList<Sefty> getSefties() {
		return sefties;
	}

	public void setSefties(ArrayList<Sefty> sefties) {
		this.sefties = sefties;
	}

	public SafetyVO(JSONArray jobj)
	{
		/*setFire_Fighting_Systems(jobj.optBoolean("Fire_Fighting_Systems"));
		setCCTV_Surveillance(jobj.optBoolean("CCTV_Surveillance"));
		setSmoke_Sensors(jobj.optBoolean("Smoke_Sensors"));
		setSmart_Card_Access(jobj.optBoolean("Smart_Card_Access"));
		setSecurity(jobj.optBoolean("Security"));
		setVideo_Door_Phone(jobj.optBoolean("Video_Door_Phone"));
		setPublic_Address_System(jobj.optBoolean("Public_Address_System"));
		setEmergency_alarm(jobj.optBoolean("Emergency_alarm"));
		setBurglar_Alarm(jobj.optBoolean("Burglar_Alarm"));
		setBoom_Barrier_Access(jobj.optBoolean("Boom_Barrier_Access"));*/
		if(jobj != null) {
			ArrayList<Sefty> sefties = new ArrayList<>();
			for (int i = 0; i < jobj.length(); i++) {
				try {
					JSONObject obj = (JSONObject) jobj.get(i);
					sefties.add(new Sefty(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setSefties(sefties);
		}

	}

	public class Sefty{
		private String title;
		private String image;

		public Sefty(JSONObject jsonObj){
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
	public boolean isFire_Fighting_Systems() {
		return Fire_Fighting_Systems;
	}
	public void setFire_Fighting_Systems(boolean fire_Fighting_Systems) {
		Fire_Fighting_Systems = fire_Fighting_Systems;
	}
	public boolean isCCTV_Surveillance() {
		return CCTV_Surveillance;
	}
	public void setCCTV_Surveillance(boolean cCTV_Surveillance) {
		CCTV_Surveillance = cCTV_Surveillance;
	}
	public boolean isSmoke_Sensors() {
		return Smoke_Sensors;
	}
	public void setSmoke_Sensors(boolean smoke_Sensors) {
		Smoke_Sensors = smoke_Sensors;
	}
	public boolean isSmart_Card_Access() {
		return Smart_Card_Access;
	}
	public void setSmart_Card_Access(boolean smart_Card_Access) {
		Smart_Card_Access = smart_Card_Access;
	}
	public boolean isSecurity() {
		return Security;
	}
	public void setSecurity(boolean security) {
		Security = security;
	}
	public boolean isVideo_Door_Phone() {
		return Video_Door_Phone;
	}
	public void setVideo_Door_Phone(boolean video_Door_Phone) {
		Video_Door_Phone = video_Door_Phone;
	}
	public boolean isPublic_Address_System() {
		return Public_Address_System;
	}
	public void setPublic_Address_System(boolean public_Address_System) {
		Public_Address_System = public_Address_System;
	}
	public boolean isEmergency_alarm() {
		return Emergency_alarm;
	}
	public void setEmergency_alarm(boolean emergency_alarm) {
		Emergency_alarm = emergency_alarm;
	}
	public boolean isBurglar_Alarm() {
		return Burglar_Alarm;
	}
	public void setBurglar_Alarm(boolean burglar_Alarm) {
		Burglar_Alarm = burglar_Alarm;
	}
	public boolean isBoom_Barrier_Access() {
		return Boom_Barrier_Access;
	}
	public void setBoom_Barrier_Access(boolean boom_Barrier_Access) {
		Boom_Barrier_Access = boom_Barrier_Access;
	}
	
	
}
