package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.PreSalesLeadDetailsEntity;

import java.util.List;

@Dao
public interface PreSalesLeadDetailsDao extends BaseDao<PreSalesLeadDetailsEntity> {

    @Query("SELECT * FROM tbl_pre_sales_lead_details ")
    List<PreSalesLeadDetailsEntity> getAllLeadDetails();

    @Query("SELECT * FROM tbl_pre_sales_lead_details where designation LIKE :designationType")
    List<PreSalesLeadDetailsEntity> getAllLeadDetailsByDesignation(String designationType);

    @Query("SELECT * FROM tbl_pre_sales_lead_details where enquiry_id LIKE :enquiryId")
    PreSalesLeadDetailsEntity getDetailByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_pre_sales_lead_details where is_synced LIKE :readyToSync")
    List<PreSalesLeadDetailsEntity> getLeadsToSync(int readyToSync);

    @Query("DELETE FROM tbl_pre_sales_lead_details where enquiry_id LIKE :enquiryId")
    void deleteLeadByEnquiryId(String enquiryId);

    @Query("UPDATE tbl_pre_sales_lead_details set is_synced= 0 where  enquiry_id LIKE :enquiryId")
    void updateSyncData(String enquiryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllLeadDetails(PreSalesLeadDetailsEntity detailsList);
}
