package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SiteVisitEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SiteVisitDao extends BaseDao<SiteVisitEntity> {

    @Query("SELECT * FROM tbl_site_visit ")
    List<SiteVisitEntity> getAllSiteVisit();

    @Query("SELECT * FROM tbl_site_visit where enquiry_id LIKE :enquiryId  ORDER BY lastupdatedon DESC LIMIT 1")
    SiteVisitEntity getSiteVisitByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_site_visit where enquiry_id LIKE :enquiryId  ORDER BY lastupdatedon DESC LIMIT 1")
    List<SiteVisitEntity> getSiteVisitList(String enquiryId);

    @Query("SELECT * FROM tbl_site_visit where enquiry_id LIKE :enquiryId")
    List<SiteVisitEntity> getAllSiteVisitForSync(String enquiryId);

    @Query("UPDATE tbl_site_visit set isSynced = 0 where  enquiry_id LIKE :enquiryId")
    void updateSyncData(String enquiryId);

    @Query("DELETE FROM tbl_site_visit where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);

    @Insert(onConflict = IGNORE)
    void insertAllSiteVisit(SiteVisitEntity siteVisitEntityList);
}
