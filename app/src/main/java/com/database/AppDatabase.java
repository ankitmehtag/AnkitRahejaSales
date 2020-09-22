package com.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.database.dao.AddAppointmentDao;
import com.database.dao.CallRecordingDao;
import com.database.dao.CallbackDao;
import com.database.dao.ClosureMasterDao;
import com.database.dao.CorporateActivityMasterDao;
import com.database.dao.CustomerInfoDao;
import com.database.dao.PreSalesLeadDetailsDao;
import com.database.dao.MeetingDao;
import com.database.dao.NotInterestedMasterDao;
import com.database.dao.ProjectMasterDao;
import com.database.dao.SalesBrokerMasterDao;
import com.database.dao.SalesClosureLeadDetailsDao;
import com.database.dao.SalesLeadDetailsDao;
import com.database.dao.SelectMultipleProjectsDao;
import com.database.dao.SiteVisitDao;
import com.database.dao.SpMasterDao;
import com.database.dao.StatusMasterDao;
import com.database.dao.SubStatusCallbackDao;
import com.database.dao.SubStatusClosureDao;
import com.database.dao.SubStatusMasterDao;
import com.database.dao.SubStatusMeetingDao;
import com.database.dao.SubStatusNotInterestedDao;
import com.database.dao.SubStatusSiteVisitDao;
import com.database.dao.UniversalContactsDao;
import com.database.entity.AddAppointmentEntity;
import com.database.entity.CallRecordingEntity;
import com.database.entity.CallbackEntity;
import com.database.entity.ClosureMasterEntity;
import com.database.entity.CorporateActivityMasterEntity;
import com.database.entity.CustomerInfoEntity;
import com.database.entity.PreSalesLeadDetailsEntity;
import com.database.entity.MeetingEntity;
import com.database.entity.NotInterestedMasterEntity;
import com.database.entity.ProjectMasterEntity;
import com.database.entity.SalesBrokerMasterEntity;
import com.database.entity.SalesClosureLeadsDetailEntity;
import com.database.entity.SalesLeadDetailEntity;
import com.database.entity.SelectMultipleProjectsEntity;
import com.database.entity.SiteVisitEntity;
import com.database.entity.SpMasterEntity;
import com.database.entity.StatusMasterEntity;
import com.database.entity.SubStatusCallbackEntity;
import com.database.entity.SubStatusClosureEntity;
import com.database.entity.SubStatusMasterEntity;
import com.database.entity.SubStatusMeetingEntity;
import com.database.entity.SubStatusNotInterestedEntity;
import com.database.entity.SubStatusSiteVisitEntity;
import com.database.entity.UniversalContactsEntity;
import com.helper.BMHConstants;
import com.sp.BMHApplication;

@Database(entities = {CustomerInfoEntity.class, CallRecordingEntity.class, StatusMasterEntity.class, SpMasterEntity.class,
        ProjectMasterEntity.class, SalesBrokerMasterEntity.class, PreSalesLeadDetailsEntity.class, CallbackEntity.class,
        MeetingEntity.class, SiteVisitEntity.class, SelectMultipleProjectsEntity.class, SalesLeadDetailEntity.class,
        NotInterestedMasterEntity.class, ClosureMasterEntity.class, SubStatusCallbackEntity.class, SubStatusMeetingEntity.class,
        SubStatusSiteVisitEntity.class, SubStatusClosureEntity.class, SubStatusNotInterestedEntity.class,
        SubStatusMasterEntity.class, SalesClosureLeadsDetailEntity.class, UniversalContactsEntity.class,
        AddAppointmentEntity.class, CorporateActivityMasterEntity.class
}, version = 1, exportSchema = false)

@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract CustomerInfoDao getCustomerDao();

    public abstract UniversalContactsDao getUniversalContactsDao();

    public abstract CallRecordingDao getCallRecordingDao();

    public abstract StatusMasterDao getStatusMasterDao();

    public abstract SalesLeadDetailsDao getSalesLeadDetailsDao();

    public abstract SalesClosureLeadDetailsDao getClosureLeadDetailsDao();

    public abstract SubStatusMasterDao getSubStatusMasterDao();

    public abstract NotInterestedMasterDao getNotInterestedMasterDao();

    public abstract ClosureMasterDao getClosureMasterDao();

    public abstract SpMasterDao getSpMasterDao();

    public abstract ProjectMasterDao getProjectMasterDao();

    public abstract SalesBrokerMasterDao getBrokerMasterDao();

    public abstract PreSalesLeadDetailsDao getLeadDetailsDao();

    public abstract CallbackDao getCallbackDao();

    public abstract SubStatusCallbackDao getSubStatusCallbackDao();

    public abstract SubStatusMeetingDao getSubStatusMeetingDao();

    public abstract SubStatusSiteVisitDao getSubStatusSiteVisitDao();

    public abstract SubStatusClosureDao getSubStatusClosureDao();

    public abstract SubStatusNotInterestedDao getSubStatusNotInterestedDao();

    public abstract AddAppointmentDao getAddAppointmentDao();

    public abstract CorporateActivityMasterDao getCorporateActivityDao();

    public abstract MeetingDao getMeetingDao();

    public abstract SiteVisitDao getSiteVisitDao();

    public abstract SelectMultipleProjectsDao getMultiSelProjectDao();

    private static AppDatabase dbInstance;

    public static AppDatabase getInstance() {
        if (null == dbInstance) {
            synchronized (AppDatabase.class) {
                if (null == dbInstance)
                    dbInstance = buildDatabaseInstance();
            }
        }
        return dbInstance;

    }

    private static AppDatabase buildDatabaseInstance() {
        return Room.databaseBuilder(BMHApplication.getInstance(),
                AppDatabase.class,
                BMHConstants.DB_NAME).fallbackToDestructiveMigration().build();
    }

    public static void cleanRoomDb() {
        dbInstance = null;
    }
}
