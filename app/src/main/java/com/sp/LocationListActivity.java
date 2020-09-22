package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.LocationVO;
import com.VO.LocationsVO;
import com.adapters.LocationsListAdapter;
import com.adapters.SubLocationsListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.filter.Builder;
import com.filter.BuilderArrayAdapter;
import com.filter.BuilderViewHolder;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.model.SubLocationRespModel;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LocationListActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

	private Activity ctx = LocationListActivity.this;
	private ListView lv_locality_list;
	private ArrayList<LocationVO> locationList;
	private LocationsListAdapter locationsListAdapter;
	private SubLocationsListAdapter subLocationsListAdapter;
	private ArrayList<SubLocationRespModel.SectorLists> subLocationList;
	//private LocationsVO locationsVo;
	private View filter_view;
	private ListView lv_filter = null;
	private ArrayAdapter<Builder> filterAdapter = null;
	private Typeface regularTypeface;
	private  AsyncThread mAsyncThread = null;
	private IntentDataObject mIntentDataObject = null;
	private Toolbar toolbar = null;
	private ImageView back_button = null;
	private int CURRENT_LIST_COUNT = 0;
	private NetworkErrorObject mNetworkErrorObject = null;
	private String getAlertParams;
	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_location_list);
		regularTypeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
		if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
				&& getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
			mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
		}
		initViews();
		setListeners();
		defaultUIState();
		setFilterAdapter();
		if(ConnectivityReceiver.isConnected()){
			getLocationData();
		}else{
			mNetworkErrorObject = Utils.showNetworkErrorDialog(this, UIEventType.RETRY_GET_LOCATIONS,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ConnectivityReceiver.isConnected()){
								getLocationData();
								mNetworkErrorObject.getAlertDialog().dismiss();
								mNetworkErrorObject = null;
							}else{
								Utils.showToast(LocationListActivity.this,getString(R.string.check_your_internet_connection));
							}
						}
					});
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(app == null) app =(BMHApplication) getApplication();
		mResources = getResources();
		BMHApplication.getInstance().setConnectivityListener(this);
	}

	private void defaultUIState() {
		if (mIntentDataObject == null && mIntentDataObject.getData() == null || toolbar == null) return;
		if (mIntentDataObject.getData().get(ParamsConstants.TITLE) != null) {
			toolbar.setTitle(mIntentDataObject.getData().get(ParamsConstants.TITLE));
		} else {
			toolbar.setTitle(getString(R.string.select_locality));
		}
		invalidateOptionsMenu();
	}
	private void initViews() {
		rl_root = (RelativeLayout)findViewById(R.id.rl_root);
		lv_locality_list = (ListView) findViewById(R.id.lv_locality_list);
		filter_view = findViewById(R.id.filter_view);
		lv_filter = (ListView) filter_view.findViewById(R.id.lv_filter);
		TextView reset = (TextView) findViewById(R.id.tv_reset_filter);
		reset.setVisibility(View.GONE);
		back_button = (ImageView)findViewById(R.id.back_button);
		TextView title = (TextView)findViewById(R.id.textview);
		toolbar = setToolBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	private void setListeners() {
		lv_locality_list.setOnItemClickListener(mOnItemClickListener);
		lv_filter.setOnItemClickListener(mOnItemClickListener);
		back_button.setOnClickListener(mOnClickListener);
	}
	OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.back_button:
					filter_view.setVisibility(View.GONE);
					break;
				case R.id.buttonFilterBy:

					break;
				default:
					break;

			}
		}
	};

	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()){
				case R.id.lv_locality_list:
					if(mIntentDataObject != null) {
						if(view.getTag(R.integer.locality_item) instanceof LocationVO) {
							LocationVO mLocationVO = (LocationVO) view.getTag(R.integer.locality_item);
							IntentDataObject tempObj = new IntentDataObject();
							tempObj.setObj(mIntentDataObject.getObj());
							if (mIntentDataObject.getData() != null){
								for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
									tempObj.putData(entry.getKey(), entry.getValue());
								}
							}
							tempObj.putData(ParamsConstants.LOCATION,mLocationVO.getId());
							tempObj.putData(ParamsConstants.TITLE, mLocationVO.getName());
							Intent mIntent = new Intent(LocationListActivity.this, ProjectsListActivity.class);
							mIntent.putExtra(IntentDataObject.OBJ, tempObj);
							startActivity(mIntent);

						}else if(view.getTag(R.integer.locality_item) instanceof SubLocationRespModel.SectorLists) {
							SubLocationRespModel.SectorLists mSector = (SubLocationRespModel.SectorLists) view.getTag(R.integer.locality_item);
							IntentDataObject tempObj = new IntentDataObject();
							tempObj.setObj(mIntentDataObject.getObj());
							if (mIntentDataObject.getData() != null){
								for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
									tempObj.putData(entry.getKey(), entry.getValue());
								}
							}
							tempObj.putData(ParamsConstants.SUB_LOCATION_ID,mSector.getSector_id());
							tempObj.putData(ParamsConstants.TITLE, mSector.getSector_name());
							Intent mIntent = new Intent(LocationListActivity.this, ProjectsListActivity.class);
							mIntent.putExtra(IntentDataObject.OBJ, tempObj);
							startActivity(mIntent);

						}
					}

					break;
				case R.id.lv_filter:
					Builder planet = filterAdapter.getItem(position);
					planet.toggleChecked();
					BuilderViewHolder viewHolder = (BuilderViewHolder) view.getTag();
					viewHolder.getCheckBox().setChecked(planet.isChecked());
					filter_view.setVisibility(View.GONE);
					sortLocationList(position);
					break;
			}
		}
	};



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.location_list, menu);
		if (!app.getFromPrefs(BMHConstants.CITYID ).equals("16")) {
			menu.getItem(0).setVisible(false);
		}else{
			menu.getItem(0).setVisible(true);
		}
		if (CURRENT_LIST_COUNT <= 1) {
			menu.getItem(1).setVisible(false);
		}else{
			menu.getItem(1).setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_filter:
				if (filter_view.getVisibility()==View.VISIBLE) {
					filter_view.setVisibility(View.GONE);
				}else{
					filter_view.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.action_heatmap:
				if(mIntentDataObject != null && mIntentDataObject.getData() != null
						&& mIntentDataObject.getData().get(ParamsConstants.SUB_LOCATION_ID) != null) {
					Intent mIntent = new Intent(LocationListActivity.this, SubLocationHeatmapActivity.class);
					mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
					startActivity(mIntent);
					overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
				}else {
					Intent mIntent = new Intent(LocationListActivity.this, LocationHeatmapActivity.class);
					mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
					startActivity(mIntent);
					overridePendingTransition(R.anim.push_up_in, R.anim.push_down_in);
				}

				break;
			case android.R.id.home:
				finish();
				//gotToHome();
				break;

		}

		return super.onOptionsItemSelected(item);
	}
	/*private void gotToHome(){
		Intent intent = new Intent(LocationListActivity.this, SearchPropertyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_CLEAR_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}*/

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected String setActionBarTitle() {
		return "Select Locality";
	}

	private void setFilterAdapter(){
		ArrayList<Builder> filterList = new ArrayList<Builder>();
		filterList.add(new Builder("psf_ltoh","Price per sq.ft. (low to high)",false));
		filterList.add(new Builder("psf_htol","Price per sq.ft. (high to low)",false));
		filterList.add(new Builder("rating","Rating (high to low)", false));
		filterList.add(new Builder("infra","Infra (high to low)", false));
		filterList.add(new Builder("needs","Needs (high to low)", false));
		filterList.add(new Builder("lifestyle","Lifestyle (high to low)", false));
		filterList.add(new Builder("returns","Returns (high to low)", false));
		filterAdapter = new BuilderArrayAdapter(this, filterList);
		lv_filter.setAdapter(filterAdapter);
	}

	private void setLocationAdapter(){
		locationsListAdapter = new LocationsListAdapter(ctx, locationList);
		lv_locality_list.setAdapter(locationsListAdapter);
		View footerView = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
		if(locationList == null || locationList.size() == 0){
			LinearLayout mView = (LinearLayout)findViewById(R.id.empty_view);
			LinearLayout.LayoutParams emptyViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
			emptyViewParams.weight = 0.75f;
			emptyViewParams.setMargins(0,0,0, (int)Utils.dp2px(5f,this));
			LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
			footerParams.weight = 0.25f;
			View emptyView = getLayoutInflater().inflate(R.layout.search_empty_view, null);
			emptyView.setLayoutParams(emptyViewParams);
			footerView.setLayoutParams(footerParams);
			mView.addView(emptyView);
			mView.addView(footerView);
			lv_locality_list.setEmptyView(mView);
		}else{
			lv_locality_list.addFooterView(footerView);
		}
		Button getAlerts = (Button) footerView.findViewById(R.id.btnGetAlerts);
		getAlerts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAlerts();
			}
		});
	}
	private void setSubLocationAdapter(String location){
		subLocationsListAdapter = new SubLocationsListAdapter(ctx, subLocationList,location);
		lv_locality_list.setAdapter(subLocationsListAdapter);
		View footerView = getLayoutInflater().inflate(R.layout.row_alert_footer, null);
		if(subLocationList == null || subLocationList.size() == 0){
			LinearLayout mView = (LinearLayout)findViewById(R.id.empty_view);
			LinearLayout.LayoutParams emptyViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
			emptyViewParams.weight = 0.75f;
			emptyViewParams.setMargins(0,0,0, (int)Utils.dp2px(5f,this));
			LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
			footerParams.weight = 0.25f;
			View emptyView = getLayoutInflater().inflate(R.layout.search_empty_view, null);
			emptyView.setLayoutParams(emptyViewParams);
			footerView.setLayoutParams(footerParams);
			mView.addView(emptyView);
			mView.addView(footerView);
			lv_locality_list.setEmptyView(mView);
		}else{
			lv_locality_list.addFooterView(footerView);
		}
		Button getAlerts = (Button) footerView.findViewById(R.id.btnGetAlerts);
		getAlerts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAlerts();
			}
		});
	}

	/*private void submitAlert() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new AsyncListner() {
			BaseVO baseVo;
			@Override
			public void OnBackgroundTaskCompleted() {
				if (baseVo == null) {
					app.showSnackBar(ctx, "Something went wrong. Try again", SnackBar.MED_SNACK);
				} else {
					if (baseVo.isSuccess()) {
						// app.showSnackBar(getActivity(),
						// baseVo.getMessage(), SnackBar.MED_SNACK);
						showSuccessDialog();
					} else {
						app.showSnackBar(ctx, baseVo.getMessage(), SnackBar.MED_SNACK);
					}
				}

				// btnGet.setVisibility(View.VISIBLE);
				// btnGet.setEnabled(true);
				// pb.setVisibility(View.GONE);
			}

			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					HashMap<String,String> params = new HashMap<>();
					params.put("email", app.getFromPrefs(BMHConstants.USER_EMAIL));
					params.put("name", app.getFromPrefs(BMHConstants.USERNAME_KEY));
					params.put("contactno", "1234567890");
					params.put("search", "");
					params.put("device_type", "android");
					params.put("device_id", app.getFromPrefs(BMHConstants.GCM_REG_ID));
					baseVo = model.submitAlert(params);
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnPreExec() {

			}
		});
		// loadingTask.dontShowProgressDialog();
		loadingTask.execute("");
	}*/

	private void showSuccessDialog() {
		LayoutInflater factory = LayoutInflater.from(ctx);
		final View dialogView = factory.inflate(R.layout.alert_registration, null);
		final AlertDialog dialog = new AlertDialog.Builder(ctx).create();

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getAlerts() {
		LayoutInflater factory = LayoutInflater.from(ctx);
		final AlertDialog dialog = new AlertDialog.Builder(ctx).create();
		final View dialogView = factory.inflate(R.layout.save_alert, null);
		final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
		final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
		final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
		Button btnSend = (Button) dialogView.findViewById(R.id.btnSend);
		ImageButton img_close = (ImageButton) dialogView.findViewById(R.id.img_close);
		TextView tvTermsofuse = (TextView) dialogView.findViewById(R.id.terms_conition);
		if(app.getFromPrefs(BMHConstants.USERNAME_KEY) != null)
			edUserName.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
		if(app.getFromPrefs(BMHConstants.USER_EMAIL) != null)
			edUserEmail.setText(app.getFromPrefs(BMHConstants.USER_EMAIL));

		edUserName.setTypeface(regularTypeface);
		edUserEmail.setTypeface(regularTypeface);
		edUserPhone.setTypeface(regularTypeface);
		btnSend.setTypeface(regularTypeface);
		OnClickListener dialogViewsClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()){
					case R.id.btnSend:
						if(isValidAlertData(dialogView)){
							getAlertParams = getAlertParams(edUserName.getText().toString(),edUserPhone.getText().toString(),edUserEmail.getText().toString());
							if(getAlertParams == null || getAlertParams.isEmpty())return;
							if(ConnectivityReceiver.isConnected()){
								sendAlertRequest(getAlertParams);
							}else{
								mNetworkErrorObject = Utils.showNetworkErrorDialog(LocationListActivity.this, UIEventType.RETRY_ALERT,
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												if(ConnectivityReceiver.isConnected()){
													sendAlertRequest(getAlertParams);
													mNetworkErrorObject.getAlertDialog().dismiss();
													mNetworkErrorObject = null;
												}else{
													Utils.showToast(LocationListActivity.this,getString(R.string.check_your_internet_connection));
												}
											}
										});
							}
							dialog.dismiss();
						}
						break;
					case R.id.img_close:
						dialog.dismiss();
						break;
					case R.id.terms_conition:
						Intent i = new Intent(LocationListActivity.this, TermsWebActivity.class);
						i.putExtra("pageType", 0);
						startActivity(i);
						break;
				}
			}
		};
		img_close.setOnClickListener(dialogViewsClick);
		tvTermsofuse.setOnClickListener(dialogViewsClick);
		btnSend.setOnClickListener(dialogViewsClick);

		dialog.setView(dialogView);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.show();

	}

	private String getAlertParams(String name,String phone,String email){
		if(name == null || name.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty() )return "";
		StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append("name=");
		mStringBuilder.append(name);
		mStringBuilder.append("&contactno=");
		mStringBuilder.append(phone);
		mStringBuilder.append("&email=");
		mStringBuilder.append(email);
		if(app.getFromPrefs(BMHConstants.CITYID) != null && !app.getFromPrefs(BMHConstants.CITYID).isEmpty() ){
			mStringBuilder.append("&city_id=");
			mStringBuilder.append(app.getFromPrefs(BMHConstants.CITYID));
		}
		if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && !app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty() ){
			mStringBuilder.append("&user_id=");
			mStringBuilder.append(app.getFromPrefs(BMHConstants.USERID_KEY));
		}
		if(mStringBuilder != null && mIntentDataObject.getData() != null) {
			HashMap<String,String> map = mIntentDataObject.getData();
			if (map.get(ParamsConstants.P_TYPE) != null) {
				mStringBuilder.append("&" + ParamsConstants.P_TYPE + "=");
				mStringBuilder.append(map.get(ParamsConstants.P_TYPE));
			}if (map.get(ParamsConstants.TYPE) != null) {
				mStringBuilder.append("&" + ParamsConstants.TYPE + "=");
				mStringBuilder.append(map.get(ParamsConstants.TYPE));
			}if (map.get(ParamsConstants.LOCATION_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.LOCATION_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.LOCATION_ID));
			}if (map.get(ParamsConstants.SUB_LOCATION_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.SUB_LOCATION_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.SUB_LOCATION_ID));
			}if (map.get(ParamsConstants.BUILDER_ID) != null) {
				mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=");
				mStringBuilder.append(map.get(ParamsConstants.BUILDER_ID));
			}if (map.get(ParamsConstants.SPECIAL_CATEGORY) != null) {
				mStringBuilder.append("&category_type=");
				mStringBuilder.append(map.get(ParamsConstants.SPECIAL_CATEGORY));
			}
		}

		return mStringBuilder.toString();
	}
	private void sendAlertRequest(String params){
		if(params == null || params.isEmpty())return;
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.GET_ALERT);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_ALERT));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		mBean.setJson(params);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(LocationListActivity.this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}

	private boolean isValidAlertData(View dialogView){
		if(dialogView == null)return false;
		final EditText edUserName = (EditText) dialogView.findViewById(R.id.et_user_name);
		final EditText edUserEmail = (EditText) dialogView.findViewById(R.id.et_email);
		final EditText edUserPhone = (EditText) dialogView.findViewById(R.id.et_mobile);
		final CheckBox edCheck = (CheckBox) dialogView.findViewById(R.id.cb_tnc);
		if (edUserName.getText().toString().trim().isEmpty()) {
			showToast("Enter your name.");
			return false;
		} if (StringUtil.checkSpecialCharacter(edUserName.getText().toString().trim())) {
			showToast("Special character and digits are not allowed in Name.");
			app.shakeEdittext(edUserName);
			return false;
		}
		else if (edUserEmail.getText().toString().trim().isEmpty()) {
			showToast("Please enter Email.");
			app.shakeEdittext(edUserEmail);
			return false;
		}else if ( edUserEmail.getText().toString().trim().isEmpty() || !Utils.isEmailValid(edUserEmail.getText().toString().trim()) ) {
			showToast("Please enter a valid Email.");
			app.shakeEdittext(edUserEmail);
			return false;
		}
		else if (edUserPhone.getText().toString().trim().isEmpty()) {
			showToast("Please enter Mobile Number.");
			app.shakeEdittext(edUserPhone);
		} else if (edUserPhone.getText().toString().trim().length() < 10) {
			showToast("Please enter valid Mobile Number.");
			app.shakeEdittext(edUserPhone);
			return false;
		}else if (!checkMobileValidity(edUserPhone.getText().toString().trim().charAt(0))) {
			showToast("Please enter valid Mobile Number.");
			app.shakeEdittext(edUserPhone);
			return false;
		}
		else if (!edCheck.isChecked()) {
			showToast("Please agree T & C");
			return false;
		}
		return  true;
	}
	protected boolean checkMobileValidity(char firstCharacter) {
		boolean validNo = false;
		switch(firstCharacter){
			case '7':
				validNo = true;
				break;
			case '8':
				validNo = true;
				break;
			case '9':
				validNo = true;
				break;
		}
		return validNo;
	}


	private void sortLocationList(final int pos){
		if(locationsListAdapter == null || locationsListAdapter.arrLocation == null)return;
		Collections.sort(locationsListAdapter.arrLocation, new Comparator<LocationVO>() {
			@Override
			public int compare(LocationVO lhs, LocationVO rhs) {
				if(pos == 0){
					return Float.compare(Utils.toFloat(lhs.getAvgPsfLocation()),Utils.toFloat(rhs.getAvgPsfLocation()));
				}else if(pos == 1){
					return Float.compare(Utils.toFloat(rhs.getAvgPsfLocation()),Utils.toFloat(lhs.getAvgPsfLocation()));
				}else if(pos == 2){
					return Integer.compare(Utils.toInt(rhs.getAvg_rating()),Utils.toInt(lhs.getAvg_rating()));
				}else if(pos == 3){
					return Integer.compare(Utils.toInt(rhs.getInfra()),Utils.toInt(lhs.getInfra()));
				}else if(pos == 4){
					return Integer.compare(Utils.toInt(rhs.getNeeds()),Utils.toInt(lhs.getNeeds()));
				}else if(pos == 5){
					return Integer.compare(Utils.toInt(rhs.getLifeStyle()),Utils.toInt(lhs.getLifeStyle()));
				}else if(pos == 6){
					return Float.compare(Utils.toFloat(rhs.getReturnsval()),Utils.toFloat(lhs.getReturnsval()));
					//return rhs.getReturnsval().compareTo(lhs.getReturnsval());
				}
				return rhs.getName().compareTo(lhs.getName());
			}
		});
		locationsListAdapter.notifyDataSetChanged();
	}

	private void getBuilderData(){
		/**/
	}

	private String getRequestParams(){
		if(mIntentDataObject == null || mIntentDataObject.getData() == null)return "";
		HashMap<String ,String > mapParams =  mIntentDataObject.getData();
		if(mapParams == null)return "";
		StringBuilder mStringBuilder = new StringBuilder("");
		for (Map.Entry<String, String> entry : mIntentDataObject.getData().entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				mStringBuilder.append(entry.getKey());
				mStringBuilder.append("=");
				mStringBuilder.append(entry.getValue());
				mStringBuilder.append("&");
			}
		}
		return Utils.removeLastAndSign(mStringBuilder.toString())!= null ? Utils.removeLastAndSign(mStringBuilder.toString()):mStringBuilder.toString();
	}


	private void getLocationData() {
		//rl_progress.setVisibility(View.VISIBLE);
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.GET_LOCATIONS);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_LOCATIONS));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		mBean.setJson(getRequestParams());
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(LocationListActivity.this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
		//    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
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
			//rl_progress.setVisibility(View.GONE);
			if (msg.obj == null) {
				//showConfirmAlert("","Unable to connect Server.Please try later");
				// showToast("Server error");
				return false;
			} else {
				ReqRespBean mBean = (ReqRespBean) msg.obj;
				switch (mBean.getApiType()) {
					case GET_LOCATIONS:
						if(mIntentDataObject == null || mIntentDataObject.getData() == null)break;
						HashMap<String ,String > mapParams =  mIntentDataObject.getData();
						if(mapParams == null)break;
						if(mapParams.get(ParamsConstants.SUB_LOCATION_ID) != null){
							//Parse sector data
							SubLocationRespModel subLocationRespModel = (SubLocationRespModel) JsonParser.convertJsonToBean(APIType.GET_SUBLOCATIONS,mBean.getJson());
							if(subLocationRespModel != null && subLocationRespModel.getSector_lists() != null){
								subLocationList = subLocationRespModel.getSector_lists();
								CURRENT_LIST_COUNT = subLocationList.size();
							}else{
								if(ConnectivityReceiver.isConnected()){
									//getLocationData();
									//Do nothing because already make an request.
								}else{
									mNetworkErrorObject = Utils.showNetworkErrorDialog(LocationListActivity.this, UIEventType.RETRY_GET_LOCATIONS,
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													if(ConnectivityReceiver.isConnected()){
														getLocationData();
														mNetworkErrorObject.getAlertDialog().dismiss();
														mNetworkErrorObject = null;
													}else{
														Utils.showToast(LocationListActivity.this,getString(R.string.check_your_internet_connection));
													}
												}
											});
								}
							}
							invalidateOptionsMenu();
							setSubLocationAdapter(mapParams.get(ParamsConstants.SUB_LOCATION_NAME));
						}else {
							// Parse location data
							if (mBean != null && mBean.getJson() != null) {
								try {
									JSONObject jsonObj = new JSONObject(mBean.getJson());
									if (jsonObj != null) {
										LocationsVO locationsVo = new LocationsVO(jsonObj);
										if(locationsVo != null && locationsVo.getArrLocation() != null) {
											locationList = locationsVo.getArrLocation();
											CURRENT_LIST_COUNT = locationsVo.getArrLocation().size();
										}
										invalidateOptionsMenu();
										setLocationAdapter();
									}else{
										setLocationAdapter();
									}
								} catch (JSONException e) {
									e.printStackTrace();
									//TODO: empty View
									setLocationAdapter();
									//TODO: retry
									if(ConnectivityReceiver.isConnected()){
										//getLocationData();
										//Do nothing because already make an request.
									}else{
										mNetworkErrorObject = Utils.showNetworkErrorDialog(LocationListActivity.this, UIEventType.RETRY_GET_LOCATIONS,
												new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														if(ConnectivityReceiver.isConnected()){
															getLocationData();
															mNetworkErrorObject.getAlertDialog().dismiss();
															mNetworkErrorObject = null;
														}else{
															Utils.showToast(LocationListActivity.this,getString(R.string.check_your_internet_connection));
														}
													}
												});
									}
								}
							}else{
								if(ConnectivityReceiver.isConnected()){
									////Do nothing because already make an request.
									// getLocationData();
								}else{
									mNetworkErrorObject = Utils.showNetworkErrorDialog(LocationListActivity.this, UIEventType.RETRY_GET_LOCATIONS,
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													if(ConnectivityReceiver.isConnected()){
														getLocationData();
														mNetworkErrorObject.getAlertDialog().dismiss();
														mNetworkErrorObject = null;
													}else{
														Utils.showToast(LocationListActivity.this,getString(R.string.check_your_internet_connection));
													}
												}
											});
								}
							}
						}
						break;
					case GET_ALERT:
						BaseRespModel baseRespModel = (BaseRespModel)JsonParser.convertJsonToBean(APIType.GET_ALERT,mBean.getJson());
						if(baseRespModel != null){
							if(baseRespModel.isSuccess()){
								showToast(baseRespModel.getMessage());
								//TODO:
							}else{
								showToast(baseRespModel.getMessage());
							}
						}else{
							showToast(getString(R.string.unable_to_connect_server));
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
	public void onBackPressed() {
		//super.onBackPressed();
		if (filter_view.getVisibility()==View.VISIBLE) {
			filter_view.setVisibility(View.GONE);
		}else{
			super.onBackPressed();
		}

	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case RETRY_GET_LOCATIONS:
					getLocationData();
					break;
				case RETRY_ALERT:
					sendAlertRequest(getAlertParams);
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}
}
