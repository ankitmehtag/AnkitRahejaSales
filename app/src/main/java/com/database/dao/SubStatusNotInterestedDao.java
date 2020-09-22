package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusNotInterestedEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubStatusNotInterestedDao extends BaseDao<SubStatusNotInterestedEntity> {

    @Query("SELECT * FROM tbl_sub_status_not_interested")
    List<SubStatusNotInterestedEntity> getAllSubStNotInterested();

    @Query("SELECT * FROM tbl_sub_status_not_interested where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    SubStatusNotInterestedEntity getSubStNotInterestedByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_not_interested where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SubStatusNotInterestedEntity> getSubStNotInterestedList(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_not_interested where enquiry_id LIKE :enquiryId")
    List<SubStatusNotInterestedEntity> getSubStNotInterestedForSync(String enquiryId);

    @Query("DELETE FROM tbl_sub_status_closure where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
