package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.StatusMasterEntity;

import java.util.List;

public class InsertStatusMasterTask  extends AsyncTask<Void, Void, Boolean> {

    private List<StatusMasterEntity> mStatusList;

    // only retain a weak reference to the activity
   public InsertStatusMasterTask(List<StatusMasterEntity> mStatusList) {
        this.mStatusList = mStatusList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getStatusMasterDao().insertAllStatus(mStatusList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
