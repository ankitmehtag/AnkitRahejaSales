package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlacesBaseVO {
	
	private String status;
	private ArrayList<PlacesLocationVO> arrLocations;
	
	
	
	public PlacesBaseVO (JSONObject obj){
		setStatus(obj.optString("status"));
		
		String status = getStatus();
		if(status.equalsIgnoreCase("OK")){
			JSONArray arrResults = obj.optJSONArray("results");
			if(arrResults != null && arrResults.length()>0){
				ArrayList<PlacesLocationVO> arrplaces = new ArrayList<PlacesLocationVO>();
				for (int i = 0; i < arrResults.length(); i++) {
					JSONObject jobj = arrResults.optJSONObject(i);
					if(jobj!=null){
						JSONObject objgeometry = jobj.optJSONObject("geometry");
						PlacesLocationVO vo = new PlacesLocationVO();
						vo.setName(jobj.optString("name"));
						vo.setVicinity(jobj.optString("vicinity"));
						
						if(objgeometry!=null){
							JSONObject objLocation = objgeometry.optJSONObject("location");
							if(objLocation!=null){
								vo.setLat(objLocation.optDouble("lat"));
								vo.setLon(objLocation.optDouble("lng"));
								arrplaces.add(vo);
							}
						}
							
					}
				}
				setArrLocations(arrplaces);
				
			}
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<PlacesLocationVO> getArrLocations() {
		return arrLocations;
	}

	public void setArrLocations(ArrayList<PlacesLocationVO> arrLocations) {
		this.arrLocations = arrLocations;
	}

}
