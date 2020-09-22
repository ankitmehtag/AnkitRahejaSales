package com.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.AppEnums.UIEventType;
import com.VO.UnitDetailVO;
import com.adapters.CustomSpinnerAdapter;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.PaymentProccessActivity;
import com.sp.R;
import com.model.NetworkErrorObject;
import com.utils.Utils;

import java.util.ArrayList;

public class BreakUpPlanFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{

	private PaymentProccessActivity fragmentActivity;

	WebView web;
	UnitDetailVO vo;
	String impStyle = "<style>table{width:100% !important; height:60% !important;}</style>";
	private TextView tvPC, tvtaxCarParking, tvGrandTotal;
//	String impStyle = "<style>table{width:100% !important; height:80% !important;}</style>";
	String rs;
	String paymentPlanUrl = "";
	protected BMHApplication app;
	private NetworkErrorObject mNetworkErrorObject = null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.price_breakup,container, false);
		rs = getActivity().getResources().getString(R.string.priceMark);
		rs = rs +" ";
		vo = ((PaymentProccessActivity) getActivity()).unitDetailVO;
		web = (WebView) view.findViewById(R.id.webviewPaymentPlan);
		final Spinner spPaymentOptions = (Spinner) view.findViewById(R.id.paymentOptions);
		int pos = this.getArguments().getInt("pos");

		ArrayList<String> arr2 = new ArrayList<String>();
		int maxParking = 0;
		try {
			maxParking = vo.getMax_parking();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		for (int i = 1; i <= maxParking ; i++) {
			arr2.add(i+" ");
		}
		if(vo != null){
//TODO:
		}

		LinearLayout accept = (LinearLayout) view.findViewById(R.id.ll_accept);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentActivity.pager.setCurrentItem(2, true);
			}
		});
		spPaymentOptions.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				String selectedS = spPaymentOptions.getSelectedItem().toString().toLowerCase();
				((PaymentProccessActivity) getActivity()).getPaymentPlan=selectedS;
				if(selectedS.contains("down payment")){
					paymentPlanUrl = vo.getDown_payment_plan();
				}else if(selectedS.contains("possession linked")){
					paymentPlanUrl = vo.getPossession_plan();
				}else if(selectedS.contains("construction linked")){
					paymentPlanUrl = vo.getConstruction_plan();
				}
				if(ConnectivityReceiver.isConnected()){
					//TODO: network call
					web.loadData(paymentPlanUrl+impStyle, "text/html", "UTF-8");
				}else{
					mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_PAYMENT_PLAN,
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(ConnectivityReceiver.isConnected()){
										//TODO: network call
										web.loadData(paymentPlanUrl+impStyle, "text/html", "UTF-8");
										mNetworkErrorObject.getAlertDialog().dismiss();
										mNetworkErrorObject = null;
									}else{
										Utils.showToast(getActivity(),getString(R.string.check_your_internet_connection));
									}
								}
							});
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if(vo.getPayment_plans() !=null && !vo.getPayment_plans().isEmpty() && !vo.getPayment_plans().get(0).equalsIgnoreCase("none")){
			CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(fragmentActivity, R.layout.textview_spinner, vo.getPayment_plans());
			spPaymentOptions.setAdapter(spinnerAdapter);
			web.setVisibility(View.VISIBLE);
			spPaymentOptions.setSelection(0);
		}else{
			LinearLayout ll = (LinearLayout) view.findViewById(R.id.llselectpaymentPlan);
			ll.setVisibility(View.GONE);
			web.setVisibility(View.GONE);
		}
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
	}

	public PaymentProccessActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(PaymentProccessActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case RETRY_PAYMENT_PLAN:
					web.loadData(paymentPlanUrl+impStyle, "text/html", "UTF-8");
					break;

			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}
}