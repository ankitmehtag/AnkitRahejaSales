package com.sp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.activities.VerifyOtpActivity;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.utils.Utils;

import java.util.Objects;

public class ForgotPasswordActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private BMHApplication app;
    private Toolbar toolbar = null;
    private Button buttonSubmit;
    private EditText forgot_pass;
    private AsyncThread mAsyncThread = null;
    private NetworkErrorObject mNetworkErrorObject = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        setListener();
    }

    private void initView() {
        toolbar = setToolBar();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        forgot_pass = findViewById(R.id.forgot_pass);
        buttonSubmit = findViewById(R.id.buttonSubmit);
    }

    private void setListener() {
        toolbar.setTitle("Reset your Password");
        setSupportActionBar(toolbar);
        buttonSubmit.setOnClickListener(mOnClickListener);
    }

    private boolean isValidData() {
        if (forgot_pass.getText().toString().trim().isEmpty()) {
            app.shakeEdittext(forgot_pass);
            forgot_pass.setSelection(forgot_pass.getText().length());
            forgot_pass.requestFocus();
            showToast("Please enter Registered Email Id or Mobile No.");
            return false;
        } else if (TextUtils.isDigitsOnly(forgot_pass.getText().toString().trim())) {
            if (forgot_pass.getText().toString().trim().length() != 10) {
                showToast("Please enter valid mobile number");
                forgot_pass.setSelection(forgot_pass.getText().length());
                forgot_pass.requestFocus();
                return false;
            }
        } else if (!TextUtils.isDigitsOnly(forgot_pass.getText().toString().trim())) {
            if (!Utils.isEmailValid(forgot_pass.getText().toString().trim())) {
                app.shakeEdittext(forgot_pass);
                forgot_pass.setSelection(forgot_pass.getText().length());
                forgot_pass.requestFocus();
                showToast("Please enter a valid Email Id");
                return false;
            }
        }
        return true;
    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSubmit:
                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            //TODO: network call
                            String params = "email=" + forgot_pass.getText().toString().trim()
                                    + "&" + ParamsConstants.USER_TYPE + "=" +
                                    BMHConstants.SALES_PERSON + "&" + ParamsConstants.BUILDER_ID + "="
                                    + BMHConstants.CURRENT_BUILDER_ID;
                            forgotPassword(params);
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(ForgotPasswordActivity.this, UIEventType.RETRY_FORGOT_PWD,
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                //TODO: network call
                                                String params = "email=" + forgot_pass.getText().toString().trim();
                                                forgotPassword(params);
                                                mNetworkErrorObject.getAlertDialog().dismiss();
                                                mNetworkErrorObject = null;
                                            } else {
                                                Utils.showToast(ForgotPasswordActivity.this, getString(R.string.check_your_internet_connection));
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
                    String params = "email=" + forgot_pass.getText().toString().trim();
                    forgotPassword(params);
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

    private void forgotPassword(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.FORGOT_PASSWORD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.FORGOT_PASSWORD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ForgotPasswordActivity.this, mOnCancelListener);
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
                    case FORGOT_PASSWORD:
                        if (mBean.getJson() != null) {
                            final BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.FORGOT_PASSWORD, mBean.getJson());
                            if (baseRespModel != null) {
                                if (baseRespModel.isSuccess()) {
                                    showToast(baseRespModel.getMessage());
                                    Intent mIntent = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                                    mIntent.putExtra(BMHConstants.USER_EMAIL, forgot_pass.getText().toString().trim());
                                    startActivity(mIntent);
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
