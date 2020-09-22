package com.database.task;

import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.UniversalContactsEntity;

import java.util.List;

public class InsertUniversalContactsTask extends AsyncTask<Void, Void, Boolean> {

    private List<UniversalContactsEntity> contactsList;

    public InsertUniversalContactsTask(List<UniversalContactsEntity> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    protected Boolean doInBackground(Void... objs) {
        try {
            AppDatabase.getInstance().getUniversalContactsDao().insertUniversalContacts(contactsList);
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;

    }
}
