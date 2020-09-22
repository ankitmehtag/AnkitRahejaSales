package com.sp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.AppEnums.UIEventType;
import com.fragments.BaseFragment;
import com.fragments.PaymentWebViewFragment;
import com.interfaces.HostActivityInterface;
import com.model.NetworkErrorObject;
import com.utils.Utils;

public class PayUResponseWebViewActivity extends BaseFragmentActivity implements HostActivityInterface,ConnectivityReceiver.ConnectivityReceiverListener{

	private Toolbar toolbar;

	private WebView webview;
	private static String TAG = PaymentWebViewFragment.class.getSimpleName();
	private LinearLayout llprogress;
	private Button btn_done;
	private NetworkErrorObject mNetworkErrorObject = null;
	private String url = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payu_response_web_view);
		toolbar = setToolBar();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		llprogress = (LinearLayout)findViewById(R.id.ll_webprogress);
		btn_done = (Button)findViewById(R.id.btn_done);
		btn_done.setOnClickListener(mOnClickListener);
		final ProgressBar pb = (ProgressBar)findViewById(R.id.progressbarweb);
		webview = (WebView)findViewById(R.id.webView1);

		if(getIntent() == null || getIntent().getStringExtra("URL") == null  || getIntent().getStringExtra("URL").isEmpty()) {
			return;
		}
		url = getIntent().getStringExtra("URL");
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				pb.setProgress(progress);
			}
		});

		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(webview, url);
				llprogress.setVisibility(View.GONE);
				Log.i(TAG, "onPageFinished()");
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(PayUResponseWebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				llprogress.setVisibility(View.VISIBLE);
			}
		});
		if(ConnectivityReceiver.isConnected()){
			//TODO: network call
			webview.loadUrl(url);
		}else{
			mNetworkErrorObject = Utils.showNetworkErrorDialog(PayUResponseWebViewActivity.this, UIEventType.RETRY_TRANSACTION_STATUS,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ConnectivityReceiver.isConnected()){
								//TODO: network call
								webview.loadUrl(url);
								mNetworkErrorObject.getAlertDialog().dismiss();
								mNetworkErrorObject = null;
							}else{
								Utils.showToast(PayUResponseWebViewActivity.this,getString(R.string.check_your_internet_connection));
							}
						}
					});
		}
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
		Intent intent = new Intent(PayUResponseWebViewActivity.this, ProjectsListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.btn_done:
					gotToHome();
					break;
			}
		}
	};


	@Override
	public void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
	}
	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case RETRY_TRANSACTION_STATUS:
					//TODO: call
					webview.loadUrl(url);
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}
}
