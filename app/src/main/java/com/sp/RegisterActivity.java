package com.sp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.adapters.CustomSpinnerAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.NetworkErrorObject;
import com.model.RegisterUserRespModel;
import com.utils.StringUtil;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = RegisterActivity.class.getSimpleName();
    public static final int BROKER_FIRM = 0, EMPLOYEE_OF_BROKER_FIRM = 1, INDIVIDUAL = 2;
    private EditText edName, editTextBusiness, editTextState, editTextCity, edEmail, edMobileNo, editTextAadhar,
            edPassword, editTextReraRegistration, editTextGSTRegistration, editTextPan, editTextRMCode, editTextBrokerFirmCode;
    private BMHApplication app;
    private Activity ctx = RegisterActivity.this;
    CheckBox edShow;

    private final int SIGNUP_REQ_CODE = 134;
    private final int LOGIN_REQ_CODE = 122;
    private EditText VISIBLE;
    private NetworkErrorObject mNetworkErrorObject = null;
    private Button buttonRegister;
    private Spinner sp_user_type;
    private Toolbar toolbar;
    private TextView spanntextview, tv_login_here;
    private int CUR_USER_TYPE = -1;

    private AsyncThread mAsyncThread = null;
    private LinearLayout rl_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        app = (BMHApplication) getApplication();
        initViews();
        setToolBar();
        setListener();
        // ------------------------------------------------------------------------------------------------------
        SpannableString text = new SpannableString("By creating an account, you agree to our Terms and Conditions");
        ClickableSpan mClickableSpan = new ClickableSpan() {
            // spannabletextview.setTypeface(font);
            // spanntextview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    Intent i = new Intent(RegisterActivity.this, TermsWebActivity.class);
                    i.putExtra("pageType", 0);
                    startActivity(i);
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(RegisterActivity.this, UIEventType.RETRY_TNC,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        Intent i = new Intent(RegisterActivity.this, TermsWebActivity.class);
                                        i.putExtra("pageType", 0);
                                        startActivity(i);
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(RegisterActivity.this, getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                }
            }
        };
        text.setSpan(mClickableSpan, 41, 61, 0);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue_color)), 41, 61, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1f), 9, 14, 0);
        spanntextview.setMovementMethod(LinkMovementMethod.getInstance());
        spanntextview.setText(text, BufferType.SPANNABLE);

        buttonRegister.setOnClickListener(mOnClickListener);
        sp_user_type.setOnItemSelectedListener(mOnItemSelectedListener);

        String[] list = getResources().getStringArray(R.array.user_type);
        ArrayList<String> userType = new ArrayList<String>(Arrays.asList(list));

        CustomSpinnerAdapter adapterNoofParkings = new CustomSpinnerAdapter(this, R.layout.user_type_spinner, userType);
        sp_user_type.setAdapter(adapterNoofParkings);


    }

    private void setListener() {

        tv_login_here.setOnClickListener(mOnClickListener);
        edShow.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // show password
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    edShow.setText("Hide");
                } else {
                    // hide password
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edShow.setText("Show");
                }
            }
        });

    }

    private void initViews() {
        toolbar = setToolBar();
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spanntextview = (TextView) findViewById(R.id.by_cre);
        tv_login_here = (TextView) findViewById(R.id.tv_login_here);
        edName = (EditText) findViewById(R.id.editTextName);
        editTextBusiness = (EditText) findViewById(R.id.editTextBusiness);
        editTextState = (EditText) findViewById(R.id.editTextState);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        edEmail = (EditText) findViewById(R.id.editTextEmail);
        edMobileNo = (EditText) findViewById(R.id.editTextMobileno);

        editTextAadhar = (EditText) findViewById(R.id.editTextAadhar);
        edPassword = (EditText) findViewById(R.id.editTextPassword);
        edShow = (CheckBox) findViewById(R.id.textWhach);
        edShow.setText("Show");

        editTextReraRegistration = (EditText) findViewById(R.id.editTextReraRegistration);
        editTextGSTRegistration = (EditText) findViewById(R.id.editTextGSTRegistration);
        editTextPan = (EditText) findViewById(R.id.editTextPan);
        InputFilter[] filterArray = new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)};
        editTextPan.setFilters(filterArray);
        editTextRMCode = (EditText) findViewById(R.id.editTextRMCode);
        editTextBrokerFirmCode = (EditText) findViewById(R.id.editTextBrokerFirmCode);
        sp_user_type = (Spinner) findViewById(R.id.sp_user_type);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

    }

    @Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isValidData() {
        String emailId = edEmail.getText().toString();
        String password = edPassword.getText().toString().trim();
        String first_name = edName.getText().toString().trim();
        String mobileNum = edMobileNo.getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";
        if (first_name.equals("")) {
            showToast("Please enter Name.");
            app.shakeEdittext(edName);
            edName.requestFocus();
            return false;
        } else if (StringUtil.checkSpecialCharacter(first_name)) {
            showToast("Special character and digits are not allowed in Name.");
            app.shakeEdittext(edName);
            edName.requestFocus();
            return false;
        } else if (first_name.length() < 3) {
            showToast("Name can not be less then 3 characters.");
            app.shakeEdittext(edName);
            edName.requestFocus();
            return false;
        } else if (editTextBusiness.getText().toString().isEmpty()) {
            showToast("Please enter Business name.");
            app.shakeEdittext(editTextBusiness);
            editTextBusiness.requestFocus();
            return false;
        } else if (editTextState.getText().toString().isEmpty()) {
            showToast("Please enter State name.");
            app.shakeEdittext(editTextState);
            editTextState.requestFocus();
            return false;
        } else if (editTextCity.getText().toString().isEmpty()) {
            showToast("Please enter City name.");
            app.shakeEdittext(editTextCity);
            editTextCity.requestFocus();
            return false;
        } else if (emailId.equals("")) {
            showToast("Please enter Email.");
            app.shakeEdittext(edEmail);
            edEmail.requestFocus();
            return false;
        } else if (!emailId.matches(emailPattern) && edEmail.length() > 0) {
            showToast("Please enter a valid Email.");
            app.shakeEdittext(edEmail);
            edEmail.requestFocus();
            return false;
        } else if (mobileNum.equals("")) {
            showToast("Please enter Mobile Number.");
            app.shakeEdittext(edMobileNo);
            edMobileNo.requestFocus();
            return false;
        } else if (mobileNum.length() < 10) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edMobileNo);
            edMobileNo.requestFocus();
            return false;
        } else if (!checkMobileValidity(mobileNum.charAt(0))) {
            showToast("Please enter valid Mobile Number.");
            app.shakeEdittext(edMobileNo);
            edMobileNo.requestFocus();
            return false;
        } /*else if (password.equals("")) {
			showToast("Please enter Password.");
			app.shakeEdittext(edPassword);
			edPassword.requestFocus();
			return false;
		}*/ /*else if (password.length() < 6) {
			showToast("Password can not be less then 6 characters.");
			edPassword.requestFocus();
			return false;
		}*/
        if (CUR_USER_TYPE == BROKER_FIRM) {
            if (editTextReraRegistration.getText().toString().isEmpty()) {
                showToast("Please enter RERA Registration no.");
                app.shakeEdittext(editTextReraRegistration);
                editTextReraRegistration.requestFocus();
                return false;
            } else if (editTextGSTRegistration.getText().toString().isEmpty()) {
                showToast("Please enter GST no.");
                app.shakeEdittext(editTextGSTRegistration);
                editTextGSTRegistration.requestFocus();
                return false;
            } else if (editTextPan.getText().toString().isEmpty()) {
                showToast("Please enter Pan no.");
                app.shakeEdittext(editTextPan);
                editTextPan.requestFocus();
                return false;
            } else if (!Utils.isValidPANCardNumber(editTextPan.getText().toString().trim())) {
                showToast("Please enter valid PAN number");
                app.shakeEdittext(editTextPan);
                editTextPan.setSelection(editTextPan.getText().length());
                editTextPan.requestFocus();
                return false;
            }/*else if(editTextRMCode.getText().toString().isEmpty()){
				showToast("Please enter RM code.");
				app.shakeEdittext(editTextRMCode);
				editTextRMCode.requestFocus();
				return false;
			}*/
        } else if (CUR_USER_TYPE == EMPLOYEE_OF_BROKER_FIRM) {
            if (editTextAadhar.getText().toString().isEmpty()) {
                showToast("Please enter Aadhar no.");
                app.shakeEdittext(editTextAadhar);
                editTextAadhar.requestFocus();
                return false;
            } else if (editTextPan.getText().toString().isEmpty()) {
                showToast("Please enter Pan no.");
                app.shakeEdittext(editTextPan);
                editTextPan.requestFocus();
                return false;
            } else if (!Utils.isValidPANCardNumber(editTextPan.getText().toString().trim())) {
                showToast("Please enter valid PAN number");
                app.shakeEdittext(editTextPan);
                editTextPan.setSelection(editTextPan.getText().length());
                editTextPan.requestFocus();
                return false;
            } else if (editTextBrokerFirmCode.getText().toString().isEmpty()) {
                showToast("Please enter Broker Firm Code.");
                app.shakeEdittext(editTextBrokerFirmCode);
                editTextBrokerFirmCode.requestFocus();
                return false;
            }
        } else if (CUR_USER_TYPE == INDIVIDUAL) {
            if (editTextAadhar.getText().toString().isEmpty()) {
                showToast("Please enter Aadhar no.");
                app.shakeEdittext(editTextAadhar);
                editTextAadhar.requestFocus();
                return false;
            } else if (editTextReraRegistration.getText().toString().isEmpty()) {
                showToast("Please enter RERA Registration no.");
                app.shakeEdittext(editTextReraRegistration);
                editTextReraRegistration.requestFocus();
                return false;
            } else if (editTextPan.getText().toString().isEmpty()) {
                showToast("Please enter Pan no.");
                app.shakeEdittext(editTextPan);
                editTextPan.requestFocus();
                return false;
            } else if (!Utils.isValidPANCardNumber(editTextPan.getText().toString().trim())) {
                showToast("Please enter valid PAN number");
                app.shakeEdittext(editTextPan);
                editTextPan.setSelection(editTextPan.getText().length());
                editTextPan.requestFocus();
                return false;
            }/*else if(editTextRMCode.getText().toString().isEmpty()){
				showToast("Please enter RM code.");
				app.shakeEdittext(editTextRMCode);
				editTextRMCode.requestFocus();
				return false;
			}*/
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected String setActionBarTitle() {
        return "Sign Up";
    }

    protected boolean checkMobileValidity(char firstCharacter) {
        boolean validNo = false;
        switch (firstCharacter) {
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

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonRegister:
					/*Intent mIntent = new Intent(RegisterActivity.this,SignUpDocActivity.class);
					IntentDataObject mIntentDataObject = new IntentDataObject();
					mIntentDataObject.putData(ParamsConstants.BROKER_ID,"Rahe9999");
					mIntentDataObject.putData(ParamsConstants.BROKER_CODE,"Rahe9999");
					mIntentDataObject.putData(ParamsConstants.TYPE,String.valueOf(CUR_USER_TYPE));
					mIntent.putExtra(IntentDataObject.OBJ,mIntentDataObject);
					startActivity(mIntent);*/

                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            registerUser();
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(RegisterActivity.this, UIEventType.RETRY_REGISTER,
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                registerUser();
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(RegisterActivity.this, getString(R.string.check_your_internet_connection));
                                            }
                                        }
                                    });
                        }
                    }
                    break;
                case R.id.tv_login_here:
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                    break;

            }
        }
    };


    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            CUR_USER_TYPE = position;
            if (position == BROKER_FIRM) {
                editTextReraRegistration.setVisibility(View.VISIBLE);
                editTextGSTRegistration.setVisibility(View.VISIBLE);
                editTextRMCode.setVisibility(view.VISIBLE);
                editTextBrokerFirmCode.setVisibility(View.GONE);
                editTextAadhar.setVisibility(View.GONE);
            } else if (position == EMPLOYEE_OF_BROKER_FIRM) {
                editTextReraRegistration.setVisibility(View.GONE);
                editTextGSTRegistration.setVisibility(View.GONE);
                editTextRMCode.setVisibility(view.GONE);
                editTextBrokerFirmCode.setVisibility(View.VISIBLE);
                editTextAadhar.setVisibility(View.VISIBLE);
            } else if (position == INDIVIDUAL) {
                editTextReraRegistration.setVisibility(View.VISIBLE);
                editTextGSTRegistration.setVisibility(View.GONE);
                editTextRMCode.setVisibility(view.VISIBLE);
                editTextBrokerFirmCode.setVisibility(View.GONE);
                editTextAadhar.setVisibility(View.VISIBLE);
            } else {

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_REGISTER:
                    registerUser();
                    break;
                case RETRY_TNC:
                    Intent i = new Intent(RegisterActivity.this, TermsWebActivity.class);
                    i.putExtra("pageType", 0);
                    startActivity(i);
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
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
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                Log.i(TAG, mBean.getJson());
                switch (mBean.getApiType()) {
                    case REGISTER_USER:
                        if (mBean.getJson() != null) {
                            RegisterUserRespModel registerUserRespModel = (RegisterUserRespModel) JsonParser.convertJsonToBean(APIType.REGISTER_USER, mBean.getJson());
                            if (registerUserRespModel != null) {
                                if (registerUserRespModel.isSuccess() && registerUserRespModel.getData() != null) {
                                    Intent mIntent = new Intent(RegisterActivity.this, SignUpDocActivity.class);
                                    IntentDataObject mIntentDataObject = new IntentDataObject();
                                    mIntentDataObject.putData(ParamsConstants.BROKER_ID, registerUserRespModel.getData().getBroker_id());
                                    mIntentDataObject.putData(ParamsConstants.BROKER_CODE, registerUserRespModel.getData().getBroker_code());
                                    mIntentDataObject.putData(ParamsConstants.TYPE, String.valueOf(CUR_USER_TYPE));
                                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                                    startActivityForResult(mIntent, SIGNUP_REQ_CODE);
                                    //finish();
                                } else {
                                    showToast(registerUserRespModel.getMessage());
                                }
                            } else {
                                showToast(getString(R.string.something_went_wrong));
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

    private void registerUser() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.REGISTER_USER);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.REGISTER_USER));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("user_name=");
        mStringBuilder.append(edName.getText().toString().trim());
        mStringBuilder.append("&business_name=");
        mStringBuilder.append(editTextBusiness.getText().toString().trim());
        mStringBuilder.append("&state=");
        mStringBuilder.append(editTextState.getText().toString().trim());
        mStringBuilder.append("&city=");
        mStringBuilder.append(editTextCity.getText().toString().trim());
        mStringBuilder.append("&email_id=");
        mStringBuilder.append(edEmail.getText().toString().trim());
        mStringBuilder.append("&mobileno=");
        mStringBuilder.append(edMobileNo.getText().toString().trim());
        mStringBuilder.append("&aadhar_no=");
        mStringBuilder.append(editTextAadhar.getText().toString().trim());
        mStringBuilder.append("&password=");
        mStringBuilder.append(edPassword.getText().toString().trim());
        mStringBuilder.append("&registerType=");
        mStringBuilder.append(CUR_USER_TYPE);
        mStringBuilder.append("&rera_no=");
        mStringBuilder.append(editTextReraRegistration.getText().toString().trim());
        mStringBuilder.append("&gst_no=");
        mStringBuilder.append(editTextGSTRegistration.getText().toString().trim());
        mStringBuilder.append("&pancard_no=");
        mStringBuilder.append(editTextPan.getText().toString().trim());
        mStringBuilder.append("&rm_code=");
        mStringBuilder.append(editTextRMCode.getText().toString().trim());
        mStringBuilder.append("&broker_firm_code=");
        mStringBuilder.append(editTextBrokerFirmCode.getText().toString().trim());
        mStringBuilder.append("&builder_id=" + BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNUP_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
