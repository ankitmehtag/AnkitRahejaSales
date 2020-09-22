package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SubStatusMeetingEntity;

public class InsertSubStatusMeetingTask extends AsyncTask<Void, Void, Boolean> {

    private SubStatusMeetingEntity meetingEntity;

    public InsertSubStatusMeetingTask(SubStatusMeetingEntity meetingEntity) {
        this.meetingEntity = meetingEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getSubStatusMeetingDao().insert(meetingEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
