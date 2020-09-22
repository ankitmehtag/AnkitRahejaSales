package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sp.PayTokenActivity;
import com.VO.UnitDetailVO;
import com.fragments.PaymentWebViewFragment;
import com.fragments.TermsFragment;

public class PayTokenFragmentsAdapter extends FragmentPagerAdapter {

	private PayTokenActivity fragmentActivity;
	private UnitDetailVO vo;
	int fragNos;

	public PayTokenFragmentsAdapter(FragmentManager fm,PayTokenActivity fragmentActivity, UnitDetailVO unitVo) {
		super(fm);
		this.fragmentActivity = fragmentActivity;
		vo = unitVo;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		Bundle b = new Bundle();
		if (position == 0) {
			TermsFragment f = new TermsFragment();
			f.setFragmentActivity(fragmentActivity);
			b.putInt("pos", position);
			b.putParcelable("unitvo", vo);
			f.setArguments(b);
			fragment = f;
		}else if (position == 1) {
			PaymentWebViewFragment f = new PaymentWebViewFragment();
			f.setFragmentActivity(fragmentActivity);
			b.putInt("pos", position);
			b.putParcelable("unitvo", vo);
			f.setArguments(b);
			fragment = f;
		}
		return fragment;
	}

	@Override
	public int getItemPosition(Object item) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return 2;
	}

}