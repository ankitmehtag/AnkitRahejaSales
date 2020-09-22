package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.VO.UnitDetailVO;
import com.adapters.CustomSpinnerAdapter;
import com.sp.BMHApplication;
import com.sp.PaymentProccessActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.UrlFactory;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PaymentPlanFragment extends Fragment {

	private PaymentProccessActivity fragmentActivity;

	//WebView web;
	UnitDetailVO vo;
	String impStyle = "<style>table{width:100% !important; height:60% !important;}</style>";
	private TextView tvPC, tvtaxCarParking, tvGrandTotal,tvPrice;
	String rs;
	private HashMap<String, String> searchParams;
	protected BMHApplication app;
	private UnitDetailVO unitDetailVO;
	private Activity ctx;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.payment_plan, container, false);
		if(fragmentActivity == null)return null;
		rs = fragmentActivity.getResources().getString(R.string.priceMark);
		rs = rs + " ";
		//	View view1 = inflater.inflate(R.layout.price_breakup, container, false);
		vo = ((PaymentProccessActivity) getActivity()).unitDetailVO;
		//web = (WebView) view.findViewById(R.id.webviewPaymentPlan);
		final Spinner spPaymentOptions = (Spinner) view.findViewById(R.id.paymentOptions);
		//int pos = this.getArguments().getInt("pos");
		ArrayList<String> arr2 = new ArrayList<String>();
		int maxParking = vo.getMax_parking();
		int minParking = vo.getMin_parking();
		int parkingInterval = vo.getParking_interval();
		parkingInterval = parkingInterval == 0 ? 1 : parkingInterval;
		for (int i = minParking; i <= maxParking; i = (i + parkingInterval)) {
			arr2.add(String.valueOf(i));
		}
		LinearLayout llNoOfParkings = (LinearLayout) view.findViewById(R.id.llNoOfParkings);
		LinearLayout ll_parking_charges = (LinearLayout) view.findViewById(R.id.ll_parking_charges);
		LinearLayout ll_parking_tax = (LinearLayout) view.findViewById(R.id.ll_parking_tax);
		final Spinner spNoofParkings = (Spinner) view.findViewById(R.id.no_of_parkings);
		if (maxParking > 0) {
			CustomSpinnerAdapter adapterNoofParkings = new CustomSpinnerAdapter(fragmentActivity, R.layout.textview_spinner, arr2);
			spNoofParkings.setAdapter(adapterNoofParkings);
			llNoOfParkings.setVisibility(View.VISIBLE);
			ll_parking_charges.setVisibility(View.VISIBLE);
			ll_parking_tax.setVisibility(View.VISIBLE);

		} else {
			llNoOfParkings.setVisibility(View.GONE);
			ll_parking_charges.setVisibility(View.GONE);
			ll_parking_tax.setVisibility(View.GONE);

		}
		if (vo != null) {
			LinearLayout llDiscount = (LinearLayout) view.findViewById(R.id.llDiscount);
			LinearLayout llDiscountedBsp = (LinearLayout) view.findViewById(R.id.llDiscountedBSP);
			TextView tvType = (TextView) view.findViewById(R.id.tvAprtmentType);
			TextView tvSize = (TextView) view.findViewById(R.id.tvAprtmentSize);
			TextView tvPricePerSqft = (TextView) view.findViewById(R.id.tvAprtmentPricePerSqFt);
			TextView tvBSP = (TextView) view.findViewById(R.id.tvBSP);
			TextView tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
			TextView tvDiscountedBSP = (TextView) view.findViewById(R.id.tvBSPAfterDiscount);
			TextView tvPLC = (TextView) view.findViewById(R.id.tvPLC);
			//TextView tvCC = (TextView) view.findViewById(R.id.tvCC);
			tvPC = (TextView) view.findViewById(R.id.tvPC);
			TextView tvEDC = (TextView) view.findViewById(R.id.tvEDC);
			TextView tvClub = (TextView) view.findViewById(R.id.tvClub);
			TextView tvIBMS = (TextView) view.findViewById(R.id.tvIBMS);
			TextView tvIFMS = (TextView) view.findViewById(R.id.tvIFMS);

			TextView tvClubPlcLabel = (TextView) view.findViewById(R.id.tvSerTAxClubMemLabel);

			TextView tvSerTAxClubMem = (TextView) view.findViewById(R.id.tvSerTAxClubMem);
			TextView tvSerTAxPlc = (TextView) view.findViewById(R.id.tvSerTAxPlc);

			TextView tvTaxBspLabel = (TextView) view.findViewById(R.id.tvSerTaxBspLabel);
			TextView tvTaxBsp = (TextView) view.findViewById(R.id.tvSerTaxBsp);
			TextView tvtaxCarParkingLabel = (TextView) view.findViewById(R.id.tvSerTaxCarParkingLabel);
			tvtaxCarParking = (TextView) view.findViewById(R.id.tvSerTaxCarParking);
			TextView tvGstPlcLabel = (TextView) view.findViewById(R.id.tvGstBspPlcLabel);
			TextView tvGstPlc = (TextView) view.findViewById(R.id.tvGstBspPlc);
			TextView tvBookingFee = (TextView) view.findViewById(R.id.tvBookingFee);
			tvGrandTotal = (TextView) view.findViewById(R.id.tvGrandTotal);

			TextView tvBuilder = (TextView) view.findViewById(R.id.tvBuilderName);
			TextView tvSqft = (TextView) view.findViewById(R.id.tvPricePsf);
			TextView tvUnit = (TextView) view.findViewById(R.id.tvUnitNo);
			TextView tvFloor = (TextView) view.findViewById(R.id.tvFloorNo);
			TextView tvProjectname = (TextView) view.findViewById(R.id.tv_project_name);
			TextView tvBhk = (TextView) view.findViewById(R.id.bhk);
			TextView tvPossession = (TextView) view.findViewById(R.id.Possession);
			TextView tvReturnPrice = (TextView)view.findViewById(R.id.tvReturnPrice);
			tvPrice = (TextView) view.findViewById(R.id.tvPrice);
			TextView tvBookingF = (TextView) view.findViewById(R.id.tvBookingF);
			ImageView imgView = (ImageView) view.findViewById(R.id.unit_img);
			ImageView imgBuilderLogo = (ImageView) view.findViewById(R.id.pagerDeveloper);
			LinearLayout ll_ibms = (LinearLayout)view.findViewById(R.id.ll_ibms);
			LinearLayout ll_ifms = (LinearLayout)view.findViewById(R.id.ll_ifms);

			LinearLayout ll_otherCharges = (LinearLayout) view.findViewById(R.id.ll_otherCharges);
			TextView tvOtherChargesTitle = (TextView)view.findViewById(R.id.tvOtherChargesTitle);
			TextView tvOtherCharges = (TextView)view.findViewById(R.id.tvOtherCharges);

			tvType.setText(vo.getDisplay_name());
			String apartmentSize = vo.getSize() + " " + vo.getSize_unit();
			tvSize.setText(apartmentSize);

			String avgPriceBsp = vo.getOrginalbsp();
			float priceBsp = Utils.toFloat(avgPriceBsp);
			if(priceBsp > 0) avgPriceBsp = Utils.priceFormat(priceBsp);
			tvBSP.setText(rs + avgPriceBsp);

			String avgPricePSF = vo.getPrice_SqFt();
			float pricePSF = Utils.toFloat(avgPricePSF);
			if(pricePSF > 0) avgPricePSF = Utils.priceFormat(pricePSF);
			tvPricePerSqft.setText(rs + avgPricePSF + " " + vo.getPrice_SqFt_unit());

			String avgPricePlc = vo.getTotal_plc();
			float pricePlc = Utils.toFloat(avgPricePlc);
			if(pricePlc > 0) avgPricePlc = Utils.priceFormat(pricePlc);
			tvPLC.setText(rs + avgPricePlc);

			String avgPriceParking = vo.getParking_charges();
			float priceParking = Utils.toFloat(avgPriceParking);
			if(priceParking > 0) avgPriceParking = Utils.priceFormat(priceParking);
			tvPC.setText(rs + avgPriceParking);

			String avgPriceClub = vo.getClub_charges();
			float priceClub = Utils.toFloat(avgPriceClub);
			if(priceClub > 0) avgPriceClub = Utils.priceFormat(priceClub);
			tvClub.setText(rs + avgPriceClub);

			String avgPriceIdc = vo.getTotal_edc_idc();
			float priceIdc = Utils.toFloat(avgPriceIdc);
			if(priceIdc > 0) avgPriceIdc = Utils.priceFormat(priceIdc);
			tvEDC.setText(rs + avgPriceIdc);

			if(vo.getTotal_ibms().equalsIgnoreCase("0")){
				ll_ibms.setVisibility(View.GONE);
			}if(vo.getTotal_ifms().equalsIgnoreCase("0")){
				ll_ifms.setVisibility(View.GONE);
			}

			String avgPriceIbms = vo.getTotal_ibms();
			float priceIbms = Utils.toFloat(avgPriceIbms);
			if(priceIbms > 0) avgPriceIbms = Utils.priceFormat(priceIbms);
			tvIBMS.setText(rs + avgPriceIbms);

			String avgPriceIfms = vo.getTotal_ifms();
			float priceIfms = Utils.toFloat(avgPriceIfms);
			if(priceIfms > 0) avgPriceIfms = Utils.priceFormat(priceIfms);
			tvIFMS.setText(rs + avgPriceIfms);

			String avgPriceClubPlc = vo.getGst_club();
			float priceClubPlc = Utils.toFloat(avgPriceClubPlc);
			if(priceClubPlc > 0) avgPriceClubPlc = Utils.priceFormat(priceClubPlc);
			tvSerTAxClubMem.setText(rs + avgPriceClubPlc);

			String avgSerTAxPlc = vo.getGst_plc();
			float priceSerTAxPlc = Utils.toFloat(avgSerTAxPlc);
			if(priceSerTAxPlc > 0) avgSerTAxPlc = Utils.priceFormat(priceSerTAxPlc);
			tvSerTAxPlc.setText(rs + avgSerTAxPlc);

			String avgPriceBspTax = vo.getTotal_bsp_tax();
			float priceBspTax = Utils.toFloat(avgPriceBspTax);
			if(priceBspTax > 0) avgPriceBspTax = Utils.priceFormat(priceBspTax);
			tvTaxBsp.setText(rs + avgPriceBspTax);

			String avgPriceParkingTax = vo.getTotal_parking_tax();
			float priceParkingTax = Utils.toFloat(avgPriceParkingTax);
			if(priceParkingTax > 0) avgPriceParkingTax = Utils.priceFormat(priceParkingTax);
			tvtaxCarParking.setText(rs + avgPriceParkingTax);

			String avgPriceGstTax = vo.getTotal_gst_vat_tax();
			float priceGstTax = Utils.toFloat(avgPriceGstTax);
			if(priceGstTax > 0) avgPriceGstTax = Utils.priceFormat(priceGstTax);
			tvGstPlc.setText(rs + avgPriceGstTax);

			String avgPriceBookingTax = vo.getBooking_fees();
			float priceBookingTax = Utils.toFloat(avgPriceBookingTax);
			if(priceBookingTax > 0) avgPriceBookingTax = Utils.priceFormat(priceBookingTax);
			tvBookingFee.setText(rs + avgPriceBookingTax);

			ll_otherCharges.setVisibility(View.GONE);

			if(vo.getOther_charges() != null){
				try {
					JSONArray mJsonArray = new JSONArray(vo.getOther_charges());
					if(mJsonArray != null) {
						for (int i = 0; i < mJsonArray.length(); i++) {
							if( mJsonArray.get(i) != null){
								JSONObject mJsonObject = (JSONObject) mJsonArray.get(i);
								float value = Utils.toFloat(mJsonObject.opt("value").toString());
								String balPrice = Utils.priceFormat(value);
								tvOtherCharges.setText(rs + balPrice);
								tvOtherChargesTitle.setText(mJsonObject.opt("title").toString());

								ll_otherCharges.setVisibility(View.VISIBLE);
							}

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


			tvGrandTotal.setText(rs + vo.getGrand_total());

			tvSqft.setText(apartmentSize);
			tvBuilder.setText(vo.getBuilderName());
			if (vo.getUnit_image() != null && !vo.getUnit_image().isEmpty()) {
				Picasso.with(ctx).load(UrlFactory.getShortImageByWidthUrl(BMHConstants.LIST_IMAGE_WIDTH,vo
						.getUnit_image())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(imgBuilderLogo);
			}
			if(vo.getIsLotteryProject() != null && vo.getIsLotteryProject().equalsIgnoreCase("1")){
				tvUnit.setVisibility(View.INVISIBLE);
				tvFloor.setVisibility(View.INVISIBLE);
				tvReturnPrice.setText("Refundable*\n*Booking Amount is refundable only if in case your allotment of unit does not get confirmed during the draw.");

			}else {
				tvReturnPrice.setText("Non-refundable");
				if (vo.getunitNo() != null) {
					tvUnit.setVisibility(View.VISIBLE);
					tvUnit.setText("Unit No. : " + vo.getunitNo());
				}
				if (vo.getUnitFloor() != null) {
					tvFloor.setVisibility(View.VISIBLE);
					tvFloor.setText("Floor : " + vo.getUnitFloor());
				}
			}
			if(vo.getProject_name() != null) {
				tvProjectname.setText(Html.fromHtml(vo.getProject_name()).toString());
			}
			tvBhk.setText(vo.getDisplay_name());

			tvPossession.setText("Possession: " + getFormattedDate(vo.getPossession_dt()));
			tvBookingF.setText(rs + vo.getBooking_fees());
			tvPrice.setText(rs + vo.getGrand_total());
			if (vo.getDiscount() != null && !vo.getDiscount().isEmpty()) {
				llDiscount.setVisibility(View.VISIBLE);
				llDiscountedBsp.setVisibility(View.VISIBLE);
				tvDiscount.setText(vo.getDiscount());
				tvDiscountedBSP.setText(rs + vo.getTotal_discounted_bsp());
			}
		}
		LinearLayout accept = (LinearLayout) view.findViewById(R.id.ll_accept);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentActivity.pager.setCurrentItem(1, true);
			}
		});

		/*spPaymentOptions.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedS = spPaymentOptions.getSelectedItem().toString().toLowerCase();
				if (selectedS.contains("down payment")) {
					web.loadData(vo.getDown_payment_plan() + impStyle, "text/html", "UTF-8");
				} else if (selectedS.contains("possession linked")) {
					web.loadData(vo.getPossession_plan() + impStyle, "text/html", "UTF-8");
				} else if (selectedS.contains("construction linked")) {
					web.loadData(vo.getConstruction_plan() + impStyle, "text/html", "UTF-8");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});*/

		if (vo.getPayment_plans() != null && !vo.getPayment_plans().isEmpty()
				&& !vo.getPayment_plans().get(0).equalsIgnoreCase("none")) {
			CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(fragmentActivity, R.layout.textview_spinner, vo.getPayment_plans());
			spPaymentOptions.setAdapter(spinnerAdapter);
			//web.setVisibility(View.VISIBLE);
			spPaymentOptions.setSelection(0);
		} else {
			LinearLayout ll = (LinearLayout) view.findViewById(R.id.llselectpaymentPlan);
			ll.setVisibility(View.GONE);
			//web.setVisibility(View.GONE);
		}

		spNoofParkings.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String parkings = spNoofParkings.getSelectedItem().toString();
				int noOfPar = Utils.toInt(parkings);
				//int noOfPar = position + 1;
				UnitDetailVO aasdf = vo;
				double pc = Utils.toDouble(vo.getParking_charges().replace(",",""));
				double taxPer = Utils.toDouble(vo.getParking_service_tax().replace(",",""));
				double totalTaxPar = Utils.toDouble(vo.getTotal_parking_tax().replace(",",""));
				double grandTotal = vo.getGrand_total_int();
				grandTotal = grandTotal - totalTaxPar - pc;
				pc = pc * noOfPar;
				totalTaxPar = (pc * taxPer) / 100;
				grandTotal = grandTotal + totalTaxPar + pc;
				tvGrandTotal.setText(rs + getDecimalFormatedPrice(grandTotal));
				//tvPrice.setText(rs + getDecimalFormatedPrice(grandTotal));
				tvPC.setText(rs + getDecimalFormatedPrice(pc));
				tvtaxCarParking.setText(rs + getDecimalFormatedPrice(totalTaxPar));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
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
		if(this.fragmentActivity == null)return;
	}

	public String getDecimalFormatedPrice(double price) {
		String pattern = "##.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);

		String a = "0";
		double b = 0;
		if (price == 0) {
			a = "0";
		} else if (price >= 1000 && price < 100000) {
			a = price + "";
			b = price / 1000f;
			a = Utils.priceFormat(price);
			//a = decimalFormat.format(b) + " K";
		} else if (price >= 100000 && price < 10000000) {
			b = price / 100000f;
			a = decimalFormat.format(b) + " Lacs";
		} else if (price >= 10000000) {
			b = price / 10000000f;
			a = decimalFormat.format(b) + " Cr";
		}
		return a;
	}

	private String getFormattedDate(String date){
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date mDate = mSimpleDateFormat.parse(date);
			SimpleDateFormat df = new SimpleDateFormat("MMM, yyyy");
			return df.format(mDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}
}