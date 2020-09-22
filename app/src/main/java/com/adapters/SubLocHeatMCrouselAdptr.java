package com.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fragments.SubLocHeatMCrouselFragment;
import com.model.SubLocHeatMRespModel;

import java.util.ArrayList;

public class SubLocHeatMCrouselAdptr extends FragmentStatePagerAdapter {

	private Context context;
	public ArrayList<SubLocHeatMRespModel.SectorLists> modelArrayList;
	private String location;

	public SubLocHeatMCrouselAdptr(Context context, FragmentManager fm, ArrayList<SubLocHeatMRespModel.SectorLists> modelArrayList,String location) {
		super(fm);
		this.context = context;
		this.modelArrayList = modelArrayList;
		this.location = location;
	}

	@Override
	public Fragment getItem(int position) {
        return SubLocHeatMCrouselFragment.newInstance(modelArrayList.get(position),location);
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
