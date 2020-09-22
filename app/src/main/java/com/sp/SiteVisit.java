package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.PageVO;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.model.SiteVisitTimeRespModel;
import com.sitevisit.GetAddressTask;
import com.utils.StringUtil;
import com.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class SiteVisit extends BaseFragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,ConnectivityReceiver.ConnectivityReceiverListener{

	public static final String TAG = SiteVisit.class.getSimpleName();
	private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1000;
	private final int REQUEST_CODE_GOOGLE_PLAY_SERVICES = 1001;
	private static final int ACCESS_FINE_LOCATION = 1002;
	private GoogleMap googleMap;
	private String projId, projName;
	private TextView tv_current_location;
	private Activity ctx = SiteVisit.this;
	private TextView tv_date_value;
	private Button btn_date,btn_time;
	final int DATE_DIALOG = 0;
	private int mSelectedDay,mSelectedMonth,mSelectedYear;
	private int mSelectedHour,mSelectedMinutes;
	private Button btn1y, btn2y, btn3y, btn4y, btn5y, btn6y, btn7y;
	private CheckBox termsCondition;
	private EditText editTextMail, editName, editPhone, editLocation;
	private TextView  tv_time_value, editProjectName;
	private Button btnSend;
	private String noOfPerson = "";
	String locationCordinates = "";
	private BMHApplication app;
	private PageVO basevo;
	private int type;
	private Toolbar toolbar;
	private TextView text_person,tv_tvLabelName,tv_date_title,tv_time_title,tv_mobile,terms_conition,myLatitude,myLongitude;
	private GoogleApiClient googleApiClient = null;
	private float ZOOM_LEVEL = 17.0f;
	private  AsyncThread mAsyncThread = null;
	private GetAddressTask mGetAddressTask = null;
	private ImageView iv_clear_address;
	protected Location mCurrentLocation;

	private Calendar OPENING_CALENDER = Calendar.getInstance(TimeZone.getDefault().getTimeZone("IST"));
	private Calendar CLOSING_CALENDER = Calendar.getInstance(TimeZone.getDefault().getTimeZone("IST"));
	private long DELTA_TIME = 90*60*1000; //  90 min (In millisecond )
	private Calendar SELECTED_CALENDER;
	private Calendar CURRENT_CALENDER;

	private DateFormat df_ddMMyyyy = null;
	private DateFormat df_ddMMyyyy_HHmm = null;
	private DateFormat df_HHmm_a = null;
	private NetworkErrorObject mNetworkErrorObject = null;
	private DatePickerDialog datePicker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_site_visit);
		app = (BMHApplication) getApplication();
		projId = getIntent().getStringExtra("projectId");
		projName = getIntent().getStringExtra("pro_name");
		df_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
		df_ddMMyyyy_HHmm = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		df_HHmm_a = new SimpleDateFormat("hh:mm aa");
		//currentDate = parseDate(df_ddMMyyyy.format(new Date()));
		//currentTime = parseDate(df_ddMMyyyy_HHmm.format(Calendar.getInstance().getTime()));
		initViews();
		setListeners();
		setTypeface();
		if (isGooglePlayServicesAvailable()) {
			turnGPSOn();
		}else{
			//TODO: error.
		}
		if(ConnectivityReceiver.isConnected()){
			//TODO: network call
			getSiteVisitTime();
		}else{
			mNetworkErrorObject = Utils.showNetworkErrorDialog(SiteVisit.this, UIEventType.SITE_VISIT_TIME,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ConnectivityReceiver.isConnected()){
								//TODO: network call
								getSiteVisitTime();
								mNetworkErrorObject.getAlertDialog().dismiss();
								mNetworkErrorObject = null;
							}else{
								Utils.showToast(SiteVisit.this,getString(R.string.check_your_internet_connection));
							}
						}
					});
		}
		/*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		if (location != null) {
			mLocationListener.onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(bestProvider, 20000, 0, mLocationListener);*/

		editProjectName.setText(Html.fromHtml(projName));
		if(app.getFromPrefs(BMHConstants.USERNAME_KEY) != null)
			editName.setText(app.getFromPrefs(BMHConstants.USERNAME_KEY));
		if(app.getFromPrefs(BMHConstants.USER_EMAIL) != null)
			editTextMail.setText(app.getFromPrefs(BMHConstants.USER_EMAIL));

		/*tv_date_value.setText(getSelectedDate());
		tv_time_value.setText("HH:MM");*/
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		showIndiaLocation();
		googleMap.setOnMarkerClickListener(markerClickListener);
		googleMap.setOnMapClickListener(mapClickListener);
		googleMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
			return;
		}
		googleMap.setMyLocationEnabled(true);
	}

	GoogleMap.OnMyLocationButtonClickListener myLocationButtonClickListener  = new GoogleMap.OnMyLocationButtonClickListener() {
		@Override
		public boolean onMyLocationButtonClick() {
			setCurrentLocation();
			return true;
		}
	};
	private void showIndiaLocation(){
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BMHConstants.INDIA_LAT,BMHConstants.INDIA_LNG), 10f));
	}
	private void initViews() {
		toolbar = setToolBar();
		toolbar.setTitle(getString(R.string.book_site_visit));
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapId);
		fm.getMapAsync(this);
		terms_conition = (TextView) findViewById(R.id.terms_conition);
		tv_date_value = (TextView) findViewById(R.id.tv_date_value);
		btn_date = (Button) findViewById(R.id.btn_date);
		tv_time_value = (TextView) findViewById(R.id.tv_time_value);
		btn_time = (Button)findViewById(R.id.btn_time);
		tv_tvLabelName = (TextView) findViewById(R.id.tvLabelName);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		text_person = (TextView) findViewById(R.id.text_person);
		tv_date_title = (TextView) findViewById(R.id.tv_date_title);
		tv_time_title = (TextView) findViewById(R.id.tv_time_title);
		termsCondition = (CheckBox) findViewById(R.id.cb_tnc);
		editTextMail = (EditText) findViewById(R.id.et_email);
		editProjectName = (TextView) findViewById(R.id.tv_project_name);
		editPhone = (EditText) findViewById(R.id.et_mobile);
		editName = (EditText) findViewById(R.id.et_user_name);
		editLocation = (EditText) findViewById(R.id.et_enter_location);
		myLatitude = (TextView) findViewById(R.id.mylatitude);
		myLongitude = (TextView) findViewById(R.id.mylongitude);
		btnSend = (Button) findViewById(R.id.btnSend);
		tv_current_location = (TextView) findViewById(R.id.tv_current_location);

		btn1y = (Button) findViewById(R.id.btn_1y);
		btn2y = (Button) findViewById(R.id.btn_2y);
		btn3y = (Button) findViewById(R.id.btn_3y);
		btn4y = (Button) findViewById(R.id.btn_4y);
		btn5y = (Button) findViewById(R.id.btn_5y);
		btn6y = (Button) findViewById(R.id.btn_6y);
		btn7y = (Button) findViewById(R.id.btn_7y);
		iv_clear_address = (ImageView)findViewById(R.id.iv_clear_address);
	}

	private void setListeners(){
		btnSend.setOnClickListener(mOnClickListener);
		btn1y.setOnClickListener(mOnClickListener);
		btn2y.setOnClickListener(mOnClickListener);
		btn3y.setOnClickListener(mOnClickListener);
		btn4y.setOnClickListener(mOnClickListener);
		btn5y.setOnClickListener(mOnClickListener);
		btn6y.setOnClickListener(mOnClickListener);
		btn7y.setOnClickListener(mOnClickListener);
		btn_date.setOnClickListener(mOnClickListener);
		tv_date_value.setOnClickListener(mOnClickListener);
		terms_conition.setOnClickListener(mOnClickListener);
		tv_time_value.setOnClickListener(mOnClickListener);
		btn_time.setOnClickListener(mOnClickListener);
		editLocation.setOnClickListener(mOnClickListener);
		tv_current_location.setOnClickListener(mOnClickListener);
		iv_clear_address.setOnClickListener(mOnClickListener);

		editLocation.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0){
					iv_clear_address.setVisibility(View.VISIBLE);
					tv_current_location.setTextColor(Color.parseColor("#999999"));
				}else{
					tv_current_location.setTextColor(Color.parseColor("#000000"));
					iv_clear_address.setVisibility(View.GONE);
				}
			}
		});

	}
	private void setTypeface(){
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
		editTextMail.setTypeface(typeface);
		editProjectName.setTypeface(typeface);
		editPhone.setTypeface(typeface);
		editName.setTypeface(typeface);
		editLocation.setTypeface(typeface);
		tv_time_value.setTypeface(typeface);
		btnSend.setTypeface(typeface);
		tv_current_location.setTypeface(typeface);

		btn1y.setTypeface(typeface);
		btn2y.setTypeface(typeface);
		btn3y.setTypeface(typeface);
		btn4y.setTypeface(typeface);
		btn5y.setTypeface(typeface);
		btn6y.setTypeface(typeface);
		btn7y.setTypeface(typeface);
		tv_mobile.setTypeface(typeface);
		tv_tvLabelName.setTypeface(typeface);
		text_person.setTypeface(typeface);
		tv_date_title.setTypeface(typeface);
		tv_time_title.setTypeface(typeface);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker != null) {
				//TODO:
			}
			return true;
		}
	};

	OnMapClickListener mapClickListener = new OnMapClickListener() {
		@Override
		public void onMapClick(LatLng position) {
			//myLatitude.setVisibility(View.VISIBLE);
			//myLongitude.setVisibility(View.VISIBLE);
			//myLatitude.setText("Latitude: " + String.valueOf(position.getLatitude()));
			//myLongitude.setText("Longitude: " + String.valueOf(position.getLongitude()));
			addMarkerOnMap(position);
			getGeoCoding(position);
		}
	};

	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btn_time:
				case R.id.tv_time_value:
					showTimePickerDialog(mSelectedHour, mSelectedMinutes, false, mOnTimeSetListener);
					break;
				case R.id.btn_date:
				case R.id.tv_date_value:
					showDialog(DATE_DIALOG);
					break;
				case R.id.btnSend:
					Utils.hideKeyboard(SiteVisit.this);
					if(isValidData()) {
						if(ConnectivityReceiver.isConnected()){
							//TODO: network call
							sendSiteVisit();
						}else{
							mNetworkErrorObject = Utils.showNetworkErrorDialog(SiteVisit.this, UIEventType.RETRY_SITEVISIT,
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											if(ConnectivityReceiver.isConnected()){
												//TODO: network call
												sendSiteVisit();
												mNetworkErrorObject.getAlertDialog().dismiss();
												mNetworkErrorObject = null;
											}else{
												Utils.showToast(SiteVisit.this,getString(R.string.check_your_internet_connection));
											}
										}
									});
						}
					}
					break;
				case R.id.btn_1y:
				case R.id.btn_2y:
				case R.id.btn_3y:
				case R.id.btn_4y:
				case R.id.btn_5y:
				case R.id.btn_6y:
				case R.id.btn_7y:
					setPersonState(view);
					if(view instanceof Button){
						Button btn = (Button)view;
						noOfPerson = btn.getText().toString();
					}
					break;
				case R.id.terms_conition:
					Intent i = new Intent(SiteVisit.this, TermsWebActivity.class);
					i.putExtra("pageType", 0);
					startActivity(i);
					break;
				case R.id.et_enter_location:
					callPlaceAutocompleteActivityIntent();
					break;
				case R.id.tv_current_location:
					if(editLocation.getText().toString().trim().isEmpty()){
						if (isGooglePlayServicesAvailable()) {
							turnGPSOn();
						}else{
							//TODO: error.
						}
					}
					break;
				case R.id.iv_clear_address:
					editLocation.setText("");
					tv_current_location.setTextColor(Color.parseColor("#000000"));
					break;
			}

		}
	};



	private void setPersonState(View view){
		btn1y.setSelected(false);
		btn2y.setSelected(false);
		btn3y.setSelected(false);
		btn4y.setSelected(false);
		btn5y.setSelected(false);
		btn6y.setSelected(false);
		btn7y.setSelected(false);
		if(view != null) view.setSelected(true);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
			case DATE_DIALOG:
				return datePicker;
			case 2:
				return null;
		}
		return null;
	}

	private void initDatePicker(Calendar c){
		datePicker = new DatePickerDialog(this, onDateSet, mSelectedYear, mSelectedMonth, mSelectedDay);
		datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
	}
	private OnDateSetListener onDateSet = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mSelectedDay = dayOfMonth;
			mSelectedMonth = monthOfYear;
			mSelectedYear = year;
			if(SELECTED_CALENDER != null){
				SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);
				if (compareDates(SELECTED_CALENDER.getTime(),OPENING_CALENDER.getTime()) > 0) {
					//Future
					mSelectedHour = OPENING_CALENDER.getTime().getHours();
					mSelectedMinutes = OPENING_CALENDER.getTime().getMinutes();
					SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);
					tv_time_value.setText(df_HHmm_a.format(SELECTED_CALENDER.getTime()));
					tv_date_value.setText(df_ddMMyyyy.format(SELECTED_CALENDER.getTime()));

				}else if (compareDates(SELECTED_CALENDER.getTime(),OPENING_CALENDER.getTime()) < 0) {
					//showToast("Past Date");
					//showToast("Site visit can't be in past time.");
					msgDialog("Site visit can't be in past time.");

				}else{
					//Today
					Calendar mCal = (Calendar) CURRENT_CALENDER.clone();
					mCal.add(Calendar.MILLISECOND,(int)DELTA_TIME);
					mSelectedHour = mCal.getTime().getHours();
					mSelectedMinutes = mCal.getTime().getMinutes();
					SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);
					tv_time_value.setText(df_HHmm_a.format(SELECTED_CALENDER.getTime()));
					tv_date_value.setText(df_ddMMyyyy.format(SELECTED_CALENDER.getTime()));
				}
			}else{
				showToast("Error");
			}
		}

	};

	private String getSelectedDate() {
		String day = (mSelectedDay > 9) ? "" + mSelectedDay : "0" + mSelectedDay;
		String month = ((mSelectedMonth + 1) > 9) ? "" + (mSelectedMonth + 1) : "0" + (mSelectedMonth + 1);
		return day + "-" + month + "-" + mSelectedYear;
	}
	private OnTimeSetListener mOnTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mSelectedHour = hourOfDay;
			mSelectedMinutes = minute;
			if(SELECTED_CALENDER != null){
				SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);
				isValidSiteVisitTime();
			}else{
				showToast("Error");
			}
		}
	};

	public static int compareDates(Date date1, Date date2) {
		SimpleDateFormat math = new SimpleDateFormat("ddMMyyyy"); // I'm working with servlets, so keeping it thread-safe
		Long date1asLong = new Long(math.format(date1));
		Long date2asLong = new Long(math.format(date2));
		return date1asLong.compareTo(date2asLong);
	}

	public static int compareDatesWithTime(Date date1, Date date2) {
		SimpleDateFormat math = new SimpleDateFormat("ddMMyyyy HHmm"); // I'm working with servlets, so keeping it thread-safe
		return math.format(date1).compareTo(math.format(date2));
	}
	private boolean isValidSiteVisitTime(){

		if (compareDates(SELECTED_CALENDER.getTime(),OPENING_CALENDER.getTime()) > 0) {
			//showToast("Future Date");
			Calendar officeOpeningDateFuture = Calendar.getInstance();
			officeOpeningDateFuture.set(SELECTED_CALENDER.get(Calendar.YEAR),SELECTED_CALENDER.get(Calendar.MONTH), SELECTED_CALENDER.get(Calendar.DAY_OF_MONTH),OPENING_CALENDER.getTime().getHours(),OPENING_CALENDER.getTime().getMinutes());

			Calendar officeClosingDateFuture = Calendar.getInstance();
			officeClosingDateFuture.set(SELECTED_CALENDER.get(Calendar.YEAR),SELECTED_CALENDER.get(Calendar.MONTH), SELECTED_CALENDER.get(Calendar.DAY_OF_MONTH),CLOSING_CALENDER.getTime().getHours(),CLOSING_CALENDER.getTime().getMinutes());

			if(compareDatesWithTime(SELECTED_CALENDER.getTime(),officeOpeningDateFuture.getTime()) < 0 ||compareDatesWithTime(SELECTED_CALENDER.getTime(),(officeClosingDateFuture.getTime())) > 0){
				//showToast("Site visit is available in between " + df_HHmm_a.format(OPENING_CALENDER.getTime()) + " to " + df_HHmm_a.format(CLOSING_CALENDER.getTime()));
				msgDialog("Site Visit is available in between " + df_HHmm_a.format(OPENING_CALENDER.getTime()) + " to " + df_HHmm_a.format(CLOSING_CALENDER.getTime())+ " and after "+ DELTA_TIME/(60*1000)+" Min. from current time.");
			}else{
				tv_time_value.setText(df_HHmm_a.format(SELECTED_CALENDER.getTime()));
			}
		} else if (compareDates(SELECTED_CALENDER.getTime(),OPENING_CALENDER.getTime()) < 0) {
			//showToast("Past Date");
			//showToast("Site visit can't be in past time.");
			msgDialog("Site visit can't be in past time.");

		} else {
			//showToast("Today");
			if(compareDatesWithTime(SELECTED_CALENDER.getTime(),OPENING_CALENDER.getTime()) < 0 ||compareDatesWithTime(SELECTED_CALENDER.getTime(),CLOSING_CALENDER.getTime()) > 0){
				//showToast("Site visit is available in between " + df_HHmm_a.format(OPENING_CALENDER.getTime()) + " to " + df_HHmm_a.format(CLOSING_CALENDER.getTime()));
				msgDialog("Site Visit is available in between " + df_HHmm_a.format(OPENING_CALENDER.getTime()) + " to " + df_HHmm_a.format(CLOSING_CALENDER.getTime())+ " and after "+ DELTA_TIME/(60*1000)+" Min. from current time.");
			}else{
				if(getDateDiff(CURRENT_CALENDER,SELECTED_CALENDER) >= DELTA_TIME/(60*1000)){
					tv_time_value.setText(df_HHmm_a.format(SELECTED_CALENDER.getTime()));
				}else{
					//tv_time_value.setText("HH:MM");
					msgDialog("Site Visit is available in between " + df_HHmm_a.format(OPENING_CALENDER.getTime()) + " to " + df_HHmm_a.format(CLOSING_CALENDER.getTime()) + " and after "+ DELTA_TIME/(60*1000)+" Min. from current time.");

					//showToast("Your Site visit time must be greater than " + DELTA_TIME/(60*1000)+" Min. from current time.");
					//msgDialog("Your Site visit time must be greater than " + DELTA_TIME/(60*1000)+" Min. from current time.");
					return false;
				}
			}
		}
		return true;
	}
	public static long getDateDiff(Calendar date1, Calendar date2) {
		SimpleDateFormat math = new SimpleDateFormat("ddMMyyyy HHmm"); // I'm working with servlets, so keeping it thread-safe
		Calendar c1 = Calendar.getInstance();
		c1.set(date1.get(Calendar.YEAR),date1.get(Calendar.MONTH),date1.get(Calendar.DAY_OF_MONTH),date1.getTime().getHours(),date1.getTime().getMinutes());

		Calendar c2 = Calendar.getInstance();
		c2.set(date2.get(Calendar.YEAR),date2.get(Calendar.MONTH),date2.get(Calendar.DAY_OF_MONTH),date2.getTime().getHours(),date2.getTime().getMinutes());
		long diff = (c2.getTime()).getTime() - (c1.getTime()).getTime();
		long diffMinutes = diff / (60 * 1000);
		return diffMinutes;
	}
	private TimePickerDialog showTimePickerDialog(int initialHour, int initialMinutes, boolean is12Hour, OnTimeSetListener listener) {
		TimePickerDialog dialog = new TimePickerDialog(this, listener, initialHour, initialMinutes, is12Hour);
		dialog.show();
		return dialog;
	}

	private void addMarkerOnMap(LatLng position){
		if(googleMap != null && position != null) {
			googleMap.clear();
			MarkerOptions markerOption = new MarkerOptions().position(position);
			googleMap.addMarker(markerOption);
			markerOption.position(position);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_LEVEL));
		}
	}

	protected LocationRequest mLocationRequest;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	private boolean isCurrentLoc = false;

	protected void startLocationUpdates() {
		if (googleApiClient == null || !googleApiClient.isConnected()) return;
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(SiteVisit.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, mLocationListener);
	}
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,mLocationListener);
	}

	LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			/*final double latti = location.getLatitude();
			final double longi = location.getLongitude();
			locationCordinates = latti + "," + longi;
			LatLng position = new LatLng(latti, longi);
			addMarkerOnMap(position);
			getGeoCoding(position);*/
			if(!isCurrentLoc) {
				LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
				addMarkerOnMap(position);
				getGeoCoding(position);
				isCurrentLoc = true;
				stopLocationUpdates();
			}
		}
	};


	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	@Override
	protected String setActionBarTitle() {
		// TODO Auto-generated method stub
		return "";
	}

	private boolean isValidData(){
		if (editName.getText().toString().trim().isEmpty()) {
			showToast("Fill the name");
			editName.requestFocus();
			app.shakeEdittext(editName);
			return false;
		} else if (StringUtil.checkSpecialCharacter(editName.getText().toString().trim())) {
			showToast("Special character and digits are not allowed in Name.");
			app.shakeEdittext(editName);
			editName.requestFocus();
			return false;
		} else if (editTextMail.getText().toString().trim().isEmpty()) {
			showToast("Enter email Id");
			editTextMail.requestFocus();
			app.shakeEdittext(editTextMail);
			return false;
		} else if (!Utils.isEmailValid(editTextMail.getText().toString().trim())) {
			showToast("Enter valid email Id");
			editTextMail.requestFocus();
			app.shakeEdittext(editTextMail);
			return false;
		} else if (editPhone.getText().toString().trim().isEmpty()) {
			showToast("Please enter Mobile Number.");
			editPhone.requestFocus();
			app.shakeEdittext(editPhone);
			return false;

		} else if (editPhone.getText().toString().trim().length() < 10) {
			showToast("Please enter valid Mobile Number.");
			editPhone.requestFocus();
			app.shakeEdittext(editPhone);
			return false;
		}else if (!Utils.isValidMobileNumber(editPhone.getText().toString().trim())) {
			showToast("Please enter valid Mobile Number.");
			editPhone.requestFocus();
			app.shakeEdittext(editPhone);
			return false;
		}else if (noOfPerson == null || noOfPerson.length() == 0) {
			showToast("Select No. of person for the site visit");
			return false;
		}else if (noOfPerson == null || noOfPerson.length() == 0) {
			showToast("Select No. of person for the site visit");
			return false;
		} else if (editLocation.getText().toString().trim().isEmpty() && tv_current_location.getText().toString().trim().isEmpty()) {
			showToast("Search Location");
			editLocation.requestFocus();
			app.shakeEdittext(editLocation);
			return false;
		}else if (tv_date_value.getText().toString().isEmpty()) {
			showToast("Select the date");
			return false;
		} else if (tv_time_value.getText().toString().isEmpty() || tv_time_value.getText().toString().equalsIgnoreCase("HH:MM")) {
			showToast("Select the time");
			return false;
		}  else if(!isValidSiteVisitTime()){
			return false;
		}else if (!termsCondition.isChecked()) {
			showToast("Agree to our terms & conditions");
			return false;
		}
		return true;
	}


	private void turnGPSOn() {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(SiteVisit.this).addOnConnectionFailedListener(this).build();
			googleApiClient.connect();
		}else{
			if(googleApiClient.isConnected()){
				processGPSTurnOnProcess();
			}else{
				googleApiClient.connect();
			}
		}
	}


	private void processGPSTurnOnProcess(){
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(30 * 1000);
		locationRequest.setFastestInterval(5 * 1000);
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
		// **************************
		builder.setAlwaysShow(true); // this is the key ingredient
		// **************************

		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				final LocationSettingsStates state = result.getLocationSettingsStates();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						Log.i(TAG, "SUCCESS:");
						if(googleMap != null) {
							googleMap.clear();
							setCurrentLocation();
						}
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						Log.i(TAG, "onResult():LocationSettingsStatusCodes.RESOLUTION_REQUIRED:");
						try {
							status.startResolutionForResult(SiteVisit.this, LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
						} catch (IntentSender.SendIntentException e) {
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						Log.i(TAG, "SETTINGS_CHANGE_UNAVAILABLE:");
				}
			}
		});
	}

	private void setCurrentLocation(){
		if(googleApiClient != null) {
			Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
			if (currentLocation != null) {
				//myLatitude.setVisibility(View.VISIBLE);
				//myLongitude.setVisibility(View.VISIBLE);
				//myLatitude.setText("Latitude: " + String.valueOf(currentLocation.getLatitude()));
				//myLongitude.setText("Longitude: " + String.valueOf(currentLocation.getLongitude()));
				LatLng position = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
				addMarkerOnMap(position);
				getGeoCoding(position);
			}/*else if(getGPSLocation() != null){
				Location latsLocation = getGPSLocation();
				LatLng position = new LatLng(latsLocation.getLatitude(), latsLocation.getLongitude());
				addMarkerOnMap(position);
				getGeoCoding(position);

			}*/
		}
	}

	public Location getGPSLocation() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lastKnownLocationGPS != null) {
				return lastKnownLocationGPS;
			} else {
				return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} else {
			return null;
		}
	}
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		processGPSTurnOnProcess();
		//if (googleApiClient != null) googleApiClient.connect();


	}
	@Override
	public void onConnectionSuspended(int i) {
	}
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		int connectionStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (connectionStatus == ConnectionResult.SUCCESS) {
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
			}
		} else {
			Log.e(TAG, GooglePlayServicesUtil.getErrorString(connectionStatus));
			GooglePlayServicesUtil.getErrorDialog(connectionStatus, this, REQUEST_CODE_GOOGLE_PLAY_SERVICES).show();
		}
	}

	private String getParams(){
		String tempLocation = editLocation.getText().toString().trim();
		if (tempLocation.isEmpty()) {
			tempLocation = tv_current_location.getText().toString();
		}
		StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append("project_id=");
		mStringBuilder.append(projId);
		mStringBuilder.append("&name=");
		mStringBuilder.append(editName.getText().toString().trim());
		mStringBuilder.append("&contact_no=");
		mStringBuilder.append(editPhone.getText().toString().trim());
		String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
		if(userId != null && userId.length() > 0) {
			mStringBuilder.append("&user_id=");
			mStringBuilder.append(userId);
		}
		mStringBuilder.append("&no_of_persons=");
		mStringBuilder.append(noOfPerson);
		mStringBuilder.append("&location=");
		mStringBuilder.append(tempLocation);
		mStringBuilder.append("&coordinates=");
		mStringBuilder.append(locationCordinates);
		mStringBuilder.append("&time=");
		mStringBuilder.append(tv_time_value.getText().toString().trim());
		mStringBuilder.append("&date=");
		mStringBuilder.append(tv_date_value.getText().toString().trim());
		mStringBuilder.append("&email=");
		mStringBuilder.append( editTextMail.getText().toString().trim());
		mStringBuilder.append("&project_name=");
		mStringBuilder.append(editProjectName.getText().toString().trim());
		return mStringBuilder.toString();
	}

	private void resetUiData(){
		editName.setText("");
		editTextMail.setText("");
		editPhone.setText("");
		noOfPerson = "";
		setPersonState(null);
		setCurrentLocation();
	}
	private void sendSiteVisit() {
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.SITE_VISIT);
		mBean.setRequestmethod(WEBAPI.POST);
		String params = getParams();
		if(params != null || params.length() > 0) mBean.setJson(params);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.SITE_VISIT));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(SiteVisit.this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}
	private void getSiteVisitTime() {
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.GET_SITEVISIT_TIME);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_SITEVISIT_TIME));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(SiteVisit.this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}

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
					case SITE_VISIT:
						BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.SITE_VISIT,mBean.getJson());
						if(baseRespModel != null){
							if(baseRespModel.isSuccess()){
								showToast(baseRespModel.getMessage());
								resetUiData();
								SiteVisit.this.finish();
							}else{
								msgDialog(baseRespModel.getMessage());
								getSiteVisitTime();
							}
						}else{
							showToast(getString(R.string.unable_to_connect_server));
						}
						break;
					case GEOCODING:
						if(mBean.getJson() != null && !mBean.getJson().isEmpty()){
							tv_current_location.setText(mBean.getJson());
						}else{
							tv_current_location.setText("");
						}
						break;
					case GET_SITEVISIT_TIME:
						SiteVisitTimeRespModel siteVisitTimeRespModel = (SiteVisitTimeRespModel) JsonParser.convertJsonToBean(APIType.GET_SITEVISIT_TIME,mBean.getJson());
						if(siteVisitTimeRespModel != null) {
							setDefaultTime(siteVisitTimeRespModel);
							//TODO:
							setDefaultDateAndTime();
						}else{
							if(ConnectivityReceiver.isConnected()){
								//TODO: network call
								//Do nothing
							}else{
								mNetworkErrorObject = Utils.showNetworkErrorDialog(SiteVisit.this, UIEventType.SITE_VISIT_TIME,
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												if(ConnectivityReceiver.isConnected()){
													//TODO: network call
													getSiteVisitTime();
													mNetworkErrorObject.getAlertDialog().dismiss();
													mNetworkErrorObject = null;
												}else{
													Utils.showToast(SiteVisit.this,getString(R.string.check_your_internet_connection));
												}
											}
										});
							}
						}
						break;
					default:
						break;
				}
			}
			return true;
		}
	});

	private void getGeoCoding(LatLng position){
		if(position == null)return;
		ReqRespBean mBean = new ReqRespBean();
		HashMap<String,String> locationMap = new HashMap<>();
		locationMap.put(ParamsConstants.LAT,String.valueOf(position.latitude));
		locationMap.put(ParamsConstants.LNG,String.valueOf(position.longitude));
		mBean.setHeader(locationMap);
		mBean.setApiType(APIType.GEOCODING);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mGetAddressTask = new GetAddressTask(this);
		//mGetAddressTask.initProgressDialog(SiteVisit.this,mOnCancelListener);
		mGetAddressTask.execute(mBean);
		mGetAddressTask = null;

	}

	private void callPlaceAutocompleteActivityIntent() {
		try {
			Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
			startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
		} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
			// TODO: Handle the error.
			showToast(e.getMessage());
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case ACCESS_FINE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
					if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
						return;
					}
					if (googleMap != null) googleMap.setMyLocationEnabled(true);
					processGPSTurnOnProcess();
				} else {
					//TODO:
				}
				break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//autocompleteFragment.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = PlaceAutocomplete.getPlace(this, data);
				//myLatitude.setVisibility(View.VISIBLE);
				//myLongitude.setVisibility(View.VISIBLE);
				//myLatitude.setText("Latitude: "+String.valueOf(place.getLatLng().latitude));
				//myLongitude.setText("Longitude: "+String.valueOf(place.getLatLng().longitude));
				editLocation.setText(place.getAddress().toString());
				Log.i(TAG, "Place:" + place.toString());
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(this, data);
				Log.i(TAG, status.getStatusMessage());
			} else if (requestCode == RESULT_CANCELED) {
				//TODO: error
			}
		}else if (requestCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
			if (resultCode == RESULT_OK) {
				showIndiaLocation();
				/*if(googleApiClient != null){
					googleApiClient.connect();
					*//*if (googleApiClient.isConnected() || googleApiClient.isConnecting()){
						googleApiClient.disconnect();
						googleApiClient.connect();
					} else if (!googleApiClient.isConnected()){
						googleApiClient.connect();
					}*//*
				}*/
				startLocationUpdates();
			}else{
				//TODO:
				showIndiaLocation();
			}
		}else if (requestCode == REQUEST_CODE_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK) {
				int connectionResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
				if (connectionResult == ConnectionResult.SUCCESS) {
					processGPSTurnOnProcess();
				} else {
					Log.e(TAG, GooglePlayServicesUtil.getErrorString(connectionResult));
					showToast("Error: "+GooglePlayServicesUtil.getErrorString(connectionResult));
					//GooglePlayServicesUtil.getErrorDialog(connectionResult, this, REQUEST_CODE_GOOGLE_PLAY_SERVICES).show();
				}
			} else {
			}

		}
	}


	DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			if(mAsyncThread != null)mAsyncThread.cancel(true);
			if(mGetAddressTask != null)mGetAddressTask.cancel(true);
			mAsyncThread = null;
			mGetAddressTask = null;
		}
	};


	@Override
	protected void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
		if(googleApiClient != null && !googleApiClient.isConnected())googleApiClient.connect();
	}

	@Override
	public void onBackPressed() {
		if(googleApiClient != null && googleApiClient.isConnected())googleApiClient.disconnect();
		this.finish();
	}

	private Date parseDate(String date) {
		try {
			return df_ddMMyyyy.parse(date);
		} catch (java.text.ParseException e) {
			return new Date(0);
		}
	}

	private Date parseTime(String time) {
		try {
			return df_ddMMyyyy_HHmm.parse(time);
		} catch (java.text.ParseException e) {
			return new Date(0);
		}
	}

	private String _12HrFormateTime(int hrs,int min){
		String _24HourTime = hrs+":"+min;
		try {
			SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
			SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
			Date _24HourDt = _24HourSDF.parse(_24HourTime);
			return _12HourSDF.format(_24HourDt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _24HourTime;
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case SITE_VISIT_TIME:
					getSiteVisitTime();
					break;
				case RETRY_SITEVISIT:
					sendSiteVisit();
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}

	private void setDefaultTime(SiteVisitTimeRespModel siteVisitTimeRespModel){
		if(siteVisitTimeRespModel == null)return;

		DELTA_TIME = siteVisitTimeRespModel.getDifference();

		OPENING_CALENDER = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		OPENING_CALENDER.setTimeInMillis(siteVisitTimeRespModel.getOffice_start_time());

		CLOSING_CALENDER = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		CLOSING_CALENDER.setTimeInMillis(siteVisitTimeRespModel.getOffice_End_time());

		CURRENT_CALENDER = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		CURRENT_CALENDER.setTimeInMillis(siteVisitTimeRespModel.getCurrent_time());
	}

	private void setDefaultDateAndTime(){
		if(CURRENT_CALENDER == null || OPENING_CALENDER == null || CLOSING_CALENDER == null ){
			return;
		}
		Calendar upperBoundCal = (Calendar) OPENING_CALENDER.clone();
		upperBoundCal.add(Calendar.MILLISECOND, ((int)DELTA_TIME)*(-1));

		Calendar lowerBoundCal = (Calendar) CLOSING_CALENDER.clone();
		lowerBoundCal.add(Calendar.MILLISECOND, ((int)DELTA_TIME)*(-1));

		int diffUpperBound = CURRENT_CALENDER.compareTo(upperBoundCal);
		int diffLowerBound = CURRENT_CALENDER.compareTo(lowerBoundCal);

		if(diffUpperBound == -1 && diffLowerBound == -1){
			tv_date_value.setText(df_ddMMyyyy.format(OPENING_CALENDER.getTime()));
			tv_time_value.setText(df_HHmm_a.format(OPENING_CALENDER.getTime()));
			mSelectedDay = OPENING_CALENDER.get(Calendar.DAY_OF_MONTH);
			mSelectedMonth = OPENING_CALENDER.get(Calendar.MONTH);
			mSelectedYear = OPENING_CALENDER.get(Calendar.YEAR);
			mSelectedHour = OPENING_CALENDER.getTime().getHours();
			mSelectedMinutes = OPENING_CALENDER.getTime().getMinutes();
			initDatePicker(OPENING_CALENDER);
			SELECTED_CALENDER = Calendar.getInstance();
			SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);

		}else if(diffUpperBound == 0 && diffLowerBound == -1){
			tv_date_value.setText(df_ddMMyyyy.format(OPENING_CALENDER.getTime()));
			Calendar mCal = (Calendar) CURRENT_CALENDER.clone();
			mCal.add(Calendar.MILLISECOND,(int)DELTA_TIME);
			tv_time_value.setText(df_HHmm_a.format(mCal.getTime()));

			mSelectedDay = OPENING_CALENDER.get(Calendar.DAY_OF_MONTH);
			mSelectedMonth = OPENING_CALENDER.get(Calendar.MONTH);
			mSelectedYear = OPENING_CALENDER.get(Calendar.YEAR);

			mSelectedHour = mCal.getTime().getHours();
			mSelectedMinutes = mCal.getTime().getMinutes();
			initDatePicker(mCal);

			SELECTED_CALENDER = Calendar.getInstance();
			SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);

		}else if(diffUpperBound == 1 && diffLowerBound == -1){
			tv_date_value.setText(df_ddMMyyyy.format(OPENING_CALENDER.getTime()));
			Calendar mCal = (Calendar) CURRENT_CALENDER.clone();
			mCal.add(Calendar.MILLISECOND,(int)DELTA_TIME);
			tv_time_value.setText(df_HHmm_a.format(mCal.getTime()));

			mSelectedDay = OPENING_CALENDER.get(Calendar.DAY_OF_MONTH );
			mSelectedMonth = OPENING_CALENDER.get(Calendar.MONTH );
			mSelectedYear = OPENING_CALENDER.get(Calendar.YEAR);

			mSelectedHour = mCal.getTime().getHours();
			mSelectedMinutes = mCal.getTime().getMinutes();

			initDatePicker(mCal);

			SELECTED_CALENDER = Calendar.getInstance();
			SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);

		}else if(diffUpperBound == -1 && diffLowerBound == 0){
			Calendar mCal = (Calendar) OPENING_CALENDER.clone();
			mCal.add(Calendar.HOUR,24);
			tv_date_value.setText(df_ddMMyyyy.format(mCal.getTime()));
			tv_time_value.setText(df_HHmm_a.format(mCal.getTime()));

			mSelectedDay = mCal.get(Calendar.DAY_OF_MONTH);
			mSelectedMonth = mCal.get(Calendar.MONTH);
			mSelectedYear = mCal.get(Calendar.YEAR);

			mSelectedHour = mCal.getTime().getHours();
			mSelectedMinutes = mCal.getTime().getMinutes();
			initDatePicker(mCal);

			SELECTED_CALENDER = Calendar.getInstance();
			SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);

		}else if(diffUpperBound == 1 && diffLowerBound == 1){
			Calendar mCal = (Calendar) OPENING_CALENDER.clone();
			mCal.add(Calendar.HOUR,24);
			tv_date_value.setText(df_ddMMyyyy.format(mCal.getTime()));
			tv_time_value.setText(df_HHmm_a.format(mCal.getTime()));

			mSelectedDay = mCal.get(Calendar.DAY_OF_MONTH);
			mSelectedMonth = mCal.get(Calendar.MONTH);
			mSelectedYear = mCal.get(Calendar.YEAR);

			mSelectedHour = mCal.getTime().getHours();
			mSelectedMinutes = mCal.getTime().getMinutes();

			initDatePicker(mCal);

			SELECTED_CALENDER = Calendar.getInstance();
			SELECTED_CALENDER.set(mSelectedYear,mSelectedMonth, mSelectedDay,mSelectedHour,mSelectedMinutes);
		}

	}

	private void msgDialog(String msg){
		/*String text;
		text = "<html><body style=\"text-align:justify;color:gray;background-color:black;\">";
		text+= msg;
		text+= "</p></body></html>";*/

		AlertDialog alertDialog = new AlertDialog.Builder(SiteVisit.this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setCancelable(false);
		alertDialog.setMessage(Html.fromHtml(msg));
		//TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
		//msgTxt.setTextSize(Utils.dp2px(20,this));
		//msgTxt.setText(Html.fromHtml(text));
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
}