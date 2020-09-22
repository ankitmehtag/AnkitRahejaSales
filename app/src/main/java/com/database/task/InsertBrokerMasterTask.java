package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SalesBrokerMasterEntity;

import java.util.List;

public class InsertBrokerMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<SalesBrokerMasterEntity> mBrokerList;

    // only retain a weak reference to the activity
    public InsertBrokerMasterTask(List<SalesBrokerMasterEntity> mBrokerList) {
        this.mBrokerList = mBrokerList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getBrokerMasterDao().insertAllBrokerMaster(mBrokerList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
