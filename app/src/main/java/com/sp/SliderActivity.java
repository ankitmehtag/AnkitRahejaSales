package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.UIEventType;
import com.adapters.SplashFragmentsAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.model.NetworkErrorObject;
import com.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;

public class SliderActivity extends FragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

	public static final int TERM_AND_CONDITION_PAGE = 0;
	public static final int PRIVACY_PAGE = 1;
	private ImageView indicator1, indicator2, indicator3, indicator4, indicator5;
	protected int _splashTime = 5000;
	private Button btnSignIn, btnRegister, btnSkip;
	private BMHApplication app;
	private Activity ctx = this;
	// private String classToStart;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean signedInUser;
	private ConnectionResult mConnectionResult;
	// private SignInButton signinButton;
	private static final int RC_SIGN_IN = 0;
	private RelativeLayout rlGLogin;
	private final int LOGIN_REQ_CODE = 122;
	private final int SIGNUP_REQ_CODE = 133;
	private boolean fromSplash = false;

	int noofsize = 3;
	ViewPager myPager = null;
	//private ViewPager pager;
	int count = 0;
	Timer timer;

	private RelativeLayout loaderView;
	private ImageView iv_indi;
	private NetworkErrorObject mNetworkErrorObject = null;
	private Typeface tf;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		String fontPath = "fonts/regular.ttf";
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		setContentView(R.layout.activity_slider);
		iv_indi = (ImageView)findViewById(R.id.iv_indi);
		myPager = (ViewPager) findViewById(R.id.pagerDeveloper);
		TextView tvTermsOfUse = (TextView) findViewById(R.id.tvTermsOfUse);
		TextView tvPrivacy = (TextView) findViewById(R.id.tvPrivacyPolicy);
		TextView tv_view = (TextView) findViewById(R.id.view);
		TextView tv_our = (TextView) findViewById(R.id.aur);
		rlGLogin = (RelativeLayout) findViewById(R.id.rlGLogin);
		loaderView = (RelativeLayout) findViewById(R.id.loader_view);
		indicator1 = (ImageView) findViewById(R.id.indicator1);
		indicator2 = (ImageView) findViewById(R.id.indicator2);
		indicator3 = (ImageView) findViewById(R.id.indicator3);
		// indicator4 = (ImageView) findViewById(R.id.indicator4);
		// indicator5 = (ImageView) findViewById(R.id.indicator5);
		btnSignIn = (Button) findViewById(R.id.signIn);
		btnRegister = (Button) findViewById(R.id.register);
		btnSkip = (Button) findViewById(R.id.skip);

		// -------------Add loader
		View includeView = findViewById(R.id.inclide_view);
		ImageView imageView = (ImageView) includeView.findViewById(R.id.img);
		imageView.setBackgroundResource(R.drawable.progress_loader);
		AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
		anim.start();


		tvPrivacy.setOnClickListener(mOnClickListener);
		tvTermsOfUse.setOnClickListener(mOnClickListener);
		RelativeLayout rlFbLogin = (RelativeLayout) findViewById(R.id.rlFbLogin);
		rlFbLogin.setOnClickListener(mOnClickListener);
		rlGLogin.setOnClickListener(mOnClickListener);
		btnSkip.setOnClickListener(mOnClickListener);
		btnSignIn.setOnClickListener(mOnClickListener);
		btnRegister.setOnClickListener(mOnClickListener);

		app = (BMHApplication) getApplication();
		fromSplash = getIntent().getBooleanExtra("fromSplash", false);


		// Font path
		// ------------------------------------------------------------------------
		SplashFragmentsAdapter adapter = new SplashFragmentsAdapter(getSupportFragmentManager(), this);

		myPager.setAdapter(adapter);


		myPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				changeIndicator(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});



		/*mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		// if (!mGoogleApiClient.isConnected()) {
		mGoogleApiClient.connect();*/
		// }

		// _________________________________ Moving pager images______________________________

		// ViewPagerAdapter adapter = new ViewPagerAdapter(SliderActivity.this,
		// noofsize);

		myPager.setAdapter(adapter);
		myPager.setCurrentItem(0);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (count <= 2) {
							myPager.setCurrentItem(count);
							count++;
						} else {
							count = 0;
							myPager.setCurrentItem(count);
						}
					}
				});
			}
		}, 200, 5000);

		// ____________________________________________________________________________________________
