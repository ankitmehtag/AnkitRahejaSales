package com.sp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.sp.CustomAsyncTask.AsyncListner;
import com.VO.LocationsVO;
import com.VO.PropertyCaraouselListVO;
import com.VO.PropertyCaraouselVO;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.model.PropertyModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectMapActivity extends BaseFragmentActivity{

	private String[] colorArr = new String[] { "EF676B", "EF676B", "EF676B", "FAB97F", "FAB97F", "FEE581", "FEE581",
			"90C97A", "90C97A", "64BD7D", "64BD7D" };
	ArrayList<PropertyCaraouselVO> arrCaroselProp;
	// private int clickedId= 0;
	private ArrayList<LatLng> arrLatlong;
	private ArrayList<Marker> arrMarkers;
//	public ProjectCrouselAdapter adapter;
	public ViewPager pager;
	private int oldPos, newPos;
//	private GoogleMap map;
	private float zoomLevel = 12.0f;
	private float currentZoom = -1;
	private LocationsVO locationsVO;
	private Activity ctx = ProjectMapActivity.this;
	private int pos;
	private boolean isOneFound = false;
	private final int INFRA = 0, NEEDS = 1, LIFESTYLE = 2, PRICE = 3, RETURNS = 4;
	private LinearLayout llLineInfra, llLineNeeds, llLineLife, llLinePrice, llLineReturns, llLineShowAll, llLineNew,
			llLineUnder, llLineReady;
	private TextView tvInfra, tvNeeds, tvLife, tvPrice, tvReturns, tv1, tv2, tv3, tv4, tv5, tvShowAll, tvNew, tvUnder,
			tvReady;
	private ObjectAnimator translateAnimation, translateAnimation2, translateAnimation3, translateAnimation4,
			translateAnimation5;
	boolean isanimated = false;
	private int value = 100;
	private int duration = 800;
	private ArrayList<Polygon> arrSectorPolygons;
	private ImageView imgLegends;
	public HashMap<String, String> searchMap;
	private PropertyCaraouselListVO propVo;
	ArrayList<PropertyCaraouselVO> arrFiltered;
	private int filteredPropType = 0;
	private String titleBarTitle = "";
	private String location_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmhmap);

		// findViewById(R.id.llRootFooterFilers).setVisibility(View.GONE);

		// ImageView imgFilter = (ImageView) findViewById(R.id.imgFilter);
		// imgFilter.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(ctx, ProjectFilter.class);
		// startActivityForResult(i, BMHConstants.FILTER_PROJ);
		//
		// overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		// // startActivity(i);
		// }
		// });

		try {
			locationsVO = getIntent().getParcelableExtra("locationvo");


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

		try {
			titleBarTitle = getIntent().getStringExtra("locationName");
			System.out.println("projectmap89" +titleBarTitle );
		} catch (Exception e) {
			e.printStackTrace();
		}

		location_type = getIntent().getStringExtra("location_type");

		if (locationsVO == null) {

			System.out.println("hh location null");

		}

		else {

			System.out.println("hh location not null");

		}

		pos = getIntent().getIntExtra("pos", -1);


		// Add image loader
				View includeView = findViewById(R.id.inclide_view);
				ImageView imageView = (ImageView) includeView.findViewById(R.id.img);
				imageView.setBackgroundResource(R.drawable.progress_loader);
				AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
				anim.start();


		Toolbar toolbar = setToolBar();
		// toolbar.setLogo(R.drawable.logo);
		toolbar.setTitle("Select Project");

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		imgLegends = (ImageView) findViewById(R.id.imgLegends);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);

		LinearLayout llInfra = (LinearLayout) findViewById(R.id.llInfra);
		LinearLayout llNeeds = (LinearLayout) findViewById(R.id.llNeeds);
		LinearLayout llLifestyle = (LinearLayout) findViewById(R.id.llLifestyle);
		LinearLayout llPrice = (LinearLayout) findViewById(R.id.llPrice);
		LinearLayout llReturns = (LinearLayout) findViewById(R.id.llReturns);

		tvInfra = (TextView) findViewById(R.id.tvInfra);
		tvNeeds = (TextView) findViewById(R.id.tvNeeds);
		tvLife = (TextView) findViewById(R.id.tvLife);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvReturns = (TextView) findViewById(R.id.tvReturns);

		llLineInfra = (LinearLayout) findViewById(R.id.llUnderlineInfra);
		llLineNeeds = (LinearLayout) findViewById(R.id.llUnderlineNeeds);
		llLineLife = (LinearLayout) findViewById(R.id.llUnderlineLifestyle);
		llLinePrice = (LinearLayout) findViewById(R.id.llUnderlinePrice);
		llLineReturns = (LinearLayout) findViewById(R.id.llUnderlineReturns);

