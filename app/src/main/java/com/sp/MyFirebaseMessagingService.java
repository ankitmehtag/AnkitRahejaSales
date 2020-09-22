package com.sp;

/**
 * Created by Naresh on 12-Feb-18.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.activities.AsmDetailsActivity;
import com.activities.BlogDetailsActivity;
import com.activities.FollowUpSalesDetailActivity;
import com.activities.BrokerDetailsActivity;
import com.activities.ChatScreen;
import com.activities.MyChat;
import com.activities.S1;
import com.activities.SpDetailsActivity;
import com.activities.AssignedSalesDetailActivity;
import com.activities.TransactionDetailsActivity;
import com.appnetwork.ReqRespBean;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.model.IvrLeadModel;
import com.model.MyTransactionsRespModel;
import com.model.Projects;
import com.utils.Config;
import com.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static com.fragments.MyTransChannelFragment.TAG_NAME;
import static com.utils.NotificationUtils.getTimeMilliSec;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private BMHApplication app;
    ReqRespBean mBean;
    //NotificationBadge mBadge;

    private Gson gson;
    private Type listType;
    private MyTransactionsRespModel.Data myTransactionsModel;
    public ArrayList<IvrLeadModel> mIvrList;
    IvrLeadModel model;
    public ArrayList<Projects> projectsMasterList;
  /*  public MyFirebaseMessagingService(ArrayList<IvrLeadModel> mIvrList, ArrayList<Projects> projectsMasterList) {

        this.mIvrList = mIvrList;
        this.projectsMasterList = projectsMasterList;
    }*/

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle extras = intent.getExtras();
        Log.e(TAG, "From: " + extras);
        String type = extras.getString("type");
        if (type.equalsIgnoreCase("8")) {
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String payload = extras.getString("payload");
            try {
                JSONObject payload_data = new JSONObject(payload);
                {
                    Intent resultIntent = null;
                    String unit_id = payload_data.getString("unit_id");
                    String unit_number = payload_data.getString("unit_number");
                    String project_name = payload_data.getString("project_name");
                    String unit_name = payload_data.getString("unit_name");
                    String unit_image = payload_data.getString("unit_image");
                    String isUnitReserved = payload_data.getString("isUnitReserved");
                    if (type.equalsIgnoreCase("8")) {
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        mIntentDataObject.putData(ParamsConstants.UNIT_ID, unit_id);
                        mIntentDataObject.putData(ParamsConstants.UNIT_NO, unit_number);
                        mIntentDataObject.putData(ParamsConstants.UNIT_TITLE, project_name);
                        mIntentDataObject.putData(ParamsConstants.PAYMENT_PLAN, "");
                        mIntentDataObject.putData(ParamsConstants.BHK_TYPE, unit_name);
                        mIntentDataObject.putData(ParamsConstants.UNIT_IMAGE, unit_image);
                        mIntentDataObject.putData(ParamsConstants.UNIT_RESERVED, isUnitReserved);
                        resultIntent = new Intent(getApplicationContext(), ChatScreen.class);
                        resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    } else {
                        resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                    }
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, payload, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, payload, resultIntent, icon);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equalsIgnoreCase("9")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            if (type.equalsIgnoreCase("9")) {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                resultIntent.putExtra("target_info", title);
            } else {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, title, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, title, resultIntent, icon);
            }
        } else if (type.equalsIgnoreCase("0")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String target_info = extras.getString("target_info");
            String notification_id = extras.getString("notification_id");
            if (type.equalsIgnoreCase("0")) {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                resultIntent.putExtra("notification_id", notification_id);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
            }
        }
        if (type.equalsIgnoreCase("1")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String target_info = extras.getString("target_info");
            String notification_id = extras.getString("notification_id");
            if (type.equalsIgnoreCase("1")) {
                resultIntent = new Intent(getApplicationContext(), BlogDetailsActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("body", body);
                resultIntent.putExtra("blog_id", target_info);
                resultIntent.putExtra("notification_id", notification_id);
            } else {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
            }
        } else if (type.equalsIgnoreCase("2")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String target_info = extras.getString("target_info");
            String notification_id = extras.getString("notification_id");
            if (type.equalsIgnoreCase("2")) {
                resultIntent = new Intent(getApplicationContext(), FAQActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("body", body);
                resultIntent.putExtra("target_info", target_info);
                resultIntent.putExtra("notification_id", notification_id);
            } else
                {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon))
                  {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            }
                   else
                {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);

              }

           }

        else if (type.equalsIgnoreCase("3")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String target_info = extras.getString("target_info");
            String notification_id = extras.getString("notification_id");
            if (type.equalsIgnoreCase("3")) {
                resultIntent = new Intent(getApplicationContext(), GetProjectListNotification.class);
                IntentDataObject mIntentDataObjecProjectList = new IntentDataObject();
                mIntentDataObjecProjectList.putData(ParamsConstants.ID, target_info);
                mIntentDataObjecProjectList.putData("notification_id", notification_id);
                //mIntentDataObject.putData(ParamsConstants.TITLE,mData.getTarget_info().getProject_name());
                            /*mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
                            mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));*/
                resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObjecProjectList);
            } else {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
            }
        } else if (type.equalsIgnoreCase("4")) {
            Intent resultIntent = null;
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String target_info = extras.getString("target_info");
            String notification_id = extras.getString("notification_id");
            if (type.equalsIgnoreCase("4")) {
                resultIntent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                mIntentDataObject.putData(ParamsConstants.ID, target_info);
                mIntentDataObject.putData("notification_id", notification_id);
                                 //mIntentDataObject.putData(ParamsConstants.TITLE,title);
                            /*mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
							mIntentDataObject.putData(ParamsConstants.CITY_ID,app.getFromPrefs(BMHConstants.CITYID));*/
                resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);

            } else {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
            }
        } else if (type.equalsIgnoreCase("6")) {
            Intent resultIntent = null;
            String se = "", nw = "";
            String title = extras.getString("title");
            String body = extras.getString("body");
            String icon = extras.getString("icon");
            String project_id = extras.getString("project_id");
            String notification_id = extras.getString("notification_id");
            String target_info = extras.getString("target_info");
            String projecttype  = extras.getString("project_type");
            String project_plan_img = extras.getString("project_plan");
            String cord = extras.getString("cord");
            JSONObject coord = null;
            try {
                coord = new JSONObject(cord);
                se = coord.getString("se");
                nw = coord.getString("nw");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type.equalsIgnoreCase("6")) {
                resultIntent = new Intent(getApplicationContext(), UnitMapActivity.class);
                        /*if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0){
                            resultIntent.putExtra(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
                        }*/
                resultIntent.putExtra("unitIds", target_info);
                resultIntent.putExtra("pro_plan_img", project_plan_img);
                resultIntent.putExtra("projectId", project_id);
                resultIntent.putExtra("notification_id", notification_id);
                resultIntent.putExtra("se", se);
                resultIntent.putExtra("nw", nw);
                resultIntent.putExtra("project_type", projecttype);



            } else {
                resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
            }
            if (TextUtils.isEmpty(icon)) {
                showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
            }
        } else if (type.equalsIgnoreCase("7")) {

            //NotificationPayLoadData mNotificationPayLoadData = (NotificationPayLoadData) JsonParser.convertJsonToBean(APIType.NOTIFICATION_PAYLOAD,data.toString());
            String title = extras.getString("title");
            String message = extras.getString("message");
            // boolean isBackground = data.getBoolean("is_background");
            String imageUrl = extras.getString("image");
            String timestamp = extras.getString("timestamp");
            String notification_id = extras.getString("notification_id");
            String payload = extras.getString("payload");
            JSONObject payload_data = null;
            try {
                payload_data = new JSONObject(payload);
                String payment_mode = payload_data.getString("payment_mode");
                Intent resultIntent = null;
                if (payload != null) {
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.putData(getString(R.string.txt_order_no), payload_data.optString(getString(R.string.txt_order_id)));
                    mIntentDataObject.putData(getString(R.string.txt_unit_no), payload_data.optString(getString(R.string.txt_unit_id)));
                    mIntentDataObject.putData(getString(R.string.txt_project_name), payload_data.optString(getString(R.string.txt_projectName)));
                    mIntentDataObject.putData(getString(R.string.payment_status), payload_data.optString(getString(R.string.txt_payment_status)));
                    //   mIntentDataObject.putData(getString(R.string.txt_payment_mode), payload_data.optString(getString(R.string.txt_paymentMode)));
                    mIntentDataObject.putData(getString(R.string.txt_notification_id), notification_id);
                    mIntentDataObject.putData(getString(R.string.txt_order_no), payload_data.optString(getString(R.string.txt_order_id)));
                    mIntentDataObject.putData(getString(R.string.txt_cheque_amount), payload_data.optString(getString(R.string.txt_chequeAmount)));
                    mIntentDataObject.putData(getString(R.string.txt_cheque_no), payload_data.optString(getString(R.string.transaction_no)));
                    mIntentDataObject.putData(getString(R.string.txt_cheque_date), payload_data.optString(getString(R.string.txt_order_date)));
                    mIntentDataObject.putData(getString(R.string.txt_customer_name), payload_data.optString(getString(R.string.txt_customerName)));
                    mIntentDataObject.putData(getString(R.string.txt_customer_email), payload_data.optString(getString(R.string.txt_customerEmail)));
                    mIntentDataObject.putData(getString(R.string.txt_customer_mobile_no), payload_data.optString(getString(R.string.txt_customer_telephone)));
                    mIntentDataObject.putData(getString(R.string.txt_pan_aadhar_no), payload_data.optString(getString(R.string.txt_pan_no)));
                    mIntentDataObject.putData(getString(R.string.txt_coupon_code), payload_data.optString(getString(R.string.txt_couponCode)));
                    mIntentDataObject.putData(getString(R.string.txt_payment_plan_key), payload_data.optString(getString(R.string.txt_payment_plan_name_key)));
                    mIntentDataObject.putData(getString(R.string.txt_payment_plan_desc_key), payload_data.optString(getString(R.string.txt_payment_plan_desc_key)));

                    if (payment_mode.equalsIgnoreCase("CC")) {
                        mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_credit_card));
                    } else if (payment_mode.equalsIgnoreCase("DC")) {
                        mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_debit_card));
                    } else if (payment_mode.equalsIgnoreCase("NB")) {
                        mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_net_banking));
                    } else {
                        mIntentDataObject.putData(getString(R.string.txt_payment_mode), payment_mode);
                    }
                    resultIntent = new Intent(getApplicationContext(), PaymentStatusActivity.class);
                    resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                } else {
                    resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                    resultIntent.putExtra("message", message);
                }
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (type.equalsIgnoreCase("10")) {
            Intent resultIntent = null;
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                //NotificationPayLoadData mNotificationPayLoadData = (NotificationPayLoadData) JsonParser.convertJsonToBean(APIType.NOTIFICATION_PAYLOAD,data.toString());
                String title = extras.getString("title");
                String message = extras.getString("message");
                // boolean isBackground = data.getBoolean("is_background");
                String imageUrl = extras.getString("image");
                String timestamp = extras.getString("timestamp");
                String notification_id = extras.getString("notification_id");
                String payload = extras.getString("payload");
                if (payload != null) {
                    JSONObject payload_data = null;
                    try {
                        payload_data = new JSONObject(payload);
                        parseTransactionJson(payload_data);
                        if (myTransactionsModel != null) {
                            resultIntent = new Intent(getApplicationContext(), TransactionDetailsActivity.class);
                            resultIntent.putExtra("ChannelTransactionModel", myTransactionsModel);
                            resultIntent.putExtra("notification_id", notification_id);
                            resultIntent.putExtra("TAG_NAME", TAG_NAME);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                            resultIntent.putExtra("message", message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }

            }
        } else if (type.equalsIgnoreCase("13")) {
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                Intent resultIntent = null;
                String title = extras.getString("title");
                String body = extras.getString("body");
                String icon = extras.getString("icon");
                String target_info = extras.getString("target_info");
                JSONObject target_info_data = null;
                String broker_name, broker_id, broker_code, notification_id;
                try {
                    target_info_data = new JSONObject(target_info);
                    broker_name = target_info_data.getString("broker_name");
                    broker_id = target_info_data.getString("broker_id");
                    broker_code = target_info_data.getString("broker_code");
                    notification_id = extras.getString("notification_id");
                    if (type.equalsIgnoreCase("13")) {
                        resultIntent = new Intent(getApplicationContext(), BrokerDetailsActivity.class);
                        resultIntent.putExtra("broker_name", broker_name);
                        resultIntent.putExtra("broker_id", broker_id);
                        resultIntent.putExtra("broker_code", broker_code);
                        resultIntent.putExtra("notification_id", notification_id);
                        resultIntent.putExtra("tag", TAG);
                    } else {
                        resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(icon)) {
                    showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                }
            }

        }

        else if (type.equalsIgnoreCase("15")) {
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                Intent resultIntent = null;
                String title = extras.getString("title");
                String body = "IVR LEADS";
                String icon = extras.getString("icon");
                String payload = extras.getString("payload");
                JSONObject target_info_data = null;

                try {
                    target_info_data = new JSONObject(payload);
                   String campaign = target_info_data.getString("campaign_title");
                   String mobile = target_info_data.getString("mobile_no");
                   String isLead =target_info_data.getString("isLead");

                   String notification_id = extras.getString("notification_id");
                 //   ArrayList<Parcelable> master_project_list = extras.getParcelableArrayList("master_project_list");
                    if (type.equalsIgnoreCase("15")) {
                        resultIntent = new Intent(getApplicationContext(), S1.class);
                        resultIntent.putExtra("campaign_title", campaign);
                        resultIntent.putExtra("mobile_no", mobile);
                        resultIntent.putExtra("IsLead", isLead);
                        resultIntent.putExtra("notification_id", notification_id);
                       // resultIntent.putExtra("master_project_list", master_project_list);


                        resultIntent.putExtra("tag", TAG);

                    }
                       else
                        {
                        resultIntent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(icon)) {

                    showNotificationMessage(getApplicationContext(), title, body, payload, resultIntent);
                }
                   else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, body, payload, resultIntent, icon);

                }
            }

        }
              else if (type.equalsIgnoreCase("14")) {
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                Intent resultIntent = null;
                String title = extras.getString("title");
                String body = extras.getString("body");
                String icon = extras.getString("icon");
                String target_info = extras.getString("target_info");
                JSONObject target_info_data = null;
                try {
                    target_info_data = new JSONObject(target_info);
                    String openType = target_info_data.getString("type");
                    String designation = target_info_data.getString("designation");
                    String enquiry_id = target_info_data.getString("enquiry_id");




                    if (openType.equalsIgnoreCase("sales")) {
                        if (designation.equalsIgnoreCase("0")) {
                            resultIntent = new Intent(getApplicationContext(), AssignedSalesDetailActivity.class);
                            resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                            resultIntent.putExtra(BMHConstants.PATH, "notification");
                            resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), FollowUpSalesDetailActivity.class);
                            resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                            resultIntent.putExtra(BMHConstants.PATH, "notification");
                            resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);
                        }
                    } else {
                        if (designation.equalsIgnoreCase("0")) {
                            resultIntent = new Intent(getApplicationContext(), SpDetailsActivity.class);
                            resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                            resultIntent.putExtra(BMHConstants.PATH, "notification");
                            resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);
                        }
                           else {
                            resultIntent = new Intent(getApplicationContext(), AsmDetailsActivity.class);
                            resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                            resultIntent.putExtra(BMHConstants.PATH, "notification");
                            resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(icon)) {
                    showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        gson = new Gson();
        listType = new TypeToken<MyTransactionsRespModel.Data>() {
        }.getType();
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            app = (BMHApplication) getApplication();
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                Log.e("JSON_OBJECT", object.toString());
                Integer type = object.getInt("type");
                if (type != null && type == 0) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String target_info = object.getString("target_info");
                    String notification_id = object.getString("notification_id");
                    /*if (type == 0) {
                        resultIntent = new Intent(getApplicationContext(), TermsAndConditionActivity.class);
                        resultIntent.putExtra("title", title);
                        resultIntent.putExtra("body", body);
                        resultIntent.putExtra("target_info", target_info);
                        resultIntent.putExtra("notification_id", notification_id);
                    } else {
                        resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                    }*/
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                }
                if (type != null && type == 1) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String target_info = object.getString("target_info");
                    String notification_id = object.getString("notification_id");
                    if (type == 1) {
                        resultIntent = new Intent(getApplicationContext(), BlogActivity.class);
                        resultIntent.putExtra("title", title);
                        resultIntent.putExtra("body", body);
                        resultIntent.putExtra("blog_id", target_info);
                        resultIntent.putExtra("notification_id", notification_id);
                    } else {
                        resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                    }
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                } else if (type != null && type == 2) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String target_info = object.getString("target_info");
                    String notification_id = object.getString("notification_id");
                   /* if (type == 2) {
                        resultIntent = new Intent(getApplicationContext(), FAQActivity.class);
                        resultIntent.putExtra("title", title);
                        resultIntent.putExtra("body", body);
                        resultIntent.putExtra("target_info", target_info);
                        resultIntent.putExtra("notification_id", notification_id);
                    } else {
                        resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                    }*/
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                } else if (type != null && type == 3) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String target_info = object.getString("target_info");
                    String notification_id = object.getString("notification_id");
                    if (type == 3) {
                        resultIntent = new Intent(getApplicationContext(), GetProjectListNotification.class);
                        //  IntentDataObject mIntentDataObjecProjectList = new IntentDataObject();
                        IntentDataObject mIntentDataObjecProjectList = new IntentDataObject();
                        mIntentDataObjecProjectList.putData(ParamsConstants.ID, target_info);
                        mIntentDataObjecProjectList.putData("notification_id", notification_id);
                        resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObjecProjectList);
                    }
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                } else if (type != null && type == 4) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String target_info = object.getString("target_info");
                    String notification_id = object.getString("notification_id");
                    if (type == 4) {
                        resultIntent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                        IntentDataObject mIntentDataObject = new IntentDataObject();
                        mIntentDataObject.putData(ParamsConstants.ID, target_info);
                        mIntentDataObject.putData("notification_id", notification_id);
                        //mIntentDataObject.putData(ParamsConstants.TITLE,title);
                        mIntentDataObject.putData(ParamsConstants.TYPE, ParamsConstants.BUY);
                        mIntentDataObject.putData(ParamsConstants.CITY_ID, app.getFromPrefs(BMHConstants.CITYID));
                        resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);

                    } else {
                        resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                    }
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                } else if (type != null && type == 6) {
                    Intent resultIntent = null;
                    String title = object.getString("title");
                    String body = object.getString("body");
                    String icon = object.getString("icon");
                    String project_id = object.getString("project_id");
                    String notification_id = object.getString("notification_id");
                    String target_info = object.getString("target_info");
                    String project_plan_img = object.getString("project_plan");
                    String cord = object.getString("cord");
                    String projecttype  = object.getString("project_type");
                    JSONObject coord = new JSONObject(cord);
                    String se = coord.getString("se");
                    String nw = coord.getString("nw");

                    if (type == 6) {
                        resultIntent = new Intent(getApplicationContext(), UnitMapActivity.class);
                        /*if(app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0){
                            resultIntent.putExtra(ParamsConstants.USER_ID,app.getFromPrefs(BMHConstants.USERID_KEY));
                        }*/
                        resultIntent.putExtra("unitIds", target_info);
                        resultIntent.putExtra("pro_plan_img", project_plan_img);
                        resultIntent.putExtra("projectId", project_id);
                        resultIntent.putExtra("notification_id", notification_id);
                        resultIntent.putExtra("se", se);
                        resultIntent.putExtra("nw", nw);
                        resultIntent.putExtra("project_type", projecttype);


                    } else {
                        resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                    }
                    if (TextUtils.isEmpty(icon)) {
                        showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                    }
                } else if (type != null && type == 7) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {

                        Map<String, String> payment_params = remoteMessage.getData();
                        JSONObject data = new JSONObject(payment_params);
                        //NotificationPayLoadData mNotificationPayLoadData = (NotificationPayLoadData) JsonParser.convertJsonToBean(APIType.NOTIFICATION_PAYLOAD,data.toString());
                        String title = data.getString("title");
                        String message = data.getString("message");
                        // boolean isBackground = data.getBoolean("is_background");
                        String imageUrl = data.getString("image");
                        String timestamp = data.getString("timestamp");
                        String notification_id = object.getString("notification_id");
                        String payload = object.getString("payload");
                        JSONObject payload_data = new JSONObject(payload);
                        String payment_mode = payload_data.getString("payment_mode");
                        Intent resultIntent = null;
                        if (payload != null) {
                            IntentDataObject mIntentDataObject = new IntentDataObject();
                            mIntentDataObject.putData(getString(R.string.txt_order_no), payload_data.optString(getString(R.string.txt_order_id)));
                            mIntentDataObject.putData(getString(R.string.txt_unit_no), payload_data.optString(getString(R.string.txt_unit_id)));
                            mIntentDataObject.putData(getString(R.string.txt_project_name), payload_data.optString(getString(R.string.txt_projectName)));
                            mIntentDataObject.putData(getString(R.string.payment_status), payload_data.optString(getString(R.string.txt_payment_status)));
                            //   mIntentDataObject.putData(getString(R.string.txt_payment_mode), payload_data.optString(getString(R.string.txt_paymentMode)));
                            //mIntentDataObject.putData(getString(R.string.txt_notification_id), notification_id);
                            mIntentDataObject.putData(getString(R.string.txt_order_no), payload_data.optString(getString(R.string.txt_order_id)));
                            mIntentDataObject.putData(getString(R.string.txt_cheque_amount), payload_data.optString(getString(R.string.txt_chequeAmount)));
                            mIntentDataObject.putData(getString(R.string.txt_cheque_no), payload_data.optString(getString(R.string.transaction_no)));
                            mIntentDataObject.putData(getString(R.string.txt_cheque_date), payload_data.optString(getString(R.string.txt_order_date)));
                            mIntentDataObject.putData(getString(R.string.txt_customer_name), payload_data.optString(getString(R.string.txt_customerName)));
                            mIntentDataObject.putData(getString(R.string.txt_customer_email), payload_data.optString(getString(R.string.txt_customerEmail)));
                            mIntentDataObject.putData(getString(R.string.txt_customer_mobile_no), payload_data.optString(getString(R.string.txt_customer_telephone)));
                            mIntentDataObject.putData(getString(R.string.txt_pan_aadhar_no), payload_data.optString(getString(R.string.txt_pan_no)));
                            mIntentDataObject.putData(getString(R.string.txt_coupon_code), payload_data.optString(getString(R.string.txt_couponCode)));
                            mIntentDataObject.putData(getString(R.string.txt_payment_plan_key), payload_data.optString(getString(R.string.txt_payment_plan_name_key)));
                            mIntentDataObject.putData(getString(R.string.txt_payment_plan_desc_key), payload_data.optString(getString(R.string.txt_payment_plan_desc_key)));


                            if (payment_mode.equalsIgnoreCase("CC")) {
                                mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_credit_card));
                            } else if (payment_mode.equalsIgnoreCase("DC")) {
                                mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_debit_card));
                            } else if (payment_mode.equalsIgnoreCase("NB")) {
                                mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_net_banking));
                            } else {
                                mIntentDataObject.putData(getString(R.string.txt_payment_mode), payment_mode);
                            }
                            resultIntent = new Intent(getApplicationContext(), PaymentStatusActivity.class);
                            resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                            resultIntent.putExtra(getString(R.string.txt_notification_id), notification_id);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                            resultIntent.putExtra("message", message);
                        }
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                        }

                    }
                } else if (type != null && type == 8) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent resultIntent = null;
                        String title = object.getString("title");
                        String body = object.getString("body");
                        String icon = object.getString("icon");
                        String payload = object.getString("payload");
                        JSONObject payload_data = new JSONObject(payload);
                        String unit_id = payload_data.getString("unit_id");
                        String unit_number = payload_data.getString("unit_number");
                        String project_name = payload_data.getString("project_name");
                        String payment_plan = payload_data.getString("payment_plan");
                        String unit_name = payload_data.getString("unit_name");
                        String unit_image = payload_data.getString("unit_image");
                        String isUnitReserved = payload_data.getString("isUnitReserved");
                        if (type == 8) {
                            IntentDataObject mIntentDataObject = new IntentDataObject();
                            mIntentDataObject.putData(ParamsConstants.UNIT_ID, unit_id);
                            mIntentDataObject.putData(ParamsConstants.UNIT_NO, unit_number);
                            mIntentDataObject.putData(ParamsConstants.UNIT_TITLE, project_name);
                            mIntentDataObject.putData(ParamsConstants.PAYMENT_PLAN, payment_plan);
                            mIntentDataObject.putData(ParamsConstants.BHK_TYPE, unit_name);
                            mIntentDataObject.putData(ParamsConstants.UNIT_IMAGE, unit_image);
                            mIntentDataObject.putData(ParamsConstants.UNIT_RESERVED, isUnitReserved);
                            resultIntent = new Intent(getApplicationContext(), ChatScreen.class);
                            resultIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                        }
                        if (TextUtils.isEmpty(icon)) {
                            showNotificationMessage(getApplicationContext(), title, body, payload, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, body, payload, resultIntent, icon);
                        }
                    }
                } else if (type != null && type == 9) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent resultIntent = null;
                        String title = object.getString("title");
                        String body = object.getString("body");
                        String icon = object.getString("icon");
                        if (type == 9) {
                            resultIntent = new Intent(getApplicationContext(), MyChat.class);
                            resultIntent.putExtra("target_info", title);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), SearchPropertyActivity.class);
                        }
                        if (TextUtils.isEmpty(icon)) {
                            showNotificationMessage(getApplicationContext(), title, body, title, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, body, title, resultIntent, icon);
                        }
                    }
                } else if (type != null && type == 10) {
                    Intent resultIntent = null;
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Map<String, String> payment_params = remoteMessage.getData();
                        JSONObject data = new JSONObject(payment_params);
                        //NotificationPayLoadData mNotificationPayLoadData = (NotificationPayLoadData) JsonParser.convertJsonToBean(APIType.NOTIFICATION_PAYLOAD,data.toString());
                        String title = data.getString("title");
                        String message = data.getString("message");
                        // boolean isBackground = data.getBoolean("is_background");
                        String imageUrl = data.getString("image");
                        String timestamp = data.getString("timestamp");
                        String notification_id = object.getString("notification_id");
                        String payload = object.getString("payload");
                        if (payload != null) {
                            JSONObject payload_data = new JSONObject(payload);
                            parseTransactionJson(payload_data);
                            if (myTransactionsModel != null) {
                                resultIntent = new Intent(getApplicationContext(), TransactionDetailsActivity.class);
                                resultIntent.putExtra("ChannelTransactionModel", myTransactionsModel);
                                resultIntent.putExtra("notification_id", notification_id);
                                resultIntent.putExtra("TAG_NAME", TAG_NAME);
                            } else {
                                resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                                resultIntent.putExtra("message", message);
                            }
                        }
                        // check for image attachment
                        if (TextUtils.isEmpty(imageUrl)) {
                            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                        }


                      }

                }


                else if (type != null && type == 13) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent resultIntent = null;
                        String title = object.getString("title");
                        String body = object.getString("body");
                        String icon = object.getString("icon");
                        String target_info = object.getString("target_info");
                        JSONObject target_info_data = new JSONObject(target_info);
                        String broker_name = target_info_data.getString("broker_name");
                        String broker_id = target_info_data.getString("broker_id");
                        String broker_code = target_info_data.getString("broker_code");
                        String notification_id = object.getString("notification_id");
                        if (type == 13) {
                            resultIntent = new Intent(getApplicationContext(), BrokerDetailsActivity.class);
                            resultIntent.putExtra("broker_name", broker_name);
                            resultIntent.putExtra("broker_id", broker_id);
                            resultIntent.putExtra("broker_code", broker_code);
                            resultIntent.putExtra("notification_id", notification_id);
                            resultIntent.putExtra("tag", TAG);
                        } else {
                            resultIntent = new Intent(getApplicationContext(), ProjectsListActivity.class);
                        }
                        if (TextUtils.isEmpty(icon)) {
                           showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                        }
                    }
                }
                else if (type != null && type == 15) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent resultIntent = null;

                        String title = object.getString("title");
                        String body = "IVR LEADS";
                        String icon = object.getString("icon");
                        String payload = object.getString("payload");
                        JSONObject target_info_data = new JSONObject(payload);
                       String campaign = target_info_data.getString("campaign_title");
                        String mobile = target_info_data.getString("mobile_no");
                        String isLead = target_info_data.getString("isLead");

                        String notification_id = object.getString("notification_id");
                       // JSONArray master_project_list = object.getJSONArray("master_project_list");
                        if (type == 15) {
                            resultIntent = new Intent(getApplicationContext(), S1.class);
                            resultIntent.putExtra("campaign_title", campaign);
                            resultIntent.putExtra("mobile_no", mobile);
                            resultIntent.putExtra("IsLead", isLead);
                            resultIntent.putExtra("notification_id", notification_id);
                           // resultIntent.putExtra("master_project_list", (Parcelable) master_project_list);

                            resultIntent.putExtra("tag", TAG);

                        } else {
                            resultIntent = new Intent(getApplicationContext(), ProjectDetailActivity.class);
                        }
                        if (TextUtils.isEmpty(icon)) {
                            showNotificationMessage(getApplicationContext(), title,body, payload, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title,body, payload, resultIntent, icon);
                        }
                    }
                }


                else if (type != null && type == 14  ) {
                    if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                        Intent resultIntent = null;
                        String title = object.getString("title");
                        String body = object.getString("body");
                        String icon = object.getString("icon");
                        String target_info = object.getString("target_info");
                        JSONObject target_info_data = new JSONObject(target_info);
                        String openType = target_info_data.getString("type");
                        String designation = target_info_data.getString("designation");
                        String enquiry_id = target_info_data.getString("enquiry_id");

                        if (openType.equalsIgnoreCase("sales")) {
                            if (designation.equalsIgnoreCase("0")) {
                                resultIntent = new Intent(getApplicationContext(), AssignedSalesDetailActivity.class);
                                resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                                resultIntent.putExtra(BMHConstants.PATH, "notification");
                                resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);
                            } else {
                                resultIntent = new Intent(getApplicationContext(), FollowUpSalesDetailActivity.class);
                                resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                                resultIntent.putExtra(BMHConstants.PATH, "notification");
                                resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);


                            }
                        } else {
                            if (designation.equalsIgnoreCase("0")) {
                                resultIntent = new Intent(getApplicationContext(), SpDetailsActivity.class);
                                resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                                resultIntent.putExtra(BMHConstants.PATH, "notification");
                                resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);
                            } else {
                                resultIntent = new Intent(getApplicationContext(), AsmDetailsActivity.class);
                                resultIntent.putExtra(BMHConstants.ENQUIRY_ID, enquiry_id);
                                resultIntent.putExtra(BMHConstants.PATH, "notification");
                                resultIntent.putExtra(BMHConstants.USER_DESIGNATION, designation);

                            }
                        }
                        if (TextUtils.isEmpty(icon)) {
                            showNotificationMessage(getApplicationContext(), title, body, target_info, resultIntent);
                        } else {
                            // image is present, show notification with image
                            showNotificationMessageWithBigImage(getApplicationContext(), title, body, target_info, resultIntent, icon);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void parseTransactionJson(JSONObject jsonObject) throws JSONException {
        myTransactionsModel = gson.fromJson(jsonObject.toString(), listType);
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
           else {
            // If the app is in background, firebase itself handles the notification
           }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        if (intent == null)
            return;
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent,null);
       // notificationUtils.showNotification(context, title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        if (intent == null)
            return;
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.app_icon_sp)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }
}