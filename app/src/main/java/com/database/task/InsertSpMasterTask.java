package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SpMasterEntity;

import java.util.List;

public class InsertSpMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<SpMasterEntity> mSpList;

    // only retain a weak reference to the activity
    public InsertSpMasterTask(List<SpMasterEntity> mStatusList) {
        this.mSpList = mStatusList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getSpMasterDao().insertAllSp(mSpList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