//		llInfra.setOnClickListener(filterListner);
//		llNeeds.setOnClickListener(filterListner);
//		llLifestyle.setOnClickListener(filterListner);
//		llPrice.setOnClickListener(filterListner);
//		llReturns.setOnClickListener(filterListner);

		LinearLayout llShowAll = (LinearLayout) findViewById(R.id.llShowAll);
		LinearLayout llNewlaunch = (LinearLayout) findViewById(R.id.llNewLaunch);
		LinearLayout llUnderCons = (LinearLayout) findViewById(R.id.llUnder);
		LinearLayout llReadyToMove = (LinearLayout) findViewById(R.id.llReady);

		tvShowAll = (TextView) findViewById(R.id.tvShowAll);
		tvNew = (TextView) findViewById(R.id.tvNewLaunch);
		tvUnder = (TextView) findViewById(R.id.tvUnder);
		tvReady = (TextView) findViewById(R.id.tvReady);

		llLineShowAll = (LinearLayout) findViewById(R.id.llUnderlineShowAll);
		llLineNew = (LinearLayout) findViewById(R.id.llUnderlineNewLaunch);
		llLineUnder = (LinearLayout) findViewById(R.id.llUnderlineUnder);
		llLineReady = (LinearLayout) findViewById(R.id.llUnderlineReady);

//		llShowAll.setOnClickListener(StatusPinsClickListner);
//		llNewlaunch.setOnClickListener(StatusPinsClickListner);
//		llUnderCons.setOnClickListener(StatusPinsClickListner);
//		llReadyToMove.setOnClickListener(StatusPinsClickListner);
//
//		initMap(null); // calling api from this method, so that first map is set
						// and
						// then data

		searchMap = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
		startSearchTask(null);

		imgLegends.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				value = tv1.getHeight();
				if (!isanimated) {
					isanimated = true;
					translateAnimation = ObjectAnimator.ofFloat(tv1, View.TRANSLATION_Y, value);
					translateAnimation.setInterpolator(new OvershootInterpolator());
					translateAnimation.setDuration(duration);
					translateAnimation.start();

					translateAnimation2 = ObjectAnimator.ofFloat(tv2, View.TRANSLATION_Y, value * 2);
					translateAnimation2.setDuration(duration);
					translateAnimation2.setInterpolator(new OvershootInterpolator());
					translateAnimation2.start();

					translateAnimation3 = ObjectAnimator.ofFloat(tv3, View.TRANSLATION_Y, value * 3);
					translateAnimation3.setDuration(duration);
					translateAnimation3.setInterpolator(new OvershootInterpolator());
					translateAnimation3.start();

					translateAnimation4 = ObjectAnimator.ofFloat(tv4, View.TRANSLATION_Y, value * 4);
					translateAnimation4.setDuration(duration);
					translateAnimation4.setInterpolator(new OvershootInterpolator());
					translateAnimation4.start();

					translateAnimation5 = ObjectAnimator.ofFloat(tv5, View.TRANSLATION_Y, value * 5);
					translateAnimation5.setDuration(duration);
					translateAnimation5.setInterpolator(new OvershootInterpolator());
					translateAnimation5.start();
				} else {
					isanimated = false;

					translateAnimation.reverse();
					translateAnimation2.reverse();
					translateAnimation3.reverse();
					translateAnimation4.reverse();
					translateAnimation5.reverse();

				}
			}
		});

		llInfra.performClick();



	}

//	private void setLocationAndSectors(LocationsVO locationsVo) {
//		if (locationsVo.isSuccess() && locationsVo.getArrLocation().size() > 0) {
//			ArrayList<LocationVO> arrLocations = locationsVo.getArrLocation();
//			// setPagerView(arrLocations);
//			for (int i = 0; i < arrLocations.size(); i++) {
//				LocationVO itemLocation = arrLocations.get(i);
//				if (itemLocation.getArrBoundries() != null) {
//					// DrawLocationPolygon(itemLocation.getArrBoundries(), i);
//					for (int j = 0; j < itemLocation.getArrSectors().size(); j++) {
//						int infra = itemLocation.getArrSectors().get(j).getInfra();
//						DrawSectorsPolygon(itemLocation.getArrSectors().get(j).getArrSectorCords(), infra);
//					}
//				}
//			}
//		}
//	}

