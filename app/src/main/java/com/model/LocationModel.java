package com.model;

import android.util.Log;

import com.sp.BMHApplication;
import com.VO.AreasVO;
import com.VO.CitiesVO;
import com.VO.LocationsVO;
import com.VO.PlacesBaseVO;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationModel {

	private static final String  TAG = LocationModel.class.getSimpleName();
	public CitiesVO getCities(BMHApplication app) throws BMHException {

		CitiesVO cityvo = null;

		String url = UrlFactory.getCitiesUrl();
		url = url + "?" + BMHConstants.BUILDER_ID_KEY + "=" + BMHConstants.CURRENT_BUILDER_ID;
		String response = ContentLoader.getJsonFromUrl(url);
		System.out.println("hs serverHit= " + url);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				app.saveIntoPrefs(BMHConstants.CITY_JSON, response);
				cityvo = new CitiesVO(jsonObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cityvo;
	}

	public AreasVO getAreas(String cityId) throws BMHException {
		AreasVO cityvo = null;
		String url = UrlFactory.getAreaUrl();
		String params = "city_id=" + cityId;
		String response = ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs serverHit= " + url);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				cityvo = new AreasVO(jsonObj);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cityvo;
	}

	// Add new "isFilter" use key use in Sorting section

	public LocationsVO getLocations(String cityid, String userid, String p_type, String type,
									String filter_type, String id, String location_type)
			throws BMHException {
		LocationsVO vo = null;
		String url;

		url = UrlFactory.getLocationsUrl()+"?sortby="+filter_type;

		String locId = location_type.equals("Location") ? "location_id="+ id : "sub_location_id="+ id;
		String params = "city_id=" + cityid + "&" + "user_id=" + userid + "&p_type=" + p_type + "&type=" + type
				+ "&"+locId;
		String response = ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs serverHit= " + url);
		System.out.println("hs params= " + params);
		Log.i(TAG,"URL:"+url);
		Log.i(TAG,"Params:"+params);
		Log.i(TAG,"Response:"+response);
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				vo = new LocationsVO(jsonObj);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vo;
	}

	public PlacesBaseVO getNearByPlaces(String latLong, String type, int radiusInMeters, String mapKey)
			throws BMHException {
		PlacesBaseVO placesvo = null;
		if(type.equalsIgnoreCase("airport")) radiusInMeters = 50000;
		StringBuffer str = new StringBuffer(UrlFactory.getPlacesUrl());
		str.append("?location=" + latLong);
		str.append("&type=" + type);
		str.append("&radius=" + radiusInMeters);
		str.append("&key=" + mapKey);
		str.append("&sensor=false");

		String response = ContentLoader.getJsonFromUrl(str.toString());
		System.out.println("hs serverHit= " + str.toString());
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj != null) {
				placesvo = new PlacesBaseVO(jsonObj);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return placesvo;
	}
}
