package com.sp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.utils.Utils;

public class Splash1Activity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private AsyncThread mAsyncThread = null;
    private final String TAG = Splash1Activity.class.getSimpleName();
    private NetworkErrorObject mNetworkErrorObject = null;
    private String params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash1);
        params = "version=" + Utils.getAppVersionString(this) + "&type=android";
        // Create the dummy account
        //  mAccount = CreateSyncAccount(this);

        if (ConnectivityReceiver.isConnected()) {
            getVersion(params);
        } else {
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              launchNewActivity();
                                          }
                                      }, 2000
            );
        }
    }

    private void launchNewActivity() {
        BMHApplication app = (BMHApplication) getApplication();
        String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
        if (userid.length() > 0) {
            Intent intent = new Intent(Splash1Activity.this, ProjectsListActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Splash1Activity.this, LoginActivity.class);
            intent.putExtra("fromSplash", true);
            startActivity(intent);
        }
        finish();
    }

    private void getVersion(String params) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_API_VERSION);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_API_VERSION));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if (params != null && params.length() > 0) mBean.setJson(params);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        //mAsyncThread.initProgressDialog(Splash1Activity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    /*DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };*/

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                Log.i(TAG, mBean.getJson());
                switch (mBean.getApiType()) {
                    case GET_API_VERSION:
                        if (mBean.getJson() != null) {
                            final BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.GET_API_VERSION, mBean.getJson());
                            if (baseRespModel != null) {
                                if (baseRespModel.isSuccess()) {
                                    new Handler().postDelayed(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      launchNewActivity();
                                                                  }
                                                              }, 2000
                                    );
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(Splash1Activity.this).create();
                                    alertDialog.setTitle("Application Update");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage(getString(R.string.application_update));
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                                            dialog.dismiss();
                                            Splash1Activity.this.finish();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            } else {
                                //TODO: error
                                errorDialog();
                            }
                        } else {
                            errorDialog();
                        }

                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case GET_VERSION:
                    getVersion(params);
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }


    private void errorDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Splash1Activity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.unable_to_connect_server));
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Splash1Activity.this.finish();
            }
        });
        alertDialog.show();
    }
}
