package com.sp;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.helper.BMHConstants;
import com.helper.ParamsConstants;

public class FAQActivity extends BaseFragmentActivity {

	private View rootView;
	private BMHApplication app;
	private WebView web;
	public static final String TAG = FAQActivity.class.getSimpleName();
	private Toolbar toolbar;



	@Override
	protected String setActionBarTitle() {
		return "";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		app = (BMHApplication) getApplication();

		toolbar = setToolBar();
		toolbar.setTitle("FAQ's");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		web = (WebView)findViewById(R.id.webViewAboutUs);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(BMHConstants.FAQ_URL + "?"+ ParamsConstants.BUILDER_ID + "="+ BMHConstants.CURRENT_BUILDER_ID);
		web.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
				viewx.loadUrl(urlx);
				return false;
			}
		});
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//MenuItem item = menu.findItem(R.id.action_filter);
		//item.setIcon(R.drawable.ic_more_vert_white_24dp);
		return true;
		//item.setVisible(false);
	}
@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_filter:
				return true;
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

}
