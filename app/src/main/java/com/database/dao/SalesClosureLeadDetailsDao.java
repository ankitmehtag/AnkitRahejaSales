package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.SalesClosureLeadsDetailEntity;

import java.util.List;

@Dao
public interface SalesClosureLeadDetailsDao extends BaseDao<SalesClosureLeadsDetailEntity> {

    @Query("SELECT * FROM tbl_sales_closure_lead_detail ")
    List<SalesClosureLeadsDetailEntity> getClosureLeadsDetail();

    @Query("SELECT * FROM tbl_sales_closure_lead_detail where designation LIKE :designationType")
    List<SalesClosureLeadsDetailEntity> getClosureLeadsByDesignation(String designationType);

    @Query("SELECT * FROM tbl_sales_closure_lead_detail where enquiry_id LIKE :enquiryId")
    SalesClosureLeadsDetailEntity getClosureLeadsByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sales_closure_lead_detail where is_synced LIKE :readyToSync")
    List<SalesClosureLeadsDetailEntity> getClosureLeadsToSync(int readyToSync);

    @Query("UPDATE tbl_sales_closure_lead_detail set remark=:remarkText, is_synced= :isSynced where  enquiry_id LIKE :enquiryId")
    void updateRemarkText(String enquiryId, String remarkText, int isSynced);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClosureLeadsDetail(SalesClosureLeadsDetailEntity detailsList);
}
