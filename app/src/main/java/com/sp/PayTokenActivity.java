package com.sp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.VO.UnitDetailVO;
import com.adapters.PayTokenFragmentsAdapter;
import com.fragments.BaseFragment;
import com.interfaces.HostActivityInterface;
import com.views.NonSwipeableViewPager;

public class PayTokenActivity extends BaseFragmentActivity implements HostActivityInterface{

	private LinearLayout llTandC;
	private LinearLayout llPayment;
	
	private TextView tvTandC;
	private TextView tvPayment;
	private UnitDetailVO unitDetailVO;
	public NonSwipeableViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_token);
		
		unitDetailVO = getIntent().getParcelableExtra("unitvo");
		
		llTandC = (LinearLayout)findViewById(R.id.userBtnLayout);
		llPayment = (LinearLayout)findViewById(R.id.profileBtnLayout);
		
		tvTandC = (TextView)findViewById(R.id.locateBtn);
		tvPayment = (TextView)findViewById(R.id.userBtn);
		
		
		pager = (NonSwipeableViewPager) findViewById(R.id.pagerDeveloper);
		PayTokenFragmentsAdapter adapter = new PayTokenFragmentsAdapter(getSupportFragmentManager(), this, unitDetailVO);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setStyle(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		
//		setToolBar();
//		toolbar.setLogo(R.drawable.logo);
		
//		PaymentWebViewFragment frag = new PaymentWebViewFragment();
//		Bundle b = new Bundle();
//		b.putParcelable("unitvo", getIntent().getParcelableExtra("unitvo"));
//		frag.setArguments(b);
//		addFragment(frag, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pay_token, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected String setActionBarTitle() {
		return "Payment";
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFragment(BaseFragment fragment, boolean withAnimation) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		if(withAnimation) {
			// TO ENABLE FRAGMENT ANIMATION
			// Format: setCustomAnimations(old_frag_exit, new_frag_enter, old_frag_enter, new_frag_exit); 
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

	private void setStyle(int position){
		int selectedTabColor = getResources().getColor(R.color.blue_color);
		if(position==0){
			llTandC.setBackgroundColor(selectedTabColor);
			tvTandC.setTextColor(selectedTabColor);
			llPayment.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tvPayment.setTextColor(Color.parseColor("#454545"));
		}
		else if(position==1){
			llTandC.setBackgroundColor(Color.parseColor("#FFFFFF"));
			tvTandC.setTextColor(Color.parseColor("#454545"));
			llPayment.setBackgroundColor(selectedTabColor);
			tvPayment.setTextColor(selectedTabColor);
		}
	}
	
	
	@Override
	public void onBackPressed() {
	switch (pager.getCurrentItem()) {
	case 0:
		super.onBackPressed();
		break;
	case 1:
		pager.setCurrentItem(0, true);
		break;
	default:
		super.onBackPressed();
		break;
	}
		
	}
	
}
