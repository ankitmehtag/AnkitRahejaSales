package com.database.task;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.database.AppDatabase;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SpMasterEntity;
import com.database.entity.StatusMasterEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMasterEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.helper.BMHConstants;
import com.model.LeadStatus;
import com.sp.R;

import java.util.List;

public class GetSalesLeadDetailTask extends AsyncTask<Void, Void, String> {
    private List<SalesLeadDetailEntity> salesDetailList;
    private List<SelectMultipleProjectsEntity> selMultiProjectsList;
    private List<ProjectMasterEntity> projectMasterList;
    private List<StatusMasterEntity> leadStatusList;
    private List<SalesBrokerMasterEntity> brokerMasterList;
    private List<ClosureMasterEntity> closureMasterList;
    private List<NotInterestedMasterEntity> notInterestedMasterList;
    SalesLeadDetailEntity leadDetailEntity;
    SubStatusCallbackEntity callbackEntity;
    SubStatusMeetingEntity meetingEntity;
    SubStatusSiteVisitEntity siteVisitEntity;
    SubStatusClosureEntity closureEntity;
    SubStatusNotInterestedEntity notInterestedEntity;
    private String enquiryId, tabName;
    Context context;
    private GetSalesLeadDetailTask.ISalesLeadCommunicator communicator;
    private GetSalesLeadDetailTask.ISalesLeadDetailCommunicator detailComm;
    private GetSalesLeadDetailTask.ISalesAssignedLeadCommunicator assignedComm;

    public GetSalesLeadDetailTask(Context context) {
        communicator = (GetSalesLeadDetailTask.ISalesLeadCommunicator) context;
    }

    public GetSalesLeadDetailTask(Context context, String enquiryId, String tabName) {
        this.enquiryId = enquiryId;
        this.context = context;
        this.tabName = tabName;
        if (tabName.equalsIgnoreCase(context.getString(R.string.tab_assigned))) {
            assignedComm = (GetSalesLeadDetailTask.ISalesAssignedLeadCommunicator) context;
        } else {
            detailComm = (GetSalesLeadDetailTask.ISalesLeadDetailCommunicator) context;
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (TextUtils.isEmpty(enquiryId))
            salesDetailList = AppDatabase.getInstance().getSalesLeadDetailsDao().getAllSalesLeads();
        else {
            if (tabName.equalsIgnoreCase(context.getString(R.string.tab_assigned))) {
                leadDetailEntity = AppDatabase.getInstance().getSalesLeadDetailsDao().getSalesLeadsByEnquiryId(enquiryId);
                selMultiProjectsList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);
            } else {
                projectMasterList = AppDatabase.getInstance().getProjectMasterDao().getAllProjects();
                selMultiProjectsList = AppDatabase.getInstance().getMultiSelProjectDao().getProjectByEnquiryId(enquiryId);
                leadStatusList = AppDatabase.getInstance().getStatusMasterDao().getAllStatusMasterList();
                brokerMasterList = AppDatabase.getInstance().getBrokerMasterDao().getAllBrokerMaster();
                closureMasterList = AppDatabase.getInstance().getClosureMasterDao().getAllClosureMasterList();
                notInterestedMasterList = AppDatabase.getInstance().getNotInterestedMasterDao().getNotInterestedMasterList();

                leadDetailEntity = AppDatabase.getInstance().getSalesLeadDetailsDao().getSalesLeadsByEnquiryId(enquiryId);
                if (leadDetailEntity != null) {
                    String currentStatus = leadDetailEntity.getCurrentStatus();
                    if (!TextUtils.isEmpty(currentStatus)) {
                        if (currentStatus.equalsIgnoreCase(context.getString(R.string.text_callback))) {
                            callbackEntity = AppDatabase.getInstance().getSubStatusCallbackDao().getCallbackByEnquiryId(enquiryId);
                            return currentStatus;
                        }
                        if (currentStatus.equalsIgnoreCase(context.getString(R.string.txt_meeting))) {
                            meetingEntity = AppDatabase.getInstance().getSubStatusMeetingDao().getSubStatusMeetingByEnquiryId(enquiryId);
                            return currentStatus;
                        }
                        if (currentStatus.equalsIgnoreCase(context.getString(R.string.txt_site_visit))) {
                            siteVisitEntity = AppDatabase.getInstance().getSubStatusSiteVisitDao().getSubStatusSiteVisitByEnquiryId(enquiryId);
                            return currentStatus;
                        }
                        if (currentStatus.equalsIgnoreCase(context.getString(R.string.text_closure))) {
                            closureEntity = AppDatabase.getInstance().getSubStatusClosureDao().getSubClosureByEnquiryId(enquiryId);
                            return currentStatus;
                        }
                        if (currentStatus.equalsIgnoreCase(context.getString(R.string.text_not_interested))) {
                            notInterestedEntity = AppDatabase.getInstance().getSubStatusNotInterestedDao().getSubStNotInterestedByEnquiryId(enquiryId);
                            return currentStatus;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String d) {
        super.onPostExecute(d);
        if (TextUtils.isEmpty(enquiryId))
            communicator.getAllSpDetails(salesDetailList);
        else if (tabName.equalsIgnoreCase(context.getString(R.string.tab_assigned)))
            assignedComm.getSalesAssignedDetail(leadDetailEntity, selMultiProjectsList);
        else
            detailComm.salesLeadDetailsCallback(projectMasterList, leadStatusList, brokerMasterList, closureMasterList,
                    notInterestedMasterList, selMultiProjectsList, leadDetailEntity);
    }

    public interface ISalesLeadCommunicator {
        void getAllSpDetails(List<SalesLeadDetailEntity> salesDetailList);
    }

    public interface ISalesAssignedLeadCommunicator {
        void getSalesAssignedDetail(SalesLeadDetailEntity leadDetail, List<SelectMultipleProjectsEntity> multipleProjectsEntityList);
    }

    public interface ISalesLeadDetailCommunicator {
        void salesLeadDetailsCallback(List<ProjectMasterEntity> projectMasterList, List<StatusMasterEntity> leadStatusList,
                                      List<SalesBrokerMasterEntity> brokerMasterList, List<ClosureMasterEntity> closureMasterList,
                                      List<NotInterestedMasterEntity> notInterestedMasterList, List<SelectMultipleProjectsEntity>
                                              multipleProjectsEntityList, SalesLeadDetailEntity leadDetail);
    }
}
