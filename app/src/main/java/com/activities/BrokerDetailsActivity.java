package com.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fragments.LeadsAssignedFragment;
import com.fragments.PersonalInfoFragment;
import com.fragments.TransactionsFragment;
import com.model.BrokersListModel;
import com.sp.BrokersListActivity;
import com.sp.MyFirebaseMessagingService;
import com.sp.R;

import java.util.Objects;

public class BrokerDetailsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private BrokersListModel mData;
    public String brokerCode, brokerName, brokerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        if (tag.equalsIgnoreCase(MyFirebaseMessagingService.TAG)) {
            brokerCode = intent.getStringExtra("broker_code");
            brokerName = intent.getStringExtra("broker_name");
            brokerId = intent.getStringExtra("broker_id");
            tabLayout.getTabAt(2).select();
        } else if (tag.equalsIgnoreCase(BrokersListActivity.TAG)) {
            mData = intent.getParcelableExtra("BROKERS_MODEL");
            brokerCode = mData.getBroker_code();
            brokerId = mData.getBroker_id();
            brokerName = mData.getBroker_name();
        }
        toolbar.setTitle(getString(R.string.txt_broker_detalis_toolbar, brokerName, brokerCode));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

       /* mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));*/

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.black_alpha_40),
                ContextCompat.getColor(this, R.color.black)
        );

        tabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        public SectionsPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LeadsAssignedFragment();
                case 1:
                    return new TransactionsFragment();
                case 2:
                    return new PersonalInfoFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
