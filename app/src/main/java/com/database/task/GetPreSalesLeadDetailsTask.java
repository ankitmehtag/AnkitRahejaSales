package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.PreSalesLeadDetailsEntity;

import java.util.List;

public class GetPreSalesLeadDetailsTask extends AsyncTask<Void, Void, Void> {
    private List<PreSalesLeadDetailsEntity> detailsEntityList;
    private IPreSalesLeadDetailsCommunicator communicator;

    public GetPreSalesLeadDetailsTask(Context context) {
        communicator = (IPreSalesLeadDetailsCommunicator) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        detailsEntityList = AppDatabase.getInstance().getLeadDetailsDao().getAllLeadDetails();
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        communicator.getPreSalesLeadsDetail(detailsEntityList);
    }

    public interface IPreSalesLeadDetailsCommunicator {
        void getPreSalesLeadsDetail(List<PreSalesLeadDetailsEntity> detailsEntityList);
    }
}
