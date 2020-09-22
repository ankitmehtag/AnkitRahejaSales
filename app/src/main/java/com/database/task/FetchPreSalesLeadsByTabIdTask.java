package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SpMasterEntity;
import com.database.entity.StatusMasterEntity;

import java.util.List;

public class FetchPreSalesLeadsByTabIdTask extends AsyncTask<Void, Void, Boolean> {
    private String designationType;
    private ILeadCommunicator communicator;

    public FetchPreSalesLeadsByTabIdTask(Context context, String designationType) {
        this.designationType = designationType;
        communicator = (ILeadCommunicator) context;
    }

    @Override
    protected Boolean doInBackground(Void... objs) {
        List<ProjectMasterEntity> projectMasterList = AppDatabase.getInstance().getProjectMasterDao().getAllProjects();
        List<StatusMasterEntity> leadStatusMasterList = AppDatabase.getInstance().getStatusMasterDao().getAllStatusMasterList();
        List<SpMasterEntity> spMasterEntityList = AppDatabase.getInstance().getSpMasterDao().getAllSalesMan();
        List<PreSalesLeadDetailsEntity> leadDetailsList = AppDatabase.getInstance().getLeadDetailsDao().getAllLeadDetailsByDesignation(designationType);
        communicator.callbackLeadsData(projectMasterList, leadStatusMasterList, spMasterEntityList, leadDetailsList);
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {

    }

    public interface ILeadCommunicator {
        void callbackLeadsData(List<ProjectMasterEntity> projectMasterList,
                               List<StatusMasterEntity> leadStatusMasterList,
                               List<SpMasterEntity> spMasterEntityList,
                               List<PreSalesLeadDetailsEntity> preSalesLeadDetailsEntity);
    }
}
