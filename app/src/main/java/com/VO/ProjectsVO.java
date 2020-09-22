package com.VO;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectsVO extends BaseVO{

	private ArrayList<ProjectVO> arrProjects;
	
	public ProjectsVO(JSONObject o){
		super(o);
		
		ArrayList<ProjectVO> arrCityVo = null;
		JSONArray arrJson;
		try {
			arrJson = o.getJSONArray("data");
			if(arrJson!= null ){
				arrCityVo = new ArrayList<ProjectVO>();
				for (int i = 0; i < arrJson.length(); i++) {
					ProjectVO cityvo = new ProjectVO(arrJson.optJSONObject(i));
					arrCityVo.add(cityvo);
				}
				this.setArrProjects(arrCityVo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<ProjectVO> getArrProjects() {
		return arrProjects;
	}

	public void setArrProjects(ArrayList<ProjectVO> arrProjects) {
		this.arrProjects = arrProjects;
	}
}
