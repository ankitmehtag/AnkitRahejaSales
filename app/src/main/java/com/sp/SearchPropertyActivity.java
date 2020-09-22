package com.sp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fragments.BaseFragment;
import com.fragments.FavFragment;
import com.fragments.SearchFragment;
import com.helper.BMHConstants;
import com.interfaces.HostActivityInterface;

public class SearchPropertyActivity extends BaseFragmentActivity implements HostActivityInterface {

	private BaseFragment selectedFragment;
	public static final int FAV_REQ_CODE = 11;
	// private Activity ctx = SearchPropertyActivity.this;
	private Typeface fond;
	public static final int TERM_AND_CONDITION_PAGE = 0;
	public static final int PRIVACY_PAGE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
		Toolbar toolbar = setDrawerAndToolbar();
		toolbar.setLogo(R.drawable.logo);
		SearchFragment frag = new SearchFragment();
		Bundle b = new Bundle();
		b.putBoolean("isFromFilter", false);
		frag.setArguments(b);
		addFragment(frag, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		app = (BMHApplication) getApplication();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_property, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected String setActionBarTitle() {
		return "";
	}

	@Override
	public void setSelectedFragment(BaseFragment fragment) {
		this.selectedFragment = fragment;

	}

	public BaseFragment getSelectedFragment() {
		return this.selectedFragment;
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		getSupportFragmentManager().popBackStackImmediate();
	}

	@Override
	public void popBackStackTillTag(String tag) {
		// TODO Auto-generated method stub
		getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	public void addFragment(BaseFragment fragment, boolean withAnimation) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (withAnimation) {
			// TO ENABLE FRAGMENT ANIMATION
			// Format: setCustomAnimations(old_frag_exit, new_frag_enter,
			// old_frag_enter, new_frag_exit);
			ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left,
					R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
		}

		ft.replace(R.id.container, fragment, fragment.getTagText());
		if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
			ft.addToBackStack(fragment.getTagText());
		}
		ft.commit();
	}

	@Override
	public void addMultipleFragments(BaseFragment[] fragments) {
	}

	public void clearBackStack() {
		final FragmentManager fragmentManager = getSupportFragmentManager();
		while (fragmentManager != null && fragmentManager.getBackStackEntryCount() != 0) {
			try {
				fragmentManager.popBackStackImmediate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else if (selectedFragment != null && !selectedFragment.onBackPressed()) {
			try {
				super.onBackPressed();
			} catch (Exception e) {

			}

		} else {
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FAV_REQ_CODE && resultCode == RESULT_OK) {
			if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
				addFragment(new FavFragment(), true);
			}
		}
	}

	@Override
	protected void onPause() {
		try {
			super.onPause();
		} catch (Exception e) {

		}

	}

}
