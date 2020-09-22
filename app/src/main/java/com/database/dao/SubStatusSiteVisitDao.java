package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusSiteVisitEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
@Dao
public interface SubStatusSiteVisitDao extends BaseDao<SubStatusSiteVisitEntity> {

    @Query("SELECT * FROM tbl_sub_status_site_visit")
    List<SubStatusSiteVisitEntity> getAllSubStatusSiteVisit();

    @Query("SELECT * FROM tbl_sub_status_site_visit where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    SubStatusSiteVisitEntity getSubStatusSiteVisitByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_site_visit where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SubStatusSiteVisitEntity> getSubStatusSiteVisitList(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_site_visit where enquiry_id LIKE :enquiryId")
    List<SubStatusSiteVisitEntity> getSubStatusSiteVisitForSync(String enquiryId);

    @Query("DELETE FROM tbl_sub_status_site_visit where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
