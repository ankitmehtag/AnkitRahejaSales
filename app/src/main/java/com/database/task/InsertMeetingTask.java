package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.MeetingEntity;

public class InsertMeetingTask extends AsyncTask<Void, Void, Boolean> {

    private MeetingEntity meetingEntity;

    // only retain a weak reference to the activity
    public InsertMeetingTask(MeetingEntity meetingEntity) {
        this.meetingEntity = meetingEntity;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getMeetingDao().insertMeetingEntity(meetingEntity);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}

