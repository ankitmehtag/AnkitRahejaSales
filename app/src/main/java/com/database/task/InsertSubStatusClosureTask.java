package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SubStatusClosureEntity;

public class InsertSubStatusClosureTask extends AsyncTask<Void, Void, Boolean> {

    private SubStatusClosureEntity closureEntity;

    public InsertSubStatusClosureTask(SubStatusClosureEntity closureEntity) {
        this.closureEntity = closureEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... obj) {
        AppDatabase.getInstance().getSubStatusClosureDao().insert(closureEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
