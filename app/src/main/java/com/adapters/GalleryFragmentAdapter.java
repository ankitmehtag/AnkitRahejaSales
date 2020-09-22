package com.adapters;

import java.util.ArrayList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.VO.MediaGellaryVO;
import com.fragments.PropertyImageFragment;
import com.fragments.PropertyPDFFragment;
import com.fragments.PropertyVideoFragment;
import com.helper.BMHConstants;

public class GalleryFragmentAdapter extends FragmentPagerAdapter {

	private ArrayList<MediaGellaryVO> arrimg;
	private FragmentActivity activity;
	int count;

	public GalleryFragmentAdapter(FragmentActivity acti, FragmentManager fm, ArrayList<MediaGellaryVO> arr) {
		super(fm);
		this.arrimg = arr;
		this.activity = acti;
		
		try {
			count = arr.size();
		} catch (Exception e) {
			
		}
		
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		int type = arrimg.get(position).getType();
		if (type == BMHConstants.TYPE_IMAGES) {

			PropertyImageFragment f = new PropertyImageFragment();
			Bundle b = new Bundle();
			b.putString("imgurl", arrimg.get(position).getUrl());
			b.putString("imgname", arrimg.get(position).getImage_name());
			b.putInt("pos", position);
			b.putInt("count", count);
			f.setFragmentActivity(activity);
			f.setArguments(b);
			fragment = f;
		} else if (type == BMHConstants.TYPE_VIDEOS) {

			PropertyVideoFragment f = new PropertyVideoFragment();
			Bundle b = new Bundle();
			b.putString("imgurl", arrimg.get(position).getUrl());
			b.putInt("pos", position);
			b.putInt("count", count);
			f.setFragmentActivity(activity);
			f.setArguments(b);
			fragment = f;

		} else if (type == BMHConstants.TYPE_PDF) {

			PropertyPDFFragment f = new PropertyPDFFragment();
			Bundle b = new Bundle();
			b.putString("imgurl", arrimg.get(position).getUrl());
			b.putInt("pos", position);
			b.putInt("count", count);
			f.setFragmentActivity(activity);
			f.setArguments(b);
			fragment = f;

		} else {
			PropertyPDFFragment f = new PropertyPDFFragment();
			Bundle b = new Bundle();
			b.putString("imgurl", arrimg.get(position).getUrl());
			b.putInt("pos", position);
			f.setFragmentActivity(activity);
			f.setArguments(b);
			fragment = f;
		}
//		else{
//			Property
//		}
		

		return fragment;
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