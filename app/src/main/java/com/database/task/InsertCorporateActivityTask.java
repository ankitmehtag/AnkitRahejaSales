package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CorporateActivityMasterEntity;

import java.util.List;

public class InsertCorporateActivityTask extends AsyncTask<Void, Void, Boolean> {

    private List<CorporateActivityMasterEntity> entityList;

    // only retain a weak reference to the activity
    public InsertCorporateActivityTask(List<CorporateActivityMasterEntity> entityList) {
        this.entityList = entityList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getCorporateActivityDao().insert(entityList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
