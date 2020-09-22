package com.interfaces;

import com.fragments.BaseFragment;

public interface HostActivityInterface {
	public void setSelectedFragment(BaseFragment fragment);
	public void popBackStack();
	public void popBackStackTillTag(String tag);
	public void addFragment(BaseFragment fragment, boolean withAnimation);
	public void addMultipleFragments(BaseFragment fragments[]);
	public BaseFragment getSelectedFragment();
}