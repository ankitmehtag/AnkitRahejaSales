package com.sp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.VO.PageVO;
import com.sp.CustomAsyncTask.AsyncListner;
import com.exception.BMHException;
import com.helper.UrlFactory;
import com.model.PropertyModel;

public class TermsWebActivity extends BaseFragmentActivity {

	private BMHApplication app;
	private WebView web;
	PageVO basevo;
	Activity ctx = this;
	int type;
	Typeface fond;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_web);

		Toolbar toolbar = setToolBar();
//		toolbar.setLogo(R.drawable.arrownav);


		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//		regularTypeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
		String fontPath = "fonts/regular.ttf";

		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		setToolBar();
		app = (BMHApplication) getApplication();
		web = (WebView) findViewById(R.id.webViewTerms);

//		web.setTypeface(tf);

		type = getIntent().getIntExtra("pageType", -1);
		startTermsTask();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.terms_web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:

				break;
			case android.R.id.home:
				finish();
				break;

		}

//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	private void startTermsTask() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner(){

			@Override
			public void OnBackgroundTaskCompleted() {
				if(basevo == null){
					showToast(getString(R.string.unable_to_connect_server));
				}else{
					if(basevo.isSuccess()){
						web.loadData(basevo.getContent(), "text/html", "UTF-8");
					}else{
						showToast( basevo.getMessage());
					}
				}
			}
			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					if(type == SliderActivity.TERM_AND_CONDITION_PAGE){
						basevo = model.getPage(UrlFactory.getT_CUrl());
					}
					else if(type == SliderActivity.PRIVACY_PAGE){
						basevo = model.getPage(UrlFactory.getPrivacyPolicy());
					}else{
						//TODO: error
					}

				} catch (BMHException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void OnPreExec() {

			}
		});
		loadingTask.execute("");
	}






	@Override
	protected String setActionBarTitle() {
		// TODO Auto-generated method stub
		type = getIntent().getIntExtra("pageType", -1);
		if(type == SliderActivity.TERM_AND_CONDITION_PAGE){
			return "Terms & Conditions";
		}else{
			return "Privacy Policy";
		}

	}
}
