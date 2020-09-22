package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.LocalityType;
import com.AppEnums.ProjectType;
import com.AppEnums.UIEventType;
import com.VO.BuildersVO;
import com.VO.CitiesVO;
import com.VO.DeveloperVO;
import com.VO.DevelopersVO;
import com.VO.ProjectVO;
import com.VO.ProjectsVO;
import com.adapters.FeaturedDevFragmentAdapter;
import com.adapters.FeaturedProjFragmentAdapter;
import com.adapters.HotProjAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.sp.BMHApplication;
import com.sp.CitySearchActivity;
import com.sp.ConnectivityReceiver;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.DatabaseHandler;
import com.sp.LocalitySearchActivity;
import com.sp.LocationListActivity;
import com.sp.LoginActivity;
import com.sp.ProjectDetailActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.exception.BMHException;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.interfaces.FragmentClickListener;
import com.jsonparser.JsonParser;
import com.model.HotProjDataModel;
import com.model.HotProjectsCountModel;
import com.model.LocationModel;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.utils.LocalityData;
import com.utils.Utils;

import java.util.ArrayList;

public class SearchFragment extends BaseFragment implements FragmentClickListener, ConnectivityReceiver.ConnectivityReceiverListener  {

	private View rootView;
	private BuildersVO buildersVo;
	private ProjectsVO projectsVo;
	private BMHApplication app;
	public static final String TAG = SearchFragment.class.getSimpleName();
	private Activity ctx;
	private String selectedType;
	private Button btnSearch;
	private TextView tv_search_locality;
	private ViewPager featured_project_pager;
	private ViewPager viewpager_hot_proj;
	private ViewPager feature_developer;
	private FeaturedProjFragmentAdapter featured_project_adapter;
	private FeaturedDevFragmentAdapter featureDeveloperAdapter;
	//private FeaturedDevFragmentAdapter featured_developer;
	private static final int SELECT_CITY_REQ = 111;
	private static final int SEARCH_LOCALITY = 112;
	private final int LOGIN_REQ_CODE = 451;
	private CitiesVO citiesVo;
	private LinearLayout ll_resi_ico_container, ll_com_ico_container;
	//private boolean isFromFilter = false;
	private ImageButton leftNav, rightNav;
	private TextView byCity, byCityFeded;
	//private Timer timer = null;
	//private Timer timer2 = null;
	// ViewPager myPager = null;

	private PagerAdapter adapter;
	private String CitySelectedId = "0";
	private RelativeLayout desableParent;
	private LinearLayout developerParent;
	private LinearLayout projectParent;

	private DatabaseHandler db;

	private RelativeLayout loaderView;
	private boolean searchTextChanged = false;
	private  AsyncThread mAsyncThread = null;
	private ImageView iv_progress,iv_clear_locality;

	private NetworkErrorObject mNetworkErrorObject = null;
	private FragmentActivity currentActivity = null;
	private Button btn_residential,btn_commercial;
	private CheckBox chk_resi_flat, chk_resi_builder, chk_resi_plots, chk_resi_house_villa, chk_com_shops, chk_com_office, chk_com_service_apartment;

	private TextView tv_feature_developer_title,tv_feature_project_title;

	private LocalityData currentLocalityData = null;

