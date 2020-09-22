package com.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sp.BMHApplication;
import com.sp.ProjectDetailActivity;
import com.sp.ProjectGalleryActivity;
import com.sp.R;
import com.sp.UnitDetailActivity;
import com.helper.BMHConstants;
import com.interfaces.GalleryCallback;

public class PropertyPDFFragment extends Fragment {

	private FragmentActivity fragmentActivity;
	private TextView tv;
	ProgressBar pb;
	LinearLayout btnDownload;
	BMHApplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_proj_pdf,container, false);
		
		Bundle b = getArguments();
		final String url = b.getString("imgurl");
		final int pos = b.getInt("pos");
		tv = (TextView) view.findViewById(R.id.tv_download);
		pb = (ProgressBar) view.findViewById(R.id.progressBarDownloading);
		btnDownload = (LinearLayout) view.findViewById(R.id.ll_download);
		app = (BMHApplication) fragmentActivity.getApplication();
		
		int count = b.getInt("count");
//		String imageName = b.getString("imgname");
		if(fragmentActivity instanceof ProjectGalleryActivity){
			TextView tvFragNo = (TextView) view.findViewById(R.id.tvFragNo);
//			TextView tvImageName = (TextView) view.findViewById(R.id.tvImageName);
			
			tvFragNo.setVisibility(View.VISIBLE);
//			tvImageName.setVisibility(View.VISIBLE);
			int n = pos+1;
			tvFragNo.setText(n+"/"+count);
//			tvImageName.setText(imageName);
		}
		
		setUiAccTodownloadingState();
		
			if(fragmentActivity instanceof ProjectGalleryActivity | fragmentActivity instanceof ProjectDetailActivity || fragmentActivity instanceof UnitDetailActivity){
				
				btnDownload.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(url.equalsIgnoreCase("")){
							app.showToastAtCenter(fragmentActivity, "Sorry, the download link is not available.");
						}else if(!url.endsWith(".pdf")){
							app.showToastAtCenter(fragmentActivity, "Sorry, the download link is broken.");
						}else{
							GalleryCallback callBack = (GalleryCallback) fragmentActivity;
							callBack.onPageClicked(pos, BMHConstants.TYPE_PDF);
							tv.setText("Downloading...");
							pb.setVisibility(View.VISIBLE);
							btnDownload.setEnabled(false);
						}
						
					}
				});
			}
		
//		img.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(fragmentActivity instanceof ProjectDetailActivity){
////					Toast.makeText(fragmentActivity, "Download PDF", 1000).show();
//					GalleryCallback callBack = (GalleryCallback) fragmentActivity;
//					callBack.onPageClicked(pos, BMHConstants.TYPE_PDF);
//				}else{
//					System.out.println("hh not instance");
//				}
//			}
//		});
		
		
		
		return view;
	}


	private void setUiAccTodownloadingState() {
		SharedPreferences prefs = fragmentActivity.getSharedPreferences(BMHConstants.PREF_NAME, Activity.MODE_PRIVATE);
		boolean isdownloading = prefs.getBoolean(BMHConstants.IS_DOWNLOADING_KEY, false);
		if(isdownloading){
			tv.setText("Downloading...");
			pb.setVisibility(View.VISIBLE);
			btnDownload.setEnabled(false);
		}else{
			tv.setText("Download");
			pb.setVisibility(View.GONE);
			btnDownload.setEnabled(true);
		}
	}

	
	@Override
	public void onStart() {
		super.onStart();
		LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(fragmentActivity);
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(BMHConstants.ACTION_DOWNLOAD_COMPLETE);
	    bManager.registerReceiver(dowloadFinished, intentFilter);
	}

	public FragmentActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(FragmentActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}
	
	   public void onStop() {
		   super.onStop();
		   LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(fragmentActivity);
		   bManager.unregisterReceiver(dowloadFinished);
		   
	   };
	   
	    BroadcastReceiver dowloadFinished = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(BMHConstants.ACTION_DOWNLOAD_COMPLETE)) {
					tv.setText("Download");
					pb.setVisibility(View.GONE);
					btnDownload.setEnabled(true);
		        }
			}
		};
		
		public void setUserVisibleHint(boolean isVisibleToUser) {
			if(isVisibleToUser && tv!=null){
				setUiAccTodownloadingState();
			}
		};
}
