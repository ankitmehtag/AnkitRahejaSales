package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.VO.PageVO;
import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.R;
import com.exception.BMHException;
import com.helper.UrlFactory;
import com.model.PropertyModel;

public class AboutUsFragment extends BaseFragment {

	private View rootView;
	private BMHApplication app;
	private Activity ctx;
	private WebView web;
	public static final String TAG = AboutUsFragment.class.getSimpleName();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_about_us, container, false);
			web = (WebView) rootView.findViewById(R.id.webViewAboutUs);
			ctx = getActivity();
			startTermsTask();
		} else {
			if(rootView.getParent() != null)
				((ViewGroup) rootView.getParent()).removeView(rootView);
			app = (BMHApplication) getActivity().getApplication();
			ctx = getActivity();
		}
		return rootView;

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);

	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
        for(int i = 0; i < menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }

	}

/*

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.property_list, menu);
		super.onCreateOptionsMenu(menu,inflater);
		menu.getItem(0).setVisible(false);
		menu.getItem(1).setVisible(false);
	}
*/


	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public String getTagText() {
		return TAG;
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	private void startTermsTask() {

		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			PageVO basevo;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (basevo == null) {
					showToast(getString(R.string.unable_to_connect_server));
				} else {
					if (basevo.isSuccess()) {
						web.loadData(basevo.getContent(), "text/html", "UTF-8");
					} else {
						showToast(basevo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					basevo = model.getPage(UrlFactory.getAboutUsUrl());
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


	protected void showToast(String msg){
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}


}
