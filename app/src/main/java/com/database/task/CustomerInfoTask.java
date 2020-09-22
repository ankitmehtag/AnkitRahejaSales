package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.CustomerInfoEntity;

import java.lang.ref.WeakReference;
import java.util.List;

public class CustomerInfoTask extends AsyncTask<Void, Void, Boolean> {

    private List<CustomerInfoEntity> custList;
    private CustomerInfoEntity customerEntity;
    private boolean updateFlag;

    // only retain a weak reference to the activity

    public CustomerInfoTask(CustomerInfoEntity customerEntity, boolean updateFlag) {
        this.customerEntity = customerEntity;
        this.updateFlag = updateFlag;
    }

    public CustomerInfoTask(List<CustomerInfoEntity> custList) {
        this.custList = custList;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        try {
            if (updateFlag)
                AppDatabase.getInstance().getCustomerDao().update(customerEntity);
            else
                AppDatabase.getInstance().getCustomerDao().insertAllCustomerInfo(custList);
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {
    }
}
