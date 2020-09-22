package com.adapters;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sp.LocationMapActivity;
import com.VO.LocationVO;
import com.fragments.AlertLocationFragment;
import com.fragments.LocationCrouselFragment;

public class LocationCrouselPagerAdapter extends FragmentStatePagerAdapter {

	private LocationMapActivity context;
	private ArrayList<LocationVO> ArrayLocations;

	public LocationCrouselPagerAdapter(LocationMapActivity context, FragmentManager fm, ArrayList<LocationVO> props) {
		super(fm);
		this.context = context;
		ArrayLocations = props;
	}

	@Override
	public Fragment getItem(int position) 
	{
        if(position == ArrayLocations.size()){
        	return AlertLocationFragment.newInstance(context, position, 0, null);
        }
        return LocationCrouselFragment.newInstance(context, position, 0, ArrayLocations.get(position));
	}

	@Override
	public int getCount()
	{		
		return ArrayLocations.size()+1;
	}

}