//	private void DrawLocationPolygon(ArrayList<BoundariesVO> arr, int pos) {
//		// if(arrLocationPolygons == null)
//		// arrLocationPolygons = new ArrayList<Polygon>();
//		PolygonOptions polyOpt = new PolygonOptions();
//		for (int i = 0; i < arr.size(); i++) {
//			polyOpt.strokeColor(Color.parseColor("#000000"));
//			polyOpt.strokeWidth(2);
//			polyOpt.fillColor(Color.parseColor("#66000000"));
//			Double d1 = Double.parseDouble(arr.get(i).getLat());
//			Double d2 = Double.parseDouble(arr.get(i).getLon());
//			polyOpt.add(new LatLng(d1, d2));
//
//		}
//		// arrLocationPolygons.add(map.addPolygon(polyOpt));
//		try {
//			map.addPolygon(polyOpt);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//
//		if (arr.size() > 0)
//			map.animateCamera(CameraUpdateFactory.newLatLngZoom(
//					new LatLng(Double.parseDouble(arr.get(0).getLat()), Double.parseDouble(arr.get(0).getLon())),
//					12.0f));
//
//	}

//	private void DrawSectorsPolygon(ArrayList<BoundariesVO> arr, int pos) {
//		if (arrSectorPolygons == null)
//			arrSectorPolygons = new ArrayList<Polygon>();
//
//		PolygonOptions polyOpt = new PolygonOptions();
//		polyOpt.strokeWidth(2);
//
//		for (int i = 0; i < arr.size(); i++) {
//			polyOpt.strokeColor(Color.parseColor("#" + colorArr[pos]));
//			polyOpt.fillColor(Color.parseColor("#cc" + colorArr[pos]));
//			Double d1 = Double.parseDouble(arr.get(i).getLat());
//			Double d2 = Double.parseDouble(arr.get(i).getLon());
//			polyOpt.add(new LatLng(d1, d2));
//
//		}
//		try {
//
//			arrSectorPolygons.add(map.addPolygon(polyOpt));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//
//		// map.addPolygon(polyOpt);
//
//		// if(arr.size()>0)
//		// map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
//		// LatLng(Double.parseDouble(arr.get(0).getLat()),
//		// Double.parseDouble(arr.get(0).getLon())), 14.0f));
//
//	}

