package com.database.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.SpMasterEntity;

import java.util.List;

public class GetSpMasterTask extends AsyncTask<Void, Void, Void> {

    private List<SpMasterEntity> spMasterList;
    private GetSpMasterTask.ISpMasterCommunicator communicator;

    public GetSpMasterTask(Activity context) {
        communicator = (GetSpMasterTask.ISpMasterCommunicator) context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        spMasterList = AppDatabase.getInstance().getSpMasterDao().getAllSalesMan();
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        communicator.getSpMasterCallback(spMasterList);
    }

    public interface ISpMasterCommunicator {
        void getSpMasterCallback(List<SpMasterEntity> spMasterList);
    }
}
