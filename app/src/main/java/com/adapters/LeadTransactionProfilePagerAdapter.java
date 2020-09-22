package com.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fragments.AssignedLeadFragment;
import com.fragments.BrokerTransactionsFragment;
import com.fragments.UserInfoFragment;

/**
 * Created by Naresh on 25-Apr-17.
 */

public class LeadTransactionProfilePagerAdapter extends FragmentPagerAdapter {
    private String brokerId,brokerCode;

    public LeadTransactionProfilePagerAdapter(String brokerId, String brokerCode, FragmentManager fm) {
        super(fm);
        this.brokerId = brokerId;
        this.brokerCode = brokerCode;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AssignedLeadFragment.getInstance(brokerId,brokerCode);
            case 1:
                return BrokerTransactionsFragment.getInstance(brokerId,brokerCode);
            case 2:
                return UserInfoFragment.getInstance(brokerId,brokerCode);
            default:
                return new AssignedLeadFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Lead Assigned";
            case 1:
                return "Transactions";
            case 2:
                return "Personal Info";

        }
        return "";
    }
}
