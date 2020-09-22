package com.database.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.database.AppDatabase;

public class ChangeRecordingStatusTask extends AsyncTask<Void, Void, String> {
    private String recFilePath;

    public ChangeRecordingStatusTask(String recFilePath) {
        this.recFilePath = recFilePath;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (!TextUtils.isEmpty(recFilePath)) {
            AppDatabase.getInstance().getCallRecordingDao().updateRecordingStatus(recFilePath);
        }
        return null;
    }
}
