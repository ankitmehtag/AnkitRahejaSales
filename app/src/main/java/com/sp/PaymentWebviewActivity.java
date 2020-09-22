package com.sp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fragments.BaseFragment;
import com.fragments.PaymentWebViewFragment;
import com.interfaces.HostActivityInterface;

public class PaymentWebviewActivity extends BaseFragmentActivity implements HostActivityInterface{

	private Toolbar toolbar;
	private BaseFragment selectedFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_webview);
		toolbar = setToolBar();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle b = new Bundle();
		PaymentWebViewFragment f = new PaymentWebViewFragment();
		f.setFragmentActivity(this);
		b.putParcelable("unitvo", getIntent().getParcelableExtra("unitvo"));
		f.setArguments(b);
		addFragment(f, false);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.payment_webview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_settings:
				break;
			case android.R.id.home:
				/*Intent intent = new Intent(PaymentWebviewActivity.this, SearchPropertyActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
						Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);*/
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setSelectedFragment(BaseFragment fragment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popBackStack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popBackStackTillTag(String tag) {
	}

	@Override
	public void addFragment(BaseFragment fragment, boolean withAnimation) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(withAnimation) {
			ft.setCustomAnimations(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_right);
		}
		ft.replace(R.id.container, fragment, fragment.getTagText());
		ft.addToBackStack(fragment.getTagText());
        ft.commit();
		
	}

	@Override
	public void addMultipleFragments(BaseFragment[] fragments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseFragment getSelectedFragment() {
		return null;
	}

	@Override
	protected String setActionBarTitle() {
		// TODO Auto-generated method stub
		return "Payment";
	}
	
	@Override
	public void onBackPressed() {
		//finish();
		gotToHome();
	}

	public void gotToHome(){
		Intent intent = new Intent(PaymentWebviewActivity.this, ProjectsListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

}
