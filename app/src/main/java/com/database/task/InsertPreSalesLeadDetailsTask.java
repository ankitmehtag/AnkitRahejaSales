package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.database.AppDatabase;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.sp.BMHApplication;
import com.utils.Connectivity;

import java.util.List;

public class InsertPreSalesLeadDetailsTask extends AsyncTask<Void, Void, Boolean> {

    private PreSalesLeadDetailsEntity mPreSalesLeadDetailsEntity;
    private List<PreSalesLeadDetailsEntity> mLeadDetailsList;
    private boolean isUpdate;
    Context context;

    // only retain a weak reference to the activity
    public InsertPreSalesLeadDetailsTask(Context context, List<PreSalesLeadDetailsEntity>  mLeadDetailsList) {
        this.context = context;
        this.mLeadDetailsList = mLeadDetailsList;
    }

    public InsertPreSalesLeadDetailsTask(Context context, PreSalesLeadDetailsEntity mLeadDetailsList, boolean isUpdate) {
        this.context = context;
        this.isUpdate = isUpdate;
        this.mPreSalesLeadDetailsEntity = mLeadDetailsList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        if (isUpdate) {
            AppDatabase.getInstance().getLeadDetailsDao().update(mPreSalesLeadDetailsEntity);
            AppDatabase.getInstance().getCustomerDao().updateCustomerName(mPreSalesLeadDetailsEntity.getCustomerName(), mPreSalesLeadDetailsEntity.getCustomerMobile());
        } else
            AppDatabase.getInstance().getLeadDetailsDao().insert(mLeadDetailsList);
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