//	OnClickListener filterListner = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			Resources r = getResources();
//			switch (v.getId()) {
//			case R.id.llInfra:
//				llLineInfra.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				tvInfra.setTextColor(r.getColor(R.color.selected_tv));
//				tvLife.setTextColor(r.getColor(R.color.unselected_tv));
//				tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
//				tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
//				tvReturns.setTextColor(r.getColor(R.color.unselected_tv));
//
//				tv1.setText("0 - 2");
//				tv2.setText("2 - 4");
//				tv3.setText("4 - 6");
//				tv4.setText("6 - 8");
//				tv5.setText("8 - 10");
//				changePolygonColor(INFRA);
//
//				break;
//			case R.id.llNeeds:
//				llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineNeeds.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
//				tvLife.setTextColor(r.getColor(R.color.unselected_tv));
//				tvNeeds.setTextColor(r.getColor(R.color.selected_tv));
//				tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
//				tvReturns.setTextColor(r.getColor(R.color.unselected_tv));
//
//				tv1.setText("0 - 2");
//				tv2.setText("2 - 4");
//				tv3.setText("4 - 6");
//				tv4.setText("6 - 8");
//				tv5.setText("8 - 10");
//
//				changePolygonColor(NEEDS);
//				break;
//			case R.id.llLifestyle:
//				llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineLife.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
//				tvLife.setTextColor(r.getColor(R.color.selected_tv));
//				tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
//				tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
//				tvReturns.setTextColor(r.getColor(R.color.unselected_tv));
//
//				tv1.setText("0 - 2");
//				tv2.setText("2 - 4");
//				tv3.setText("4 - 6");
//				tv4.setText("6 - 8");
//				tv5.setText("8 - 10");
//
//				changePolygonColor(LIFESTYLE);
//				break;
//			case R.id.llPrice:
//
//				llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLinePrice.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLineReturns.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
//				tvLife.setTextColor(r.getColor(R.color.unselected_tv));
//				tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
//				tvPrice.setTextColor(r.getColor(R.color.selected_tv));
//				tvReturns.setTextColor(r.getColor(R.color.unselected_tv));
//
//
//
//				ArrayList<LegendsVO> aar = locationsVO.getArrPriceConditions();
//				if (aar != null) {
//					tv1.setText(aar.get(0).getMin() + " - " + aar.get(0).getMax() + " Psf");
//					tv2.setText(aar.get(1).getMin() + " - " + aar.get(1).getMax() + " Psf");
//					tv3.setText(aar.get(2).getMin() + " - " + aar.get(2).getMax() + " Psf");
//					tv4.setText(aar.get(3).getMin() + " - " + aar.get(3).getMax() + " Psf");
//					tv5.setText(aar.get(4).getMin() + " - " + aar.get(4).getMax() + " Psf");
//				}
//
//				changePolygonColor(PRICE);
//
//				break;
//			case R.id.llReturns:
//
//				llLineInfra.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineLife.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineNeeds.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLinePrice.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineReturns.setBackgroundColor(r.getColor(R.color.selected_underline));
//
//				tvInfra.setTextColor(r.getColor(R.color.unselected_tv));
//				tvLife.setTextColor(r.getColor(R.color.unselected_tv));
//				tvNeeds.setTextColor(r.getColor(R.color.unselected_tv));
//				tvPrice.setTextColor(r.getColor(R.color.unselected_tv));
//				tvReturns.setTextColor(r.getColor(R.color.selected_tv));
//
//				ArrayList<LegendsVO> aarR = locationsVO.getArrReturnsConditions();
//				if (aarR != null) {
//					tv1.setText(aarR.get(0).getMin() + " - " + aarR.get(0).getMax());
//					tv2.setText(aarR.get(1).getMin() + " - " + aarR.get(1).getMax());
//					tv3.setText(aarR.get(2).getMin() + " - " + aarR.get(2).getMax());
//					tv4.setText(aarR.get(3).getMin() + " - " + aarR.get(3).getMax());
//					tv5.setText(aarR.get(4).getMin() + " - " + aarR.get(4).getMax());
//				}
//
//				changePolygonColor(RETURNS);
//
//				break;
//			default:
//				break;
//			}
//		}
//
//	};

//	private void changePolygonColor(int filter) {
//		if (locationsVO != null) {
//			if (arrSectorPolygons != null) {
//				for (int i = 0; i < arrSectorPolygons.size(); i++) {
//					arrSectorPolygons.get(i).remove();
//				}
//			}
//			ArrayList<LocationVO> arrLocations = locationsVO.getArrLocation();
//			for (int i = 0; i < arrLocations.size(); i++) {
//				LocationVO itemLocation = arrLocations.get(i);
//				if (itemLocation.getArrBoundries() != null) {
//					// DrawLocationPolygon(itemLocation.getArrBoundries(), i);
//					for (int j = 0; j < itemLocation.getArrSectors().size(); j++) {
//						int value = 0;
//						switch (filter) {
//						case INFRA:
//							value = itemLocation.getArrSectors().get(j).getInfra();
//							break;
//						case NEEDS:
//							value = itemLocation.getArrSectors().get(j).getNeeds();
//							break;
//						case LIFESTYLE:
//							value = itemLocation.getArrSectors().get(j).getLifestyle();
//							break;
//						case PRICE:
//
//
//
//							value = itemLocation.getArrSectors().get(j).getPrice();
//							ArrayList<LegendsVO> arP = locationsVO.getArrPriceConditions();
//							for (int k = 0; k < arP.size(); k++) {
//								int min = arP.get(k).getMin();
//								int max = arP.get(k).getMax();
//								if (value >= min && value < max) {
//									value = k;
//									break;
//								}
//
//								if (k == arP.size() - 1 && value >= min && value <= max) {
//									value = k;
//								}
//							}
//							break;
//						case RETURNS:
//							value = itemLocation.getArrSectors().get(j).getReturns();
//							ArrayList<LegendsVO> arrR = locationsVO.getArrReturnsConditions();
//							for (int l = 0; l < arrR.size(); l++) {
//								int min = arrR.get(l).getMin();
//								int max = arrR.get(l).getMax();
//								if (value >= min && value < max) {
//									value = l;
//									break;
//								}
//
//								if (l == arrR.size() - 1 && value >= min && value <= max) {
//									value = l;
//								}
//							}
//							break;
//						default:
//							break;
//						}
//
//						DrawSectorsPolygon(itemLocation.getArrSectors().get(j).getArrSectorCords(), value);
//					}
//
//				}
//			}
//		}
//	}

