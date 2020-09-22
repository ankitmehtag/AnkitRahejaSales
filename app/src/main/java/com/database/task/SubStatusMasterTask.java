package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SubStatusMasterEntity;

import java.util.List;

public class SubStatusMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<SubStatusMasterEntity> mSubStatusList;

    public SubStatusMasterTask(List<SubStatusMasterEntity> mSubStatusList) {
        this.mSubStatusList = mSubStatusList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getSubStatusMasterDao().insertSubStatusMasterList(mSubStatusList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
