package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sp.PaymentProccessActivity;
import com.VO.UnitDetailVO;
import com.fragments.ApplicationFormFragment;
import com.fragments.BreakUpPlanFragment;
import com.fragments.PaymentPlanFragment;
import com.fragments.RentPaymentPlanFragment;
import com.fragments.TermsFragment;

public class PaymentFragmentsAdapter extends FragmentPagerAdapter {

    private PaymentProccessActivity fragmentActivity;
    private PaymentProccessActivity Activity;

    private UnitDetailVO vo;
    String type;
    int fragNos;

    public PaymentFragmentsAdapter(FragmentManager fm, PaymentProccessActivity fragmentActivity, UnitDetailVO unitVo, int count, String type) {
        super(fm);
        this.fragmentActivity = fragmentActivity;
        vo = unitVo;
        fragNos = count;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle b = new Bundle();
        if (position == 0) {
            if (type.equalsIgnoreCase("Rent")) {
                RentPaymentPlanFragment f = new RentPaymentPlanFragment();
                f.setFragmentActivity(fragmentActivity);
                f.setArguments(b);
                fragment = f;
            } else {
                PaymentPlanFragment f = new PaymentPlanFragment();
                f.setFragmentActivity(fragmentActivity);
                f.setArguments(b);
                fragment = f;
            }
        } else if (position == 1) {
            BreakUpPlanFragment f = new BreakUpPlanFragment();
            f.setFragmentActivity(fragmentActivity);
            f.setArguments(b);
            fragment = f;
        } else if (position == 2) {
            TermsFragment f = new TermsFragment();
            f.setFragmentActivity(fragmentActivity);
            f.setArguments(b);
            fragment = f;
        }
//		else if (position == 2) {
//			PersonalDetails p = new PersonalDetails();
//		    p.setActivity(Activity);
//		    b.putInt("pos", position);
//		    b.putParcelable("unitvo", vo);
//		    p.setArguments(b);
//		    fragment = p;
//			
//		}
        else if (position == 2) {
            ApplicationFormFragment f = new ApplicationFormFragment();
            f.setFragmentActivity(fragmentActivity);
            f.setArguments(b);
            fragment = f;
        }
        //=========================


//		else if (position == 3) {
//			PaymentWebViewFragment f = new PaymentWebViewFragment();
//			f.setFragmentActivity(fragmentActivity);
//			b.putInt("pos", position);
//			b.putParcelable("unitvo", vo);
//			f.setArguments(b);
//			fragment = f;
//		}
        return fragment;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragNos;
    }

}