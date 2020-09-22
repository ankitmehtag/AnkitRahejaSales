package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusClosureEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubStatusClosureDao extends BaseDao<SubStatusClosureEntity> {

    @Query("SELECT * FROM tbl_sub_status_closure")
    List<SubStatusClosureEntity> getAllSubClosure();

    @Query("SELECT * FROM tbl_sub_status_closure where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    SubStatusClosureEntity getSubClosureByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_closure where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SubStatusClosureEntity> getSubStatusClosureList(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_closure where enquiry_id LIKE :enquiryId")
    List<SubStatusClosureEntity> getSubClosureForSync(String enquiryId);

    @Query("DELETE FROM tbl_sub_status_closure where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
