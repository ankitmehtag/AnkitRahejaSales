package com.interfaces;

import android.view.MenuItem;

import com.fragments.SalesClosuresFragment;

public interface OnMenuItemClickListener<T> {
    void onMenuClick(MenuItem item, T fragment);
}
