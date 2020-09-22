package com.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.VO.BaseVO;
import com.VO.LocationVO;
import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.LocationMapActivity;
import com.sp.ProjectMapActivity;
import com.sp.R;
import com.sp.SliderActivity;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.model.PropertyModel;

import java.util.HashMap;

public class AlertLocationFragment extends Fragment {
	
	private int GET_ALERT_REQ=548;
	BMHApplication app;
	Button btnGet;
	ProgressBar pb;
	
	static Typeface fond;
	public static Fragment newInstance(Activity context, int pos, float scale, LocationVO vo){
		Bundle b = new Bundle();
		b.putInt("pos", pos);
//		b.putFloat("scale", scale);
		return Fragment.instantiate(context, AlertLocationFragment.class.getName(), b);
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
//		fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
//		TextView tv_alert = (TextView)v.findViewById(R.id.textViewAlertTitle);
//		tv_alert.setTypeface(fond);
		
		
		
		View v = inflater.inflate(R.layout.aletrs_crousel_item, container, false);
		app = (BMHApplication) getActivity().getApplication();
		btnGet = (Button) v.findViewById(R.id.btnGetAlerts);
		pb = (ProgressBar) v.findViewById(R.id.progressBarALert);
		TextView textViewAlertTitle = (TextView) v.findViewById(R.id.textViewAlertTitle);
		
		pb.setVisibility(View.GONE);
		
		btnGet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
    			String userEmail = app.getFromPrefs(BMHConstants.USER_EMAIL);
				if(userEmail.isEmpty()){
					Intent i = new Intent(getActivity(), SliderActivity.class);
					AlertLocationFragment.this.startActivityForResult(i, GET_ALERT_REQ);
				}else{
					submitAlert();
				}
			}
		});
		
		if(getActivity() instanceof ProjectMapActivity){
			textViewAlertTitle.setText("Get notified as soon as a new property added for your request.");
		}
		return v;
	}
	
	
	private void submitAlert() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(getActivity(), new AsyncListner(){
			BaseVO baseVo;
					@Override
					public void OnBackgroundTaskCompleted() {
						if(baseVo == null){
							showToast("Something went wrong. Try again");
						}else{
							if(baseVo.isSuccess()){
//								app.showSnackBar(getActivity(), baseVo.getMessage(), SnackBar.MED_SNACK);
								showSuccessDialog();
							}else{
								showToast( baseVo.getMessage());
							}
						}
						
						btnGet.setVisibility(View.VISIBLE);
						btnGet.setEnabled(true);
						pb.setVisibility(View.GONE);
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						PropertyModel model = new PropertyModel();
						try {
							HashMap<String, String> map = null; 
							if(getActivity() instanceof LocationMapActivity){
								LocationMapActivity acti = (LocationMapActivity) getActivity();
								map  = acti.searchMap;
							}else if(getActivity() instanceof ProjectMapActivity){
								ProjectMapActivity acti = (ProjectMapActivity) getActivity();
								map  = acti.searchMap;
							}
							
							
							if(map !=null){
								map.put("email", app.getFromPrefs(BMHConstants.USER_EMAIL));
								map.put("device_type", "android");
								map.put("device_id", app.getFromPrefs(BMHConstants.GCM_REG_ID));
								baseVo = model.submitAlert(map);
							}
						} catch (BMHException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void OnPreExec() {
						btnGet.setVisibility(View.INVISIBLE);
						btnGet.setEnabled(false);
						pb.setVisibility(View.VISIBLE);
					}
				});
		loadingTask.dontShowProgressDialog();
		loadingTask.execute("");
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("hh location alert fragment acti result");
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == GET_ALERT_REQ && resultCode == Activity.RESULT_OK){
			submitAlert();
		}
	}
	
	private void showSuccessDialog(){
		 LayoutInflater factory = LayoutInflater.from(getActivity());
		    final View dialogView = factory.inflate(R.layout.alert_registration, null);
		    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
		    
		    Button close = (Button) dialogView.findViewById(R.id.btnClose);
		    
		    close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		    dialog.setView(dialogView);
		    
		    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		    dialog.show();
	}

	protected void showToast(String msg){
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}

}