//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//	}
//
//	private void setPagerView(ArrayList<PropertyCaraouselVO> props) {
//		pager = null;
//		adapter = null;
//		pager = (ViewPager) findViewById(R.id.myviewpager);
//		adapter = new ProjectCrouselAdapter(ProjectMapActivity.this, this.getSupportFragmentManager(), props);
//		pager.setAdapter(adapter);
//		pager.setOnPageChangeListener(new OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int arg0) {
//				onPageChnaged(arg0);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		});
//		pager.setCurrentItem(0);
//		pager.setOffscreenPageLimit(getResources().getInteger(R.integer.offScreenLimit));
//		pager.setPageMargin(getResources().getInteger(R.integer.pager_padding));
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.bmhmap, menu);
//		getMenuInflater().inflate(R.menu.project_filter, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//
//		switch (id) {
//		case R.id.action_list:
//			// Intent i = new Intent(ctx, ProjectsListActivity.class);
//			// i.putParcelableArrayListExtra("properties", propCaraousel);
//			// startActivity(i);
//
//			Intent i = new Intent(ctx, ProjectsListActivity.class);
//			i.putExtra("searchParams", searchMap);
//			if (filteredPropType == 0) {
//				i.putParcelableArrayListExtra("properties", propVo.getPropertiesArr());
//			} else {
//
//				i.putParcelableArrayListExtra("properties", arrFiltered);
//			}
//			startActivityForResult(i, BMHConstants.PICK_PROJECT_LIST);
//			return true;
//
//		case android.R.id.home:
//			finish();
//			break;
//
//		default:
//			Intent intent = new Intent(ctx, ProjectFilter.class);
//			// i.putParcelableArrayListExtra("locations",locationsVo.getArrLocation());
//			intent.putExtra("searchParams", searchMap);
//			startActivityForResult(intent, BMHConstants.PICK_LOCATION_LIST);
//			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//			break;
//
//		}
//
//		// if (id == R.id.action_list) {
//		// // Intent i = new Intent(ctx, ProjectsListActivity.class);
//		// // i.putParcelableArrayListExtra("properties", propCaraousel);
//		// // startActivity(i);
//		//
//		// Intent i = new Intent(ctx, ProjectsListActivity.class);
//		// i.putExtra("searchParams", searchMap);
//		// if (filteredPropType == 0) {
//		// i.putParcelableArrayListExtra("properties",
//		// propVo.getPropertiesArr());
//		// } else {
//		//
//		// i.putParcelableArrayListExtra("properties", arrFiltered);
//		// }
//		// startActivityForResult(i, BMHConstants.PICK_PROJECT_LIST);
//		// return true;
//		// }
//		return super.onOptionsItemSelected(item);
//	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == BMHConstants.PICK_PROJECT_LIST) {
//			if (resultCode == RESULT_OK) {
//				String id = data.getStringExtra("result");
//
//				Intent i = new Intent(ctx, ProjectDetailActivity.class);
//				i.putExtra("searchParams", searchMap);
//				i.putExtra("propertyId", id);
//				System.out.println("hh sending id = " + id);
//				startActivity(i);
//			} else if (resultCode == RESULT_CANCELED) {
//
//			}
//		} else if (requestCode == BMHConstants.FILTER_PROJ && resultCode == RESULT_OK) {
//			searchMap = (HashMap<String, String>) data.getSerializableExtra("result");
//			if (searchMap != null) {
//				searchMap.put("city", app.getFromPrefs(BMHConstants.CITYID));
//				startSearchTask(null);
//			}
//		} else if (requestCode == BMHConstants.PICK_LOCATION_LIST && resultCode == RESULT_OK) {
//			HashMap<String, String> hashMap = (HashMap<String, String>) data.getSerializableExtra("map");
//			if (searchMap != null) {
//				searchMap.put("city", app.getFromPrefs(BMHConstants.CITYID));
//				startSearchTask(hashMap);
//			}
//		}
//
//	}

