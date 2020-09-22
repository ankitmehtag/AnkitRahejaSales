package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.VO.UnitType;
import com.fragments.UnitTypeFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitTypePagerAdapter extends FragmentStatePagerAdapter {

	private  ArrayList<UnitType> unitType = null;
	private HashMap<String,String> map;
	public UnitTypePagerAdapter(FragmentManager fm,HashMap<String, String> map, ArrayList<UnitType> unitType) {
		super(fm);
		this.unitType = unitType;
		this.map = map;
		//this.ctx = ctx;
	}

	@Override
	public Fragment getItem(int position) {

		UnitTypeFragment mFragment = UnitTypeFragment.newInstance(unitType.get(position));
		Bundle b = new Bundle();
		b.putSerializable("obj", unitType.get(position));
		b.putSerializable("map",map);
		mFragment.setArguments(b);
		return mFragment;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return unitType.size();
	}

}