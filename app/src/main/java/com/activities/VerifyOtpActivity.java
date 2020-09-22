package com.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.SmsListener;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.sp.BaseFragmentActivity;
import com.sp.R;
import com.sp.SmsReceiver;

import java.util.ArrayList;
import java.util.List;

public class VerifyOtpActivity extends BaseFragmentActivity {
    private static final String TAG = VerifyOtpActivity.class.getSimpleName();
    private EditText et_otp;
    private Button btn_submit;
    private TextView tv_msg, tv_msg2, tv_msg3, tv_resend_otp;
    private String username = "";
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private Typeface regular;
    private AsyncThread mAsyncThread = null;
    private NetworkErrorObject mNetworkErrorObject = null;
    String[] permission = new String[]{
            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS};

    @Override
    protected String setActionBarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        initViews();
        setListeners();
        setTypeface();
        username = getIntent().getStringExtra(BMHConstants.USER_EMAIL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                //do nothing
            }
        }
    }

    public void initViews() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_otp = (EditText) findViewById(R.id.et_otp);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_msg2 = (TextView) findViewById(R.id.tv_msg2);
        tv_msg3 = (TextView) findViewById(R.id.tv_msg3);
        tv_resend_otp = (TextView) findViewById(R.id.tv_resend_otp);

    }

    public void setListeners() {
        btn_submit.setOnClickListener(mOnClickListener);
        tv_resend_otp.setOnClickListener(mOnClickListener);
    }

    public void setTypeface() {
        btn_submit.setTypeface(regular);
        et_otp.setTypeface(regular);
        tv_msg.setTypeface(regular);
        tv_msg2.setTypeface(regular);
        tv_msg3.setTypeface(regular);
        tv_resend_otp.setTypeface(regular);

    }

    public boolean isValidData() {
        if (username == null || username.isEmpty()) {
            showToast(getString(R.string.something_went_wrong));
            return false;
        } else if (et_otp.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.enter_otp));
            return false;
        }
        return true;
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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_submit:
                    if (isValidData()) {
                        if (isNetworkAvailable()) {
                            verifyOTP();
                        } else {
                            showToast(getString(R.string.network_not_available));
                        }
                    }
                    break;
                case R.id.tv_resend_otp:
                    if (isNetworkAvailable()) {
                        forgotPassword();
                    } else {
                        showToast(getString(R.string.network_not_available));
                    }
                    break;
            }
        }
    };

    private void verifyOTP() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.VERIFY_OTP);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.VERIFY_OTP));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("email=" + username);
        mStringBuilder.append("&otp=" + et_otp.getText().toString().trim());
        mStringBuilder.append("&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON);
        mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=" + BMHConstants.CURRENT_BUILDER_ID);
        if (mStringBuilder != null && mStringBuilder.length() > 0)
            mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(VerifyOtpActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void forgotPassword() {
        String params = "email=" + username + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON;
        ;
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.FORGOT_PASSWORD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.FORGOT_PASSWORD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(VerifyOtpActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }


    SmsListener mSmsListener = new SmsListener() {
        @Override
        public void messageReceived(String messageText) {
            showToast(messageText);
            messageText = messageText.replaceAll("\\D+", "");
            et_otp.setText("" + messageText);
            if (isValidData()) {
                if (isNetworkAvailable()) {
                    verifyOTP();
                } else {
                    showToast(getString(R.string.network_not_available));
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        SmsReceiver.bindListener(mSmsListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SmsReceiver.bindListener(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BMHConstants.MULTIPLE_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
                return;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                switch (mBean.getApiType()) {
                    case VERIFY_OTP:
                        if (mBean.getJson() != null) {
                            final BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.VERIFY_OTP, mBean.getJson());
                            if (baseRespModel != null) {
                                if (baseRespModel.isSuccess()) {
                                    showToast(baseRespModel.getMessage());
                                    Intent mIntent = new Intent(VerifyOtpActivity.this, NewPasswordActivity.class);
                                    mIntent.putExtra(BMHConstants.USER_EMAIL, username);
                                    startActivity(mIntent);
                                    finish();
                                } else {
                                    showToast(baseRespModel.getMessage());
                                }
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }
                        break;
                    case FORGOT_PASSWORD:
                        if (mBean.getJson() != null) {
                            final BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.FORGOT_PASSWORD, mBean.getJson());
                            if (baseRespModel != null) {
                                showToast(baseRespModel.getMessage());
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

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permission) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), BMHConstants.MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