//	private void initMap(HashMap<String, String> filter_type) {
//		if (map == null) {
//			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
//			if (fm != null) {
//				map = fm.getMap();
//				setMapAndZoom(filter_type);
//			}
//		} else {
//			setMapAndZoom(filter_type);
//		}
//	}
//
//	private void setMapAndZoom(HashMap<String, String> filter_type) {
//		if (map != null) {
//			map.clear();
//			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//			map.getUiSettings().setZoomControlsEnabled(false);
//			map.setPadding(0, 0, 0, getResources().getInteger(R.integer.map_padding));
//			// getProjectsByLocationId();
//			setCustomZomm();
//
//			map.setOnMarkerClickListener(new OnMarkerClickListener() {
//				public boolean onMarkerClick(Marker arg0) {
//					return true;
//				}
//			});
//
//			searchMap = (HashMap<String, String>) getIntent().getSerializableExtra("searchParams");
//			if (searchMap != null) {
//				startSearchTask(filter_type);
//			}
//
//			if (locationsVO != null)
//				DrawLocationPolygon(locationsVO.getArrLocation().get(pos).getArrBoundries(), 0);
//		}
//	}

//	private void setCustomZomm() {
//		ImageView plus = (ImageView) findViewById(R.id.imgPlus);
//		ImageView minus = (ImageView) findViewById(R.id.imgMinus);
//		plus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (currentZoom < map.getMaxZoomLevel()) {
//					currentZoom = currentZoom + 1;
//					map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
//				}
//			}
//		});
//		minus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (currentZoom > map.getMinZoomLevel()) {
//					currentZoom = currentZoom - 1;
//					map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));
//				}
//			}
//		});
//
//		map.setOnCameraChangeListener(new OnCameraChangeListener() {
//			@Override
//			public void onCameraChange(CameraPosition pos) {
//				if (pos.zoom != currentZoom) {
//					currentZoom = pos.zoom;
//				}
//			}
//
//		});
//	}

//	@Override
//	public void onclickCrouselItem(View v) {
//		int pos = (Integer) v.getTag();
//		Intent i = new Intent(ctx, ProjectDetailActivity.class);
//		i.putExtra("propertyId", arrCaroselProp.get(pos).getId());
//		i.putExtra("searchParams", searchMap);
//		startActivity(i);
//		// ctx.finish();
//	}
//
//	@Override
//	public void onPageChnaged(int position) {
//		if (position < propVo.getPropertiesArr().size()) {
//			oldPos = newPos;
//			newPos = position;
//			arrMarkers.get(oldPos).remove();
//			// String titleOld;
//			String titleNew;
//			String statusOld;
//			String statusNew;
//			if (filteredPropType == 0) {
//				statusOld = propVo.getPropertiesArr().get(oldPos).getStatus();
//				titleNew = propVo.getPropertiesArr().get(newPos).getDisplay_name();
//				statusNew = propVo.getPropertiesArr().get(newPos).getStatus();
//			} else {
//				statusOld = arrFiltered.get(oldPos).getStatus();
//				statusNew = arrFiltered.get(newPos).getStatus();
//				titleNew = arrFiltered.get(newPos).getDisplay_name();
//			}
//			Marker markerolder = map
//					.addMarker(new MarkerOptions().position(arrLatlong.get(oldPos)).icon(getPinViaStatus(statusOld)));
//			markerolder.showInfoWindow();
//			arrMarkers.set(oldPos, markerolder);
//
//			arrMarkers.get(newPos).remove();
//			Marker mark = map.addMarker(new MarkerOptions().title(titleNew).position(arrLatlong.get(newPos))
//					.icon(getPinViaStatus(statusNew)));
//			mark.showInfoWindow();
//			arrMarkers.set(newPos, mark);
//
//			map.animateCamera(CameraUpdateFactory.newLatLngZoom(arrLatlong.get(newPos), zoomLevel));
//		}
//	}

	/*
	 * Method called to set the pins of properties on map parameters required
	 * are array list of Link@PropertyCaraouselVO
	 */
