package com.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.database.AppDatabase;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.UniversalContactsEntity;
import com.helper.BMHConstants;

import java.util.Date;

public abstract class PhoneCallReceiver extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;  //because the passed incoming is only valid in ringing
    CustomerInfoEntity custObj;
    UniversalContactsEntity universalContactsEntity;
    AppDatabase customerDb;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    customerDb = AppDatabase.getInstance();
                    Thread.sleep(2000);
                    if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {

                        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                        } else {
                            int state = 0;
                            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                            savedNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                                state = TelephonyManager.CALL_STATE_IDLE;
                            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                                state = TelephonyManager.CALL_STATE_OFFHOOK;
                            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                                state = TelephonyManager.CALL_STATE_RINGING;
                            }
                            if (!TextUtils.isEmpty(savedNumber)) {
                                savedNumber = validateMobileNumber(savedNumber);
                                if (customerDb != null) {
                                    custObj = customerDb.getCustomerDao().getCustomerInfoByMobile(savedNumber);
                                    if (custObj == null)
                                        universalContactsEntity = customerDb.getUniversalContactsDao().getUniversalContactsByMobile(savedNumber);
                                }
                                onCallStateChanged(context, state, savedNumber);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private String validateMobileNumber(String mobNo) {
                //checking whether mobile number starts with "0"
                if (mobNo.startsWith("0"))
                    mobNo = mobNo.substring(0, mobNo.length());
                    //checking whether mobile number starts with "+91"
                else if (mobNo.startsWith("+"))
                    mobNo = mobNo.substring(3, mobNo.length());
                if (mobNo.matches(BMHConstants.MOBILE_REGEX)) {
                    System.out.println("valid mobile number");
                } else {
                    System.out.println("invalid mobile number");
                }
                return mobNo;
            }
        });

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
    }

    //Derived classes should override these to respond to specific events of interest
    protected abstract void onIncomingCallStarted(Context ctx, String number, Date start);

    protected abstract void onOutgoingCallStarted(Context ctx, String number, Date start);

    protected abstract void onIncomingCallEnded(Context ctx, String recordingFilePath, String number, Date start, Date end);

    protected abstract void onOutgoingCallEnded(Context ctx, String recordingFilePath, String number, Date start, Date end);

    protected abstract void onMissedCall(Context ctx, String number, Date start);

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    callStartTime = new Date();
                    savedNumber = number;
                    onIncomingCallStarted(context, number, callStartTime);

                       }

                        break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();

                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, "", savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, "", savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }
}
