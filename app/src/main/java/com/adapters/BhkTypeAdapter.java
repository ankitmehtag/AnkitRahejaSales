package com.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fragments.BhkTypeFragment;
import com.model.BhkUnitSpecification;

import java.util.ArrayList;
import java.util.List;

public class BhkTypeAdapter extends FragmentPagerAdapter {

	private ArrayList<List<BhkUnitSpecification>> bhkSpecification = null;
	///protected Context ctx;
	public BhkTypeAdapter(FragmentManager fm, ArrayList<List<BhkUnitSpecification>> bhkSpecification) {
		super(fm);
		this.bhkSpecification = bhkSpecification;
		//this.ctx = ctx;
	}




	@Override
	public Fragment getItem(int position) {
		List<BhkUnitSpecification> obj = bhkSpecification.get(position);
		Fragment mBhkTypeFragment = BhkTypeFragment.newInstance(obj);
		/*Bundle b = new Bundle();
		b.putParcelableArray("obj", obj);
		mBhkTypeFragment.setArguments(b);*/
		return mBhkTypeFragment;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return bhkSpecification.size();
	}

}