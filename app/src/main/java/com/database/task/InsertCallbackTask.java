package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CallbackEntity;

public class InsertCallbackTask extends AsyncTask<Void, Void, Boolean> {

    private CallbackEntity callbackEntity;

    public InsertCallbackTask(CallbackEntity callbackEntity) {
        this.callbackEntity = callbackEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getCallbackDao().insertCallbackEntity(callbackEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
