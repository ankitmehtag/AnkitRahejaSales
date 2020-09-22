package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CallbackEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.google.gson.Gson;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SyncDataTask extends AsyncTask<Void, Void, JSONObject> {

    private String leadPhase;
    private PreSalesLeadDetailsEntity preSalesEntity;
    private List<CallbackEntity> callbackEList;
    private List<MeetingEntity> meetingEList;
    private List<SiteVisitEntity> siteVisitEList;
    private List<SubStatusCallbackEntity> subStatusCallbackList;
    private List<SubStatusMeetingEntity> subStatusMeetingList;
    private List<SubStatusSiteVisitEntity> subStatusSiteVisitList;
    private List<SubStatusClosureEntity> subStatusClosureList;
    private List<SubStatusNotInterestedEntity> subStatusNotInterestedList;
    private List<SelectMultipleProjectsEntity> multipleProjectsEList;
    private ISyncDataCommunicator communicator;
    private BMHApplication app;
    JSONArray jsonArray, closureJArray;
    JSONObject jsonObject, closureJObj;
    Gson gson;
    JSONObject jsonObj = new JSONObject();

    // only retain a weak reference to the activity
    public SyncDataTask(Context context, String leadPhase) {
        communicator = (ISyncDataCommunicator) context;
        this.leadPhase = leadPhase;
        gson = new Gson();
        app = BMHApplication.getInstance();
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected JSONObject doInBackground(Void... objs) {

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
                        // String enquiryId = closureEntity.getEnquiryId();
                        // SalesClosureLeadsDetailEntity clEntity = AppDatabase.getInstance().getClosureLeadDetailsDao().getClosureLeadsByEnquiryId(enquiryId);
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
    protected void onPostExecute(JSONObject aVoid) {
        super.onPostExecute(aVoid);
        communicator.getSyncDataCallback(jsonObj);
    }

    public interface ISyncDataCommunicator {
        void getSyncDataCallback(JSONObject jsonObject);
    }
}
