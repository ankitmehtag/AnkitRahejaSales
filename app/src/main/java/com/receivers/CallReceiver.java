package com.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.activities.AlertDialogActivity;
import com.activities.UniversalDialogActivity;
import com.helper.BMHConstants;
import com.services.CallRecordingService;
import com.sp.BMHApplication;
import com.utils.Utils;

import java.io.File;
import java.util.Date;

public class CallReceiver extends PhoneCallReceiver {

    private String recFilePath = "";

    @Override
    protected void onIncomingCallStarted(Context mContext, String number, Date start) {
        initCallRecording(mContext, number);
    }

    @Override
    protected void onOutgoingCallStarted(Context mContext, String number, Date start) {
        initCallRecording(mContext, number);
    }

    private void initCallRecording(Context mContext, String number) {
        if (!TextUtils.isEmpty(number)) {
            custObj = customerDb.getCustomerDao().getCustomerInfoByMobile(number);
            if (custObj == null) {
                universalContactsEntity = customerDb.getUniversalContactsDao().getUniversalContactsByMobile(number);
                if(universalContactsEntity!=null) {
                    if (!TextUtils.isEmpty(universalContactsEntity.getMobileNo()) && number.equalsIgnoreCase(universalContactsEntity.getMobileNo()))
                        startRecordingService(mContext, number);
                }
            } else {
                if (!TextUtils.isEmpty(custObj.getMobileNo()) && number.equalsIgnoreCase(custObj.getMobileNo())) {
                    //  Utils.scheduleJob(mContext);
                    startRecordingService(mContext, number);
                }
            }
        }
    }

    @Override
    protected void onIncomingCallEnded(Context mContext, String recordingFilePath, String number, Date start, Date end) {
        // OPEN POPUP DIALOG
        stopRecordingService(mContext);
        if (custObj != null && !TextUtils.isEmpty(custObj.getMobileNo())) {
            if (number.equalsIgnoreCase(custObj.getMobileNo())) {
                callEndDialog(mContext, custObj.getCustomerName(), custObj.getMobileNo(), custObj.getEnquiryId(), custObj.getLeadPhase(), custObj.getAssignType());
            }
        } else {
            if (universalContactsEntity != null && !TextUtils.isEmpty(universalContactsEntity.getMobileNo())) {
                endCallUniversalDialog(mContext, universalContactsEntity.getName(), universalContactsEntity.getMobileNo());
            }
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context mContext, String recordingFilePath, String number, Date start, Date end) {
        stopRecordingService(mContext);
        if (custObj != null && !TextUtils.isEmpty(custObj.getMobileNo())) {
            if (number.equalsIgnoreCase(custObj.getMobileNo())) {
                callEndDialog(mContext, custObj.getCustomerName(), custObj.getMobileNo(), custObj.getEnquiryId(), custObj.getLeadPhase(), custObj.getAssignType());
            }
        }
              else {
            if (universalContactsEntity != null && !TextUtils.isEmpty(universalContactsEntity.getMobileNo())) {
                endCallUniversalDialog(mContext, universalContactsEntity.getName(), universalContactsEntity.getMobileNo());

                    }
        }
    }

    @Override
    protected void onMissedCall(Context mContext, String number, Date start) {

    }

    private void endCallUniversalDialog(Context mContext, String name, String mobileNo) {
        recFilePath = BMHApplication.getInstance().getFromPrefs(BMHConstants.RECORDING_FILE_PATH);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(mContext, UniversalDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BMHConstants.CUSTOMER_NAME, name);
        intent.putExtra(BMHConstants.MOBILE_NO, mobileNo);
        intent.putExtra(BMHConstants.RECORDING_FILE_PATH, recFilePath);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }

    private void callEndDialog(final Context mContext, String name, String mobile, String enquiryId, String leadPhase, String assignType) {
        recFilePath = BMHApplication.getInstance().getFromPrefs(BMHConstants.RECORDING_FILE_PATH);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setClass(mContext, AlertDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BMHConstants.CUSTOMER_NAME, name);
        intent.putExtra(BMHConstants.MOBILE_NO, mobile);
        intent.putExtra(BMHConstants.ENQUIRY_ID, enquiryId);
        intent.putExtra(BMHConstants.CURRENT_PHASE, leadPhase);
        intent.putExtra(BMHConstants.ASSIGN_TYPE, assignType);
        intent.putExtra(BMHConstants.RECORDING_FILE_PATH, recFilePath);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void startRecordingService(Context mContext, String number) {
        File outputFile = Utils.createRecordingDir(mContext, number);
        recFilePath = outputFile.getPath();
        Intent startIntent = new Intent(mContext,
                CallRecordingService.class);
        //   startIntent.putExtra(BMHConstants.MOBILE_NO, number);
        startIntent.putExtra("OUTPUT_PATH", recFilePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(startIntent);
        } else {
            mContext.startService(startIntent);
        }
    }

    private void stopRecordingService(Context mContext) {
        Intent stopIntent = new Intent(mContext,
                CallRecordingService.class);
        mContext.stopService(stopIntent);
    }
}
