package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.UnitDetailVO;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.exception.BMHException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.helper.BMHConstants;
import com.helper.ContentLoader;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.model.NetworkErrorObject;
import com.model.UpdatePersonalDetailRespModel;
import com.model.UserModel;
import com.model.VerifyCodeRespModel;
import com.utils.StringUtil;
import com.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersonalDetails extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = PersonalDetails.class.getSimpleName();
    private EditText editFirst, editLast, editEmail, editPhone, editCoApplicant, editPan, editAddress, editCity,
            editState, editZip, et_co_sales_person, et_coupon_code, et_broker_code;
    private TextView calendra_date, tv_info;
    private Activity ctx = PersonalDetails.this;
    private BMHApplication app = (BMHApplication) getApplication();
    private FragmentActivity fragmentActivity;
    private Button make_payment, btn_payu, btn_cc_avenue;
    final int DATE_DIALOG_ID = 0;
    UnitDetailVO mUnitDetails;
    private int mSelectedDay, mSelectedMonth, mSelectedYear;
    private RelativeLayout rl_dob_root;
    private NetworkErrorObject mNetworkErrorObject = null;
    private HashMap<String, String> personalDetailMap = new HashMap<String, String>();
    private AsyncThread mAsyncThread = null;
    private TextView tv_coupon_code_verify, tv_co_sales_code_verify, tv_broker_code;
    private ImageView iv_coupon_info, iv_co_sales_info, iv_broker_code;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_personal);
        app = (BMHApplication) getApplication();
        Toolbar toolbar = setToolBar();
        toolbar.setTitle("Applicant Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        editFirst = (EditText) findViewById(R.id.first_name);
        editLast = (EditText) findViewById(R.id.last_name);
        editEmail = (EditText) findViewById(R.id.email);
        editPhone = (EditText) findViewById(R.id.phone);
        rl_dob_root = (RelativeLayout) findViewById(R.id.rl_dob_root);
        calendra_date = (TextView) findViewById(R.id.calendra_date);
        editCoApplicant = (EditText) findViewById(R.id.co_applicant);
        editPan = (EditText) findViewById(R.id.et_pan_number);
        InputFilter[] filterArray = new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)};
        editPan.setFilters(filterArray);
        editAddress = (EditText) findViewById(R.id.address);
        editCity = (EditText) findViewById(R.id.city);
        editState = (EditText) findViewById(R.id.state);
        editZip = (EditText) findViewById(R.id.zip_code);
        tv_info = (TextView) findViewById(R.id.pers_info);
        make_payment = (Button) findViewById(R.id.make_payment);
        btn_payu = (Button) findViewById(R.id.btn_payu);
        btn_cc_avenue = (Button) findViewById(R.id.btn_cc_avenue);

        et_co_sales_person = (EditText) findViewById(R.id.et_co_sales_person);
        et_co_sales_person.setTag(false);

        et_broker_code = (EditText) findViewById(R.id.et_broker_code);

        et_coupon_code = (EditText) findViewById(R.id.et_coupon_code);
        et_coupon_code.setTag(false);
        et_coupon_code.setText(getIntent().getStringExtra(getString(R.string.txt_coupon_code_key)));
        tv_coupon_code_verify = (TextView) findViewById(R.id.tv_coupon_code_verify);
        tv_co_sales_code_verify = (TextView) findViewById(R.id.tv_co_sales_code_verify);
        tv_broker_code = (TextView) findViewById(R.id.tv_broker_code);

        iv_coupon_info = (ImageView) findViewById(R.id.iv_coupon_info);
        iv_co_sales_info = (ImageView) findViewById(R.id.iv_co_sales_info);
        iv_broker_code = (ImageView) findViewById(R.id.iv_broker_code);

        et_co_sales_person.setTypeface(typeface);
        et_coupon_code.setTypeface(typeface);
        et_broker_code.setTypeface(typeface);

        tv_info.setTypeface(typeface);
        editFirst.setTypeface(typeface);
        editLast.setTypeface(typeface);
        editEmail.setTypeface(typeface);
        editPhone.setTypeface(typeface);
        calendra_date.setTypeface(typeface);
        editCoApplicant.setTypeface(typeface);
        editPan.setTypeface(typeface);
        editAddress.setTypeface(typeface);
        editCity.setTypeface(typeface);
        editState.setTypeface(typeface);
        editZip.setTypeface(typeface);
        btn_payu.setTypeface(typeface);
        btn_cc_avenue.setTypeface(typeface);

        rl_dob_root.setOnClickListener(mOnClickListener);
        calendra_date.setOnClickListener(mOnClickListener);
        make_payment.setOnClickListener(mOnClickListener);
        btn_payu.setOnClickListener(mOnClickListener);
        btn_cc_avenue.setOnClickListener(mOnClickListener);
        tv_coupon_code_verify.setOnClickListener(mOnClickListener);
        tv_co_sales_code_verify.setOnClickListener(mOnClickListener);
        tv_broker_code.setOnClickListener(mOnClickListener);
        iv_co_sales_info.setOnClickListener(mOnClickListener);
        iv_broker_code.setOnClickListener(mOnClickListener);
        iv_coupon_info.setOnClickListener(mOnClickListener);

        et_co_sales_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_co_sales_person.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                et_co_sales_person.setTag(false);
                iv_co_sales_info.setVisibility(View.INVISIBLE);
            }
        });
        et_broker_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_broker_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                iv_broker_code.setVisibility(View.INVISIBLE);
            }
        });
        et_coupon_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_coupon_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                et_coupon_code.setTag(false);
                iv_coupon_info.setVisibility(View.INVISIBLE);
            }
        });

        Calendar mCalendar = Calendar.getInstance();
        mSelectedDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mSelectedMonth = mCalendar.get(Calendar.MONTH);
        mSelectedYear = mCalendar.get(Calendar.YEAR);

        updateDateDisplay();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mUnitDetails = intent.getParcelableExtra("unitvo");
        }
		/*if(ConnectivityReceiver.isConnected()){
			//TODO: network call
			new LongOperation().execute(app.getFromPrefs(BMHConstants.USERID_KEY));
		}else{
			mNetworkErrorObject = Utils.showNetworkErrorDialog(PersonalDetails.this, UIEventType.GET_PERSONAL_DETAILS,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(ConnectivityReceiver.isConnected()){
								//TODO: network call
								new LongOperation().execute(app.getFromPrefs(BMHConstants.USERID_KEY));
								mNetworkErrorObject.getAlertDialog().dismiss();
								mNetworkErrorObject = null;
							}else{
								Utils.showToast(PersonalDetails.this,getString(R.string.check_your_internet_connection));
							}
						}
					});
		}*/
        btn_payu.setSelected(true);
        // ----------------------------- End
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, onDateSet, mSelectedYear, mSelectedMonth, mSelectedDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;
        }
        return null;
    }

    private void updateDateDisplay() {
        calendra_date.setText(mSelectedDay + "-" + (mSelectedMonth + 1) + "-" + mSelectedYear);
    }

    private OnDateSetListener onDateSet = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedYear = year;
            mSelectedMonth = monthOfYear;
            mSelectedDay = dayOfMonth;
            updateDateDisplay();
        }
    };


    public boolean isValidData() {
        boolean isValid = false;
        String first_name = editFirst.getText().toString().trim();
        String last_name = editLast.getText().toString().trim();
        String emailId = editEmail.getText().toString().trim();
        String mobileNum = editPhone.getText().toString().trim();
        String dob = calendra_date.getText().toString().trim();
        String co_applicant = editCoApplicant.getText().toString().trim();
        String pan_number = editPan.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String state = editState.getText().toString().trim();
        String zip = editZip.getText().toString().trim();
        if (first_name.equals("")) {
            app.showToastAtCenter(ctx, "Please enter First Name.");
            app.shakeEdittext(editFirst);
            editFirst.setSelection(editFirst.getText().length());
            editFirst.requestFocus();
            return isValid;
        } else if (StringUtil.checkSpecialCharacter(first_name)) {
            app.showToastAtCenter(ctx, "Special character and digits are not allowed in Name.");
            app.shakeEdittext(editFirst);
            editFirst.setSelection(editFirst.length());
            editFirst.requestFocus();
            return isValid;
        } else if (first_name.length() < 3) {
            app.showToastAtCenter(ctx, "Name can not be less then 3 characters.");
            app.shakeEdittext(editFirst);
            editFirst.setSelection(editFirst.getText().length());
            editFirst.requestFocus();
            return isValid;
        } /*else if (last_name.equals("")) {
			app.showToastAtCenter(ctx, "Please enter Last Name.");
			app.shakeEdittext(editLast);
			editLast.setSelection(editLast.getText().length());
			editLast.requestFocus();
			return isValid;
		}*/ else if (emailId.equals("")) {
            app.showToastAtCenter(ctx, "Please enter Email.");
            app.shakeEdittext(editEmail);
            editEmail.setSelection(editEmail.getText().length());
            editEmail.requestFocus();
            return isValid;
        } else if (!StringUtil.validEmail(emailId)) {
            app.showToastAtCenter(ctx, "Please enter a valid Email.");
            app.shakeEdittext(editEmail);
            editEmail.setSelection(editEmail.getText().length());
            editPhone.requestFocus();
            return isValid;
        } else if (mobileNum.equals("")) {
            app.showToastAtCenter(ctx, "Please enter Mobile Number.");
            app.shakeEdittext(editPhone);
            editPhone.setSelection(editPhone.getText().length());
            editPhone.requestFocus();
            return isValid;
        } else if (mobileNum.length() < 10 || !Utils.isValidMobileNumber(mobileNum)) {
            app.showToastAtCenter(ctx, "Please enter valid Mobile Number.");
            app.shakeEdittext(editPhone);
            editPhone.setSelection(editPhone.getText().length());
            editPhone.requestFocus();
            return isValid;
        }/*else if (co_applicant.equals("")) {
			app.showToastAtCenter(ctx, "Please enter co-applicant name ");
			app.shakeEdittext(editCoApplicant);
			editCoApplicant.setSelection(editCoApplicant.getText().length());
			editCoApplicant.requestFocus();
			return isValid;
		}*/ else if (pan_number.isEmpty()) {
            app.showToastAtCenter(ctx, "Please enter PAN number");
            app.shakeEdittext(editPan);
            editPan.setSelection(editPan.getText().length());
            editPan.requestFocus();
            return isValid;
        } else if (!Utils.isValidPANCardNumber(pan_number)) {
            app.showToastAtCenter(ctx, "Please enter valid PAN number");
            app.shakeEdittext(editPan);
            editPan.setSelection(editPan.getText().length());
            editPan.requestFocus();
            return isValid;
        } else if (address.isEmpty()) {
            app.showToastAtCenter(ctx, "Please enter Address");
            app.shakeEdittext(editAddress);
            editAddress.setSelection(editAddress.getText().length());
            editAddress.requestFocus();
            return isValid;
        } else if (address.length() < 3) {
            app.showToastAtCenter(ctx, "Address can not be less then 3 characters.");
            app.shakeEdittext(editAddress);
            editAddress.setSelection(editAddress.getText().length());
            editAddress.requestFocus();
            return isValid;
        } else if (city.equals("")) {
            app.showToastAtCenter(ctx, "Please enter city");
            app.shakeEdittext(editCity);
            editCity.setSelection(editCity.getText().length());
            editCity.requestFocus();
            return isValid;
        } else if (StringUtil.checkSpecialCharacter(city)) {
            app.showToastAtCenter(ctx, "Special character and digits are not allowed in City.");
            app.shakeEdittext(editCity);
            editCity.setSelection(editCity.length());
            editCity.requestFocus();
            return isValid;
        } else if (state.equals("")) {
            app.showToastAtCenter(ctx, "Please enter state");
            app.shakeEdittext(editState);
            editState.setSelection(editState.getText().length());
            editState.requestFocus();
            return isValid;
        } else if (StringUtil.checkSpecialCharacter(state)) {
            app.showToastAtCenter(ctx, "Special character and digits are not allowed in State.");
            app.shakeEdittext(editState);
            editState.setSelection(editState.length());
            editState.requestFocus();
            return isValid;
        } else if (zip.isEmpty()) {
            app.showToastAtCenter(ctx, "Please enter Pin Code");
            app.shakeEdittext(editZip);
            editZip.setSelection(editZip.getText().length());
            editZip.requestFocus();
            return isValid;
        } else if (!Utils.isValidPinCode(zip)) {
            app.showToastAtCenter(ctx, "Please enter valid Pin Code");
            app.shakeEdittext(editZip);
            editZip.setSelection(editZip.getText().length());
            editZip.requestFocus();
            return isValid;
        } else if (et_coupon_code.getText().toString().trim().length() != 0) {
            if (et_coupon_code.getTag() != null && et_coupon_code.getTag() instanceof Boolean && (Boolean) et_coupon_code.getTag() == false) {
                showToast("Please verify coupon code");
                return false;
            }
        } else if (et_co_sales_person.getText().toString().trim().length() != 0) {
            if (et_co_sales_person.getTag() != null && et_co_sales_person.getTag() instanceof Boolean && (Boolean) et_co_sales_person.getTag() == false) {
                showToast("Please verify co sales person code");
                return false;
            }
        }
        isValid = true;
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                String param = "user_id=" + params[0];
                response = ContentLoader.getJsonUsingPost(WEBAPI.getWEBAPI(APIType.SHOW_USER_INFO), param);

            } catch (BMHException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Gson gson = new Gson();
                UserModel model = null;
                try {
                    model = gson.fromJson(result, UserModel.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                setData(model);
            } else {
                showToast(getString(R.string.something_went_wrong));
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setData(UserModel model) {
        if (model == null || model.getData() == null) return;
        editFirst.setText(model.getData().getFirst_name());
        editLast.setText(model.getData().getLast_name());
        editEmail.setText(model.getData().getEmail());
        if (model.getData().getDob() != null && !model.getData().getDob().isEmpty()) {
            Date mDOB = parseDate(model.getData().getDob());
            Calendar cal = Calendar.getInstance();
            cal.setTime(mDOB);
            mSelectedYear = cal.get(Calendar.YEAR);
            mSelectedMonth = cal.get(Calendar.MONTH);
            mSelectedDay = cal.get(Calendar.DAY_OF_MONTH);
            updateDateDisplay();
        }
        editCoApplicant.setText(model.getData().getCoapplicant());

        //editPhone.setText(model.getData().getMobile());
        //editPan.setText(model.getData().getPan_no());
        //editAddress.setText(model.getData().getAddress());
        //editCity.setText(model.getData().getCity());
        //editState.setText(model.getData().getState());
        //editZip.setText(model.getData().getZip());
    }


    private void updatePersionalData(final HashMap<String, String> map) {
        if (map == null) return;
        String dataParams = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            dataParams = dataParams.length() > 0 ? dataParams + "&" + key + "=" + value : key + "=" + value;
            if (btn_payu.isSelected()) dataParams = dataParams + "&gatewayType=" + "0";
        }
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.UPDATE_PERSONAL_DETAILS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.UPDATE_PERSONAL_DETAILS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(PersonalDetails.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;


    }

    private void updatePersionalData1(final HashMap<String, String> map) {
        if (map == null) return;
        CustomAsyncTask loadingTask = new CustomAsyncTask(ctx, new CustomAsyncTask.AsyncListner() {
            boolean Issuccess = false;
            String message = "";

            @Override
            public void OnBackgroundTaskCompleted() {
                if (Issuccess) {
                    showToast(getString(R.string.now_ur_unit_is_reserved));
                    Intent i = new Intent(PersonalDetails.this, PaymentWebviewActivity.class);
                    i.putExtra("unitvo", mUnitDetails);
                    startActivity(i);
                } else {
                    message = message != null && !message.isEmpty() ? message : getString(R.string.something_went_wrong);
                    showToast(message);
                }
            }

            @Override
            public void DoBackgroundTask(String[] nUrl) {
                try {
                    String ExtraFilter = "";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        ExtraFilter = ExtraFilter.length() > 0 ? ExtraFilter + "&" + key + "=" + value : key + "=" + value;
                        if (btn_cc_avenue.isSelected())
                            ExtraFilter = ExtraFilter + "&gatewayType=" + "1";
                    }
                    String url = UrlFactory.postPersionalInformation();
                    String response = ContentLoader.getJsonUsingPost(url, ExtraFilter);
                    if (response != null) {
                        JsonParser p = new JsonParser();
                        JsonElement jele = p.parse(response);
                        if (jele != null) {
                            JsonObject obj = jele.isJsonObject() ? jele.getAsJsonObject() : null;
                            if (obj != null) {
                                Issuccess = obj.get("success") != null ? obj.get("success").getAsBoolean() : false;
                                message = obj.get("message") != null ? obj.get("message").getAsString() : "Unexpected response error";
                            }
                        }
                    }
                    Log.d("responce", response);

                } catch (BMHException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnPreExec() {

            }
        });
        loadingTask.execute("");
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

    @Override
    protected String setActionBarTitle() {
        return "Applicant Details";
    }

    private Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            Log.i(TAG, "parseDate():" + e);
            return new Date(0);
        }
    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_dob_root:
                case R.id.calendra_date:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case R.id.make_payment:
                    if (isValidData()) {
                        app.saveIntoPrefs(ParamsConstants.PAN_CARD_NUMBER, editPan.getText().toString().trim());
                        app.saveIntoPrefs(ParamsConstants.EMAIL_ID, editEmail.getText().toString().trim());
                        personalDetailMap.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                        Intent intent = getIntent();
                        String payment_plan = intent.getStringExtra(ParamsConstants.PAYMENT_PLAN);
                        personalDetailMap.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                        personalDetailMap.put("payment_plan", payment_plan);
                        personalDetailMap.put("first_name", editFirst.getText().toString().trim());
                        personalDetailMap.put("last_name", editLast.getText().toString().trim());
                        personalDetailMap.put("email", editEmail.getText().toString().trim());
                        personalDetailMap.put("app_type", BMHConstants.SALES_PERSON);
                        personalDetailMap.put("mobile", editPhone.getText().toString().trim());
                        personalDetailMap.put("dob", calendra_date.getText().toString().trim());
                        personalDetailMap.put("coapplicant", editCoApplicant.getText().toString().trim());
                        personalDetailMap.put("pan_no", editPan.getText().toString().trim());
                        personalDetailMap.put("address", editAddress.getText().toString().trim());
                        personalDetailMap.put("city", editCity.getText().toString().trim());
                        personalDetailMap.put("state", editState.getText().toString().trim());
                        personalDetailMap.put("country", "India");// ===static for now
                        personalDetailMap.put("zip", editZip.getText().toString().trim());
                        personalDetailMap.put("nationality", "India");
                        personalDetailMap.put("throughAgent", "Android");
                        if (mUnitDetails != null) {
                            personalDetailMap.put("booking_amt", mUnitDetails.getBooking_fees());
                            personalDetailMap.put("total_amt", mUnitDetails.getGrand_total());
                        }
                        personalDetailMap.put(ParamsConstants.EMP_CODE, app.getFromPrefs(BMHConstants.SP_CODE));
                        if (!et_co_sales_person.getText().toString().isEmpty())
                            personalDetailMap.put("co_sales_person", et_co_sales_person.getText().toString().trim());
                        if (!et_coupon_code.getText().toString().isEmpty())
                            personalDetailMap.put("coupen_code", et_coupon_code.getText().toString().trim());
                        if (!et_broker_code.getText().toString().isEmpty())
                            personalDetailMap.put("broker_code", et_broker_code.getText().toString().trim());

                        if (mUnitDetails != null) {
                            personalDetailMap.put("unit_id", mUnitDetails.getId());
                            personalDetailMap.put("project_id", mUnitDetails.getProperty_id());
                            personalDetailMap.put("project_name", mUnitDetails.getProject_name());
                        }
                        if (ConnectivityReceiver.isConnected()) {
                            //TODO: network call
                            if (btn_payu.isSelected()) {
                                updatePersionalData(personalDetailMap);
                            } else {
                                updatePersionalData1(personalDetailMap);
                            }
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(PersonalDetails.this, UIEventType.RETRY_MAKE_PAYMENT,
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                if (btn_payu.isSelected()) {
                                                    updatePersionalData(personalDetailMap);
                                                } else {
                                                    updatePersionalData1(personalDetailMap);
                                                }
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(PersonalDetails.this, getString(R.string.check_your_internet_connection));
                                            }
                                        }
                                    });
                        }
                    }
                    break;
                case R.id.btn_payu:
                    btn_payu.setSelected(true);
                    btn_cc_avenue.setSelected(false);
                    break;
                case R.id.btn_cc_avenue:
                    btn_payu.setSelected(false);
                    btn_cc_avenue.setSelected(true);
                    break;
                case R.id.tv_broker_code:
                    if (et_broker_code.getText().toString().isEmpty()) {
                        showToast("Enter Broker code");
                    } else {
                        checkBrokerCode();
                    }
                    break;
                case R.id.tv_co_sales_code_verify:
                    if (et_co_sales_person.getText().toString().isEmpty()) {
                        showToast("Enter Co sales person code");
                    } else {
                        checkCoSalesPersonCode();
                    }
                    break;
                case R.id.tv_coupon_code_verify:
                    if (et_coupon_code.getText().toString().isEmpty()) {
                        showToast("Enter Coupon code");
                    } else {
                        checkBrokerAndCouponCode();
                    }
                    break;
                case R.id.iv_co_sales_info:
                    if (iv_co_sales_info.getTag(R.integer.info) != null && iv_co_sales_info.getTag(R.integer.info) instanceof VerifyCodeRespModel) {
                        VerifyCodeRespModel model = (VerifyCodeRespModel) iv_co_sales_info.getTag(R.integer.info);
                        showInfoDialog(APIType.VERIFY_CO_SLAES_PERSON_CODE, model);
                    }
                    break;
                case R.id.iv_broker_code:
                    if (iv_broker_code.getTag(R.integer.info) != null && iv_broker_code.getTag(R.integer.info) instanceof VerifyCodeRespModel) {
                        VerifyCodeRespModel model = (VerifyCodeRespModel) iv_broker_code.getTag(R.integer.info);
                        showInfoDialog(APIType.VERIFY_BROKER_CODE, model);
                    }
                    break;
                case R.id.iv_coupon_info:
                    if (iv_coupon_info.getTag(R.integer.info) != null && iv_coupon_info.getTag(R.integer.info) instanceof VerifyCodeRespModel) {
                        VerifyCodeRespModel model = (VerifyCodeRespModel) iv_coupon_info.getTag(R.integer.info);
                        showInfoDialog(APIType.VERIFY_COUPON_CODE, model);
                    }
                    break;
            }
        }
    };


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
                    case UPDATE_PERSONAL_DETAILS:
                        if (mBean.getJson() != null) {
                            final UpdatePersonalDetailRespModel updatePersonalDetailRespModel = (UpdatePersonalDetailRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.UPDATE_PERSONAL_DETAILS, mBean.getJson());
                            if (updatePersonalDetailRespModel != null) {
                                if (updatePersonalDetailRespModel.isSuccess() && updatePersonalDetailRespModel.getIsUnitReserved() == 0) {
                                    showToast(getString(R.string.now_ur_unit_is_reserved));
                                    Intent mIntent = new Intent(PersonalDetails.this, PayUPaymentProcessActivity.class);
                                    IntentDataObject mIntentDataObject = new IntentDataObject();
                                    if (mUnitDetails.getIsLotteryProject().equalsIgnoreCase("1")) {
                                        mIntentDataObject.putData(ParamsConstants.UNIT_ID, "");
                                    } else {
                                        mIntentDataObject.putData(ParamsConstants.UNIT_ID, mUnitDetails.getunitNo());
                                    }
                                    mIntentDataObject.putData(ParamsConstants.DISPLAY_NAME, mUnitDetails.getProject_name());
                                    mIntentDataObject.setObj(updatePersonalDetailRespModel);
                                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                                    startActivity(mIntent);
                                } else {
                                    showToast(getString(R.string.txt_this_unit_is_already_reserved));
                                }

                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }
                        break;
                    case VERIFY_COUPON_CODE:
                        if (mBean.getJson() != null) {
                            VerifyCodeRespModel model = (VerifyCodeRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.VERIFY_COUPON_CODE, mBean.getJson());
                            if (model != null) {
                                if (model.isSuccess()) {
                                    et_coupon_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                                    et_coupon_code.setTag(true);
                                    iv_coupon_info.setVisibility(View.VISIBLE);
                                    iv_coupon_info.setTag(R.integer.info, model);
                                    showToast(model.getMessage());
                                } else {
                                    et_coupon_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    et_coupon_code.setTag(false);
                                    iv_coupon_info.setVisibility(View.INVISIBLE);
                                    iv_coupon_info.setTag(R.integer.info, null);
                                    showToast(model.getMessage());
                                }
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                                iv_coupon_info.setTag(R.integer.info, null);
                            }
                        }
                        break;
                    case VERIFY_CO_SLAES_PERSON_CODE:
                        if (mBean.getJson() != null) {
                            VerifyCodeRespModel model = (VerifyCodeRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.VERIFY_CO_SLAES_PERSON_CODE, mBean.getJson());
                            if (model != null) {
                                if (model.isSuccess()) {
                                    et_co_sales_person.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                                    et_co_sales_person.setTag(true);
                                    iv_co_sales_info.setVisibility(View.VISIBLE);
                                    iv_co_sales_info.setTag(R.integer.info, model);
                                    showToast(model.getMessage());
                                } else {
                                    et_co_sales_person.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    et_co_sales_person.setTag(false);
                                    iv_co_sales_info.setVisibility(View.INVISIBLE);
                                    iv_co_sales_info.setTag(R.integer.info, null);
                                    showToast(model.getMessage());
                                }
                            } else {
                                iv_co_sales_info.setTag(R.integer.info, null);
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }

                        break;
                    case VERIFY_BROKER_CODE:
                        if (mBean.getJson() != null) {
                            VerifyCodeRespModel model = (VerifyCodeRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.VERIFY_BROKER_CODE, mBean.getJson());
                            if (model != null) {
                                if (model.isSuccess()) {
                                    et_broker_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                                    et_broker_code.setTag(true);
                                    iv_broker_code.setVisibility(View.VISIBLE);
                                    iv_broker_code.setTag(R.integer.info, model);
                                    showToast(model.getMessage());
                                } else {
                                    et_broker_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    et_broker_code.setTag(false);
                                    iv_broker_code.setVisibility(View.INVISIBLE);
                                    iv_broker_code.setTag(R.integer.info, null);
                                    showToast(model.getMessage());
                                }
                            } else {
                                iv_broker_code.setTag(R.integer.info, null);
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


    @Override
    public void onResume() {
        super.onResume();
        app = (BMHApplication) getApplication();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case GET_PERSONAL_DETAILS:
                    new LongOperation().execute(app.getFromPrefs(BMHConstants.USERID_KEY));
                    break;
                case RETRY_MAKE_PAYMENT:
                    if (btn_payu.isSelected()) {
                        updatePersionalData(personalDetailMap);
                    } else {
                        updatePersionalData1(personalDetailMap);
                    }
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

    private void checkBrokerAndCouponCode() {
        String dataParams = "";
        ReqRespBean mBean = new ReqRespBean();
        dataParams = "coupen_code=" + et_coupon_code.getText().toString().trim();
        if (mUnitDetails != null && mUnitDetails.getId() != null)
            dataParams = dataParams + "&unit_id=" + mUnitDetails.getId();
        mBean.setApiType(APIType.VERIFY_COUPON_CODE);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.VERIFY_COUPON_CODE));
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(PersonalDetails.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void checkCoSalesPersonCode() {
        String dataParams = "";
        ReqRespBean mBean = new ReqRespBean();
        dataParams = "co_sales_person=" + et_co_sales_person.getText().toString().trim();
        mBean.setApiType(APIType.VERIFY_CO_SLAES_PERSON_CODE);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.VERIFY_CO_SLAES_PERSON_CODE));
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(PersonalDetails.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void checkBrokerCode() {
        String dataParams = "";
        ReqRespBean mBean = new ReqRespBean();
        dataParams = "broker_code=" + et_broker_code.getText().toString().trim();
        mBean.setApiType(APIType.VERIFY_BROKER_CODE);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.VERIFY_BROKER_CODE));
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(PersonalDetails.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void showInfoDialog(APIType apiType, VerifyCodeRespModel model) {
        if (model == null || model.getData() == null) return;
        LayoutInflater factory = LayoutInflater.from(this);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final View dialogView = factory.inflate(R.layout.disclaimer_alert, null);
        final TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        final TextView tv_disclaimer = (TextView) dialogView.findViewById(R.id.tv_disclaimer);
        if (apiType == APIType.VERIFY_CO_SLAES_PERSON_CODE) {
            tv_title.setText("Co Sales Person Details");
            tv_disclaimer.setText("Co Sales Person: " + model.getData().getCo_sales_Person_name());
        } else if (apiType == APIType.VERIFY_COUPON_CODE) {
            tv_title.setText("Offer Details");
            tv_disclaimer.setText("Payment Plan: " + model.getData().getPayment_plan_name() + "\n"
                    + "Offer Details: " + model.getData().getOffered_text());
        } else if (apiType == APIType.VERIFY_BROKER_CODE) {
            tv_title.setText("Broker Details");
            tv_disclaimer.setText("Broker: " + model.getData().getBroker_name());
        } else {
            tv_title.setText("Error");
            tv_disclaimer.setText(getString(R.string.something_went_wrong));
        }

        Button btn_i_agree = (Button) dialogView.findViewById(R.id.btn_i_agree);
        btn_i_agree.setText("OK");
        tv_title.setTypeface(typeface);
        tv_disclaimer.setTypeface(typeface);
        btn_i_agree.setTypeface(typeface);
        View.OnClickListener dialogViewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
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


}
