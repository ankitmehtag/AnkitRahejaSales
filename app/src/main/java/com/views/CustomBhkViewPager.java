package com.views;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Naresh on 16-Mar-17.
 */

public class CustomBhkViewPager extends ViewPager {
    PagerAdapter mPagerAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPagerAdapter != null) {
            super.setAdapter(mPagerAdapter);
           // mPageIndicator.setViewPager(this);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    public CustomBhkViewPager(Context context) {
        super(context);
    }

    public CustomBhkViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
