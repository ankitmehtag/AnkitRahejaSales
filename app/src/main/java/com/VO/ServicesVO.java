package com.VO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesVO {

	private boolean Intercom;
	private boolean Home_Automation;
	private boolean Power_Backup;
	private boolean Laundromat;
	private boolean High_Speed_Elevators;
	private boolean Automated_Car_Wash;
	private boolean Piped_Gas;
	private boolean AC_in_Lobby;
	private boolean Solar_Water_Heater;
	private boolean Treated_Water_Supply;
	private boolean Podium_Parking;
	private boolean Central_Wifi;
	private boolean Sewage_Treatment_Plant;
	private boolean LEED_Certified;
	private ArrayList<Service> services;

	public ArrayList<Service> getServices() {
		return services;
	}

	public void setServices(ArrayList<Service> services) {
		this.services = services;
	}

	public ServicesVO(JSONArray jobj)
	{
		if(jobj != null) {
		 /*setIntercom(jobj.optBoolean("Intercom"));
		 setHome_Automation(jobj.optBoolean("Home_Automation"));
		 setPower_Backup(jobj.optBoolean("Power_Backup"));
		 setLaundromat(jobj.optBoolean("Laundromat"));
		 setHigh_Speed_Elevators(jobj.optBoolean("High_Speed_Elevators"));
		 setAutomated_Car_Wash(jobj.optBoolean("Automated_Car_Wash"));
		 setPiped_Gas(jobj.optBoolean("Piped_Gas"));
		 setAC_in_Lobby(jobj.optBoolean("AC_in_Lobby"));
		 setSolar_Water_Heater(jobj.optBoolean("Solar_Water_Heater"));
		 setTreated_Water_Supply(jobj.optBoolean("Treated_Water_Supply"));
		 setPodium_Parking(jobj.optBoolean("Podium_Parking"));
		 setCentral_Wifi(jobj.optBoolean("Central_Wifi"));
		 setSewage_Treatment_Plant(jobj.optBoolean("Sewage_Treatment_Plant"));
		 setLEED_Certified(jobj.optBoolean("LEED_Certified"));*/

			ArrayList<Service> mServices = new ArrayList<>();
			for (int i = 0; i < jobj.length(); i++){
				try {
					JSONObject obj = (JSONObject) jobj.get(i);
					mServices.add(new Service(obj));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setServices(mServices);
		}

	}

	public class Service{
		private String title;
		private String image;

		public Service(JSONObject jsonObj){
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
	public boolean isIntercom() {
		return Intercom;
	}
	public void setIntercom(boolean intercom) {
		Intercom = intercom;
	}
	public boolean isHome_Automation() {
		return Home_Automation;
	}
	public void setHome_Automation(boolean home_Automation) {
		Home_Automation = home_Automation;
	}
	public boolean isPower_Backup() {
		return Power_Backup;
	}
	public void setPower_Backup(boolean power_Backup) {
		Power_Backup = power_Backup;
	}
	public boolean isLaundromat() {
		return Laundromat;
	}
	public void setLaundromat(boolean laundromat) {
		Laundromat = laundromat;
	}
	public boolean isHigh_Speed_Elevators() {
		return High_Speed_Elevators;
	}
	public void setHigh_Speed_Elevators(boolean high_Speed_Elevators) {
		High_Speed_Elevators = high_Speed_Elevators;
	}
	public boolean isAutomated_Car_Wash() {
		return Automated_Car_Wash;
	}
	public void setAutomated_Car_Wash(boolean automated_Car_Wash) {
		Automated_Car_Wash = automated_Car_Wash;
	}
	public boolean isPiped_Gas() {
		return Piped_Gas;
	}
	public void setPiped_Gas(boolean piped_Gas) {
		Piped_Gas = piped_Gas;
	}
	public boolean isAC_in_Lobby() {
		return AC_in_Lobby;
	}
	public void setAC_in_Lobby(boolean aC_in_Lobby) {
		AC_in_Lobby = aC_in_Lobby;
	}
	public boolean isSolar_Water_Heater() {
		return Solar_Water_Heater;
	}
	public void setSolar_Water_Heater(boolean solar_Water_Heater) {
		Solar_Water_Heater = solar_Water_Heater;
	}
	public boolean isTreated_Water_Supply() {
		return Treated_Water_Supply;
	}
	public void setTreated_Water_Supply(boolean treated_Water_Supply) {
		Treated_Water_Supply = treated_Water_Supply;
	}
	public boolean isPodium_Parking() {
		return Podium_Parking;
	}
	public void setPodium_Parking(boolean podium_Parking) {
		Podium_Parking = podium_Parking;
	}
	public boolean isCentral_Wifi() {
		return Central_Wifi;
	}
	public void setCentral_Wifi(boolean central_Wifi) {
		Central_Wifi = central_Wifi;
	}
	public boolean isSewage_Treatment_Plant() {
		return Sewage_Treatment_Plant;
	}
	public void setSewage_Treatment_Plant(boolean sewage_Treatment_Plant) {
		Sewage_Treatment_Plant = sewage_Treatment_Plant;
	}
	public boolean isLEED_Certified() {
		return LEED_Certified;
	}
	public void setLEED_Certified(boolean lEED_Certified) {
		LEED_Certified = lEED_Certified;
	}



}
