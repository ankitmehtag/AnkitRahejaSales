package com.database.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.database.AppDatabase;
import com.database.entity.CallbackEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.sp.R;

import java.util.List;

public class GetPreSalesLeadDetailTask extends AsyncTask<Void, Void, String> {

    private String enquiryId;
    private Context context;
    private PreSalesLeadDetailsEntity preSalesLeadDetailsEntity;
    private CallbackEntity callbackEntity;
    private MeetingEntity meetingEntity;
    private SiteVisitEntity siteVisitEntity;
    private List<SelectMultipleProjectsEntity> multipleProjectsEntityList;
    private ILeadDetailCommunicator communicator;

    // only retain a weak reference to the activity
    public GetPreSalesLeadDetailTask(Activity context, String enquiryId) {
        this.enquiryId = enquiryId;
        communicator = (ILeadDetailCommunicator) context;
        this.context = context;
    }


    // doInBackground methods runs on a worker thread
    @Override
    protected String doInBackground(Void... objs) {
        multipleProjectsEntityList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);
        preSalesLeadDetailsEntity = AppDatabase.getInstance().getLeadDetailsDao().getDetailByEnquiryId(enquiryId);
        String currentStatus = preSalesLeadDetailsEntity.getCurrentStatus();


        if (!TextUtils.isEmpty(currentStatus) ) {

            if (currentStatus.equalsIgnoreCase(context.getResources().getStringArray(R.array.leads_array)[3])) {
                callbackEntity = AppDatabase.getInstance().getCallbackDao().getCallbackByEnquiryId(enquiryId);
                return currentStatus;
            }

            if (currentStatus.equalsIgnoreCase(context.getResources().getStringArray(R.array.leads_array)[4])) {
                meetingEntity = AppDatabase.getInstance().getMeetingDao().getMeetingByEnquiryId(enquiryId);
                return currentStatus;
            }

            if (currentStatus.equalsIgnoreCase(context.getResources().getStringArray(R.array.leads_array)[5])) {
                siteVisitEntity = AppDatabase.getInstance().getSiteVisitDao().getSiteVisitByEnquiryId(enquiryId);
                return currentStatus;
            }
        }
        return currentStatus;

    }

    @Override
      protected void onPostExecute(String currentStatus) {
        super.onPostExecute(currentStatus);
        communicator.getLeadsDetailCallback(multipleProjectsEntityList, callbackEntity, meetingEntity,
                siteVisitEntity, preSalesLeadDetailsEntity);

    }

    public interface ILeadDetailCommunicator {
        void getLeadsDetailCallback(List<SelectMultipleProjectsEntity> multipleProjectsEntityList,
                                    CallbackEntity callbackEntity, MeetingEntity meetingEntity,
                                    SiteVisitEntity siteVisitEntity, PreSalesLeadDetailsEntity preSalesLeadDetailsEntity);
    }


       }
