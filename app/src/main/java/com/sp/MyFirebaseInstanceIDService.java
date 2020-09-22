package com.sp;

/**
 * Created by Naresh on 12-Feb-18.
 */


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.utils.Config;
import com.utils.Utils;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private BMHApplication app;
    private AsyncThread mAsyncThread = null;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "onTokenRefresh(): " + refreshedToken);
        storeRegIdInPref(refreshedToken);
        registerFCMToken(refreshedToken);
    }

    private void storeRegIdInPref(String token) {
        app = (BMHApplication) getApplication();
        if(app != null) {
            app.saveIntoPrefs(ParamsConstants.FCM_TOKEN, token);
        }
    }


    private void registerFCMToken(String fcmToken){
        Log.e(TAG, "registerFCMToken(): " + fcmToken);
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
        mStringBuilder.append(Utils.getDeviceId(getApplicationContext()));
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.DEVICE_TOKEN);
        mStringBuilder.append("=");
        mStringBuilder.append(fcmToken);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.APP_TYPE);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.BROKER);
        mStringBuilder.append("&");
        mStringBuilder.append(ParamsConstants.BUILDER_ID);
        mStringBuilder.append("=");
        mStringBuilder.append(BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(getApplicationContext());
        mAsyncThread = new AsyncThread();
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case REGISTER_FCM_TOKEN:
                        BaseRespModel baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.REGISTER_FCM_TOKEN,mBean.getJson());
                        if(baseRespModel != null && baseRespModel.isSuccess()) {
                            Log.i(TAG,"Token registered successfully");
                            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
                            registrationComplete.putExtra(ParamsConstants.FCM_TOKEN, FirebaseInstanceId.getInstance().getToken());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                        }else{
                            Intent registrationComplete = new Intent(Config.REGISTRATION_FAILED);
                            registrationComplete.putExtra(ParamsConstants.FCM_TOKEN, FirebaseInstanceId.getInstance().getToken());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
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
