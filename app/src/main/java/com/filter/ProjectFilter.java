package com.filter;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.android.gms.internal.el;
import com.sp.ProjectMapActivity;
import com.sp.R;
import com.utils.Connectivity;
import com.views.RangeSeekBar;
import com.views.RangeSeekBar.OnRangeSeekBarChangeListener;
import java.util.*;

public class ProjectFilter extends Activity {

	String status_value = "";
	String sort_value = "";
	String offer_value = "";
	String status_possession = "";
	ArrayList<String> amenities_value = new ArrayList<>();
	String bhk_value = "";

	String price_range_min_value = "";
	String price_range_max_value = "";

	String price_psf_min_value = "";
	String price_psf_max_value = "";

	String residential_value = "";
	String commercial_value = "";

	String built_area_min_value = "";
	String built_area_max_value = "";

	private Activity ctx = this;

	private View rootView;
	private PopupWindow pw;
	private int selectedSortType = 0;
	private boolean lToHSelected, HTolSelected, btnAllSelected = false;
	private boolean ResiSelected, ComSelected = false;
	private LinearLayout ResiComm;
	private HashMap<String, String> searchValuesMap;
	private final int PRICE = 0;
	private final int PRICEPSF = 1;
	private final int POSSESSION = 2;
	private final int RELEVANCE = 3;
	private AutoCompleteTextView autoCompleteCity;
	private Button price, pricePSF, Possession, Relevance, Newest, Rating, Infra, Needs, Lifestyle, Returns, btn1y,
			btn2y, btn3y, btn4y, btn5y, btn6y, btnlaunch, btnUnder, btnReady, btnResidential, btnCommercial, BHKCheck,
			onebhk, twobhk, threebhk, fourbhk, fivebhk, fivePbhk, projectFilter, btnClosePopup;
	// private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5,
	// checkbox6, check_off;
	private Activity dialog;
	private RangeSeekBar<Integer> seekBarPrice, seekBarBuildUp, seekBarPsf;
	private String[] priceLabels = { "25L", "50L", "1Cr", "2Cr", "3Cr", "4Cr", "5Cr" };
	private String[] builtUpAreaLabels = { "250", "500", "1000", "2500", "3000", "3500", "4000", "4500", "5000", "6000",
			"7000", "8000" };
	private String[] psfLabels = { "500", "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000",
			"10000", "15000", "20000" };
	private String[] priceLabelsInt = { "2500000", "5000000", "10000000", "20000000", "30000000", "40000000",
			"50000000" };
	private String[] builtUpAreaLabelsInt = { "250", "500", "1000", "2500", "3000", "3500", "4000", "4500", "5000",
			"6000", "7000", "8000" };
	private String[] psfLabelsInt = { "500", "1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000",
			"10000", "15000", "20000" };

	private final int MIN_PRICE = 0, MAX_PRICE = priceLabels.length - 1;
	private final int MIN_PSF = 0, MAX_PSF = psfLabels.length - 1;
	private final int MIN_AREA = 0, MAX_AREA = builtUpAreaLabels.length - 1;

	String filer_type = "";

