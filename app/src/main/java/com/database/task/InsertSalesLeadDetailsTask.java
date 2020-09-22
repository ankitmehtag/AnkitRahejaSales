package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.database.AppDatabase;
import com.database.entity.SalesLeadDetailEntity;
import com.sp.BMHApplication;
import com.utils.Connectivity;

import java.util.List;

public class InsertSalesLeadDetailsTask extends AsyncTask<Void, Void, Boolean> {

    private SalesLeadDetailEntity mLeadDetailsEntity;
    private List<SalesLeadDetailEntity> mLeadDetailsList;
    private int updateAction, isSynced;
    private String enquiryId;
    private boolean isUpdate;
    Context context;

    // only retain a weak reference to the activity
    public InsertSalesLeadDetailsTask(Context context, List<SalesLeadDetailEntity> mLeadDetailsList) {
        this.mLeadDetailsList = mLeadDetailsList;
        this.context = context;
    }

    public InsertSalesLeadDetailsTask(Context context, SalesLeadDetailEntity mLeadDetailsList, boolean isUpdate) {
        this.context = context;
        this.isUpdate = isUpdate;
        this.mLeadDetailsEntity = mLeadDetailsList;
    }

    public InsertSalesLeadDetailsTask(Context context, String enquiryId, int updateAction, int isSynced) {
        this.context = context;
        this.enquiryId = enquiryId;
        this.updateAction = updateAction;
        this.isSynced = isSynced;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        if (!TextUtils.isEmpty(enquiryId)) {
            if (updateAction == 0) {
                //  AppDatabase.getInstance().getSalesLeadDetailsDao().deleteLeadByEnquiryId(enquiryId);
                AppDatabase.getInstance().getSalesLeadDetailsDao().updateAcceptRejectAction(enquiryId, updateAction, -1, isSynced);
            } else {
                int isAssigned = updateAction == 1 ? 1 : -1;
                AppDatabase.getInstance().getSalesLeadDetailsDao().updateAcceptRejectAction(enquiryId, updateAction, isAssigned, isSynced);
            }
        } else if (isUpdate) {
            AppDatabase.getInstance().getSalesLeadDetailsDao().update(mLeadDetailsEntity);
            // AppDatabase.getInstance().getCustomerDao().updateCustomerName(mLeadDetailsEntity.getCustomerName(), mLeadDetailsEntity.getCustomerMobile());
        } else
            AppDatabase.getInstance().getSalesLeadDetailsDao().insertAllSalesLeadDetail(mLeadDetailsList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {
        if (!Connectivity.isConnected(context)) {
            Toast.makeText(BMHApplication.getInstance(), "saved changes locally", Toast.LENGTH_LONG).show();
        }
    }
}
