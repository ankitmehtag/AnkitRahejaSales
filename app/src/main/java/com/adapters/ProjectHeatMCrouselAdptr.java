package com.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.ViewGroup;

import com.fragments.ProjectHeatMCrouselFragment;
import com.model.ProjectsListRespModel;

import java.util.ArrayList;

public class ProjectHeatMCrouselAdptr extends FragmentStatePagerAdapter {

	private Context context;
	public ArrayList<ProjectsListRespModel.Data> modelArrayList;
	private ArrayList<ProjectHeatMCrouselFragment> fragmentList = new ArrayList<ProjectHeatMCrouselFragment>();

	public ProjectHeatMCrouselAdptr(Context context, FragmentManager fm, ArrayList<ProjectsListRespModel.Data> modelArrayList) {
		super(fm);
		this.context = context;
		this.modelArrayList = modelArrayList;
	}

	@Override
	public Fragment getItem(int position) {
		ProjectHeatMCrouselFragment f = ProjectHeatMCrouselFragment.newInstance(modelArrayList.get(position));
		fragmentList.add(f);
        return f;
	}

	@Override
	public int getCount() {
		return modelArrayList.size();
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		try{
			super.finishUpdate(container);
		} catch (NullPointerException nullPointerException){
			System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
		}
	}

	public void toggleFav(String propertyId){
		if(propertyId == null ||propertyId == ""){
			return;
		}
		for(int i = 0; i < fragmentList.size(); i++){
			ProjectHeatMCrouselFragment frag = fragmentList.get(i);
			String id = frag.heatmapDataModel.getID();
			if(id.equals(propertyId)){
				boolean fav = frag.heatmapDataModel.isFavorite() ? false : true;
				frag.heatmapDataModel.setFavorite(fav);
				notifyDataSetChanged();
				break;
			}
		}
	}

	public void setCurrentItem(String projectId, ViewPager vp){
		if(projectId == null || projectId == "" || vp == null){
			return;
		}
		for(int i = 0; i < modelArrayList.size(); i++){
			ProjectsListRespModel.Data temp = modelArrayList.get(i);
			String id = temp.getID();
			if(id.equals(projectId)){
				vp.setCurrentItem(i);
				break;
			}
		}
	}
}
