package com.database.task;

import android.app.Activity;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SelectMultipleProjectsEntity;

import java.util.List;

public class GetMultipleProjectTask extends AsyncTask<Void, Void, Void> {
    private String enquiryId;
    private List<SelectMultipleProjectsEntity> multipleProjectsEntityList;
    List<ProjectMasterEntity> projectMasterList;

    private GetMultipleProjectTask.IMultiProjCommunicator communicator;

    public GetMultipleProjectTask(Activity context, String enquiryId) {
        this.enquiryId = enquiryId;
        communicator = (IMultiProjCommunicator) context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        multipleProjectsEntityList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);
        projectMasterList = AppDatabase.getInstance().getProjectMasterDao().getAllProjects();
        return null;
    }

    @Override
    protected void onPostExecute(Void d) {
        super.onPostExecute(d);
        communicator.getMultiProjCallback(multipleProjectsEntityList, projectMasterList);
    }

    public interface IMultiProjCommunicator {
        void getMultiProjCallback(List<SelectMultipleProjectsEntity> multipleProjectsEntityList, List<ProjectMasterEntity> projectMasterList);
    }
}
