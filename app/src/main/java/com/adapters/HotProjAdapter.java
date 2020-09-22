package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fragments.HotProjFragment;
import com.model.HotProjDataModel;

import java.util.ArrayList;

public class HotProjAdapter extends FragmentStatePagerAdapter {

	private ArrayList<HotProjDataModel> dataModel = null;
	///protected Context ctx;
	public HotProjAdapter(FragmentManager fm,ArrayList<HotProjDataModel> dataModel) {
		super(fm);
		this.dataModel = dataModel;
		//this.ctx = ctx;
	}

	@Override
	public Fragment getItem(int position) {

		Fragment mHotProjFragment = HotProjFragment.newInstance(dataModel.get(position));
		Bundle b = new Bundle();
		b.putSerializable("obj", dataModel.get(position));
		mHotProjFragment.setArguments(b);
		return mHotProjFragment;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return dataModel.size();
	}

}