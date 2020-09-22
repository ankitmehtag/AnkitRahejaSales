package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusSiteVisitEntity;

public class InsertSubStatusSiteVisitTask extends AsyncTask<Void, Void, Boolean> {

    private SubStatusSiteVisitEntity siteVisitEntity;

    public InsertSubStatusSiteVisitTask(SubStatusSiteVisitEntity siteVisitEntity) {
        this.siteVisitEntity = siteVisitEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... obj) {
        AppDatabase.getInstance().getSubStatusSiteVisitDao().insert(siteVisitEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
