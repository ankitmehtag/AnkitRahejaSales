package com.adapters;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.VO.ProjectVO;
import com.fragments.FeaturedProjImageFragment;

import java.util.ArrayList;

public class FeaturedProjFragmentAdapter extends FragmentStatePagerAdapter {

	public ArrayList<ProjectVO> projectVOList;
	private ArrayList<FeaturedProjImageFragment> fragmentList = new ArrayList<FeaturedProjImageFragment>();
	int count;
	protected Activity ctx;
	public FeaturedProjFragmentAdapter(FragmentManager fm, ArrayList<ProjectVO> projectVOList) {
		super(fm);
		this.projectVOList = projectVOList;
		count = projectVOList.size();
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
			
			FeaturedProjImageFragment f = new FeaturedProjImageFragment();
			Bundle b = new Bundle();
			b.putInt("pos", position);
			b.putParcelable("projectvo", projectVOList.get(position));
			f.setArguments(b);
			fragmentList.add(f);
			
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
	
	public void toggleFav(String propertyId){
		if(propertyId == null ||propertyId == ""){
			return;
		}
		for(int i = 0; i < count; i++){
			FeaturedProjImageFragment frag = fragmentList.get(i);
			String id = frag.vo.getId();
			if(id.equals(propertyId)){
				boolean fav = frag.vo.isUser_favourite() ? false : true;
				frag.vo.setUser_favourite(fav);
				notifyDataSetChanged();
				break;
			}
		}
	}

}