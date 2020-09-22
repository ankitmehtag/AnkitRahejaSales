package com.database.task;

import android.os.AsyncTask;
import android.util.Log;

import com.database.AppDatabase;

public class ClearDatabaseTask extends AsyncTask<Void, Void, Boolean> {

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().clearAllTables();
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {
        Log.d("Database instance", "Status" + bool);
    }
}