	private ArrayList<ProjectVO> featureProjectsList = null;
	private ArrayList<DeveloperVO> featureDevelopersList = null;
	private String tempFevProjId = "";

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			ctx = currentActivity;
			rootView = inflater.inflate(R.layout.landing_page, container, false);
			initViews();
			setListeners();
			setFonts();
			app = (BMHApplication) currentActivity.getApplication();
			db = new DatabaseHandler(currentActivity);
			viewpager_hot_proj.setVisibility(View.GONE);
			//viewpager_hot_proj.setPageTransformer(true, new ReaderViewPagerTransformer(ReaderViewPagerTransformer.TransformType.ZOOM_OUT));
			iv_progress.setBackgroundResource(R.drawable.progress_loader);
			AnimationDrawable anim = (AnimationDrawable) iv_progress.getBackground();
			anim.start();
			builderTypeButtonState(true);
			projectParent.setVisibility(View.GONE);
			developerParent.setVisibility(View.GONE);
			byCity.setSelected(true);
			CitySelectedId = app.getFromPrefs(BMHConstants.CITYID);
			if(ConnectivityReceiver.isConnected()){
				defaultCalls();
			}else{
				mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.DEFAULT_CALLS, new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(ConnectivityReceiver.isConnected()) {
							defaultCalls();
						}else{
							Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
						}
					}
				});
			}
		} else {
			if(rootView != null && rootView.getParent() != null)
				((ViewGroup) rootView.getParent()).removeView(rootView);
			app = (BMHApplication) currentActivity.getApplication();
			ctx = currentActivity;
		}

		if(app.getFromPrefs(BMHConstants.CITYNAME).isEmpty()){
			showFadedView(true);
		}else{
			showFadedView(false);
			byCity.setText(app.getFromPrefs(BMHConstants.CITYNAME));
			showDataOnUI();
		}

		if(tv_search_locality.getText().length() > 0)iv_clear_locality.setVisibility(View.VISIBLE);
		else iv_clear_locality.setVisibility(View.GONE);

		return rootView;
	}// End of OncreateView().


	private void initViews(){
		iv_clear_locality = (ImageView)rootView.findViewById(R.id.iv_clear_locality);
		iv_progress = (ImageView)rootView.findViewById(R.id.img);
		loaderView = (RelativeLayout) rootView.findViewById(R.id.loader_view);
		ll_resi_ico_container = (LinearLayout) rootView.findViewById(R.id.ll_resi_ico_container);
		ll_com_ico_container = (LinearLayout) rootView.findViewById(R.id.ll_com_ico_container);
		tv_search_locality = (TextView) rootView.findViewById(R.id.tv_search_locality);
		featured_project_pager = (ViewPager) rootView.findViewById(R.id.pagerProjects);
		feature_developer = (ViewPager) rootView.findViewById(R.id.pagerDeveloper);
		viewpager_hot_proj = (ViewPager) rootView.findViewById(R.id.viewpager_hot_proj);

		chk_resi_flat = (CheckBox) rootView.findViewById(R.id.chk_resi_flat);
		chk_resi_builder = (CheckBox) rootView.findViewById(R.id.chk_resi_builder);
		chk_resi_plots = (CheckBox) rootView.findViewById(R.id.chk_resi_plots);
		chk_resi_house_villa = (CheckBox) rootView.findViewById(R.id.chk_resi_house_villa);

		chk_com_shops = (CheckBox) rootView.findViewById(R.id.chk_com_shops);
		chk_com_office = (CheckBox) rootView.findViewById(R.id.chk_com_office);
		chk_com_service_apartment = (CheckBox) rootView.findViewById(R.id.chk_com_service_apartment);

		btn_residential = (Button) rootView.findViewById(R.id.btn_residential);
		btn_commercial = (Button) rootView.findViewById(R.id.btn_commercial);

		tv_feature_developer_title = (TextView) rootView.findViewById(R.id.tv_feature_developer_title);
		tv_feature_project_title = (TextView) rootView.findViewById(R.id.tv_feature_project_title);
		projectParent = (LinearLayout) rootView.findViewById(R.id.project_parent);
		developerParent = (LinearLayout) rootView.findViewById(R.id.developer_parent);
		byCity = (TextView) rootView.findViewById(R.id.byCity);
		byCityFeded = (TextView) rootView.findViewById(R.id.byCityFeded);
		desableParent = (RelativeLayout) rootView.findViewById(R.id.desable_parent);
		btnSearch = (Button) rootView.findViewById(R.id.buttonSearch);

	}

	private void setListeners(){
		btn_commercial.setOnClickListener(mOnClickListener);
		btn_residential.setOnClickListener(mOnClickListener);
		btnSearch.setOnClickListener(mOnClickListener);
		byCity.setOnClickListener(mOnClickListener);
		byCityFeded.setOnClickListener(mOnClickListener);
		iv_clear_locality.setOnClickListener(mOnClickListener);
		tv_search_locality.setOnClickListener(mOnClickListener);
		//featured_project_pager.setOnTouchListener(mFeatureProjTouchListener);
		//feature_developer.setOnTouchListener(mFeatureDevTouchListener);


	}
	private void setFonts(){
		Typeface font = Typeface.createFromAsset(currentActivity.getAssets(), "fonts/regular.ttf");
		tv_search_locality.setTypeface(font);
		chk_resi_flat.setTypeface(font);
		chk_resi_builder.setTypeface(font);
		chk_resi_plots.setTypeface(font);
		chk_resi_house_villa.setTypeface(font);
		chk_com_shops.setTypeface(font);
		chk_com_office.setTypeface(font);
		chk_com_service_apartment.setTypeface(font);

	}


	private TextWatcher mLocationTextChangeListener = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		@Override
		public void afterTextChanged(Editable s) {

		}
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		currentActivity = (FragmentActivity) activity;
	}
	private void defaultCalls() {
		startCityTask();
		// searchTask("");
		startFeaturedDevTask();
		startFeaturedProjectsTask();
		getHotProjectsData();
	}

	private SharedPreferences getSharedPreferences(String mypreferences2, int modePrivate) {
		// TODO Auto-generated method stub
		return null;
	}

	private ContextWrapper getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void runOnUiThread(Runnable runnable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		currentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}

	@Override
	public String getTagText() {
		return TAG;
	}

	@Override
	public boolean onBackPressed() {
		//if(timer2 != null) timer2.cancel();
		currentActivity.finish();
		return true;
	}


	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.buttonSearch:
					if(ConnectivityReceiver.isConnected()){
						searchProperty();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.SEARCH_PROPERTY,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											searchProperty();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.byCityFeded:
					if(ConnectivityReceiver.isConnected()){
						CityButtonClick();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_CITY_FEDED,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											CityButtonClick();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.byCity:
					if(ConnectivityReceiver.isConnected()) {
						CityButtonClick();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_CITY_FEDED,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											CityButtonClick();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.iv_clear_locality:
					tv_search_locality.setText("");
					currentLocalityData = null;
					iv_clear_locality.setVisibility(View.GONE);

					break;
				case R.id.btn_residential:
					builderTypeButtonState(true);
					break;
				case R.id.btn_commercial:
					builderTypeButtonState(false);
					break;
				case R.id.tv_search_locality:
					//TODO:
					Intent searchLocality = new Intent(currentActivity, LocalitySearchActivity.class);
					startActivityForResult(searchLocality,SEARCH_LOCALITY);
					break;
			}// End of Switch
		}
	};


	private void searchProperty(){
		Intent mIntent =  new Intent(currentActivity,LocationListActivity.class);;
		IntentDataObject mIntentDataObject = new IntentDataObject();
		mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
		mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));
		if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()){
			mIntentDataObject.putData(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
		}
		if (tv_search_locality.getText().length() > 0 && currentLocalityData != null) {
			if(currentLocalityData.getSubtitle().equals(LocalityType.LOCATION.value)){
				mIntentDataObject.putData(ParamsConstants.LOCATION_ID,currentLocalityData.getId());
				mIntentDataObject.putData(ParamsConstants.TITLE,getString(R.string.select_locality));
			}else if(currentLocalityData.getSubtitle().equals(LocalityType.SUBLOCATION.value)){
				mIntentDataObject.putData(ParamsConstants.SUB_LOCATION_ID,currentLocalityData.getId());
				mIntentDataObject.putData(ParamsConstants.SUB_LOCATION_NAME,currentLocalityData.getLocation_Name());
				mIntentDataObject.putData(ParamsConstants.TITLE,currentLocalityData.getTitle());
			}else if(currentLocalityData.getSubtitle().equals(LocalityType.BUILDER.value)){
				mIntentDataObject.putData(ParamsConstants.BUILDER_ID,currentLocalityData.getId());
				mIntentDataObject.putData(ParamsConstants.BUILDER_NAME,currentLocalityData.getTitle());
				mIntentDataObject.putData(ParamsConstants.TITLE,currentLocalityData.getTitle());
				mIntent = new Intent(currentActivity,ProjectsListActivity.class);
			}else if(currentLocalityData.getSubtitle().equals(LocalityType.PROJECT.value)){
				//Do nothing.
			}
		}
		String params = getBuilderTypeParams();
		if(params != null && params.length() > 0) {
			mIntentDataObject.putData(ParamsConstants.P_TYPE, params);
		}
		mIntent.putExtra(IntentDataObject.OBJ,mIntentDataObject);
		startActivity(mIntent);
	}

	private void startCityTask() {

		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

			@Override
			public void OnBackgroundTaskCompleted() {
				if (citiesVo == null) {
					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						startCityTask();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_GET_CITY,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											startCityTask();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}

				} else {
					if (citiesVo.isSuccess() && citiesVo.getArrCity().size() > 0) {
						// setCityAutocompleteAdapter(citiesVo);
					} else {
						showToast(citiesVo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				LocationModel model = new LocationModel();
				try {
					citiesVo = model.getCities(app);
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		//loadingTask.dontShowProgressDialog();
		// loaderView.setVisibility(View.VISIBLE);
		loadingTask.execute("");
	}



	private void startFeaturedDevTask() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			DevelopersVO vo;
			ProgressBar pb;
			//Button btnRetry;
			int m_count = 0;

			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					pb.setVisibility(View.GONE);
					//btnRetry.setVisibility(View.VISIBLE);
				} else {
					if (vo.isSuccess()) {
						pb.setVisibility(View.GONE);
						//btnRetry.setVisibility(View.GONE);
						feature_developer = (ViewPager) rootView.findViewById(R.id.pagerDeveloper);
						feature_developer.setClipToPadding(false);
						feature_developer.setPadding(80, 0, 80, 0);
						feature_developer.setPageMargin(25);
						featureDeveloperAdapter = new FeaturedDevFragmentAdapter(SearchFragment.this.getChildFragmentManager(),vo.getDevelopers());
						feature_developer.setAdapter(featureDeveloperAdapter);
						feature_developer.setAdapter(null);
						feature_developer.setAdapter(featureDeveloperAdapter);
						featureDevelopersList = vo.getDevelopers();
						startFeatureDevCarousel();
						//final int max = vo.getDevelopers().size();
						/*timer2 = new Timer();
						try {
							if (isAdded()) {
								timer2.schedule(new TimerTask() {

									@Override
									public void run() {
										if(currentActivity != null)
											currentActivity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													if (m_count < max) {
														feature_developer.setCurrentItem(m_count);
														m_count++;
													} else {
														m_count = 0;
														feature_developer.setCurrentItem(m_count);
													}
												}
											});
									}
								}, 200, 6000);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
*/
						developerParent.setVisibility(View.VISIBLE);

					} else {
						featureDevelopersList = null;
						featureDeveloperAdapter = null;
						feature_developer = (ViewPager) rootView.findViewById(R.id.pagerDeveloper);
						feature_developer.setAdapter(null);
						pb.setVisibility(View.GONE);
						//btnRetry.setVisibility(View.VISIBLE);
						//btnRetry.setText(vo.getMessage());
						developerParent.setVisibility(View.GONE);

					}

				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					vo = model.getFeaturedDevelopers(CitySelectedId + "");

				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
				pb = (ProgressBar) rootView.findViewById(R.id.progBarDev);
				//btnRetry = (Button) rootView.findViewById(R.id.btnRetryDev);
				pb.setVisibility(View.VISIBLE);
				/*btnRetry.setVisibility(View.GONE);
				btnRetry.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startFeaturedDevTask();
					}
				});*/
			}
		});

		loadingTask.dontShowProgressDialog();
		// loaderView.setVisibility(View.VISIBLE);
		loadingTask.execute("");

	}

	public void startFeaturedProjectsTask() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			ProjectsVO vo;
			ProgressBar pb;
			//Button btnRetry;
			int count = 0;
			@Override
			public void OnBackgroundTaskCompleted() {
				if (vo == null) {
					pb.setVisibility(View.GONE);
				//	btnRetry.setVisibility(View.VISIBLE);
				} else {
					if (vo.isSuccess()) {
						pb.setVisibility(View.GONE);
					//	btnRetry.setVisibility(View.GONE);
						featured_project_pager = (ViewPager) rootView.findViewById(R.id.pagerProjects);
						featured_project_pager.setClipToPadding(false);
						featured_project_pager.setPadding(80, 0, 80, 0);
						featured_project_pager.setPageMargin(25);
						try {
							featured_project_adapter = new FeaturedProjFragmentAdapter(SearchFragment.this.getChildFragmentManager(), vo.getArrProjects());
							featured_project_pager.setAdapter(featured_project_adapter);
						} catch (Exception e) {
							e.printStackTrace();
						}
						featureProjectsList = vo.getArrProjects();
						startFeatureProjectCarousel();
						projectParent.setVisibility(View.VISIBLE);
					} else {
						featured_project_adapter = null;
						featured_project_pager = (ViewPager) rootView.findViewById(R.id.pagerProjects);
						featured_project_pager.setAdapter(null);
						pb.setVisibility(View.GONE);
						//btnRetry.setVisibility(View.VISIBLE);
						//btnRetry.setText(vo.getMessage());
						projectParent.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					String userid = "";
					try {
						userid = app.getFromPrefs(BMHConstants.USERID_KEY);
					} catch (Exception e) {
						System.out.println("user is not log in");
					}
					System.out.println("searchfragment 1888 user_id " + userid);
					if (userid == "") {
						vo = model.getFeaturedProjects(CitySelectedId + "");
					} else {
						// sending userid if user is login
						vo = model.getFeaturedProjects(CitySelectedId + "", userid);
					}

					// vo = model.getFeaturedProjects(CitySelectedId + "");
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {
				pb = (ProgressBar) rootView.findViewById(R.id.progBarProj);
				//btnRetry = (Button) rootView.findViewById(R.id.btnRetryProj);
				pb.setVisibility(View.VISIBLE);
				//btnRetry.setVisibility(View.GONE);
				/*btnRetry.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startFeaturedProjectsTask();
					}
				});*/
			}
		});

		loadingTask.dontShowProgressDialog();
		// loaderView.setVisibility(View.VISIBLE);
		loadingTask.execute("");
	}


	private void startBuildersTask() {

		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

			@Override
			public void OnBackgroundTaskCompleted() {
				if (buildersVo == null) {
					// app.showAppMessage(ctx,
					// "Something went wrong, Please Try again.");
					showToast(getString(R.string.unable_to_connect_server));
				} else {
					if (buildersVo.isSuccess() && buildersVo.getArrBuilders().size() > 0) {
						// setBuilderAutocompleteAdapter(buildersVo);
					} else {
						showToast(buildersVo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					buildersVo = model.getBuilders(app);
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		loadingTask.dontShowProgressDialog();
		// loaderView.setVisibility(View.VISIBLE);
		loadingTask.execute("");
	}

	private void startProjectTask() {

		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {

			@Override
			public void OnBackgroundTaskCompleted() {
				if (projectsVo == null) {
					// app.showAppMessage(ctx,
					// "Something went wrong, Please Try again.");
					showToast(getString(R.string.unable_to_connect_server));
				} else {
					if (projectsVo.isSuccess() && projectsVo.getArrProjects().size() > 0) {
						// setProjectsAutocompleteAdapter(projectsVo);
					} else {
						showToast(projectsVo.getMessage());
					}
				}
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					projectsVo = model.getAllProjects(app);
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		loadingTask.dontShowProgressDialog();
		loaderView.setVisibility(View.VISIBLE);
		loadingTask.execute("");
	}

	@Override
	public void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
		if(featureDevelopersList != null && featureDevelopersList.size() != 0) {
			featureDeveloperAdapter = new FeaturedDevFragmentAdapter(SearchFragment.this.getChildFragmentManager(),featureDevelopersList);
			feature_developer.setAdapter(featureDeveloperAdapter);
			featureProjRunnable(featureDevelopersList.size());
			//Re-run callback
			featureDevHandler.postDelayed(featureDevRunnable, ANIM_VIEWPAGER_DELAY);
		}
		if(featureProjectsList != null && featureProjectsList.size() != 0) {
			featured_project_adapter = new FeaturedProjFragmentAdapter(SearchFragment.this.getChildFragmentManager(), featureProjectsList);
			featured_project_pager.setAdapter(featured_project_adapter);
			featureProjRunnable(featureProjectsList.size());
			//Re-run callback
			featureProjHandler.postDelayed(featureProjRunnable, ANIM_VIEWPAGER_DELAY);
		}
		if(mNetworkErrorObject != null) mNetworkErrorObject.getAlertDialog().dismiss();
		if(ConnectivityReceiver.isConnected()){
			//TODO: network call
			startFeaturedProjectsTask();
		}else{
			mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_FEATURE_PROJECTS,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ConnectivityReceiver.isConnected()){
								//TODO: network call
								startFeaturedProjectsTask();
								mNetworkErrorObject.getAlertDialog().dismiss();
								mNetworkErrorObject = null;
							}else{
								Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
							}
						}
					});
		}

	}

	@Override
	public void onPause() {
		if (mAsyncThread != null)
			mAsyncThread.cancel(true);
		if (featureProjHandler != null) {
			//Remove callback
			featureProjHandler.removeCallbacks(featureProjRunnable);
		}if (featureDevHandler != null) {
			//Remove callback
			featureDevHandler.removeCallbacks(featureDevRunnable);
		}
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == LOGIN_REQ_CODE && resultCode == Activity.RESULT_OK) {
			String projectId = app.getFromPrefs(BMHConstants.VALUE);
			if(projectId != null && projectId.length() > 0) {
				favRequest(projectId);
				app.saveIntoPrefs(BMHConstants.VALUE, "");
			}
			//TODO
		} else if (requestCode == SELECT_CITY_REQ && resultCode == Activity.RESULT_OK ) {
			showFadedView(false);
			String city = intent.getStringExtra("city");
			CitySelectedId = app.getFromPrefs(BMHConstants.CITYID);
			byCity.setText(city);
			if(ConnectivityReceiver.isConnected()){
				//TODO: network call
				getHotProjectsData();
			}else{
				mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_HOT_PROJECTS,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(ConnectivityReceiver.isConnected()){
									//TODO: network call
									getHotProjectsData();
									mNetworkErrorObject.getAlertDialog().dismiss();
									mNetworkErrorObject = null;
								}else{
									Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
								}
							}
						});
			}
			showDataOnUI();
		}else if (requestCode == SEARCH_LOCALITY && resultCode == Activity.RESULT_OK ) {
			//TODO:
			IntentDataObject mIntentDataObject = (IntentDataObject) intent.getSerializableExtra("obj");
			currentLocalityData= (LocalityData) mIntentDataObject.getObj();
			tv_search_locality.setText(currentLocalityData.getTitle());
			if(tv_search_locality.getText().length() > 0)iv_clear_locality.setVisibility(View.VISIBLE);
			else iv_clear_locality.setVisibility(View.GONE);
		}

	}

	private void showDataOnUI() {
		tv_search_locality.setText("");
		//if(timer2 != null) timer2.cancel();
		startFeaturedProjectsTask();
		startFeaturedDevTask();
	}

	public void CityButtonClick() {
		Gson gson = new Gson();
		String modelData = gson.toJson(citiesVo);
		Intent intent = new Intent(currentActivity, CitySearchActivity.class);
		intent.putExtra("data", modelData);
		startActivityForResult(intent, SELECT_CITY_REQ);
	}

	@Override
	public void onDestroy() {
		/*try {
			if (timer2 != null) {
				timer2.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		super.onDestroy();
	}

	private void showFadedView(boolean isFaded){
		if(isFaded){
			desableParent.setVisibility(View.VISIBLE);
		}else{
			desableParent.setVisibility(View.GONE);
		}
	}

	public void getHotProjectsData() {
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.HOT_PROJECTS);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.HOT_PROJECTS));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		String cityId = app.getFromPrefs(BMHConstants.CITYID);
		mBean.setJson("city_id="+cityId);
		mBean.setmHandler(mHandler);
		mBean.setCtx(currentActivity);
		mAsyncThread = new AsyncThread();
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}

	DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			if(mAsyncThread != null)mAsyncThread.cancel(true);
			mAsyncThread = null;
		}
	};

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.obj == null) {
				//showConfirmAlert("","Unable to connect Server.Please try later");
				// showToast("Server error");
				return false;
			} else {
				ReqRespBean mBean = (ReqRespBean) msg.obj;
				switch (mBean.getApiType()) {
					case HOT_PROJECTS:
						HotProjectsCountModel model = (HotProjectsCountModel) JsonParser.convertJsonToBean(APIType.HOT_PROJECTS,mBean.getJson());
						if(model != null && model.getData() != null){
							ArrayList<HotProjDataModel> modelsList = new ArrayList<>(5);
							if(model.getData().getIsHotProjectAvailable() == 1){
								modelsList.add(new HotProjDataModel(ProjectType.HOT,getString(R.string.hot_projects),R.drawable.hot));
							}if(model.getData().getIsLiveProjectAvailable() == 1){
								modelsList.add(new HotProjDataModel(ProjectType.LIVE,getString(R.string.live_projects),R.drawable.live));
							}if(model.getData().getIsReadyToMoveInProjectAvailable() == 1){
								modelsList.add(new HotProjDataModel(ProjectType.READY_TO_MOVE,getString(R.string.ready_to_move_projects),R.drawable.ready_to_move));
							}if(model.getData().getIsLuxuryProjectAvailable() == 1){
								modelsList.add(new HotProjDataModel(ProjectType.LUXURY,getString(R.string.luxury_projects),R.drawable.luxury));
							}if(model.getData().getIsAffordableHomesProjectAvailable() == 1){
								modelsList.add(new HotProjDataModel(ProjectType.AFFORDABLE,getString(R.string.affordable_projects),R.drawable.affordable));
							}
							if(modelsList.size() > 0){
								viewpager_hot_proj.setVisibility(View.VISIBLE);
								viewpager_hot_proj.setClipToPadding(false);
								viewpager_hot_proj.setPadding(50, 30, 50, 30);
								viewpager_hot_proj.setPageMargin(20);
								HotProjAdapter adapter = new HotProjAdapter(SearchFragment.this.getChildFragmentManager(),modelsList);
								viewpager_hot_proj.setAdapter(adapter);
								//int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,     getResources().getDisplayMetrics());
								//viewpager_hot_proj.setPageMargin(-margin);

							}else{
								viewpager_hot_proj.setVisibility(View.GONE);
							}
						}
						break;
					case FAV_PROJECT:
						if(featured_project_adapter != null && mBean.getRequestObj() != null && mBean.getRequestObj() instanceof  String) {
							String id = (String) mBean.getRequestObj();
							featured_project_adapter.toggleFav(id);
						}
						break;
					default:
						break;
				}
			}
			return true;
		}
	});
	@Override
	public void myOnClickListener(Fragment fragment, Object obj) {
		if(fragment instanceof HotProjFragment) {
			if (obj != null && obj instanceof HotProjDataModel) {
				BMHConstants.SELECTED_PROPERTY_TYPE = BMHConstants.NONE;
				HotProjDataModel model = (HotProjDataModel) obj;
				IntentDataObject mIntentDataObject = new IntentDataObject();
				mIntentDataObject.putData(ParamsConstants.SPECIAL_CATEGORY, (model.getProjType()).value);
				mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
				mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));
				if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0){
					mIntentDataObject.putData(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
				}
				mIntentDataObject.putData(ParamsConstants.TITLE, model.getProjName());
				Intent mIntent = new Intent(currentActivity, ProjectsListActivity.class);
				mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
				startActivity(mIntent);
			}
		}else if(fragment instanceof FeaturedDevelopersFragment){
			if(FeaturedDevelopersFragment.CURRENT_EVENT == FeaturedDevelopersFragment.VIEW_ALL_PROJECTS){
				//if (Connectivity.isConnectedWithDoalog(currentActivity)) {
					if (obj != null && obj instanceof DeveloperVO) {
						BMHConstants.SELECTED_PROPERTY_TYPE = BMHConstants.NONE;
						DeveloperVO dev = (DeveloperVO)obj;
						IntentDataObject mIntentDataObject = new IntentDataObject();
						mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
						mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));
						if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0){
							mIntentDataObject.putData(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
						}
						mIntentDataObject.putData(ParamsConstants.BUILDER_ID, dev.getBuilder_id());
						mIntentDataObject.putData(ParamsConstants.BUILDER_NAME, dev.getName_builder());
						mIntentDataObject.putData(ParamsConstants.TITLE, dev.getCompany_name());
						Intent mIntent = new Intent(currentActivity, ProjectsListActivity.class);
						mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
						startActivity(mIntent);
					}
				//}

			}
		}else if(fragment instanceof FeaturedProjImageFragment) {
			if (FeaturedProjImageFragment.CURRENT_EVENT == FeaturedProjImageFragment.PROJECT_CLICK_EVENT) {
				//if (Connectivity.isConnectedWithDoalog(currentActivity)) {
					if (obj != null && obj instanceof ProjectVO) {
						ProjectVO vo = (ProjectVO) obj;
						IntentDataObject mIntentDataObject = new IntentDataObject();
						mIntentDataObject.putData(ParamsConstants.ID,vo.getId());
						mIntentDataObject.putData(ParamsConstants.TITLE,vo.getName());
						mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
						mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));
						if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0){
							mIntentDataObject.putData(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
						}
						Intent mIntent = new Intent(currentActivity, ProjectDetailActivity.class);
						mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
						startActivity(mIntent);
					}
				//}
			}else if(FeaturedProjImageFragment.CURRENT_EVENT == FeaturedProjImageFragment.FAV_CLICK_EVENT){
				if (obj != null && obj instanceof ProjectVO) {
					final ProjectVO vo = (ProjectVO) obj;
					if(app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0){
						app.saveIntoPrefs(BMHConstants.VALUE,vo.getId());
						Intent i = new Intent(ctx, LoginActivity.class);
						i.putExtra(LoginActivity.LOGIN_INTENT,false);// set false if login screen open from intent.
						startActivityForResult(i, LOGIN_REQ_CODE);
					}else{
						tempFevProjId = vo.getId();
						if(ConnectivityReceiver.isConnected()){
							//TODO: network call
							favRequest(tempFevProjId);
						}else{
							mNetworkErrorObject = Utils.showNetworkErrorDialog(currentActivity, UIEventType.RETRY_FEV,
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											if(ConnectivityReceiver.isConnected()){
												//TODO: network call
												favRequest(tempFevProjId);
												mNetworkErrorObject.getAlertDialog().dismiss();
												mNetworkErrorObject = null;
											}else{
												Utils.showToast(currentActivity,getString(R.string.check_your_internet_connection));
											}
										}
									});
						}
					}
				}
			}
		}
	}

	private void favRequest(String id){
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.FAV_PROJECT);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.FAV_PROJECT));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
		StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append(ParamsConstants.USER_ID);
		mStringBuilder.append("=");
		mStringBuilder.append(userId);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.ID);
		mStringBuilder.append("=");
		mStringBuilder.append(id);
		mStringBuilder.append("&");
		mStringBuilder.append(ParamsConstants.TYPE);
		mStringBuilder.append("=");
		mStringBuilder.append(ParamsConstants.BUY);
		mBean.setJson(mStringBuilder.toString());
		mBean.setRequestObj(id);
		mBean.setmHandler(mHandler);
		mBean.setCtx(currentActivity);
		mAsyncThread = new AsyncThread();
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case SEARCH_PROPERTY:
					searchProperty();
					break;
				case DEFAULT_CALLS:
					defaultCalls();
					break;
				case RETRY_CITY_FEDED:
					CityButtonClick();
					break;
				case RETRY_GET_CITY:
					startCityTask();
					break;
				case RETRY_FEATURE_PROJECTS:
					startFeaturedProjectsTask();
					break;
				case RETRY_HOT_PROJECTS:
					getHotProjectsData();
					break;
				case RETRY_FEV:
					favRequest(tempFevProjId);
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}


	private void builderTypeButtonState(boolean isResidential){
		btn_residential.setSelected(isResidential);
		btn_commercial.setSelected(!isResidential);
		int resiVisibility = isResidential ? View.VISIBLE : View.GONE;
		int comVisibility = isResidential ? View.GONE : View.VISIBLE;
		((LinearLayout) rootView.findViewById(R.id.ll_resi_ico_container)).setVisibility(resiVisibility);
		((LinearLayout) rootView.findViewById(R.id.ll_com_ico_container)).setVisibility(comVisibility);
		if(isResidential) {
			chk_com_shops.setChecked(false);
			chk_com_office.setChecked(false);
			chk_com_service_apartment.setChecked(false);
		}else{
			chk_resi_flat.setChecked(false);
			chk_resi_builder.setChecked(false);
			chk_resi_plots.setChecked(false);
			chk_resi_house_villa.setChecked(false);
		}
	}


	/**
	 * To get comma separated values of selected property type
	 * @return String
	 *
	 */
	private String getBuilderTypeParams(){
		String residential_value = "";
		if(btn_residential.isSelected()){
			if(chk_resi_flat.isChecked() || chk_resi_builder.isChecked() || chk_resi_plots.isChecked() || chk_resi_house_villa.isChecked()){
				if(chk_resi_flat.isChecked()){
					residential_value+= "FLAT," ;
				}if(chk_resi_builder.isChecked()){
					residential_value += "Builder_Floor,";
				}if(chk_resi_plots.isChecked()){
					residential_value += "PLOT,";
				}if(chk_resi_house_villa.isChecked()){
					residential_value += "House_Villa,";
				}
				return Utils.removeLastComma(residential_value);
			}
		}else if (btn_commercial.isSelected()){
			if(chk_com_shops.isChecked() || chk_com_office.isChecked() || chk_com_service_apartment.isChecked()){
				if(chk_com_shops.isChecked()){
					residential_value+= "Shop/Showroom," ;
				}if(chk_com_office.isChecked()){
					residential_value+= "OFFICE_SPACE," ;
				}if(chk_com_service_apartment.isChecked()) {
					residential_value += "Service_Apartment,";
				}
				return Utils.removeLastComma(residential_value);
			}
		}else{
			//TODO: do nothing.
			return residential_value;
		}
		return residential_value;
	}

	private Runnable featureProjRunnable;
	private Handler featureProjHandler;
	private static final long ANIM_VIEWPAGER_DELAY = 8000;
	private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 400;
	boolean featureProjStopSliding = false;

	View.OnTouchListener mFeatureProjTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_CANCEL:
					break;
				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					startFeatureProjectCarousel();
					break;
				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					stopFeatureProjectCarousel();
					break;
			}
			return false;
		}
	};
	public void featureProjRunnable(final int size) {
		featureProjHandler = new Handler();
		featureProjRunnable = new Runnable() {
			public void run() {
				if (!featureProjStopSliding) {
					if (featured_project_pager.getCurrentItem() == size - 1) {
						featured_project_pager.setCurrentItem(0);
					} else {
						featured_project_pager.setCurrentItem(featured_project_pager.getCurrentItem() + 1, true);
					}
					featureProjHandler.postDelayed(featureProjRunnable, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}

	private void startFeatureProjectCarousel(){
		if (featureProjectsList != null && featureProjectsList.size() != 0) {
			featureProjStopSliding = false;
			featureProjRunnable(featureProjectsList.size());
			featureProjHandler.postDelayed(featureProjRunnable, ANIM_VIEWPAGER_DELAY_USER_VIEW);
		}
	}
	private void stopFeatureProjectCarousel(){
		if (featureProjHandler != null && featureProjStopSliding == false) {
			featureProjStopSliding = true;
			featureProjHandler.removeCallbacks(featureProjRunnable);
		}
	}

	// -------------------------------------------- //

	private Runnable featureDevRunnable;
	private Handler featureDevHandler;
	boolean featureDevStopSliding = false;

	View.OnTouchListener mFeatureDevTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_CANCEL:
					break;
				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					startFeatureDevCarousel();
					break;
				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					stopFeatureDevCarousel();
					break;
			}
			return false;
		}
	};
	public void featureDevRunnable(final int size) {
		featureDevHandler = new Handler();
		featureDevRunnable = new Runnable() {
			public void run() {
				if (!featureDevStopSliding) {
					if (feature_developer.getCurrentItem() == size - 1) {
						feature_developer.setCurrentItem(0);
					} else {
						feature_developer.setCurrentItem(feature_developer.getCurrentItem() + 1, true);
					}
					featureDevHandler.postDelayed(featureDevRunnable, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}

	private void startFeatureDevCarousel(){
		if (featureDevelopersList != null && featureDevelopersList.size() != 0) {
			featureDevStopSliding = false;
			featureDevRunnable(featureDevelopersList.size());
			featureDevHandler.postDelayed(featureDevRunnable, ANIM_VIEWPAGER_DELAY_USER_VIEW);
		}


	}
	private void stopFeatureDevCarousel(){
		if (featureDevHandler != null && featureDevStopSliding == false) {
			featureDevStopSliding = true;
			featureDevHandler.removeCallbacks(featureDevRunnable);
		}
	}
	// -------------------------------------------- //


	private void showToast(String msg){
		Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
	}
}