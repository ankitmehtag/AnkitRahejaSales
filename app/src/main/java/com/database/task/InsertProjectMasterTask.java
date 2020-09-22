package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.ProjectMasterEntity;

import java.util.List;

public class InsertProjectMasterTask extends AsyncTask<Void, Void, Boolean> {

    private List<ProjectMasterEntity> mProjectList;

    // only retain a weak reference to the activity
    public InsertProjectMasterTask(List<ProjectMasterEntity> mProjectList) {
        this.mProjectList = mProjectList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        AppDatabase.getInstance().getProjectMasterDao().insertAllProjects(mProjectList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }
}
