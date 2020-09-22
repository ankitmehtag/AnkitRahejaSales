package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SiteVisitEntity;

public class InsertSiteVisitTask extends AsyncTask<Void, Void, Boolean> {

    private SiteVisitEntity siteVisitEntity;

    // only retain a weak reference to the activity
    public InsertSiteVisitTask(SiteVisitEntity siteVisitEntity) {
        this.siteVisitEntity = siteVisitEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getSiteVisitDao().insertAllSiteVisit(siteVisitEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}