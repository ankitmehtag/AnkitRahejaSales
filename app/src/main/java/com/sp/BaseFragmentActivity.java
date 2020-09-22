package com.sp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fragments.DrawerFragment;

public abstract class BaseFragmentActivity extends AppCompatActivity {

	protected BMHApplication app;
	private Toolbar appbar;
	protected abstract String setActionBarTitle();
	protected DrawerLayout mDrawerLayout;
	public final static int FIRST_PAGE = 0;
	public final static float BIG_SCALE = 1.0f;
	public final static float SMALL_SCALE = 0.6f;
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	protected Resources mResources = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//		}

//		if(!(this instanceof SearchPropertyActivity) && !(this instanceof LocationMapActivity)){
//			SystemBarTintManager tintManager = new SystemBarTintManager(this);
//			tintManager.setStatusBarTintEnabled(true);
//			tintManager.setStatusBarTintColor(Color.parseColor(getString(R.string.action_bar_green)));
//		}
		app =(BMHApplication) getApplication();
		mResources = getResources();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(app == null)
			app =(BMHApplication) getApplication();
		mResources = getResources();
	}


	protected void hideSoftKeyboard(EditText editText){
		app.hideSoftKeyboard(editText);
	}

	/**
	 * Shows the soft keyboard
	 */
	public void showSoftKeyboard(View view) {
		app.showSoftKeyboard(view);
	}

	public boolean getConnectivityStatus() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()){
				return true ;
			}
			else{
				return false ;
			}
		else
			return false;
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public String getRoundedFormatedPrice(int price){
		String a = "0";
		float b = 0;
		if(price==0){
			a="0";
		}else if(price>=1000 && price<100000){
			b = price/1000f;
			a = b +" K";
		}else if(price>=100000 && price<10000000){
			b = price/100000f;
			a = b +" L";
		}else if(price>=10000000){
			b = price/10000000f;
			a = b +" Cr";
		}
		return a;
	}

	public Toolbar setDrawerAndToolbar(){
		appbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(appbar);
		getSupportActionBar().setTitle(setActionBarTitle());

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		DrawerFragment fragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
		fragment.setUp(mDrawerLayout, appbar);
		return appbar;
	}

	public Toolbar setToolBar(){
		appbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(appbar);
		getSupportActionBar().setTitle(setActionBarTitle());
		return appbar;
	}


	public String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

//	public boolean onOptionItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		
//		 switch (item.getItemId()) {
//	        case R.id.action_unit_filter:
//	            unitFilter();
//	            return true;
//	        
//	        default:
//	            return super.onOptionItemSelected(item);
//	    }
//		
//	}

//	private void unitFilter() {
//		// TODO Auto-generated method stub
//		
//	}

	protected void showToast(String msg){
		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
	}

}
