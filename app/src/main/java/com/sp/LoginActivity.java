package com.sp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.iid.FirebaseInstanceId;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.LoginRespData;
import com.model.NetworkErrorObject;
import com.utils.BlurBuilder;
import com.utils.Utils;
import com.views.CustomProgressDialog;

public class LoginActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = LoginActivity.class.getSimpleName();
    private BMHApplication app;
    private EditText edEmail, etPassword;
    private Activity ctx = LoginActivity.this;
    private String classToStart;
    private final int SIGNUP_REQ_CODE = 134;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private boolean fromLogin = true;
    private static final int RC_SIGN_IN = 0;
    private final int LOGIN_REQ_CODE = 122;
    private RelativeLayout rlGLogin;
    public static final String LOGIN_INTENT = "LOGIN_INTENT";
    CheckBox edShow;
    private RelativeLayout loaderView;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayout llRegisterHere;
    private TextView tvForgot, tv_create_account;
    private RelativeLayout rlFbLogin;
    private Button buttonLogin;
    private NetworkErrorObject mNetworkErrorObject = null;
    private CustomProgressDialog progressDialog;
    private AsyncThread mAsyncThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (BMHApplication) getApplication();
        initViews();
        setListeners();
        setTypeface();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            app.saveIntoPrefs(ParamsConstants.FCM_TOKEN, refreshedToken);
            registerFCMToken(refreshedToken);


        }
        if (getIntent() != null && getIntent().getBooleanExtra(LOGIN_INTENT, true) != true) {
            fromLogin = getIntent().getBooleanExtra(LOGIN_INTENT, true);
        }
        classToStart = getIntent().getStringExtra("classToStart");
        if (classToStart == null) {
            classToStart = BMHConstants.HOME_ACTI;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        Log.d(TAG, "On GoogleApiClient onConnectionFailed()");
                        if (!result.hasResolution()) {
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), LoginActivity.this, 0).show();
                            return;
                        }
                        if (!mIntentInProgress) {
                            if (signedInUser) {
                                resolveSignInError(result);
                            }
                        }
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Log.d(TAG, "On GoogleApiClient onConnected()");
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.i(TAG, "On GoogleApiClient onConnectionSuspended()");
            }
        });

    }// End of Oncreate


    private void setTypeface() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        edEmail.setTypeface(font);
        etPassword.setTypeface(font);
    }

    private void initViews() {
        edEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        edShow = (CheckBox) findViewById(R.id.textWhach);
        edShow.setText("Show");
        llRegisterHere = (LinearLayout) findViewById(R.id.llRegisterHere);
        tvForgot = (TextView) findViewById(R.id.tvForgotPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        rlFbLogin = (RelativeLayout) findViewById(R.id.rlFbLogin);
        rlGLogin = (RelativeLayout) findViewById(R.id.rlGLogin);
        tv_create_account = (TextView) findViewById(R.id.tv_create_account);

		/*View includeView = findViewById(R.id.inclide_view);
		ImageView imageView = (ImageView) includeView.findViewById(R.id.img);
		loaderView = (RelativeLayout) findViewById(R.id.loader_view);
		imageView.setBackgroundResource(R.drawable.progress_loader);
		AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
		anim.start();*/
    }

    private void setListeners() {
        buttonLogin.setOnClickListener(mOnClickListener);
        rlFbLogin.setOnClickListener(mOnClickListener);
        rlGLogin.setOnClickListener(mOnClickListener);
        tvForgot.setOnClickListener(mOnClickListener);
        llRegisterHere.setOnClickListener(mOnClickListener);
        tv_create_account.setOnClickListener(mOnClickListener);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    userLoginResponseToServer();
                    return true;
                }
                return false;
            }
        });


        edShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edShow.setText("Hide");
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edShow.setText("Show");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        app = (BMHApplication) getApplication();
        BMHApplication.getInstance().setConnectivityListener(this);
    }


    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonLogin:
                    //tempLogin();
                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            //TODO: network call
                            userLoginResponseToServer();
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(LoginActivity.this, UIEventType.RETRY_LOGIN,
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                userLoginResponseToServer();
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(LoginActivity.this, getString(R.string.check_your_internet_connection));
                                            }
                                        }
                                    });
                        }
                    }
                    break;

                case R.id.tvForgotPassword:
                    Intent i = new Intent(ctx, ForgotPasswordActivity.class);
                    startActivity(i);
                    break;
                case R.id.llRegisterHere:
                case R.id.tv_create_account:
                    registerOnClick(v);
                    finish();
                    break;
            }
        }
    };

    public boolean isValidData() {
        String email = edEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (email.isEmpty()) {
            app.shakeEdittext(edEmail);
            showToast("Please enter user id.");
            edEmail.requestFocus();
            return false;
        } else if (TextUtils.isDigitsOnly(email)) {
            if (email.length() != 10) {
                showToast("Please enter valid mobile number");
                edEmail.setSelection(edEmail.getText().length());
                edEmail.requestFocus();
                return false;
            }
        } else if (!TextUtils.isDigitsOnly(email)) {
            if (!Utils.isEmailValid(email)) {
                app.shakeEdittext(edEmail);
                edEmail.setSelection(email.length());
                edEmail.requestFocus();
                showToast("Please enter a valid Email Id");
                return false;
            }
        } else if (password.isEmpty()) {
            app.shakeEdittext(etPassword);
            showToast("Please enter password.");
            etPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            app.shakeEdittext(etPassword);
            showToast("Minimum 6 digit length required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void registerOnClick(View v) {
        Intent signupIntent = new Intent(ctx, RegisterActivity.class);
        startActivityForResult(signupIntent, SIGNUP_REQ_CODE);
    }

    public static boolean checkSpecialCharacter(String name) {
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);
            if (!Character.isLetter(ch) && !(ch == ' ')) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

			/*case android.R.id.home:
				finish();
				break;*/

        }

        return super.onOptionsItemSelected(item);
    }
    // ================================= Facebook login

    public boolean getConnectivityStatus() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            Log.d(TAG, "handleSignInResult:opr" + result.isSuccess());
