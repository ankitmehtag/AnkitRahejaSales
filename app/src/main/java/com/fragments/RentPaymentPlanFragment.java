package com.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.PaymentProccessActivity;
import com.sp.R;
import com.VO.UnitDetailVO;

public class RentPaymentPlanFragment extends Fragment {

	private PaymentProccessActivity fragmentActivity;

	UnitDetailVO vo;
	String impStyle = "<style>table{width:100% !important; height:60% !important;}</style>";
	private TextView tvGrandTotal;
//	String impStyle = "<style>table{width:100% !important; height:80% !important;}</style>";
	String rs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rent_payment_layout,container, false);
		rs = fragmentActivity.getResources().getString(R.string.Rs);
		rs = rs +" ";
		vo = ((PaymentProccessActivity) getActivity()).unitDetailVO;
		
		if(vo != null){
			TextView tvType = (TextView) view.findViewById(R.id.tvAprtmentType);
			TextView tvSize = (TextView) view.findViewById(R.id.tvAprtmentSize);
			
			TextView tvRentAmount = (TextView) view.findViewById(R.id.tvRentAmount);
			TextView tvSecurityDepo = (TextView) view.findViewById(R.id.tvSecurityDepo);
			TextView tvMaintance = (TextView) view.findViewById(R.id.tvMaintance);
			
			tvGrandTotal = (TextView) view.findViewById(R.id.tvGrandTotal);
			
			tvType.setText(vo.getDisplay_name());
			tvSize.setText(vo.getSize()+" Sqft");
			tvRentAmount.setText(rs+vo.getTotal_rent()+"");
			tvSecurityDepo.setText(rs+ vo.getSecurity_amount());
			tvMaintance.setText(rs+vo.getMaintance_charge());
			
			try {
				float f1 = Float.parseFloat(vo.getTotal_rent());
				float f2 = Float.parseFloat(vo.getSecurity_amount());
				float f3 = Float.parseFloat(vo.getMaintance_charge());
				
				float f4 = f1+f2+f3;
				tvGrandTotal.setText(rs+f4);
			} catch (Exception e) {
			}
			
				
		}
		
		LinearLayout accept = (LinearLayout) view.findViewById(R.id.ll_accept);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentActivity.pager.setCurrentItem(1, true);
			}
		});
		
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public PaymentProccessActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(PaymentProccessActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}
	
}