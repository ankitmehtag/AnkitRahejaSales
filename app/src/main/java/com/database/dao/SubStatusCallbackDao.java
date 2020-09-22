package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusCallbackEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubStatusCallbackDao extends BaseDao<SubStatusCallbackEntity> {

    @Query("SELECT * FROM tbl_sub_status_callback")
    List<SubStatusCallbackEntity> getAllCallback();

    @Query("SELECT * FROM tbl_sub_status_callback where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    SubStatusCallbackEntity getCallbackByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_callback where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SubStatusCallbackEntity> getSubStatusCallbackList(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_callback where enquiry_id LIKE :enquiryId")
    List<SubStatusCallbackEntity> getAllCallbackForSync(String enquiryId);

    @Query("DELETE FROM tbl_sub_status_callback where enquiry_id LIKE :enquiryId ")
    void deleteSyncedData(String enquiryId);
}
