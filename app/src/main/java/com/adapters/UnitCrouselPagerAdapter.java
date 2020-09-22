package com.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sp.UnitMapActivity;
import com.VO.UnitCaraouselVO;
import com.fragments.UnitCrouselFragment;

import java.util.ArrayList;

public class UnitCrouselPagerAdapter extends FragmentStatePagerAdapter{

	private UnitMapActivity context;
	private ArrayList<UnitCaraouselVO> arrayUnits;
	private ArrayList<UnitCrouselFragment> fragmentList = new ArrayList<UnitCrouselFragment>();

	public UnitCrouselPagerAdapter(UnitMapActivity context, FragmentManager fm, ArrayList<UnitCaraouselVO> props) {
		super(fm);
		this.context = context;
		arrayUnits = props;
	}

	@Override
	public Fragment getItem(int position) {
		UnitCrouselFragment mUnitCrouselFragment = (UnitCrouselFragment) UnitCrouselFragment.newInstance(arrayUnits.get(position));
		fragmentList.add(mUnitCrouselFragment);
        return mUnitCrouselFragment;
	}

	@Override
	public int getCount() {
		if(arrayUnits == null)return 0;
		else
		return arrayUnits.size();
	}
	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

/*
	@Override
	public void finishUpdate(ViewGroup container) {
		try{
			super.finishUpdate(container);
		} catch (NullPointerException nullPointerException){
			System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
		}
	}
*/

	public void toggleFav(String unitId){
		if(unitId == null || unitId.isEmpty() || fragmentList == null){
			return;
		}
		for(int i = 0; i < fragmentList.size(); i++){
			UnitCrouselFragment frag = fragmentList.get(i);
			if(frag != null) {
				String id = frag.unit.getUnit_id();
				if (id.equals(unitId)) {
					boolean fav = frag.unit.isUser_favourite() ? false : true;
					frag.unit.setUser_favourite(fav);
					notifyDataSetChanged();
					break;
				}
			}
		}
	}
}
