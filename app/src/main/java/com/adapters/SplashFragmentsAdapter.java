package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sp.SliderActivity;
import com.fragments.SplashFragment;

public class SplashFragmentsAdapter extends FragmentPagerAdapter {

	private SliderActivity fragmentActivity;

	public SplashFragmentsAdapter(FragmentManager fm, SliderActivity fragmentActivity) {
		super(fm);
		this.fragmentActivity = fragmentActivity;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment;
		SplashFragment f = new SplashFragment();
		f.setFragmentActivity(fragmentActivity);
		fragment = f;

		Bundle b = new Bundle();
		b.putInt("pos", position);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return 3;
	}

}