package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.VO.DeveloperVO;
import com.fragments.FeaturedDevelopersFragment;

import java.util.ArrayList;

public class FeaturedDevFragmentAdapter extends FragmentStatePagerAdapter {

	private ArrayList<DeveloperVO> arrimg;
	int count;
	//private Context ctx = null;

	public FeaturedDevFragmentAdapter(FragmentManager fm, ArrayList<DeveloperVO> arr) {
		super(fm);
		this.arrimg = arr;
		count = arr.size();
		//this.ctx = ctx;
	}
	@Override
	public void finishUpdate(ViewGroup container) {
		try{
			super.finishUpdate(container);
		} catch (NullPointerException nullPointerException){
			System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
		}
	}
	@Override
	public Fragment getItem(int position) {
			FeaturedDevelopersFragment f = new FeaturedDevelopersFragment();
			Bundle b = new Bundle();
			b.putInt("pos", position);
			b.putParcelable("developervo", arrimg.get(position));
			f.setArguments(b);
			
			
		return f;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return count;
	}

}