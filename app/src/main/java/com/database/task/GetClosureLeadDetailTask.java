package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.database.AppDatabase;
import com.database.entity.SalesClosureLeadsDetailEntity;

public class GetClosureLeadDetailTask extends AsyncTask<Void, Void, Void> {
    private SalesClosureLeadsDetailEntity closureDetailEntity;
    private String enquiryId;
    private GetClosureLeadDetailTask.IClosureLeadDetailCommunicator detailComm;

    public GetClosureLeadDetailTask(Context context, String enquiryId) {
        this.enquiryId = enquiryId;
        detailComm = (GetClosureLeadDetailTask.IClosureLeadDetailCommunicator) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!TextUtils.isEmpty(enquiryId))
            closureDetailEntity = AppDatabase.getInstance().getClosureLeadDetailsDao().getClosureLeadsByEnquiryId(enquiryId);
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        detailComm.closureLeadDetailsCallback(closureDetailEntity);
    }

    public interface IClosureLeadDetailCommunicator {
        void closureLeadDetailsCallback(SalesClosureLeadsDetailEntity leadDetail);
    }
}