//	private void setPropertiesOnMap(ArrayList<PropertyCaraouselVO> props) {
//
//		setPagerView(props);
//		if (arrMarkers != null) {
//			for (int i = 0; i < arrMarkers.size(); i++) {
//				arrMarkers.get(i).remove();
//			}
//			arrMarkers.clear();
//		}
//		arrLatlong = new ArrayList<LatLng>();
//		arrMarkers = new ArrayList<Marker>();
//		for (int i = 0; i < props.size(); i++) {
//			double lat = Double.parseDouble(props.get(i).getLatitude());
//			double lon = Double.parseDouble(props.get(i).getLongitude());
//			arrLatlong.add(new LatLng(lat, lon));
//			Marker marker = map.addMarker(
//					new MarkerOptions().position(arrLatlong.get(i)).icon(getPinViaStatus(props.get(i).getStatus())));
//			arrMarkers.add(marker);
//		}
//
//		oldPos = 0;
//		newPos = 0;
//		try{
//			map.moveCamera(CameraUpdateFactory.newLatLngZoom(arrLatlong.get(arrLatlong.size() - 1), zoomLevel));
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//		}
//		onPageChnaged(0);
//	}

//	private BitmapDescriptor getPinViaStatus(String status) {
//		BitmapDescriptor pin;
//		if (status.equalsIgnoreCase("UnderConstruction")) {
//			pin = BitmapDescriptorFactory.fromResource(R.drawable.pin_undercons);
//		} else if (status.equalsIgnoreCase("NewLaunch")) {
//			pin = BitmapDescriptorFactory.fromResource(R.drawable.pin_newlaunch);
//		} else {
//			pin = BitmapDescriptorFactory.fromResource(R.drawable.pin_readytomove);
//		}
//		return pin;
//	}

	@Override
	protected String setActionBarTitle() {
		return "";
	}

	private void startSearchTask(final HashMap<String, String> filter_type) {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

			@Override
			public void OnBackgroundTaskCompleted() {

				Intent i = new Intent(ctx, ProjectsListActivity.class);
				i.putExtra("searchParams", searchMap);
				i.putExtra("filter_type", filter_type);
				i.putExtra("toptitle", titleBarTitle);
				i.putExtra("location_type", location_type);


				try {
					if (filteredPropType == 0) {
						i.putParcelableArrayListExtra("properties", propVo.getPropertiesArr());
					} else {

						i.putParcelableArrayListExtra("properties", arrFiltered);
					}

				} catch (Exception e) {

				}


				startActivityForResult(i, BMHConstants.PICK_PROJECT_LIST);
				ProjectMapActivity.this.finish();

//				if (propVo == null) {
//					app.showSnackBarError(ctx, new SnackBarCallback() {
//						@Override
//						public void onActionButtonClick(Parcelable token) {
//							startSearchTask(filter_type);
//						}
//					});
//				} else {
//					if (propVo.isSuccess()) {
//						// put those Projects on map
//
//						arrCaroselProp = propVo.getPropertiesArr();
//						setPropertiesOnMap(arrCaroselProp);
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								imgLegends.performClick();
//							}
//						}, 1000);
//					} else {
//						setPagerView(new ArrayList<PropertyCaraouselVO>());
//						app.showSnackBar(ctx, propVo.getMessage(), SnackBar.MED_SNACK);
//					}
//				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					if (filter_type == null) {
						HashMap<String, String> map = new HashMap<String, String>();
						propVo = model.searchPropertyies(searchMap, map);
					} else {
						propVo = model.searchPropertyies(searchMap, filter_type);
					}
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		loadingTask.dontShowProgressDialog();
		loadingTask.execute("");
	}

