package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.database.AppDatabase;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;

public class InsertClosureLeadsDetailTask extends AsyncTask<Void, Void, Boolean> {

    private SalesClosureLeadsDetailEntity mClosureEntity;

    Context context;
    private String enquiryId, remark;
    private int isSynced;

    // only retain a weak reference to the activity
    public InsertClosureLeadsDetailTask(Context context, SalesClosureLeadsDetailEntity mClosureEntity) {
        this.mClosureEntity = mClosureEntity;
        this.context = context;
    }

    public InsertClosureLeadsDetailTask(Context context, String enquiryId, String remark, int isSynced) {
        this.context = context;
        this.enquiryId = enquiryId;
        this.remark = remark;
        this.isSynced = isSynced;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        if (!TextUtils.isEmpty(enquiryId)) {
            AppDatabase.getInstance().getClosureLeadDetailsDao().updateRemarkText(enquiryId, remark, isSynced);
        } else
            AppDatabase.getInstance().getClosureLeadDetailsDao().insert(mClosureEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {
        if (!Connectivity.isConnected(context)) {
            Toast.makeText(BMHApplication.getInstance(), R.string.saved_data_locally, Toast.LENGTH_LONG).show();
        }
    }
}