//            handleSignInResult(result);
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking sign in state...");
            progressDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    progressDialog.dismiss();
//                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected String setActionBarTitle() {
        return "Login";
    }

    ////facebook impleetation
    private void googlePlusLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

   /* private void getUserInfoFb() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
        loginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                progressDialog = progressDialog(LoginActivity.this);
                if (profile != null) {
					*//*String email = object.optString("email");
					String fname = object.optString("name");
					if(email.length() <=0 ) email = facebook_id + "@facebook.com";
					app.saveIntoPrefs(BMHConstants.THIRD_PARTY, "fb");
					app.saveIntoPrefs(BMHConstants.USER_ID_THIRD_PARTY, facebook_id);
					app.saveIntoPrefs(BMHConstants.USER_FNAME_THIRD_PARTY, profile.getName());
					app.saveIntoPrefs(BMHConstants.USER_EMIL_THIRD_PARTY, profile.get);*//*
                }


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        try {
                            if (response != null) {
//                                mEtEmail.setText(object.getString("email"));
//								mEtfirstName.setText(object.getString("first_name"));
//								mEtlastName.setText(object.getString("last_name"));
//								String userId = object.getString("id");
//								String imgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
//								profileImagePath = imgUrl;
//								if (imgUrl.trim() != null) {
//									Picasso.with(ProfileActivity.this).load(imgUrl).into(imgUserProfile);
//								} else {
//									imgUserProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile_pic));
//								}
                                Log.i(TAG, object.toString());
                                String fbId = object.optString("id");
                                String email = object.optString("email");
                                String fname = object.optString("first_name");
                                String last_name = object.optString("last_name");
                                if (email.length() <= 0) email = fbId + "@facebook.com";
                                //app.saveIntoPrefs(BMHConstants.THIRD_PARTY, "fb");
                                app.saveIntoPrefs(BMHConstants.USER_ID_THIRD_PARTY, fbId);
                                app.saveIntoPrefs(BMHConstants.USER_FNAME_THIRD_PARTY, fname + " " + last_name);
                                app.saveIntoPrefs(BMHConstants.USER_EMIL_THIRD_PARTY, email);
                                // Login through FB from BMH server.
                                sendFbLoginResponseToServer();
                            }
                        } catch (Exception e) {
                            if (progressDialog != null) progressDialog.dismiss();
                            Log.e(TAG, "getUserInfoFb():" + e);
                            showToast(getString(R.string.something_went_wrong));

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (progressDialog != null) progressDialog.dismiss();
                System.out.println("onCancel");
            }


            @Override
            public void onError(FacebookException error) {
                if (progressDialog != null) progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login failed. Please try later", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result != null && result.isSuccess() && result.getSignInAccount() != null) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //app.saveIntoPrefs(BMHConstants.THIRD_PARTY, "gplus");
            app.saveIntoPrefs(BMHConstants.USER_ID_THIRD_PARTY, acct.getId());
            app.saveIntoPrefs(BMHConstants.USER_FNAME_THIRD_PARTY, acct.getDisplayName());
            app.saveIntoPrefs(BMHConstants.USER_EMIL_THIRD_PARTY, acct.getEmail());
            sendGoogleLoginResponseToServer();
            //TODO: G+ Login
        } else {
            showToast(getString(R.string.something_went_wrong));
        }
    }

    private void sendGoogleLoginResponseToServer() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_THIRD_PARTY_LOGIN);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_THIRD_PARTY_LOGIN));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String id = app.getFromPrefs(BMHConstants.USER_ID_THIRD_PARTY);
        String email = app.getFromPrefs(BMHConstants.USER_EMIL_THIRD_PARTY);
        String username = app.getFromPrefs(BMHConstants.USER_FNAME_THIRD_PARTY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("email=" + email);
        mStringBuilder.append("&username=" + username);
        mStringBuilder.append("&fb_id=" + username);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(LoginActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else if (requestCode == RESULT_CANCELED) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                //String error = result.getStatus().getStatusMessage();
                showToast("Login Canceled");
            }
            mIntentInProgress = false;
        } else if (requestCode == LOGIN_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (fromLogin) {
                    Intent intent = new Intent(ctx, ProjectsListActivity.class);
                    startActivity(intent);
                    ctx.finish();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            } else if (requestCode == RESULT_CANCELED) {
                //
            }
        } else if (requestCode == SIGNUP_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Intent loginIntent = new Intent(ctx, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.putExtra("email", data.getStringExtra("email"));
                startActivityForResult(loginIntent, LOGIN_REQ_CODE);
            } else if (requestCode == RESULT_CANCELED) {
                //
            }
        } else {
            if (data != null) {
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            showToast("Network available");
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_LOGIN:
                    userLoginResponseToServer();
                    break;
                case RETRY_FB_LOGIN:
                    //getUserInfoFb();
                    break;
                case RETRY_GPLUS_LOGIN:
                    googlePlusLogin();
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        } else {
            showToast("Network not available");
        }
    }


    private CustomProgressDialog progressDialog(Context currentContext) {
        CustomProgressDialog mProgressDialog = new CustomProgressDialog(currentContext, R.style.progress_dialog_theme);
        mProgressDialog.setCancelable(true);
        Bitmap map = BitmapFactory.decodeResource(currentContext.getResources(), R.drawable.progress_bg);
        Bitmap fast = BlurBuilder.fastblur(map, 0.2f, 90);
        final Drawable draw = new BitmapDrawable(currentContext.getResources(), fast);
        Window window = mProgressDialog.getWindow();
        window.setBackgroundDrawable(draw);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        mProgressDialog.show();
        return mProgressDialog;
    }


    private void sendFbLoginResponseToServer() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_THIRD_PARTY_LOGIN);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_THIRD_PARTY_LOGIN));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String id = app.getFromPrefs(BMHConstants.USER_ID_THIRD_PARTY);
        String email = app.getFromPrefs(BMHConstants.USER_EMIL_THIRD_PARTY);
        String username = app.getFromPrefs(BMHConstants.USER_FNAME_THIRD_PARTY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("email=" + email);
        mStringBuilder.append("&username=" + username);
        mStringBuilder.append("&fb_id=" + username);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        //mAsyncThread.initProgressDialog(LoginActivity.this,mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }

    private void userLoginResponseToServer() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.USER_LOGIN);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.USER_LOGIN));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("username=" + edEmail.getText().toString().trim());
        mStringBuilder.append("&password=" + etPassword.getText().toString().trim());
        mStringBuilder.append("&builder_id=" + BMHConstants.CURRENT_BUILDER_ID);
        mStringBuilder.append("&deviceId=" + Utils.getDeviceId(this));
        mStringBuilder.append("&" + ParamsConstants.USER_TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.SALES_PERSON);
        mStringBuilder.append("&deviceToken=" + FirebaseInstanceId.getInstance().getToken());
        mStringBuilder.append("&deviceType=" + "android");
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(LoginActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }


    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //rl_progress.setVisibility(View.GONE);
            //if(progressDialog != null)progressDialog.dismiss();
            if (msg.obj == null) {
                //showConfirmAlert("","Unable to connect Server.Please try later");
                // showToast("Server error");
                if (progressDialog != null) progressDialog.dismiss();
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case GET_THIRD_PARTY_LOGIN:
                    case USER_LOGIN:
                        LoginRespData basevo = (LoginRespData) JsonParser.convertJsonToBean(APIType.USER_LOGIN, mBean.getJson());
                        if (basevo != null) {
                            if (basevo.isSuccess() && basevo.getData() != null) {
                                saveUserInfo(basevo);
                                showToast(getString(R.string.successfully_logedin));
                                if (fromLogin) {
                                    Utils.createFileDirectory(LoginActivity.this);
                                    Intent intent = new Intent(LoginActivity.this, ProjectsListActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                   /* MySyncAdapter mySyncAdapter = new MySyncAdapter(getApplicationContext(), true);
                                    mySyncAdapter.initializeSyncAdapter();*/
                                    //if(progressDialog != null)progressDialog.dismiss();

                                } else {
                                    Intent returnIntent = new Intent();
                                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    setResult(RESULT_OK, returnIntent);
                                    //if(progressDialog != null)progressDialog.dismiss();
                                }
                                finish();
                            } else if (!basevo.isSuccess()) {
                                showToast(basevo.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                            if (progressDialog != null) progressDialog.dismiss();
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


    private void saveUserInfo(LoginRespData basevo) {
        if (basevo == null) return;
        if (basevo.getData() != null) {
            app.saveIntoPrefs(BMHConstants.USERID_KEY, basevo.getData().getUser_id());
            app.saveIntoPrefs(BMHConstants.USER_DESIGNATION, basevo.getData().getDesignation());
            app.saveIntoPrefs(BMHConstants.USERNAME_KEY, basevo.getData().getFirst_name() + " " + basevo.getData().getLast_name());
            app.saveIntoPrefs(BMHConstants.SP_CODE, basevo.getData().getEmp_code());
            app.saveIntoPrefs(BMHConstants.USER_IMAGE_URL, basevo.getData().getUser_image());
            app.saveIntoPrefs(BMHConstants.USER_EMAIL, basevo.getData().getEmail());
            app.saveIntoPrefs(BMHConstants.MOBILE_KEY, basevo.getData().getPhone_number());
            app.saveIntoPrefs(BMHConstants.IS_PRE_SALES, basevo.getData().getPreSales());
        }
    }

    private void registerFCMToken(String fcmToken) {
        Log.d(TAG, "registerFCMToken(): " + fcmToken);
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.REGISTER_FCM_TOKEN);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.REGISTER_FCM_TOKEN));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        String userId = app.getFromPrefs(BMHConstants.USERID_KEY);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(userId);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.DEVICE_TYPE);
        mStringBuilder.append("=android&");
        mStringBuilder.append(ParamsConstants.DEVICE_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(Utils.getDeviceId(this));
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.DEVICE_TOKEN);
        mStringBuilder.append("=");
        mStringBuilder.append(fcmToken);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.APP_TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.SALES_PERSON);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        //   mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }
}