// Loading Font Face
		// Applying font
		tvPrivacy.setTypeface(tf);
		btnSignIn.setTypeface(tf);
		btnRegister.setTypeface(tf);
		btnSkip.setTypeface(tf);
		tv_view.setTypeface(tf);
		tv_our.setTypeface(tf);
		tvTermsOfUse.setTypeface(tf);
		/*if (app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
			showDisclaimerDialog();
		}*/
	}


	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.signIn:
					// Intent i = new
					// Intent(SliderActivity.this,LoginActivity.class);
					// i.putExtra("classToStart", BMHConstants.HOME_ACTI);
					// startActivity(i);
					// SliderActivity.this.finish();
					final Intent loginIntent = new Intent(ctx, LoginActivity.class);
					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						startActivityForResult(loginIntent, LOGIN_REQ_CODE);
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(SliderActivity.this, UIEventType.RETRY_LOGIN,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											startActivityForResult(loginIntent, LOGIN_REQ_CODE);
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.register:
					Intent signupIntent = new Intent(ctx, RegisterActivity.class);
					startActivityForResult(signupIntent, SIGNUP_REQ_CODE);

					// Intent i = new
					// Intent(SliderActivity.this,RegisterActivity.class);
					// i.putExtra("fromslider", true);
					// startActivity(i);
					// SliderActivity.this.finish();
					break;
				case R.id.skip:
					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						launchNewActivity();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(SliderActivity.this, UIEventType.RETRY_HOME_PAGE,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											launchNewActivity();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.rlGLogin:
					if (getConnectivityStatus()) {
						// classToStart = BMHConstants.HOME_ACTI;

						googlePlusLogin();
					} else {
						showToast("No Network");
					}
					break;
				case R.id.rlFbLogin:
					if (getConnectivityStatus()) {
						// classToStart = BMHConstants.HOME_ACTI;
						//app.LoginWithFacebook(SliderActivity.this, SearchPropertyActivity.class, SliderActivity.this);
					} else {
						showToast("No Network");
					}
					break;
				case R.id.tvPrivacyPolicy:

					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
						i.putExtra("pageType", PRIVACY_PAGE);
						startActivity(i);
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(SliderActivity.this, UIEventType.RETRY_PRIVACY,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
											i.putExtra("pageType", PRIVACY_PAGE);
											startActivity(i);
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;
				case R.id.tvTermsOfUse:
					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
						i.putExtra("pageType", TERM_AND_CONDITION_PAGE);
						startActivity(i);
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(SliderActivity.this, UIEventType.RETRY_TNC,
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
											i.putExtra("pageType", TERM_AND_CONDITION_PAGE);
											startActivity(i);
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
					break;


			}
		}
	};

	private void launchNewActivity() {
		finish();
		// startActivity(new
		// Intent(SliderActivity.this,NewLaunchesActivity.class));
		// SliderActivity.this.finish();
		Intent intent = new Intent(SliderActivity.this, ProjectsListActivity.class);
		startActivity(intent);
		SliderActivity.this.finish();
	}

	private void changeIndicator(int pos) {
		switch (pos) {
			case 0:
				iv_indi.setImageResource(R.drawable.search_selected);
				/*indicator1.setImageResource(R.drawable.indicator_selected);
				indicator2.setImageResource(R.drawable.indicator_unselected);
				indicator3.setImageResource(R.drawable.indicator_unselected);*/
				// indicator4.setImageResource(R.drawable.indicator_unselected);
				// indicator5.setImageResource(R.drawable.indicator_unselected);
				break;
			case 1:
				iv_indi.setImageResource(R.drawable.venue_selected);
				/*indicator1.setImageResource(R.drawable.indicator_unselected);
				indicator2.setImageResource(R.drawable.indicator_selected);
				indicator3.setImageResource(R.drawable.indicator_unselected);*/
				// indicator4.setImageResource(R.drawable.indicator_unselected);
				// indicator5.setImageResource(R.drawable.indicator_unselected);
				break;
			case 2:
				iv_indi.setImageResource(R.drawable.booking_selected);
				/*indicator1.setImageResource(R.drawable.indicator_unselected);
				indicator2.setImageResource(R.drawable.indicator_unselected);
				indicator3.setImageResource(R.drawable.indicator_selected);*/
				// indicator4.setImageResource(R.drawable.indicator_unselected);
				// indicator5.setImageResource(R.drawable.indicator_unselected);
				break;
			// case 3:
			// indicator1.setImageResource(R.drawable.indicator_unselected);
			// indicator2.setImageResource(R.drawable.indicator_unselected);
			// indicator3.setImageResource(R.drawable.indicator_unselected);
			// indicator4.setImageResource(R.drawable.indicator_selected);
			// indicator5.setImageResource(R.drawable.indicator_unselected);
			// break;
			// case 4:
			// indicator1.setImageResource(R.drawable.indicator_unselected);
			// indicator2.setImageResource(R.drawable.indicator_unselected);
			// indicator3.setImageResource(R.drawable.indicator_unselected);
			// indicator4.setImageResource(R.drawable.indicator_unselected);
			// indicator5.setImageResource(R.drawable.indicator_selected);
			// break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				signedInUser = false;
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			}
			mIntentInProgress = false;
		} else if (requestCode == LOGIN_REQ_CODE) {
			if (resultCode == RESULT_OK) {
				if (fromSplash) {
					Intent intent = new Intent(ctx, ProjectsListActivity.class);
					startActivity(intent);
					ctx.finish();
				} else {
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}
		} else if (requestCode == SIGNUP_REQ_CODE) {
			if (resultCode == RESULT_OK) {
				Intent loginIntent = new Intent(ctx, LoginActivity.class);
				loginIntent.putExtra("email", data.getStringExtra("email"));
				startActivityForResult(loginIntent, LOGIN_REQ_CODE);
			}
		} else {
			/*if (data != null) {
				Session.getActiveSession().onActivityResult(SliderActivity.this, requestCode, resultCode, data);
				if (app.mCurrentSession.isOpened()) {
					// showLoading(false);
					Request.newMeRequest(app.mCurrentSession, new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							app.getFbUserInfo(user);
						}

					}).executeAsync();

				}
				// else if(!app.mCurrentSession.isOpened()){
				// if(app.mProgressDialog!=null){
				// app.mProgressDialog.dismiss();
				// }
				// }
			}*/
		}

	}

	public boolean getConnectivityStatus() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()) {
				return true;
			} else {
				return false;
			}
		else
			return false;
	}

	protected void onStart() {
		super.onStart();
		// if (!mGoogleApiClient.isConnected()) {
		// mGoogleApiClient.connect();
		// }

	}

	protected void onStop() {
		super.onStop();
		// if (mGoogleApiClient.isConnected()) {
		// mGoogleApiClient.disconnect();
		// }
	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}


	private void getProfileInformation() {
		/*try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				System.out.println("personName " + personName);

				app.saveIntoPrefs(BMHConstants.THIRD_PARTY, "gplus");
				app.saveIntoPrefs(BMHConstants.USER_ID_THIRD_PARTY, currentPerson.getId());
				app.saveIntoPrefs(BMHConstants.USER_FNAME_THIRD_PARTY, currentPerson.getDisplayName());
				app.saveIntoPrefs(BMHConstants.USER_EMIL_THIRD_PARTY, email);

				startFbLoginTask();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}


	public void signIn(View v) {
		googlePlusLogin();
	}

	private void googlePlusLogin() {
		if (!mGoogleApiClient.isConnecting()) {
			signedInUser = true;
			resolveSignInError();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
	}
	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case RETRY_HOME_PAGE:
					launchNewActivity();
					break;
				case RETRY_LOGIN:
					Intent loginIntent = new Intent(ctx, LoginActivity.class);
					startActivityForResult(loginIntent, LOGIN_REQ_CODE);
					break;
				case RETRY_PRIVACY:
					Intent mIntentPrivacy = new Intent(SliderActivity.this, TermsWebActivity.class);
					mIntentPrivacy.putExtra("pageType", PRIVACY_PAGE);
					startActivity(mIntentPrivacy);
					break;
				case RETRY_TNC:
					Intent mIntentTNC = new Intent(SliderActivity.this, TermsWebActivity.class);
					mIntentTNC.putExtra("pageType", TERM_AND_CONDITION_PAGE);
					startActivity(mIntentTNC);
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}

	private void showDisclaimerDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		final View dialogView = factory.inflate(R.layout.disclaimer_alert, null);
		final TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
		final TextView tv_disclaimer = (TextView) dialogView.findViewById(R.id.tv_disclaimer);

		SpannableString disclaimer_part_1 = new SpannableString(getString(R.string.disclaimer_part_1));
		SpannableString terms_of_services = new SpannableString(getString(R.string.terms_of_services));
		SpannableString disclaimer_part_2 = new SpannableString(getString(R.string.disclaimer_part_2));
		SpannableString privacy = new SpannableString(getString(R.string.privacy));
		SpannableString disclaimer_part_3 = new SpannableString(getString(R.string.disclaimer_part_3));

		ClickableSpan mClickableSpanTermsOfServices = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				if(ConnectivityReceiver.isConnected()){
					Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
					i.putExtra("pageType", TERM_AND_CONDITION_PAGE);
					startActivity(i);
				}else{
					Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
				}
			}
		};
		ClickableSpan mClickableSpanPrivacy = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				if(ConnectivityReceiver.isConnected()){
					Intent i = new Intent(SliderActivity.this, TermsWebActivity.class);
					i.putExtra("pageType", PRIVACY_PAGE);
					startActivity(i);
				}else{
					Utils.showToast(SliderActivity.this,getString(R.string.check_your_internet_connection));
				}
			}
		};
		disclaimer_part_1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, disclaimer_part_1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_disclaimer.setText(disclaimer_part_1);
		tv_disclaimer.append(" ");

		terms_of_services.setSpan(mClickableSpanTermsOfServices, 0, terms_of_services.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_disclaimer.append(terms_of_services);
		tv_disclaimer.append(" ");

		disclaimer_part_2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, disclaimer_part_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_disclaimer.append(disclaimer_part_2);
		tv_disclaimer.append(" ");

		privacy.setSpan(mClickableSpanPrivacy, 0, privacy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_disclaimer.append(privacy);
		tv_disclaimer.append(" ");

		disclaimer_part_3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, disclaimer_part_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_disclaimer.append(disclaimer_part_3);

		tv_disclaimer.setMovementMethod(LinkMovementMethod.getInstance());

		Button btn_i_agree = (Button) dialogView.findViewById(R.id.btn_i_agree);
		tv_title.setTypeface(tf);
		tv_disclaimer.setTypeface(tf);
		btn_i_agree.setTypeface(tf);
		View.OnClickListener dialogViewsClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()){
					case R.id.btn_i_agree:
						dialog.dismiss();
						break;
				}
			}
		};
		btn_i_agree.setOnClickListener(dialogViewsClick);
		dialog.setView(dialogView);
		dialog.setCancelable(false);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.show();

	}
    protected void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}
