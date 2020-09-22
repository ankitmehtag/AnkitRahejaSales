package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SubStatusNotInterestedEntity;

public class InsertSubStatusNotInterestedTask extends AsyncTask<Void, Void, Boolean> {

    private SubStatusNotInterestedEntity notInterestedEntity;

    public InsertSubStatusNotInterestedTask(SubStatusNotInterestedEntity notInterestedEntity) {
        this.notInterestedEntity = notInterestedEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... obj) {
        AppDatabase.getInstance().getSubStatusNotInterestedDao().insert(notInterestedEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
