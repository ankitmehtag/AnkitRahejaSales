package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CustomerInfoEntity;

public class AlarmServicesAsyncTask extends AsyncTask<Void, Void, Void> {
    private CustomerInfoEntity customerInfoEntity;
    private AlarmServicesAsyncTask.ICustomerEntityInfoCommunicator communicator;
    String enquiryId;

    public AlarmServicesAsyncTask(Context context, String enquiryId) {
        communicator = (AlarmServicesAsyncTask.ICustomerEntityInfoCommunicator) context;
        this.enquiryId = enquiryId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        customerInfoEntity = AppDatabase.getInstance().getCustomerDao().getCustomerInfoByEnquiry(enquiryId);
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        communicator.getCustomerInfoEntity(customerInfoEntity);
    }

    public interface ICustomerEntityInfoCommunicator {
        void getCustomerInfoEntity(CustomerInfoEntity customerInfoEntity);
    }
}
