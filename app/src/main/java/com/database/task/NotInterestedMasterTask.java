package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.NotInterestedMasterEntity;

import java.util.List;

public class NotInterestedMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<NotInterestedMasterEntity> mNotInterestList;

    // only retain a weak reference to the activity
    public NotInterestedMasterTask(List<NotInterestedMasterEntity> mStatusList) {
        this.mNotInterestList = mStatusList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getNotInterestedMasterDao().insertAllMaster(mNotInterestList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
