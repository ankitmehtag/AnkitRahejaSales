package com.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fragments.LocHeatMCrouselFragment;
import com.model.LocationHeatmapRespModel;

import java.util.ArrayList;

public class LocHeatMCrouselAdptr extends FragmentStatePagerAdapter {

	private Context context;
	public ArrayList<LocationHeatmapRespModel.LocationList> modelArrayList;

	public LocHeatMCrouselAdptr(Context context, FragmentManager fm, ArrayList<LocationHeatmapRespModel.LocationList> modelArrayList) {
		super(fm);
		this.context = context;
		this.modelArrayList = modelArrayList;
	}

	@Override
	public Fragment getItem(int position) {
        return LocHeatMCrouselFragment.newInstance(modelArrayList.get(position));
	}

	@Override
	public int getCount() {
		return modelArrayList.size();
	}

	/*@Override
	public float getPageWidth(int position) {
		*//*if (position == 0 || position == 2) {
			return 0.8f;
		}*//*
		return 0.8f;
	}
*/
}
