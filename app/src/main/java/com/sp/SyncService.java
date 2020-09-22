package com.sp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.jsonparser.JsonParser;
import com.model.AddLeadData;
import com.model.AddLeadReqModel;
import com.model.AddLeadRespModel;
import com.utils.SharePrefUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class SyncService extends Service {
    private Timer mSyncTimer = null;
    private final String TAG = SyncService.class.getSimpleName();
    private Handler syncHandler =  new Handler();
    private NetworkInfo activeNetwork;
    private AsyncThread mAsyncThread = null;
    private ArrayList<APIType> requestQue = new ArrayList<>();
    private BMHApplication app = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        app =(BMHApplication) getApplication();
        ConnectivityManager cm = (ConnectivityManager) SyncService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        startSyncing();


    }

    private void startSyncing(){
        if (mSyncTimer != null)  mSyncTimer.cancel();
        else   mSyncTimer = new Timer();
        mSyncTimer.scheduleAtFixedRate(new SyncTimerTask(), BMHConstants.USER_TRACKING_FREQUENCY, BMHConstants.USER_TRACKING_FREQUENCY);
    }


    Runnable userTrackingRunnable = new Runnable() {
        @Override
        public void run() {
            if(activeNetwork == null){
                ConnectivityManager cm = (ConnectivityManager) SyncService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                if(requestQue.size() == 0) {
                    sendEnqReq(dataReq());
                }
            }else{
                //TODO do nothing
            }
        }
    };
    class SyncTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            syncHandler.post(userTrackingRunnable);
        }
    }// End of TimeDisplayTimerTask


    private String dataReq() {
        String json = "";
        String dataArray = SharePrefUtil.getEnquiry(this);
        if(dataArray != null && !dataArray.isEmpty()) {
            ArrayList<AddLeadData> mAddLeadDatas = (ArrayList<AddLeadData>) JsonParser.convertJsonToBean(APIType.LEAD_DATA, dataArray);
            AddLeadReqModel addLeadReqModel = new AddLeadReqModel();
            addLeadReqModel.setDatalist(mAddLeadDatas);
            addLeadReqModel.setUserid(app.getFromPrefs(BMHConstants.USERID_KEY));
            json = JsonParser.convertBeanToJson(addLeadReqModel);
        }

        return json;
    }
    private void sendEnqReq(String dataParams){
        if(dataParams == null || dataParams.isEmpty())return;
        requestQue.add(APIType.ADD_LEAD);
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.ADD_LEAD);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.ADD_LEAD));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        if(dataParams != null && dataParams.length() > 0) mBean.setJson(dataParams);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.execute(mBean);
        mAsyncThread = null;

    }

    private String getDateTime(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
        return sdf.format(date);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(mSyncTimer != null ) {
                mSyncTimer.cancel();
            }
            if(syncHandler != null && userTrackingRunnable != null) {
                syncHandler.removeCallbacks(userTrackingRunnable);
            }
            if(mAsyncThread != null){
                mAsyncThread.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                // mLog.info("mHandler() Response is null");
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                requestQue.remove(mBean.getApiType());
                //  mLog.info("mHandler() API Type:" + mBean.getApiType() + ",URL: " + mBean.getUrl() +", Response:"+ mBean.getJson());
                switch (mBean.getApiType()) {
                    case ADD_LEAD:
                        if(mBean.getJson() != null) {
                            final AddLeadRespModel addLeadRespModel = (AddLeadRespModel) com.jsonparser.JsonParser.convertJsonToBean(APIType.ADD_LEAD, mBean.getJson());
                            if (addLeadRespModel != null) {
                                if(addLeadRespModel.isSuccess()){
                                    if(addLeadRespModel.getData() != null){
                                        String enqData = SharePrefUtil.getEnquiry(SyncService.this);
                                        ArrayList<AddLeadData> leadDataList = (ArrayList<AddLeadData>) JsonParser.convertJsonToBean(APIType.LEAD_DATA,enqData);
                                        if(leadDataList != null) {
                                            for (AddLeadData temp : addLeadRespModel.getData()) {
                                                for (int i = 0; i < leadDataList.size(); i++) {
                                                    if(temp.getUpdated_time() == leadDataList.get(i).getUpdated_time()){
                                                        leadDataList.remove(i);
                                                        break;
                                                    }
                                                }
                                            }
                                            if(leadDataList.size() > 0){
                                                SharePrefUtil.setEnquiry(SyncService.this, JsonParser.convertBeanToJson(leadDataList));
                                            }else {
                                                SharePrefUtil.setEnquiry(SyncService.this, "");
                                            }
                                        }
                                    }
                                }else{
                                    // showToast(addLeadRespModel.getMessage());
                                }

                            }else{
                                //showToast(getString(R.string.something_went_wrong));
                            }
                        }else{
                            //showToast(getString(R.string.something_went_wrong));
                        }
                        break;

                    default:
                        break;
                }
            }
            return true;
        }
    });


}// end of class