//	OnClickListener StatusPinsClickListner = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			Resources r = getResources();
//			switch (v.getId()) {
//			case R.id.llShowAll:
//				llLineShowAll.setBackgroundColor(r.getColor(R.color.selected_underline));
//				llLineNew.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineUnder.setBackgroundColor(r.getColor(R.color.unselected_underline));
//				llLineReady.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//				tvShowAll.setTextColor(r.getColor(R.color.selected_tv));
//				tvNew.setTextColor(r.getColor(R.color.unselected_tv));
//				tvUnder.setTextColor(r.getColor(R.color.unselected_tv));
//				tvReady.setTextColor(r.getColor(R.color.unselected_tv));
//
//				filteredPropType = 0;
//				setPropertiesOnMap(propVo.getPropertiesArr());
//
//				// changePolygonColor(INFRA);
//
//				break;
//			case R.id.llUnder:
//				if (propVo != null) {
//					ArrayList<PropertyCaraouselVO> arr = propVo.getPropertiesArr();
//					for (int i = 0; i < arr.size(); i++) {
//						if (arr.get(i).getStatus().equalsIgnoreCase("UnderConstruction")) {
//							if (!isOneFound) {
//								arrFiltered = new ArrayList<PropertyCaraouselVO>();
//								isOneFound = true;
//							}
//							arrFiltered.add(arr.get(i));
//						}
//					}
//					if (isOneFound) {
//						filteredPropType = 1;
//						setPropertiesOnMap(arrFiltered);
//						llLineShowAll.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineNew.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineUnder.setBackgroundColor(r.getColor(R.color.selected_underline));
//						llLineReady.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//						tvShowAll.setTextColor(r.getColor(R.color.unselected_tv));
//						tvNew.setTextColor(r.getColor(R.color.unselected_tv));
//						tvUnder.setTextColor(r.getColor(R.color.selected_tv));
//						tvReady.setTextColor(r.getColor(R.color.unselected_tv));
//						isOneFound = false;
//					} else {
//						app.showToastAtCenter(ctx, "No Projects available.");
//					}
//				}
//
//				// changePolygonColor(NEEDS);
//				break;
//			case R.id.llNewLaunch:
//				if (propVo != null) {
//					ArrayList<PropertyCaraouselVO> arr = propVo.getPropertiesArr();
//
//					for (int i = 0; i < arr.size(); i++) {
//						if (arr.get(i).getStatus().equalsIgnoreCase("NewLaunch")) {
//							if (!isOneFound) {
//								arrFiltered = new ArrayList<PropertyCaraouselVO>();
//								isOneFound = true;
//							}
//							arrFiltered.add(arr.get(i));
//						}
//					}
//
//					if (isOneFound) {
//						filteredPropType = 2;
//						setPropertiesOnMap(arrFiltered);
//						llLineShowAll.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineNew.setBackgroundColor(r.getColor(R.color.selected_underline));
//						llLineUnder.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineReady.setBackgroundColor(r.getColor(R.color.unselected_underline));
//
//						tvShowAll.setTextColor(r.getColor(R.color.unselected_tv));
//						tvNew.setTextColor(r.getColor(R.color.selected_tv));
//						tvUnder.setTextColor(r.getColor(R.color.unselected_tv));
//						tvReady.setTextColor(r.getColor(R.color.unselected_tv));
//						isOneFound = false;
//					} else {
//						app.showToastAtCenter(ctx, "No Projects available.");
//					}
//
//				}
//				// changePolygonColor(LIFESTYLE);
//				break;
//			case R.id.llReady:
//				if (propVo != null) {
//					ArrayList<PropertyCaraouselVO> arr = propVo.getPropertiesArr();
//					for (int i = 0; i < arr.size(); i++) {
//						if (arr.get(i).getStatus().equalsIgnoreCase("ReadyToMove")) {
//							if (!isOneFound) {
//								arrFiltered = new ArrayList<PropertyCaraouselVO>();
//								isOneFound = true;
//							}
//							arrFiltered.add(arr.get(i));
//						}
//					}
//					if (isOneFound) {
//						filteredPropType = 3;
//						setPropertiesOnMap(arrFiltered);
//						llLineShowAll.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineNew.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineUnder.setBackgroundColor(r.getColor(R.color.unselected_underline));
//						llLineReady.setBackgroundColor(r.getColor(R.color.selected_underline));
//
//						tvShowAll.setTextColor(r.getColor(R.color.unselected_tv));
//						tvNew.setTextColor(r.getColor(R.color.unselected_tv));
//						tvUnder.setTextColor(r.getColor(R.color.unselected_tv));
//						tvReady.setTextColor(r.getColor(R.color.selected_tv));
//						isOneFound = false;
//					} else {
//						app.showToastAtCenter(ctx, "No Projects available.");
//					}
//				}
//				// changePolygonColor(PRICE);
//
//				break;
//			default:
//				break;
//			}
//		}
//
//	};

	//=======================================

	public String getDecimalFormatedPrice(double price){
		String pattern = "##.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);

		String a = "0";
		double b = 0;
		if(price==0){
			 a="0";
		}else if(price>=1000 && price<100000){
			a = price+"";
			b = price/1000f;
			 a = decimalFormat.format(b) +" K";
		}else if(price>=100000 && price<10000000){
			b = price/100000f;
			a = decimalFormat.format(b) +" Lacs";
		}else if(price>=10000000){
			b = price/10000000f;
			a = decimalFormat.format(b) +" Cr";
		}

		return a;
	}

}
