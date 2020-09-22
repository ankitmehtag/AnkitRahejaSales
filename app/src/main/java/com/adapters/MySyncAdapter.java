package com.adapters;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;

import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.database.UpdateStatusConverter;
import com.database.entity.AddAppointmentEntity;
import com.database.task.InsertAddAppointmentTask;
import com.model.SubStatus;
import com.sp.R;

import com.AppEnums.APIType;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.appnetwork.VolleyMultipartRequest;
import com.appnetwork.WEBAPI;
import com.database.AppDatabase;
import com.database.entity.CallRecordingEntity;
import com.database.entity.CallbackEntity;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.database.entity.StatusMasterEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMasterEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.database.task.ClosureMasterTask;
import com.database.task.InsertBrokerMasterTask;
import com.database.task.InsertCallbackTask;
import com.database.task.InsertClosureLeadsDetailTask;
import com.database.task.InsertMeetingTask;
import com.database.task.InsertMultiSelectTask;
import com.database.task.InsertPreSalesLeadDetailsTask;
import com.database.task.InsertProjectMasterTask;
import com.database.task.InsertSalesLeadDetailsTask;
import com.database.task.InsertSiteVisitTask;
import com.database.task.InsertStatusMasterTask;
import com.database.task.NotInterestedMasterTask;
import com.database.task.UpdateCallRecordingTask;
import com.database.task.UpdateSyncDataTask;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.model.Assign_To;
import com.model.Details;
import com.model.LeadStatus;
import com.model.NotInterestedLead;
import com.model.NotInterestedModel;
import com.model.Projects;
import com.sp.BMHApplication;
import com.utils.Connectivity;
import com.utils.StringUtil;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySyncAdapter extends AbstractThreadedSyncAdapter implements InsertAddAppointmentTask.IAppointmentCommunicator {

    Context mContext;
    //TODO change this constant SYNC_INTERVAL to change the sync frequency
    public static final int SYNC_INTERVAL = 60 * 60 * 10;       // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final int SYNCABLE = 1;
    public AppDatabase customerDb;

    public MySyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
        //   startSyncProcess();
        customerDb = AppDatabase.getInstance();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient providerClient,
                              SyncResult syncResult) {
        Log.i("MySyncAdapter", "onPerformSync for account[" + account.name + "]");
        try {
            //  notifyDataDownloaded();
            startSyncProcess();
        } catch (Exception e) {
            syncResult.hasHardError();
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, Account newAccount, int syncInterval, int flexTime) {
        // Account account = getSyncAccount(mContext);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(newAccount, authority)
                    .setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(newAccount, authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The mContext used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.i("MySyncAdapter", "syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with MySyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The mContext used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE); // Get an instance of the Android account manager
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type)); // Create the account type and default account
        // If the password doesn't exist, the account doesn't exist
        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                Log.e("MySyncAdapter", "getSyncAccount Failed to create new account.");
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        Log.i("MySyncAdapter", "onAccountCreated");
        MySyncAdapter.configurePeriodicSync(context, newAccount, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, BMHConstants.AUTHORITY, true);
        ContentResolver.setIsSyncable(newAccount, BMHConstants.AUTHORITY, SYNCABLE);
        ContentResolver.addPeriodicSync(newAccount, BMHConstants.AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        syncImmediately(context);
    }


    public void initializeSyncAdapter() {
        Log.d("MySyncAdapter", "initializeSyncAdapter");
        getSyncAccount(mContext);
    }

    private void startSyncProcess() {
        //   providerClient.getLocalContentProvider().getReadPermission();
        if (Connectivity.isConnected(mContext)) {
            new AutoSyncDataTask().execute();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        new AutoSyncRecordingTask().execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(r);
            thread.start();
        }
    }

    @Override
    public void getSyncAppointList(JSONObject jsonObject) {
        if (Connectivity.isConnected(mContext)) {
            if (jsonObject.optJSONArray("add_appointment_list").length() > 0) {
                syncAddAppointments(jsonObject);
            } else {
                getSalesData();
            }
        }
    }

    private void syncAddAppointments(final JSONObject jsonObject) {

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_ADD_APPOINTMENTS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            new UpdateSyncDataTask(response.optJSONArray("data"), mContext.getString(R.string.txt_add_appointment)).execute();
                            getSalesData();
                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                      }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
          /*  @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", jsonObject.toString());
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeJson);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "addAppointment_sync");
    }

    class AutoSyncDataTask extends AsyncTask<Void, Void, JSONObject> {

        private BMHApplication app;
        private JSONArray jsonArray, closureJArray;
        private JSONObject jsonObject, closureJObj;
        private Gson gson;
        private String leadPhase;
        JSONObject jsonObj = new JSONObject();
        private List<CallbackEntity> callbackEList;
        private List<MeetingEntity> meetingEList;
        private List<SiteVisitEntity> siteVisitEList;
        private PreSalesLeadDetailsEntity preSalesEntity;
        private List<SubStatusCallbackEntity> subStatusCallbackList;
        private List<SubStatusMeetingEntity> subStatusMeetingList;
        private List<SubStatusSiteVisitEntity> subStatusSiteVisitList;
        private List<SubStatusClosureEntity> subStatusClosureList;
        private List<SubStatusNotInterestedEntity> subStatusNotInterestedList;
        private List<SelectMultipleProjectsEntity> multipleProjectsEList;

        public AutoSyncDataTask() {
            this.app = BMHApplication.getInstance();
            gson = new Gson();
            leadPhase = BMHApplication.getInstance().getFromPrefs(BMHConstants.IS_PRE_SALES);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            if (leadPhase.equalsIgnoreCase(BMHConstants.LEAD_PHASE_PRE_SALES)) {
                List<PreSalesLeadDetailsEntity> detailsEntityList = AppDatabase.getInstance().getLeadDetailsDao().getLeadsToSync(1);
                try {
                    jsonArray = new JSONArray();
                    jsonObj.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                    jsonObj.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);

                    for (int i = 0; i < detailsEntityList.size(); i++) {
                        preSalesEntity = detailsEntityList.get(i);
                        String enquiryId = preSalesEntity.getEnquiryId();
                        multipleProjectsEList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);

                        // SEARCH ON EACH LEAD STATUS TO CREATE HISTORY
                        callbackEList = AppDatabase.getInstance().getCallbackDao().getAllCallbackForSync(enquiryId);
                        meetingEList = AppDatabase.getInstance().getMeetingDao().getAllMeetingsForSync(enquiryId);
                        siteVisitEList = AppDatabase.getInstance().getSiteVisitDao().getAllSiteVisitForSync(enquiryId);

                        jsonObject = new JSONObject();
                        String entString = gson.toJson(preSalesEntity);
                        jsonObject.put("enquiry_id", enquiryId);
                        jsonObject.put("lead_details", entString);
                        if (multipleProjectsEList.size() > 0) {
                            String multipleProjects = gson.toJson(multipleProjectsEList);
                            jsonObject.put("selected_project", multipleProjects);
                        }
                        if (callbackEList.size() > 0) {
                            String callbackEString = gson.toJson(callbackEList);
                            jsonObject.put("callback", callbackEString);
                        }
                        if (meetingEList.size() > 0) {
                            String meetingEString = gson.toJson(meetingEList);
                            jsonObject.put("meeting", meetingEString);
                        }
                        if (siteVisitEList.size() > 0) {
                            String siteVisitEString = gson.toJson(siteVisitEList);
                            jsonObject.put("site_visit", siteVisitEString);
                        }
                        jsonArray.put(i, jsonObject);
                    }
                    jsonObj.put("data_list", jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                List<SalesClosureLeadsDetailEntity> closureList = AppDatabase.getInstance().getClosureLeadDetailsDao().getClosureLeadsToSync(1);
                List<SalesLeadDetailEntity> salesEntityList = AppDatabase.getInstance().getSalesLeadDetailsDao().getSalesLeadsToSync(1);
                try {
                    jsonObject = new JSONObject();

                    jsonObj.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                    jsonObj.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);

                    if (closureList != null) {
                        SalesClosureLeadsDetailEntity closureEntity;
                        int closureSize = closureList.size();
                        closureJArray = new JSONArray();
                        for (int i = 0; i < closureSize; i++) {
                            closureEntity = closureList.get(i);
                            closureJObj = new JSONObject();
                            String entString = gson.toJson(closureEntity);
                            closureJObj.put("closure_details", entString);
                            closureJArray.put(i, closureJObj);
                        }
                        jsonObj.put("closure_list", closureJArray);
                    }
                    if (salesEntityList != null) {
                        SalesLeadDetailEntity salesEntity;
                        int salesSize = salesEntityList.size();
                        jsonArray = new JSONArray();
                        for (int i = 0; i < salesSize; i++) {
                            salesEntity = salesEntityList.get(i);
                            String enquiryId = salesEntity.getEnquiryId();
                            multipleProjectsEList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);

                            // SEARCH ON EACH LEAD STATUS TO CREATE HISTORY
                            subStatusCallbackList = AppDatabase.getInstance().getSubStatusCallbackDao().getAllCallbackForSync(enquiryId);
                            subStatusMeetingList = AppDatabase.getInstance().getSubStatusMeetingDao().getSubStatusMeetingForSync(enquiryId);
                            subStatusSiteVisitList = AppDatabase.getInstance().getSubStatusSiteVisitDao().getSubStatusSiteVisitForSync(enquiryId);
                            subStatusClosureList = AppDatabase.getInstance().getSubStatusClosureDao().getSubClosureForSync(enquiryId);
                            subStatusNotInterestedList = AppDatabase.getInstance().getSubStatusNotInterestedDao().getSubStNotInterestedForSync(enquiryId);

                            jsonObject = new JSONObject();

                            String entString = gson.toJson(salesEntity);
                            jsonObject.put("enquiry_id", enquiryId);
                            jsonObject.put("lead_details", entString);
                            if (multipleProjectsEList.size() > 0) {
                                String multipleProjects = gson.toJson(multipleProjectsEList);
                                jsonObject.put("selected_project", multipleProjects);
                            }
                            if (subStatusCallbackList.size() > 0) {
                                String callbackEString = gson.toJson(subStatusCallbackList);
                                jsonObject.put("callback", callbackEString);
                            }
                            if (subStatusMeetingList.size() > 0) {
                                String meetingEString = gson.toJson(subStatusMeetingList);
                                jsonObject.put("meeting", meetingEString);
                            }
                            if (subStatusSiteVisitList.size() > 0) {
                                String siteVisitEString = gson.toJson(subStatusSiteVisitList);
                                jsonObject.put("site_visit", siteVisitEString);
                            }
                            if (subStatusClosureList.size() > 0) {
                                String closureEString = gson.toJson(subStatusClosureList);
                                jsonObject.put("closure", closureEString);
                            }
                            if (subStatusNotInterestedList.size() > 0) {
                                String notInterestedEString = gson.toJson(subStatusNotInterestedList);
                                jsonObject.put("not_interested", notInterestedEString);
                            }
                            jsonArray.put(i, jsonObject);
                        }
                    }
                    jsonObj.put("data_list", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                if (leadPhase.equalsIgnoreCase(BMHConstants.LEAD_PHASE_PRE_SALES))
                    startPreSalesOfflineSync(jsonObject);
                else
                    startSalesOfflineSync(jsonObject);
            }
        }
    }

    private void getPreSalesData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_USER_PRE_SALES_LIST),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        return;
                                    }
                                    insertToDb(jsonObject);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_DESIGNATION, BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION)));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void getSalesData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.USER_SALES),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    if (jsonObject.optString("message").equalsIgnoreCase("No Data Found")) {
                                        return;
                                    }
                                    saveToRoomDb(jsonObject, BMHConstants.SP_DESIGNATION);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("", "VolleyError " + error);
                    }
                }
        )
                  {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_DESIGNATION, BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION)));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void saveToRoomDb(JSONObject jsonObject, String designation) {

        JSONArray jsonArray = null;
        SalesBrokerMasterEntity salesBrokerMasterEntity;
        ProjectMasterEntity projectMasterEntity;
        StatusMasterEntity statusMasterEntity;
        NotInterestedMasterEntity notInterestedMasterEntity;
        ClosureMasterEntity closureMasterEntity;
        List<SalesBrokerMasterEntity> mBrokerMasterList = new ArrayList<>();
        List<ProjectMasterEntity> mProjectsMasterList = new ArrayList<>();
        List<StatusMasterEntity> mStatusMasterList = new ArrayList<>();
        List<NotInterestedMasterEntity> mNotInterestedMasterList = new ArrayList<>();
        List<ClosureMasterEntity> mClosureMasterList = new ArrayList<>();
        List<SubStatusMasterEntity> mSubStatusList = new ArrayList<>();
        ArrayList<NotInterestedLead> brokerList = new ArrayList<>();
        ArrayList<LeadStatus> leadStatusList = new ArrayList<>();
        ArrayList<NotInterestedModel> notInterestedList = new ArrayList<>();
        ArrayList<NotInterestedModel> closureList = new ArrayList<>();
        List<SalesLeadDetailEntity> mLeadDetailList = new ArrayList<>();

        try {
            String projectName = "";
            JSONArray projectArray = jsonObject.optJSONArray("projects");
            if (projectArray != null) {
                int projLength = projectArray.length();
                for (int i = 0; i < projLength; i++) {
                    JSONObject object = projectArray.getJSONObject(i);
                    String projectId = object.optString("project_id");
                    projectName = object.optString("project_name");
                    projectMasterEntity = new ProjectMasterEntity(projectId, projectName);
                    mProjectsMasterList.add(projectMasterEntity);
                }
                new InsertProjectMasterTask(mProjectsMasterList).execute();
            }
            JSONArray brokersArray = jsonObject.optJSONArray("brokers");
            if (brokersArray != null) {
                int brokerLength = brokersArray.length();
                for (int i = 0; i < brokerLength; i++) {
                    JSONObject object = brokersArray.getJSONObject(i);
                    String salesId = object.optString("id");
                    String salesName = object.optString("title");
                    String address = object.optString("address");
                    brokerList.add(new NotInterestedLead(salesId, salesName, address));
                    salesBrokerMasterEntity = new SalesBrokerMasterEntity(salesId, salesName, address);
                    mBrokerMasterList.add(salesBrokerMasterEntity);
                }
                new InsertBrokerMasterTask(mBrokerMasterList).execute();
            }
            JSONArray leadArray = jsonObject.optJSONArray("lead_status");
            if (leadArray != null) {
                int leadLength = leadArray.length();
                for (int i = 0; i < leadLength; i++) {
                    JSONObject object = leadArray.getJSONObject(i);
                    String dispositionId = object.optString("disposition_id");
                    String name = object.optString("title");
                    leadStatusList.add(new LeadStatus(dispositionId, name));
                    statusMasterEntity = new StatusMasterEntity(name, dispositionId, 0);
                    mStatusMasterList.add(statusMasterEntity);
                }
                new InsertStatusMasterTask(mStatusMasterList).execute();
            }

            JSONArray notInterestedArray = jsonObject.optJSONArray("not_interested");
            if (leadArray != null) {
                int notInterestedLength = notInterestedArray.length();
                for (int i = 0; i < notInterestedLength; i++) {
                    JSONObject object = notInterestedArray.getJSONObject(i);
                    String id = object.optString("id");
                    String name = object.optString("title");
                    notInterestedList.add(new NotInterestedModel(id, name));
                    notInterestedMasterEntity = new NotInterestedMasterEntity(id, name);
                    mNotInterestedMasterList.add(notInterestedMasterEntity);
                }
                new NotInterestedMasterTask(mNotInterestedMasterList).execute();
            }

            JSONArray closureArray = jsonObject.optJSONArray("closure");
            if (leadArray != null) {
                int closureLength = closureArray.length();
                for (int i = 0; i < closureLength; i++) {
                    JSONObject object = closureArray.getJSONObject(i);
                    String id = object.optString("id");
                    String name = object.optString("title");
                    closureList.add(new NotInterestedModel(id, name));
                    closureMasterEntity = new ClosureMasterEntity(id, name);
                    mClosureMasterList.add(closureMasterEntity);
                }
                new ClosureMasterTask(mClosureMasterList).execute();
            }

            jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray != null) {
                int arrayLength = jsonArray.length();
                for (int i = 0; i < arrayLength; i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    int isAssigned = jObject.optInt("isAssigned");
                    String enquiryId = jObject.optString("enquiry_id");
                   /* String cust_mob = jObject.optString("customer_mobile");
                    String cust_name = jObject.optString("customer_name");
                    customer = new CustomerInfoEntity(cust_mob, enquiryId, cust_name, BMHConstants.LEAD_PHASE_SALES, String.valueOf(isAssigned));
                    mCustomerList.add(customer);*/
                    String isLeadType = jObject.optString("is_lead_type");
                    String lastUpdatedOn = jObject.optString("lastupdatedon");
                    String scheduledDateTime = jObject.optString("scheduledatetime");
                    JSONObject object = jObject.optJSONObject("details");
                    ArrayList<Projects> selectedProjList = new ArrayList<>();

                    String custMob = object.optString("Mobile_No");
                    String custAlternateMob = object.optString("Alternate_Mobile_No");
                    String custName = object.optString("Name");
                    String custEmail = object.optString("Email_ID");
                    String campaignName = object.optString("Campaign");
                    String campaignDate = object.optString("Campaign_Date");
                    String budget = object.optString("Budget");
                    String currentStatus = object.optString("Current_Status");
                    String date = object.optString("date");
                    String time = object.optString("time");
                    String address = object.optString("Address");
                    String remark = object.optString("remark");
                    String leadType = object.optString("lead_type");
                    String broker_name = object.optString("broker_name");
                    int noOfPersons = object.optInt("no_of_persons");
                    String unitNo = object.optString("unit_no");
                    String amount = object.optString("amount");
                    String towerNo = object.optString("tower_no");
                    String chequeNo = object.optString("cheque_number");
                    String chequeDate = object.optString("cheque_date");
                    String bankName = object.optString("bank_name");

                    JSONArray selectedList = object.optJSONArray("selected_project");
                    if (selectedList != null) {
                        int selectedLength = selectedList.length();
                        for (int j = 0; j < selectedLength; j++) {
                            JSONObject obj = selectedList.optJSONObject(j);
                            selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                        }
                    }
                    String subStatusItem = "", subStatusId = "";
                    ArrayList<SubStatus> updateStatusList = new ArrayList<>();
                    if (isAssigned == 2) {
                        subStatusItem = object.optString("closureSubStatus");
                    } else {
                       /* JSONArray subStatusArray = object.optJSONArray("subStatus");
                        if (subStatusArray != null && subStatusArray.length() > 0) {
                            StringBuilder builderStatus = new StringBuilder();
                            StringBuilder builderStatusId = new StringBuilder();
                            for (int j = 0; j < subStatusArray.length(); j++) {
                                JSONObject obj = subStatusArray.getJSONObject(j);
                                mSubStatusList.add(new SubStatusMasterEntity(obj.optString("id"), obj.optString("title")));
                                builderStatus.append(obj.optString("title")).append(",");
                                builderStatusId.append(obj.optString("id")).append(",");
                            }
                            subStatusItem = StringUtil.removeLastComma(builderStatus.toString());
                            subStatusId = StringUtil.removeLastComma(builderStatusId.toString());
                        }*/
                        JSONArray subStatusArray = object.optJSONArray("subStatus");
                        if (subStatusArray != null) {
                            int selectedLength = subStatusArray.length();
                            for (int j = 0; j < selectedLength; j++) {
                                JSONObject obj = subStatusArray.optJSONObject(j);
                                updateStatusList.add(new SubStatus(obj.optString("id"), obj.optString("title")));
                            }
                        }
                    }

                    if (selectedProjList != null && selectedProjList.size() > 0) {
                        SelectMultipleProjectsEntity multiSelectEntity =
                                new SelectMultipleProjectsEntity(enquiryId, Utils.getSelectedMultiProjectIds(selectedProjList), Utils.getMultiSelectedProject(selectedProjList),
                                        0, "", Utils.getCurrentDateTime());
                        new InsertMultiSelectTask(multiSelectEntity, 0).execute();
                    }

                    String userType = designation.equalsIgnoreCase("0") ? BMHConstants.SP_DESIGNATION : BMHConstants.ASM_DESIGNATION;

                    // CLOSURE LEADS DATA
                    if (isAssigned == 2) {
                        SalesClosureLeadsDetailEntity closureEntity = new SalesClosureLeadsDetailEntity(enquiryId, custName, custMob, custEmail,
                                budget, projectName, currentStatus, subStatusItem, unitNo, scheduledDateTime, amount, towerNo, chequeNo, chequeDate,
                                bankName, remark, 0, 0, lastUpdatedOn, designation);

                        new InsertClosureLeadsDetailTask(getContext(), closureEntity).execute();
                    }

                    SalesLeadDetailEntity detailEntity = new SalesLeadDetailEntity(enquiryId, custName, custEmail, custMob,
                            custAlternateMob, Utils.getMultiSelectedProject(selectedProjList), Utils.getSelectedMultiProjectIds(selectedProjList),
                            budget, broker_name, Utils.getBrokerId(brokerList, broker_name), isLeadType, currentStatus,
                            Utils.getLeadStatusId(leadStatusList, currentStatus), scheduledDateTime, date, time, isAssigned, address, noOfPersons,
                            "", UpdateStatusConverter.fromArrayList(updateStatusList), unitNo, amount, towerNo, chequeNo, chequeDate, bankName, leadType, remark, -1,
                            "", "", "", "", "", "", 0,
                            "", "", "",
                            campaignName, campaignDate, 0, 0, 0, lastUpdatedOn,
                            userType, -1, "");
                    mLeadDetailList.add(detailEntity);
                }
                new InsertSalesLeadDetailsTask(getContext(), mLeadDetailList).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void insertToDb(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        JSONObject objLeads = null;
        PreSalesLeadDetailsEntity preSalesLeadDetailsEntity;
        ArrayList<Details> mDetailsList = new ArrayList<>();
        List<PreSalesLeadDetailsEntity> mPreSalesLeadDetailList = new ArrayList<>();
        if (jsonArray != null) {
            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {
                int isAssigned = jsonArray.optJSONObject(i).optInt("isAssigned");
                String assignedTo = jsonArray.optJSONObject(i).optString("salesperson_name");
                String lastUpdatedOn = jsonArray.optJSONObject(i).optString("lastupdatedon");
                String scheduledDateTime = jsonArray.optJSONObject(i).optString("scheduledatetime");
                JSONObject object = jsonArray.optJSONObject(i).optJSONObject("details");
                ArrayList<Assign_To> assignList = new ArrayList<>();
                ArrayList<LeadStatus> leadsList = new ArrayList<>();
                ArrayList<Projects> projectsList = new ArrayList<>();
                ArrayList<Projects> selectedProjList = new ArrayList<>();

                String custMob = object.optString("Mobile_No");
                String custAlternateMob = object.optString("Alternate_Mobile_No");
                String enquiryId = object.optString("Enquiry_ID");
                String custName = object.optString("Name");
                String custEmail = object.optString("Email_ID");
                String campaignName = object.optString("Campaign");
                String campaignDate = object.optString("Campaign_Date");
                String budget = object.optString("Budget");
                String currentStatus = object.optString("Current_Status");

                String date = object.optString("date");
                String time = object.optString("time");
                String address = object.optString("Address");
                String projectName = object.optString("Project_Name");
                String remark = object.optString("remark");
                String leadType = object.optString("lead_type");
                String budget_min = object.optString("budget_min");
                String budget_max = object.optString("budget_max");
                int noOfPersons = object.optInt("no_of_persons");


                JSONArray projectArray = object.optJSONArray("Project_List");
                if (projectArray != null) {
                    int projectLength = projectArray.length();
                    for (int j = 0; j < projectLength; j++) {
                        JSONObject obj = projectArray.optJSONObject(j);
                        projectsList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                JSONArray selectedList = object.optJSONArray("selected_project");
                if (selectedList != null) {
                    int selectedLength = selectedList.length();
                    for (int j = 0; j < selectedLength; j++) {
                        JSONObject obj = selectedList.optJSONObject(j);
                        selectedProjList.add(new Projects(obj.optString("project_id"), obj.optString("project_name")));
                    }
                }
                JSONArray array = object.optJSONArray("Assign_To");
                if (array != null) {
                    int assignLength = array.length();
                    for (int j = 0; j < assignLength; j++) {
                        JSONObject obj = array.optJSONObject(j);
                        assignList.add(new Assign_To(obj.optString("salesperson_id"), obj.optString("salesperson_name")));
                    }
                }
                JSONArray arrayLead = object.optJSONArray("lead_status");
                if (arrayLead != null) {

                    int leadLength = arrayLead.length();
                    for (int j = 0; j < leadLength; j++) {
                        objLeads = arrayLead.optJSONObject(j);
                        leadsList.add(new LeadStatus(objLeads.optString("disposition_id"), objLeads.optString("title")));
                    }
                }

                mDetailsList.add(new Details(enquiryId, campaignName, campaignDate, custName, custEmail, custMob,
                        custAlternateMob, budget, currentStatus, scheduledDateTime, date, time, address, projectName,
                        projectsList, selectedProjList, assignList, leadsList, remark, leadType, budget_min, budget_max,
                        noOfPersons, lastUpdatedOn));

                if (currentStatus.equalsIgnoreCase(getContext().getResources().getStringArray(R.array.leads_array)[3])) {
                    CallbackEntity callbackEntity = new CallbackEntity(enquiryId, "",
                            scheduledDateTime, date, time, "", remark, leadType, 0,
                            "", lastUpdatedOn);
                    new InsertCallbackTask(callbackEntity).execute();

                }
                if (currentStatus.equalsIgnoreCase(getContext().getResources().getStringArray(R.array.leads_array)[4])) {
                    MeetingEntity meetingEntity = new MeetingEntity(Long.getLong(""), enquiryId, currentStatus, time,
                            date, address, leadType, remark, scheduledDateTime, Utils.getCurrentDateTime(), 0,
                            "", lastUpdatedOn);
                    new InsertMeetingTask(meetingEntity).execute();

                }
                if (currentStatus.equalsIgnoreCase(getContext().getResources().getStringArray(R.array.leads_array)[5])) {
                    SiteVisitEntity siteVisitEntity = new SiteVisitEntity(enquiryId, currentStatus, "",
                            date, time, address, leadType, String.valueOf(noOfPersons), remark, "",
                            "", scheduledDateTime, "", "", 0,
                            "", lastUpdatedOn);
                    new InsertSiteVisitTask(siteVisitEntity).execute();
                }
                //     List<SelectMultipleProjectsEntity> multiList = new ArrayList<>();
                int size = selectedProjList.size();
                if (size > 0) {
                    StringBuilder builderProId = new StringBuilder();
                    StringBuilder builderProName = new StringBuilder();
                    for (int j = 0; j < size; j++) {
                        builderProId.append(selectedProjList.get(j).getProject_id()).append(",");
                        builderProName.append(selectedProjList.get(j).getProject_name()).append(",");
                    }
                    SelectMultipleProjectsEntity multiProjEntity = new SelectMultipleProjectsEntity(enquiryId,
                            StringUtil.removeLastComma(builderProId.toString()),
                            StringUtil.removeLastComma(builderProName.toString()), 0, "",
                            lastUpdatedOn);
                    new InsertMultiSelectTask(multiProjEntity, 0).execute();
                }

                preSalesLeadDetailsEntity = new PreSalesLeadDetailsEntity(enquiryId, custName, custMob,
                        custEmail, custAlternateMob, Utils.getLeadStatusId(leadsList, currentStatus),
                        currentStatus, scheduledDateTime, Utils.getSelectedMultiProjectIds(selectedProjList), remark,
                        budget, date, time, budget_min, budget_max,
                        "", assignedTo, Utils.getAssignToId(assignList, assignedTo), isAssigned, campaignName, campaignDate, 0,
                        0, 0, lastUpdatedOn, getContext().getString(R.string.txt_asm), "");
                mPreSalesLeadDetailList.add(preSalesLeadDetailsEntity);
            }
            new InsertPreSalesLeadDetailsTask(getContext(), mPreSalesLeadDetailList).execute();
        }
    }

    private void startPreSalesOfflineSync(final JSONObject jsonObject) {

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_OFFLINE_LEADS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            try {
                                new UpdateSyncDataTask(BMHConstants.LEAD_PHASE_SALES, response.optJSONArray("data")).execute();
                                Thread.sleep(1000);
                                getPreSalesData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeJson);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "pre_sales_sync");
    }

    private void startSalesOfflineSync(final JSONObject jsonObject) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_OFFLINE_SALES_LEADS),
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        if (response.optBoolean("success")) {
                            new UpdateSyncDataTask(BMHConstants.LEAD_PHASE_SALES, response.optJSONArray("data")).execute();
                            try {
                                Thread.sleep(1000);
                                //  getSalesData();
                                new AutoSyncAddAppointmentTask().execute();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeJson);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "sales_sync");
    }

    class AutoSyncAddAppointmentTask extends AsyncTask<Void, Void, JSONObject> {
        private List<AddAppointmentEntity> addAppointList;
        JSONObject jsonObj = new JSONObject();
        JSONObject appointJObj;
        JSONArray appointJArray;
        Gson gson;

        @Override
        protected JSONObject doInBackground(Void... voids) {
            addAppointList = AppDatabase.getInstance().getAddAppointmentDao().getAppointmentsToSync(1);
            try {
                jsonObj.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                jsonObj.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                jsonObj.put(ParamsConstants.USER_DESIGNATION, BMHApplication.getInstance().getFromPrefs((BMHConstants.USER_DESIGNATION)));

                if (addAppointList != null) {
                    AddAppointmentEntity appointEntity;
                    gson = new Gson();
                    int appointSize = addAppointList.size();
                    appointJArray = new JSONArray();
                    for (int i = 0; i < appointSize; i++) {
                        appointEntity = addAppointList.get(i);
                        appointJObj = new JSONObject();
                        String entString = gson.toJson(appointEntity);
                        appointJObj.put("add_appointment", entString);
                        appointJArray.put(i, appointJObj);
                    }
                    jsonObj.put("add_appointment_list", appointJArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (Connectivity.isConnected(BMHApplication.getInstance())) {
                if (jsonObject.optJSONArray("add_appointment_list").length() > 0) {
                    syncAddAppointments(jsonObject);
                } else {
                    //   Toast.makeText(SalesActivity.this, getString(R.string.txt_device_data_synced), Toast.LENGTH_SHORT).show();
                    getSalesData();
                }
            }
        }
    }

    class AutoSyncRecordingTask extends AsyncTask<Void, String, List<CallRecordingEntity>> {
        List<CallRecordingEntity> recordingEList;

        @Override
        protected List<CallRecordingEntity> doInBackground(Void... voids) {
            recordingEList = AppDatabase.getInstance().getCallRecordingDao().getRecordingToSync(1);
            return null;
        }

        @Override
        protected void onPostExecute(List<CallRecordingEntity> callRecordingEntities) {
            super.onPostExecute(callRecordingEntities);
            if (recordingEList.size() > 0) {
                CallRecordingEntity recordingEntity;
                for (int i = 0; i < recordingEList.size(); i++) {
                    recordingEntity = recordingEList.get(i);
                    File file = Utils.getCallRecordingDir(mContext, recordingEntity.getRecFileName());
                    //File file = new File(recordingEntity.getRecFileName());
                    if (file.exists()) {
                        // SYNC UNIVERSAL CALL RECORDINGS SYNC
                        if (TextUtils.isEmpty(recordingEntity.getEnquiryId())) {
                            syncUniversalRecording(recordingEntity.getMobileNo(), file.getPath(),
                                    recordingEntity.getRemark(), recordingEntity.getTimeStamp());
                        } else {
                            // NORMAL CALL RECORDINGS SYNC
                            //File file = new File(recordingEntity.getRecFileName());
                            syncCallRecordings(recordingEntity.getEnquiryId(), recordingEntity.getMobileNo(), file.getPath(),
                                    recordingEntity.getLeadUpdateStatus(), recordingEntity.getTimeStamp());
                        }
                    }
                }
                // getPreSalesData();
            }

            new InsertAddAppointmentTask(mContext).execute();
        }
    }

    private void syncUniversalRecording(final String mobileNo, final String filePath, final String remark, final String timeStamp) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_UNIVERSAL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String resultResponse = new String(response.data);
                            if (resultResponse.equalsIgnoreCase(null)) {
                                return;
                            }
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(BMHConstants.MOBILE_NO, mobileNo);
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                params.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                params.put(BMHConstants.TIME_STAMP, timeStamp);
                params.put(BMHConstants.REMARK, remark);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, mContext)));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                //   headers.put("Content-Type", WEBAPI.contentTypeAudio);
                return headers;
            }
        };

        BMHApplication.getInstance().addToRequestQueue(multipartRequest, "call_recording_sync");
    }

    private void syncCallRecordings(final String enquiryId, final String mobileNo, final String filePath, final int leadUpdateStatus, final String timeStamp) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SYNC_CALL_RECORDING),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String resultResponse = new String(response.data);
                            JSONObject result = new JSONObject(resultResponse);
                            if (result.optBoolean("success")) {
                                // Toast.makeText(mContext, result.optString("message"), Toast.LENGTH_SHORT).show();
                                new UpdateCallRecordingTask(result).execute();
                            } else {
                                //  Toast.makeText(PreSalesActivity.this, result.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, BMHApplication.getInstance().getFromPrefs(BMHConstants.USERID_KEY));
                params.put(BMHConstants.ENQUIRY_ID, enquiryId);
                params.put(BMHConstants.MOBILE_NO, mobileNo);
                params.put(BMHConstants.LEAD_UPDATE_STATUS, String.valueOf(leadUpdateStatus));
                params.put(BMHConstants.TIME_STAMP, timeStamp);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("uploadedfile", new DataPart(new File(filePath).getName(), Utils.getFileInBytes(filePath, mContext)));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                return headers;
            }
        };

        BMHApplication.getInstance().addToRequestQueue(multipartRequest, "call_recording_sync");
    }
}
