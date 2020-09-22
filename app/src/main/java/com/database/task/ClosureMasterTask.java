package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.ClosureMasterEntity;

import java.util.List;

public class ClosureMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<ClosureMasterEntity> mClosureList;

    // only retain a weak reference to the activity
    public ClosureMasterTask(List<ClosureMasterEntity> mClosureList) {
        this.mClosureList = mClosureList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getClosureMasterDao().insertAllClosureMaster(mClosureList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
