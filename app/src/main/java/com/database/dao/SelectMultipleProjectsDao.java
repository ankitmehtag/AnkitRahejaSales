package com.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.database.entity.SelectMultipleProjectsEntity;

import java.util.List;


@Dao
public interface SelectMultipleProjectsDao extends BaseDao<SelectMultipleProjectsEntity> {

    @Query("SELECT * FROM tbl_select_multiple_Projects where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SelectMultipleProjectsEntity> getProjectByEnquiryId(String enquiryId);

    @Query("DELETE FROM tbl_select_multiple_Projects where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
