package com.adapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fragments.BlogRecylerFragment;
import com.model.BlogData;
import com.model.Category;

import java.util.ArrayList;

/**
 * Created by Mohit on 7/4/2018.
 */

public class BlogPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Fragment fragment = null;
    private ArrayList<String> tabList;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<BlogData> blogDataArrayList;
    private Bundle mBundle;
    private String banner_img_url, banner_name, blog_id;

    public BlogPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabList, ArrayList<Category> categoryArrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabList = tabList;
        this.categoryArrayList = categoryArrayList;
    }

    public BlogPagerAdapter(FragmentManager fm, ArrayList<Category> categoryArrayList) {
        super(fm);
        this.categoryArrayList = categoryArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        for (int i = 0; i < mNumOfTabs; i++) {
            if (i == position) {
                mBundle = new Bundle();
                blogDataArrayList = categoryArrayList.get(i).getBlog_data();
                banner_img_url = categoryArrayList.get(i).getBanner().getBanner_img();
                banner_name = categoryArrayList.get(i).getBanner().getBanner_text();
                blog_id = categoryArrayList.get(i).getBanner().getBlog_id();
                BlogRecylerFragment fragment = new BlogRecylerFragment();
                mBundle.putParcelableArrayList("blog_data", blogDataArrayList);
                mBundle.putString("banner_img", banner_img_url);
                mBundle.putString("banner_text", banner_name);
                mBundle.putString("blog_id", blog_id);
                mBundle.putString("category_id", categoryArrayList.get(i).getCategory_id());
                return fragment.newInstance(mBundle);
            }
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList.get(position);
    }
}
