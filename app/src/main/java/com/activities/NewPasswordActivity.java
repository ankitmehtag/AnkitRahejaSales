package com.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.ConnectivityReceiver;
import com.sp.LoginActivity;
import com.sp.R;
import com.utils.Utils;

public class NewPasswordActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private BMHApplication app;
    private Activity ctx = NewPasswordActivity.this;
    private Button buttonSubmit;
    private EditText et_new_password, et_confirm_password;
    private AsyncThread mAsyncThread = null;
    private NetworkErrorObject mNetworkErrorObject = null;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        initView();
        setListener();
    }

    private void initView() {
        username = getIntent().getStringExtra(BMHConstants.USER_EMAIL);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
    }

    private void setListener() {
        buttonSubmit.setOnClickListener(mOnClickListener);
    }

    private boolean isValidData() {
        if (et_new_password.getText().toString().trim().isEmpty()) {
            app.shakeEdittext(et_new_password);
            et_new_password.setSelection(et_new_password.getText().length());
            et_new_password.requestFocus();
            showToast("Please Create your password");
            return false;
        } else if (et_new_password.getText().toString().trim().length() < 6) {
            app.shakeEdittext(et_new_password);
            et_new_password.setSelection(et_new_password.getText().length());
            et_new_password.requestFocus();
            showToast("Minimum 6 digit length required");
            return false;
        } else if (et_confirm_password.getText().toString().trim().isEmpty()) {
            app.shakeEdittext(et_confirm_password);
            et_confirm_password.setSelection(et_confirm_password.getText().length());
            et_confirm_password.requestFocus();
            showToast("Please Confirm password");
            return false;
        } else if (!et_new_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())) {
            app.shakeEdittext(et_confirm_password);
            et_confirm_password.setSelection(et_confirm_password.getText().length());
            et_confirm_password.requestFocus();
            showToast("Password miss match");
            return false;
        }

        return true;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSubmit:
                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            //TODO: network call
                            String params = "email=" + username
                                    + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON
                                    + "&new_password=" + et_new_password.getText().toString().trim()
                                    + "&confirm_password=" + et_confirm_password.getText().toString().trim()
                                    + "&" + ParamsConstants.BUILDER_ID + "="
                                    + BMHConstants.CURRENT_BUILDER_ID;
                            createNewPassword(params);
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(NewPasswordActivity.this, UIEventType.RETRY_FORGOT_PWD,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                String params = "email=" + username
                                                        + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON
                                                        + "&new_password=" + et_new_password.getText().toString().trim()
                                                        + "&confirm_password=" + et_confirm_password.getText().toString().trim();
                                                createNewPassword(params);
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(NewPasswordActivity.this, getString(R.string.check_your_internet_connection));
                                            }
                                        }
                                    });
                        }
                    }
                    break;
            }
        }
    };

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
                case RETRY_FORGOT_PWD:
                    String params = "email=" + username
                            + "&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON
                            + "&new_password=" + et_new_password.getText().toString().trim()
                            + "&confirm_password=" + et_confirm_password.getText().toString().trim();
                    createNewPassword(params);
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forgot_password, menu);
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

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    private void createNewPassword(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.CREATE_NEW_PASSWORD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.CREATE_NEW_PASSWORD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(NewPasswordActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
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
                    case CREATE_NEW_PASSWORD:
                        if (mBean.getJson() != null) {
                            final BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.VERIFY_OTP, mBean.getJson());
                            if (baseRespModel != null) {
                                if (baseRespModel.isSuccess()) {
                                    showToast(baseRespModel.getMessage());
                                    Intent mIntent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                                    startActivity(mIntent);
                                    finishAffinity();
                                } else {
                                    showToast(baseRespModel.getMessage());
                                }
								/*AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
								alertDialog.setTitle("Forgot Password");
								alertDialog.setMessage(baseRespModel.getMessage());
								alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										if (baseRespModel.isSuccess()) {
											dialog.dismiss();
										}else{
											dialog.dismiss();
										}
									}
								});
								alertDialog.show();*/
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
}
