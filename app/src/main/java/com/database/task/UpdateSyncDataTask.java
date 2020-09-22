package com.database.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.database.AppDatabase;
import com.helper.BMHConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateSyncDataTask extends AsyncTask<Void, Void, String> {

    private JSONArray jsonArray;
    private String leadPhase;
    private String syncDataType;

    public UpdateSyncDataTask(String leadPhase, JSONArray jsonArray) {
        this.leadPhase = leadPhase;
        this.jsonArray = jsonArray;
    }

    public UpdateSyncDataTask(JSONArray jsonArray, String syncDataType) {
        this.syncDataType = syncDataType;
        this.jsonArray = jsonArray;
    }

    @Override
    protected String doInBackground(Void... voids) {

        int length = jsonArray.length();
        if (!TextUtils.isEmpty(syncDataType)) {
            if (length > 0) {
                AppDatabase.getInstance().getAddAppointmentDao().deleteSyncData(1);
            }
        } else {
            if (leadPhase.equalsIgnoreCase(BMHConstants.LEAD_PHASE_PRE_SALES)) {
                JSONObject jsonObject;
                for (int i = 0; i < length; i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.optBoolean("status")) {
                            String enquiryId = jsonObject.optString("enquiry_id");
                            AppDatabase.getInstance().getLeadDetailsDao().deleteLeadByEnquiryId(enquiryId);
                            if (AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId).size() > 0)
                                AppDatabase.getInstance().getMultiSelProjectDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getCallbackDao().getCallbackList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getCallbackDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getMeetingDao().getMeetingList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getMeetingDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSiteVisitDao().getSiteVisitList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSiteVisitDao().deleteSyncedData(enquiryId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                JSONObject jsonObject;
                for (int i = 0; i < length; i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.optBoolean("status")) {
                            String enquiryId = jsonObject.optString("enquiry_id");
                            AppDatabase.getInstance().getSalesLeadDetailsDao().deleteLeadByEnquiryId(enquiryId);
                            if (AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId).size() > 0)
                                AppDatabase.getInstance().getMultiSelProjectDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSubStatusCallbackDao().getSubStatusCallbackList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSubStatusCallbackDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSubStatusMeetingDao().getSubStatusMeetingList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSubStatusMeetingDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSubStatusSiteVisitDao().getSubStatusSiteVisitList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSubStatusSiteVisitDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSubStatusClosureDao().getSubStatusClosureList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSubStatusClosureDao().deleteSyncedData(enquiryId);
                            if (AppDatabase.getInstance().getSubStatusNotInterestedDao().getSubStNotInterestedList(enquiryId).size() > 0)
                                AppDatabase.getInstance().getSubStatusNotInterestedDao().deleteSyncedData(enquiryId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
