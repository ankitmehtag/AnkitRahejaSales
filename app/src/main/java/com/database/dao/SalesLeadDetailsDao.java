package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.SalesLeadDetailEntity;

import java.util.List;

@Dao
public interface SalesLeadDetailsDao extends BaseDao<SalesLeadDetailEntity> {

    @Query("SELECT * FROM tbl_sales_lead_detail ")
    List<SalesLeadDetailEntity> getAllSalesLeads();

    @Query("SELECT * FROM tbl_sales_lead_detail where designation LIKE :designationType")
    List<SalesLeadDetailEntity> getAllSalesLeadsByDesignation(String designationType);

    @Query("SELECT * FROM tbl_sales_lead_detail where enquiry_id LIKE :enquiryId")
    SalesLeadDetailEntity getSalesLeadsByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sales_lead_detail where is_synced LIKE :readyToSync")
    List<SalesLeadDetailEntity> getSalesLeadsToSync(int readyToSync);

    @Query("DELETE FROM tbl_sales_lead_detail where enquiry_id = :enquiryId")
    void deleteLeadByEnquiryId(String enquiryId);

    @Query("UPDATE tbl_sales_lead_detail set is_synced= 0 where  enquiry_id LIKE :enquiryId")
    void updateSyncData(String enquiryId);

    @Query("UPDATE tbl_sales_lead_detail set user_action= :updateAction, is_synced= :isSynced ,is_assigned= :isAssigned where  enquiry_id LIKE :enquiryId")
    void updateAcceptRejectAction(String enquiryId, int updateAction, int isAssigned, int isSynced);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSalesLeadDetail(List<SalesLeadDetailEntity> detailsList);
}
