package com.database.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.StatusMasterEntity;

import java.util.List;

public class GetStatusMasterTask extends AsyncTask<Void, Void, Void> {

    private List<StatusMasterEntity> statusMasterList;
    private IStatusMasterCommunicator communicator;

    public GetStatusMasterTask(Activity context) {
        communicator = (IStatusMasterCommunicator) context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        statusMasterList = AppDatabase.getInstance().getStatusMasterDao().getAllStatusMasterList();
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        communicator.getStatusMasterCallback(statusMasterList);
    }

    public interface IStatusMasterCommunicator {
        void getStatusMasterCallback(List<StatusMasterEntity> leadStatusMasterList);
    }
}
