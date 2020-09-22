package com.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fragments.PayUChequeDDFragment;
import com.fragments.PayUCreditFragment;
import com.fragments.PayUNetBankingFragment;

/**
 * Created by Naresh on 25-Apr-17.
 */

public class PaymentMethodsPagerAdapter extends FragmentPagerAdapter {

    public PaymentMethodsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            case 1:
                return new PayUCreditFragment();
            case 2:
                return new PayUNetBankingFragment();
            case 3:
                return new PayUChequeDDFragment();
            default:
                return new PayUCreditFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Credit Card";
            case 1:
                return "Debit Card";
            case 2:
                return "Net Banking";
            case 3:
                return "Cheque/Demand Draft";
        }
        return "";
    }
}
