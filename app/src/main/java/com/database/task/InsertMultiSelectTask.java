package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SelectMultipleProjectsEntity;

public class InsertMultiSelectTask extends AsyncTask<Void, Void, Boolean> {

    private SelectMultipleProjectsEntity multipleProjectsEntity;
    private int toUpdate;

    public InsertMultiSelectTask(SelectMultipleProjectsEntity multipleProjectsEntity, int toUpdate) {
        this.multipleProjectsEntity = multipleProjectsEntity;
        this.toUpdate = toUpdate;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        if (toUpdate == 1) {
            AppDatabase.getInstance().getMultiSelProjectDao().update(multipleProjectsEntity);
        } else {
            AppDatabase.getInstance().getMultiSelProjectDao().insert(multipleProjectsEntity);
        }
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
