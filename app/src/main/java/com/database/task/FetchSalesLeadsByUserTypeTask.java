package com.database.task;

import android.content.Context;
import android.os.AsyncTask;

import com.database.AppDatabase;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.CorporateActivityMasterEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.StatusMasterEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;

import java.util.List;

public class FetchSalesLeadsByUserTypeTask extends AsyncTask<Void, Void, Boolean> {

    private String userType;
    private ISalesLeadCommunicator communicator;


    public FetchSalesLeadsByUserTypeTask(Context context, String userType) {
        this.userType = userType;
        this.communicator = (ISalesLeadCommunicator) context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        List<ProjectMasterEntity> projectMasterList = AppDatabase.getInstance().getProjectMasterDao().getAllProjects();
        List<StatusMasterEntity> leadStatusMasterList = AppDatabase.getInstance().getStatusMasterDao().getAllStatusMasterList();
        List<CorporateActivityMasterEntity> corporateMasterList = AppDatabase.getInstance().getCorporateActivityDao().getAllCorpActivity();
        List<SalesBrokerMasterEntity> brokerMasterList = AppDatabase.getInstance().getBrokerMasterDao().getAllBrokerMaster();
        List<ClosureMasterEntity> closureMasterList = AppDatabase.getInstance().getClosureMasterDao().getAllClosureMasterList();
        List<NotInterestedMasterEntity> notInterestedMasterList = AppDatabase.getInstance().getNotInterestedMasterDao().getNotInterestedMasterList();

        List<SubStatusCallbackEntity> spSubStatusCallbackList = AppDatabase.getInstance().getSubStatusCallbackDao().getAllCallback();
        List<SubStatusClosureEntity> spSubStatusClosureList = AppDatabase.getInstance().getSubStatusClosureDao().getAllSubClosure();
        List<SubStatusMeetingEntity> spSubStatusMeetingList = AppDatabase.getInstance().getSubStatusMeetingDao().getAllSubStatusMeetings();
        List<SubStatusNotInterestedEntity> spSubStatusNotInterestedList = AppDatabase.getInstance().getSubStatusNotInterestedDao().getAllSubStNotInterested();
        List<SubStatusSiteVisitEntity> spSubStatusSiteVisitList = AppDatabase.getInstance().getSubStatusSiteVisitDao().getAllSubStatusSiteVisit();

        List<SalesLeadDetailEntity> salesLeadDetailsList = AppDatabase.getInstance().getSalesLeadDetailsDao().getAllSalesLeads();
        communicator.callbackSalesLeads(projectMasterList, leadStatusMasterList, corporateMasterList, brokerMasterList, closureMasterList,
                notInterestedMasterList, spSubStatusCallbackList, spSubStatusClosureList, spSubStatusMeetingList,
                spSubStatusNotInterestedList, spSubStatusSiteVisitList, salesLeadDetailsList);
        return true;
    }


    public interface ISalesLeadCommunicator {
        void callbackSalesLeads(List<ProjectMasterEntity> projectMasterList,
                                List<StatusMasterEntity> leadStatusMasterList,
                                List<CorporateActivityMasterEntity> corporateMasterList,
                                List<SalesBrokerMasterEntity> spBrokerMasterEntityList,
                                List<ClosureMasterEntity> spClosureMasterEntityList,
                                List<NotInterestedMasterEntity> spNotInterestedMasterEntityList,
                                List<SubStatusCallbackEntity> spSubStatusCallbackEntityList,
                                List<SubStatusClosureEntity> spSubStatusClosureEntityList,
                                List<SubStatusMeetingEntity> spSubStatusMeetingEntityList,
                                List<SubStatusNotInterestedEntity> spSubStatusNotInterestedEntityList,
                                List<SubStatusSiteVisitEntity> spSubStatusSiteVisitEntityList,
                                List<SalesLeadDetailEntity> salesLeadDetailsEntityList
        );
    }
}