	Typeface fond;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_filter);

		fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

		// Relevance = (Button) findViewById(R.id.btn_relevance);
		Newest = (Button) findViewById(R.id.btn_newest);
		price = (Button) findViewById(R.id.btn_price);
		pricePSF = (Button) findViewById(R.id.btn_pricepsf);
		Rating = (Button) findViewById(R.id.btn_rating);
		Possession = (Button) findViewById(R.id.btn_possession);
		Infra = (Button) findViewById(R.id.btn_infra);
		Needs = (Button) findViewById(R.id.btn_needs);
		Lifestyle = (Button) findViewById(R.id.btn_lofestyle);
		Returns = (Button) findViewById(R.id.btn_returns);

		btn1y = (Button) findViewById(R.id.btn_1y);
		btn2y = (Button) findViewById(R.id.btn_2y);
		btn3y = (Button) findViewById(R.id.btn_3y);
		btn4y = (Button) findViewById(R.id.btn_4y);
		btn5y = (Button) findViewById(R.id.btn_5y);
		btn6y = (Button) findViewById(R.id.btn_6y);

		// btnlaunch = (Button) findViewById(R.id.btnNew_launch);
		// btnUnder = (Button) findViewById(R.id.btn_under_construction);
		// btnReady = (Button) findViewById(R.id.btn_readytomove);

		final CheckBox checklaunch = (CheckBox) findViewById(R.id.chk_new_launch);
		final CheckBox checkUnder = (CheckBox) findViewById(R.id.chk_under_construction);
		final CheckBox checkReady = (CheckBox) findViewById(R.id.chk_readytomove);

		btnResidential = (Button) findViewById(R.id.btn_residential);
		final Button btnCommercial = (Button) findViewById(R.id.btn_commercial);

		final CheckBox flating = (CheckBox) findViewById(R.id.chk_resi_flat);
		final CheckBox builder = (CheckBox) findViewById(R.id.chk_resi_builder);
		final CheckBox plots = (CheckBox) findViewById(R.id.chk_resi_plots);
		final CheckBox housevilla = (CheckBox) findViewById(R.id.chk_resi_house_villa);
		final CheckBox shops = (CheckBox) findViewById(R.id.chk_com_shops);
		final CheckBox office = (CheckBox) findViewById(R.id.chk_com_office);
		final CheckBox serviceAppartment = (CheckBox) findViewById(R.id.chk_com_service_apartment);

		final LinearLayout llPrice = (LinearLayout) findViewById(R.id.llseekbar);
		LinearLayout llBuiltUpArea = (LinearLayout) findViewById(R.id.llseekbar_builtArea);
		LinearLayout llseekBarPSF = (LinearLayout) findViewById(R.id.llseekbar_pricePSF);

		// =========== Set only font static as Heading
		TextView status = (TextView) findViewById(R.id.status);
		TextView sort = (TextView) findViewById(R.id.text_sort);
		TextView project = (TextView) findViewById(R.id.with);
		TextView possession = (TextView) findViewById(R.id.textView4);
		TextView select_b = (TextView) findViewById(R.id.builder_li);
		TextView text_ser = (TextView) findViewById(R.id.tv_selected_builders);
		TextView property = (TextView) findViewById(R.id.tv_showing_floors);
		TextView property_text = (TextView) findViewById(R.id.tv_filter_by_text);
		TextView bhk = (TextView) findViewById(R.id.bhk_list);
		TextView price_rang = (TextView) findViewById(R.id.text_seekbar);
		TextView build = (TextView) findViewById(R.id.text_built);
		TextView pricepsf = (TextView) findViewById(R.id.text_price);
		TextView amenities = (TextView) findViewById(R.id.ame);

		status.setTypeface(fond);
		sort.setTypeface(fond);
		project.setTypeface(fond);
		possession.setTypeface(fond);
		select_b.setTypeface(fond);
		text_ser.setTypeface(fond);
		property.setTypeface(fond);
		property_text.setTypeface(fond);
		bhk.setTypeface(fond);
		price_rang.setTypeface(fond);
		build.setTypeface(fond);
		pricepsf.setTypeface(fond);
		amenities.setTypeface(fond);
		// Relevance.setTypeface(fond);
		Newest.setTypeface(fond);
		price.setTypeface(fond);
		pricePSF.setTypeface(fond);
		Rating.setTypeface(fond);
		Possession.setTypeface(fond);
		Infra.setTypeface(fond);
		Needs.setTypeface(fond);
		Lifestyle.setTypeface(fond);
		Returns.setTypeface(fond);
		btn1y.setTypeface(fond);
		btn2y.setTypeface(fond);
		btn3y.setTypeface(fond);
		btn4y.setTypeface(fond);
		btn5y.setTypeface(fond);
		btn6y.setTypeface(fond);
		// btnlaunch.setTypeface(fond);
		// btnUnder.setTypeface(fond);
		// btnReady.setTypeface(fond);
		checklaunch.setTypeface(fond);
		checkUnder.setTypeface(fond);
		checkReady.setTypeface(fond);

		btnResidential.setTypeface(fond);
		btnCommercial.setTypeface(fond);
		flating.setTypeface(fond);
		builder.setTypeface(fond);
		plots.setTypeface(fond);
		housevilla.setTypeface(fond);
		shops.setTypeface(fond);
		serviceAppartment.setTypeface(fond);

		// ________________________________ Status

		checklaunch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!arg0.isSelected()) {
					arg0.setSelected(true);
					// checkUnder.setSelected(false);
					// checkReady.setSelected(false);
					status_value += "New Launch,";

				} else {
					arg0.setSelected(false);
					status_value = status_value.replace("New Launch,", "");
					System.out.println(" status "+status_value);
				}

			}
		});

		checkUnder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!arg0.isSelected()) {
					//arg0.setSelected(false);
					//checklaunch.setSelected(false);
					checkReady.setSelected(true);

					status_value += "Under Construction,";

				} else {
					//arg0.setSelected(false);
					status_value = status_value.replace("Under Construction,", "");
					System.out.println(" status "+status_value);
				}

			}
		});

		final LinearLayout linear3 = (LinearLayout)findViewById(R.id.linear3);
		 linear3.setVisibility(View.VISIBLE);

		checkReady.setOnClickListener(new OnClickListener() {

			boolean ReadyToMoveIn = false;

			@Override
			public void onClick(View arg0) {
				if (!arg0.isSelected()) {
					arg0.setSelected(true);
					// checkUnder.setSelected(false);
					// checklaunch.setSelected(false);
					linear3.setVisibility((linear3.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);

					status_value += "Ready To Move,";


				} else {
					arg0.setSelected(false);
					status_value = status_value.replace("Ready To Move,", "");


				}


//			 switch (arg0.getId()) {
//				 case R.id.btn_readytomove:
//				 if (checkReady.getVisibility() == View.VISIBLE) {
//				 LinearLayout linear3 = (LinearLayout)findViewById(R.id.linear3);
//				 linear3.setVisibility(View.GONE);
//				 } else {
//					 checkReady.setVisibility(View.GONE);
//					 LinearLayout linear3 = (LinearLayout)findViewById(R.id.linear3);
//					 linear3.setVisibility(View.VISIBLE);
//				 }
//				 break;
//				 }

			}

		});

		//-----------------------------------------------------------------------
//		final TextView button = (TextView) findViewById(R.id.specification_view);
//		button.setTypeface(font);
//
//		button.setOnClickListener(new OnClickListener() {
//
//			boolean unitSpecificationOpen = false;
//
//			public void onClick(View v) {
//				text_view.setVisibility((text_view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
//
//				int imgResource = -1;
//				if (unitSpecificationOpen == true) {
//					imgResource = R.drawable.circle_plus;
//					unitSpecificationOpen = false;
//				} else {
//					imgResource = R.drawable.intrested;
//					unitSpecificationOpen = true;
//				}
//
//           button.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0);
//
//			}
//		});

		//------------------------------------------------------------------


		// btnlaunch.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		// btnUnder.setSelected(false);
		// btnReady.setSelected(false);
		//
		// status_value += "New Launch,";
		//
		//
		// }
		// });

		// btnUnder.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		// btnlaunch.setSelected(false);
		// btnReady.setSelected(false);
		// status_value += "Under Construction,";
		// }
		// });

		// btnReady.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		// //-------------------case hide the section
		//
		//// switch (v.getId()) {
		//// case R.id.btn_readytomove:
		//// if (btnReady.getVisibility() == View.VISIBLE) {
		//// LinearLayout linear3 = (LinearLayout)findViewById(R.id.linear3);
		//// linear3.setVisibility(View.GONE);
		//// } else {
		//// btnReady.setVisibility(View.VISIBLE);
		//// }
		//// break;
		//// }
		//
		//
		// btnlaunch.setSelected(false);
		// btnUnder.setSelected(false);
		// status_value += "Ready To Move,";
		// }
		// });

		// __________________________________________Sort By filter

		// Relevance.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		// Newest.setSelected(false);
		// Rating.setSelected(false);
		// Infra.setSelected(false);
		// Needs.setSelected(false);
		// Lifestyle.setSelected(false);
		// Returns.setSelected(false);
		// btn_unit_price.setSelected(false);
		// pricePSF.setSelected(false);
		// Possession.setSelected(false);
		//
		// sort_value = "Relevance";
		// }
		// });

		Newest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Rating.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Newest";
			}
		});

		price.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupLowHigh(price, PRICE, "btn_unit_price");
				Newest.setSelected(false);
				// Relevance.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				Rating.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				sort_value = "btn_unit_price";
			}
		});
		pricePSF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupLowHigh(pricePSF, PRICEPSF, "pricePSF");

				// Relevance.setSelected(false);
				Newest.setSelected(false);
				price.setSelected(false);
				Rating.setSelected(false);
				Possession.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				sort_value = "pricePSF";
			}
		});

		Rating.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Rating";
			}
		});

		Possession.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupLowHigh(Possession, POSSESSION, "Possession");
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Rating.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				sort_value = "Possession";
			}
		});

		Infra.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				Rating.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Infra";
			}
		});
		Needs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				Rating.setSelected(false);
				Infra.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Needs";
			}
		});
		Lifestyle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				Rating.setSelected(false);
				Needs.setSelected(false);
				Needs.setSelected(false);
				Returns.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Lifestyle";
			}
		});

		Returns.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				// Relevance.setSelected(false);
				Newest.setSelected(false);
				Rating.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				sort_value = "Returns";
			}
		});

		// =========================Set Offers CheckBox

		final CheckBox off_check = (CheckBox) findViewById(R.id.offers);
		off_check.setTypeface(fond);
		off_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				offer_value = "Discount";

			}
		});

		// ________________ Select Property Type

		btnResidential.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final LinearLayout res = (LinearLayout) findViewById(R.id.residential_container);
				res.setVisibility(View.VISIBLE);
				final LinearLayout com = (LinearLayout) findViewById(R.id.commercial_container);
				com.setVisibility(View.INVISIBLE);
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);
				bhk.setVisibility(View.VISIBLE);
				res.setVisibility(View.VISIBLE);
				com.setVisibility(View.INVISIBLE);

				if (!v.isSelected()) {
					v.setSelected(true);
				}

				// switch (v.getId()) {
				// case R.id.btn_residential:
				// if (bhk.getVisibility() == View.INVISIBLE);
				//
				// btnResidential.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				//
				// }
				// });
				//
				// case R.id.btn_commercial:
				// if (com.getVisibility() == View.VISIBLE) {
				// } else {
				// res.setVisibility(View.VISIBLE);
				// }
				//
				// if (!v.isSelected()) {
				// v.setSelected(true);
				// }
				//
				// break;
				// }

				btnCommercial.setSelected(false);
			}
		});

		btnCommercial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout res = (LinearLayout) findViewById(R.id.residential_container);
				res.setVisibility(View.INVISIBLE);
				LinearLayout com = (LinearLayout) findViewById(R.id.commercial_container);
				com.setVisibility(View.VISIBLE);
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);

				switch (v.getId()) {
				case R.id.btn_residential:
					if (bhk.getVisibility() == View.VISIBLE) {

					} else {
						bhk.setVisibility(View.VISIBLE);
						res.setVisibility(View.VISIBLE);
					}

					if (!v.isSelected()) {
						v.setSelected(true);
					}

					break;

				case R.id.btn_commercial:
					if (com.getVisibility() == View.VISIBLE) {
						bhk.setVisibility(View.GONE);
					} else {
						com.setVisibility(View.VISIBLE);
					}
					break;
				}

				if (!v.isSelected()) {
					v.setSelected(true);
				}

				btnResidential.setSelected(false);

			}

		});

		flating.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);

				if (!v.isSelected()) {
					v.setSelected(true);
				}
				if (plots.isChecked()) {
					bhk.setVisibility(View.VISIBLE);
				}
				residential_value += "flats,";

				// bhk_value += "2bhk,";

			}
		});

		builder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);

				if (!v.isSelected()) {
					v.setSelected(true);
				}
				if (plots.isChecked()) {
					bhk.setVisibility(View.VISIBLE);
				}

				residential_value += "builderfloors,";

			}
		});

		plots.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);
				if (!v.isSelected()) {
					v.setSelected(true);
					bhk.setVisibility(View.GONE);
				} else {
					v.setSelected(false);
					bhk.setVisibility(View.VISIBLE);
				}

				residential_value += "plots,";
			}
		});

		housevilla.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final RelativeLayout bhk = (RelativeLayout) findViewById(R.id.rl_bhk_container);
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				if (plots.isChecked()) {
					bhk.setVisibility(View.VISIBLE);
				}

				residential_value += "housevilla";
			}
		});

		shops.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!v.isSelected()) {
					v.setSelected(true);
					commercial_value += "shops,";
				}
			}
		});

		office.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!v.isSelected()) {
					v.setSelected(true);
					commercial_value += "office,";
				}
			}
		});

		serviceAppartment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!v.isSelected()) {
					v.setSelected(true);
					commercial_value += "serviceapartment,";
				}

			}
		});

		// flating.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		// }
		// });
		//
		// builder.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (!v.isSelected()) {
		// v.setSelected(true);
		// }
		//
		// }
		// });

		// =========================================

		// btn1y.setOnClickListener(bhkClick);
		// btn2y.setOnClickListener(bhkClick);
		// btn3y.setOnClickListener(bhkClick);
		// btn4y.setOnClickListener(bhkClick);
		// btn5y.setOnClickListener(bhkClick);
		// btn6y.setOnClickListener(bhkClick);

		// btnlaunch.setOnClickListener(statusClick);
		// btnUnder.setOnClickListener(statusClick);
		// btnReady.setOnClickListener(statusClick);

		// ===================== Possession in year select only one

		final Button Psix = (Button) findViewById(R.id.btn_1y);
		final Button Pone = (Button) findViewById(R.id.btn_2y);
		final Button Ptwo = (Button) findViewById(R.id.btn_3y);
		final Button Pthree = (Button) findViewById(R.id.btn_4y);
		final Button Pfour = (Button) findViewById(R.id.btn_5y);
		final Button Pfive = (Button) findViewById(R.id.btn_6y);

		Psix.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Pone.setSelected(false);
				Ptwo.setSelected(false);
				Pthree.setSelected(false);
				Pfour.setSelected(false);
				Pfive.setSelected(false);
				status_possession = "6m";

			}
		});

		Pone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Psix.setSelected(false);
				Ptwo.setSelected(false);
				Pthree.setSelected(false);
				Pfour.setSelected(false);
				Pfive.setSelected(false);
				status_possession = "1y";

			}
		});

		Ptwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Psix.setSelected(false);
				Pone.setSelected(false);
				Pthree.setSelected(false);
				Pfour.setSelected(false);
				Pfive.setSelected(false);

				status_possession = "2y";
			}
		});

		Pthree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Psix.setSelected(false);
				Pone.setSelected(false);
				Ptwo.setSelected(false);
				Pfour.setSelected(false);
				Pfive.setSelected(false);
				status_possession = "3y";

			}
		});

		Pfour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Psix.setSelected(false);
				Pone.setSelected(false);
				Ptwo.setSelected(false);
				Pthree.setSelected(false);
				Pfive.setSelected(false);
				status_possession = "4y";

			}
		});

		Pfive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!v.isSelected()) {
					v.setSelected(true);
				}
				Psix.setSelected(false);
				Pone.setSelected(false);
				Ptwo.setSelected(false);
				Pthree.setSelected(false);
				Pfour.setSelected(false);
				status_possession = "5y";

			}
		});

		// =========================================================================================================================

		final CheckBox power = (CheckBox) findViewById(R.id.chk_power_backup);
		power.setTypeface(fond);
		power.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				amenities_value.add("Power_Backup");
			}
		});

		final CheckBox wifi = (CheckBox) findViewById(R.id.chk_wifi);
		wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				amenities_value.add("Central_Wi-fi");
			}
		});

		final CheckBox gym = (CheckBox) findViewById(R.id.chk_gym);
		gym.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Gymnasium");
			}
		});

		final CheckBox gas = (CheckBox) findViewById(R.id.chk_gas);
		gas.setTypeface(fond);
		gas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Piped_Gas");
			}
		});
		final CheckBox lift = (CheckBox) findViewById(R.id.chk_lift);
		lift.setTypeface(fond);
		lift.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Lift");
			}
		});
		final CheckBox parking = (CheckBox) findViewById(R.id.chk_parking);
		parking.setTypeface(fond);
		parking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Podium_Parking");
			}
		});

		final CheckBox security = (CheckBox) findViewById(R.id.chk_security);
		security.setTypeface(fond);
		security.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("24*7_Security");
			}
		});

		final CheckBox ac = (CheckBox) findViewById(R.id.chk_ac);
		ac.setTypeface(fond);
		ac.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("AC_in_Lobby");
			}
		});
		final CheckBox swimming = (CheckBox) findViewById(R.id.chk_swimming);
		swimming.setTypeface(fond);
		swimming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Swimming");
			}
		});

		final CheckBox home = (CheckBox) findViewById(R.id.chk_home_automation);
		home.setTypeface(fond);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				amenities_value.add("Home_Automation");
			}
		});
		// ============================================================================================================================
		final CheckBox onebhk = (CheckBox) findViewById(R.id.chk_one_bhk);
		onebhk.setTypeface(fond);
		onebhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "1bhk,";
			}
		});

		final CheckBox twobhk = (CheckBox) findViewById(R.id.chk_two_bhk);
		twobhk.setTypeface(fond);
		twobhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "2bhk,";
			}
		});
		final CheckBox threebhk = (CheckBox) findViewById(R.id.chk_three_bhk);
		threebhk.setTypeface(fond);
		threebhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "3bhk,";
			}
		});
		final CheckBox fourbhk = (CheckBox) findViewById(R.id.chk_four_bhk);
		fourbhk.setTypeface(fond);
		fourbhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "4bhk,";
			}
		});
		final CheckBox fivebhk = (CheckBox) findViewById(R.id.chk_five_bhk);
		fivebhk.setTypeface(fond);
		fivebhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "5bhk,";
			}
		});
		final CheckBox sixbhk = (CheckBox) findViewById(R.id.chk_five_pluse_bhk);
		sixbhk.setTypeface(fond);
		sixbhk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bhk_value += "5bhk,";
			}
		});
		// ==========================================================================================================

		// =================== Reset All button

		Button reset = (Button) findViewById(R.id.tv_reset_filter);
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				off_check.setChecked(false);
				flating.setChecked(false);
				builder.setChecked(false);
				plots.setChecked(false);
				housevilla.setChecked(false);
				shops.setChecked(false);
				office.setChecked(false);
				serviceAppartment.setChecked(false);
				onebhk.setChecked(false);
				twobhk.setChecked(false);
				threebhk.setChecked(false);
				fourbhk.setChecked(false);
				fivebhk.setChecked(false);
				sixbhk.setChecked(false);
				gas.setChecked(false);
				lift.setChecked(false);
				parking.setChecked(false);
				gym.setChecked(false);
				security.setChecked(false);
				wifi.setChecked(false);
				ac.setChecked(false);
				swimming.setChecked(false);
				home.setChecked(false);
				power.setChecked(false);
				// btnlaunch.setSelected(false);
				// btnUnder.setSelected(false);
				// btnReady.setSelected(false);
				checklaunch.setSelected(false);
				checkUnder.setSelected(false);
				checkReady.setSelected(false);

				// Relevance.setSelected(false);
				Newest.setSelected(false);
				price.setSelected(false);
				pricePSF.setSelected(false);
				Rating.setSelected(false);
				Possession.setSelected(false);
				Infra.setSelected(false);
				Needs.setSelected(false);
				Lifestyle.setSelected(false);
				Returns.setSelected(false);

			}
		});



		// =============================================================

		// back Button method

		ImageButton img = (ImageButton) findViewById(R.id.back_button);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProjectFilter.this, ProjectMapActivity.class);
				finish();
			}
		});

		// Cancel page method

		// TextView tvtext = (TextView) findViewById(R.id.textView1);
		// tvtext.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(ProjectFilter.this,
		// ProjectMapActivity.class);
		// finish();
		// }
		//
		// });

		// Aleart Dialog on edit click event

		/*TextView tv = (TextView) findViewById(R.id.text_search);
		tv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// abondend code since it is handling in projectlistactivity
//				Intent intent = new Intent(ProjectFilter.this, BuilderActivity.class);
//				startActivityForResult(intent,78393);
			}
		});*/

		// Filter Apply button use method Temporary

		final Button apply = (Button) findViewById(R.id.btn_apply_filter);
		apply.setTypeface(fond);

		apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Connectivity.isConnectedWithDoalog(ctx)) {
				HashMap<String, String> map = new HashMap<>();

				// Set Key and Value
				// Set Status
				if (!status_value.equals("")) {
					map.put("project_status", status_value);
				}

				if (sort_value.equals("Newest")) {
					map.put("newest", sort_value);
				}

				if (sort_value.equals("priceltoh")) {
					map.put("priceltoh", sort_value);
				}

				if (sort_value.equals("pricehtol")) {
					map.put("pricehtol", sort_value);
				}

				if (sort_value.equals("Rating")) {
					map.put("rating", sort_value);
				}

				if (sort_value.equals("Infra")) {
					map.put("infra", sort_value);
				}

				if (sort_value.equals("Needs")) {
					map.put("needs", sort_value);
				}

				if (sort_value.equals("Lifestyle")) {
					map.put("lifestyle", sort_value);
				}

				if (sort_value.equals("Possessionltoh")) {
					map.put("possessionltoh", sort_value);
				}
				if (sort_value.equals("Possessionhtol")) {
					map.put("possessionhtol", sort_value);
				}

				if (sort_value.equals("pricePSFhtol")) {
					map.put("psfhtol", sort_value);
				}
				if (sort_value.equals("pricePSFltoh")) {
					map.put("psfltoh", sort_value);
				}

				if (sort_value.equals("Returns")) {
					map.put("returnsval", sort_value);
				}

				if (!offer_value.equals("")) {
					map.put("discount", offer_value);
				}

				// --------------------------------------------------------------------------------------------
				if (!status_possession.equals("")) {
					map.put("posession", status_possession);
				}

				for (int i = 0; i < amenities_value.size(); i++) {
					map.put(amenities_value.get(i), "true");
				}

				if (bhk_value.length() > 2) {
					map.put("bhk", bhk_value);
				}

				if (price_range_min_value.length() > 0) {
					map.put("min-p", price_range_min_value);
				}
				if (price_range_max_value.length() > 0) {
					map.put("max-p", price_range_max_value);
				}

				if (price_psf_min_value.length() > 0) {
					map.put("min_area_range", price_psf_min_value);
				}
				if (price_psf_max_value.length() > 0) {
					map.put("max_area_range", price_psf_max_value);
				}
				// ___________________________________________________________________________________________-
				// set built up area min and max
				if (built_area_min_value.length() > 0) {
					map.put("min_area_range", built_area_min_value);
				}
				if (built_area_max_value.length() > 0) {
					map.put("max_area_range", built_area_max_value);
				}

				if (residential_value.length() > 2) {
					map.put("projectType", residential_value);
				}

				if (commercial_value.length() > 2) {
					map.put("projectType", commercial_value);
				}


				Intent intent = new Intent(ProjectFilter.this, ProjectMapActivity.class);
				intent.putExtra("map", map);
				setResult(RESULT_OK, intent);
				finish();

				}
				     }
		   });
		// ==========================Seek bar

		addPriceSeekBar(llPrice);
		addBuildUpSeekBar(llBuiltUpArea);
		addSeekBarPsf(llseekBarPSF);

	}

	// =========================================== Seek bar use method

	private void addPriceSeekBar(LinearLayout llParent) {
		seekBarPrice = new RangeSeekBar<Integer>(MIN_PRICE, MAX_PRICE, this);
		final TextView Price_left = (TextView) findViewById(R.id.Price_left);
		final TextView Price_right = (TextView) findViewById(R.id.Price_right);

		Price_left.setTypeface(fond);
		Price_right.setTypeface(fond);
		Price_left.setText(priceLabels[seekBarPrice.getAbsoluteMinValue()]);
		Price_right.setText(priceLabels[seekBarPrice.getAbsoluteMaxValue()]);
		seekBarPrice.setNotifyWhileDragging(true);
		seekBarPrice.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				Price_left.setText(priceLabels[minValue]);
				Price_right.setText(priceLabels[maxValue]);
				maxValue = maxValue - 1;
				System.out.println("btn_unit_price range " + minValue + "," + maxValue);
				if (minValue < 0) {
					minValue = 0;
				}
				if (maxValue < 0) {
					maxValue = 0;
				}
				System.out.println("btn_unit_price range value " + priceLabelsInt[minValue] + "," + priceLabelsInt[maxValue]);

				price_range_min_value = priceLabelsInt[minValue];
				price_range_max_value = priceLabelsInt[maxValue];
			}

		});
		llParent.addView(seekBarPrice);

		Price_left.setSelected(false);

	}

	private void addBuildUpSeekBar(LinearLayout llBuiltUpArea) {
		seekBarBuildUp = new RangeSeekBar<Integer>(MIN_AREA, MAX_AREA, this);
		final TextView tvBuildUpAreaLeft = (TextView) findViewById(R.id.built_area_left);
		final TextView tvBuildUpAreaRignt = (TextView) findViewById(R.id.built_area_right);
		tvBuildUpAreaLeft.setTypeface(fond);
		tvBuildUpAreaRignt.setTypeface(fond);

		tvBuildUpAreaLeft.setText("" + builtUpAreaLabels[seekBarBuildUp.getAbsoluteMinValue()] + " sqft");
		tvBuildUpAreaRignt.setText("" + builtUpAreaLabels[seekBarBuildUp.getAbsoluteMaxValue()] + " sqft");
		seekBarBuildUp.setNotifyWhileDragging(true);
		seekBarBuildUp.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				// tvBuildUpArea.setText("BuiltUp Area: "+(minValue)+" -
				// "+(maxValue)+" Sqft");
				tvBuildUpAreaLeft.setText("" + builtUpAreaLabels[minValue] + " sqft");
				tvBuildUpAreaRignt.setText(builtUpAreaLabels[maxValue] + " sqft");

				System.out.println("btn_unit_price psf range " + minValue + "," + maxValue);
				System.out.println(
						"btn_unit_price psf range value " + builtUpAreaLabels[minValue] + "," + builtUpAreaLabels[maxValue]);

				price_psf_min_value = builtUpAreaLabels[minValue];
				price_psf_max_value = builtUpAreaLabels[maxValue];
			}
		});
		llBuiltUpArea.addView(seekBarBuildUp);
	}

	private void addSeekBarPsf(LinearLayout llseekBarPSF) {
		seekBarPsf = new RangeSeekBar<Integer>(MIN_PSF, MAX_PSF, this);
		// LinearLayout llPsf = (LinearLayout)
		// rootView.findViewById(R.id.price_psf_text);
		final TextView tvpsf_left = (TextView) findViewById(R.id.price_psf_left);
		final TextView tvpsf_right = (TextView) findViewById(R.id.price_psf_right);
		tvpsf_left.setTypeface(fond);
		tvpsf_right.setTypeface(fond);

		tvpsf_left.setText(psfLabels[seekBarPsf.getAbsoluteMinValue()]);
		tvpsf_right.setText(psfLabels[seekBarPsf.getAbsoluteMaxValue()] + "");
		seekBarPsf.setNotifyWhileDragging(true);
		seekBarPsf.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
				// tvpsf.setText("Price: "+minValue+" - "+maxValue+" psf");
				tvpsf_left.setText(psfLabels[minValue]);
				tvpsf_right.setText(psfLabels[maxValue] + "");
			}
		});
		llseekBarPSF.addView(seekBarPsf);
	}

	// ===============================================

	private void showPopupLowHigh(final Button btn, final int type, final String buttonType) {
		if (pw != null) {
			pw.dismiss();
		}
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.high_low_layout, null);
		// getInstance a 100px width and 200px height popup window
		pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);

		pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		pw.setOutsideTouchable(true);

		// set actions to buttons we have in our popup
		final Button ltoh = (Button) layout.findViewById(R.id.btnLtoH);
		ltoh.setTypeface(fond);
		ltoh.setOnClickListener(new OnClickListener() {
			public void onClick(View vv) {

				selectedSortType = type;
				lToHSelected = true;
				HTolSelected = false;
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				btn.setSelected(true);
				pw.dismiss();

				sort_value = buttonType + "ltoh";

			}
		});

		final Button htol = (Button) layout.findViewById(R.id.btnHtoL);
		htol.setTypeface(fond);
		htol.setOnClickListener(new OnClickListener() {
			public void onClick(View vv) {
				selectedSortType = type;
				lToHSelected = false;
				HTolSelected = true;
				price.setSelected(false);
				pricePSF.setSelected(false);
				Possession.setSelected(false);
				btn.setSelected(true);
				pw.dismiss();

				sort_value = buttonType + "htol";
			}
		});

		switch (selectedSortType) {
		case PRICE:
			if (type == PRICE) {
				if (lToHSelected) {
					ltoh.setSelected(true);
					htol.setSelected(false);
				} else if (HTolSelected) {
					htol.setSelected(true);
					ltoh.setSelected(false);
				}
			} else {
				ltoh.setSelected(false);
				htol.setSelected(false);
			}
			break;
		case PRICEPSF:
			if (type == PRICEPSF) {
				if (lToHSelected) {
					ltoh.setSelected(true);
					htol.setSelected(false);
				} else if (HTolSelected) {
					htol.setSelected(true);
					ltoh.setSelected(false);
				}
			} else {
				ltoh.setSelected(false);
				htol.setSelected(false);
			}
			break;
		case POSSESSION:
			if (type == POSSESSION) {
				if (lToHSelected) {
					ltoh.setSelected(true);
					htol.setSelected(false);
				} else if (HTolSelected) {
					htol.setSelected(true);
					ltoh.setSelected(false);
				}
			} else {
				ltoh.setSelected(false);
				htol.setSelected(false);
			}
			break;

		default:
			break;
		}

		// finally show the popup in the center of the window or any position
		pw.setOutsideTouchable(true);
		pw.showAsDropDown(btn);
	}

	// ==========================================================================

	// OnClickListener bhkClick = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (v.isSelected()) {
	// v.setSelected(false);
	// } else {
	// v.setSelected(true);
	// }
	//
	// String bhk = "";
	// if (btn1y.isSelected()) {
	// bhk = ",1";
	// } else if (bhk.contains(",1")) {
	// bhk = bhk.replace(",1", "");
	// }
	// if (btn2y.isSelected()) {
	// bhk = bhk + ",2";
	// } else if (bhk.contains(",2")) {
	// bhk = bhk.replace(",2", "");
	// }
	// if (btn3y.isSelected()) {
	// bhk = bhk + ",3";
	// } else if (bhk.contains(",3")) {
	// bhk = bhk.replace(",3", "");
	// }
	// if (btn4y.isSelected()) {
	// bhk = bhk + ",4";
	// } else if (bhk.contains(",4")) {
	// bhk = bhk.replace(",4", "");
	// }
	// if (btn5y.isSelected()) {
	// bhk = bhk + ",5";
	// } else if (bhk.contains(",5")) {
	// bhk = bhk.replace(",5", "");
	// }
	//
	// if (btn6y.isSelected()) {
	// // bhk = "1,2";
	// } else if (bhk.contains(",1")) {
	// // bhk = bhk.replace(",1", "");
	// }
	//
	// }
	// };

	// OnClickListener statusClick = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// LinearLayout bhk = (LinearLayout) findViewById(R.id.linear3);
	//
	// switch (v.getId()) {
	// case R.id.btnNew_launch:
	// bhk.setVisibility(View.VISIBLE);
	// break;
	// case R.id.btn_under_construction:
	// bhk.setVisibility(View.VISIBLE);
	// break;
	// case R.id.btn_readytomove:
	// if (bhk.getVisibility() == View.VISIBLE) {
	// bhk.setVisibility(View.GONE);
	// } else {
	// bhk.setVisibility(View.VISIBLE);
	// }
	// break;
	//
	// default:
	// break;
	// }
	//
	// // bhk.setVisibility(View.GONE);
	//
	// if (v.isSelected()) {
	// v.setSelected(false);
	// } else {
	// v.setSelected(true);
	// }
	//
	// String status = "";
	// if (btn1y.isSelected()) {
	// status = ",1";
	// } else if (status.contains(",1")) {
	// status = status.replace(",1", "");
	// }
	// if (btn2y.isSelected()) {
	// status = status + ",2";
	// } else if (status.contains(",2")) {
	// status = status.replace(",2", "");
	// }
	// if (btn3y.isSelected()) {
	// status = status + ",3";
	// } else {
	//
	// }
	//
	// // if(!img.isSelected()){
	// // String ptype = (String) v.getContentDescription();
	// // LinearLayout llBed = (LinearLayout)
	// // rootView.findViewById(R.id.linear6);
	// // if(ptype.equalsIgnoreCase("PLOT") |
	// // ptype.equalsIgnoreCase("SHOPS") |
	// // ptype.equalsIgnoreCase("OFFICE")){
	// // llBed.setVisibility(View.GONE);
	// // }else{
	// // llBed.setVisibility(View.VISIBLE);
	// // }
	// // // searchValuesMap.put("p_type", ptype);
	// // v.setSelected(true);
	// // img.setSelected(true);
	// //
	// // app.saveIntoPrefs(BMHConstants.UNIT_TYPE, img.getId());
	// // }
	// }
	// };

	//--------------------------------

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);



	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}




}
