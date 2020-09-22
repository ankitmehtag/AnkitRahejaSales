package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusMeetingEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubStatusMeetingDao extends BaseDao<SubStatusMeetingEntity> {

    @Query("SELECT * FROM tbl_sub_status_meeting")
    List<SubStatusMeetingEntity> getAllSubStatusMeetings();

    @Query("SELECT * FROM tbl_sub_status_meeting where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    SubStatusMeetingEntity getSubStatusMeetingByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_meeting where enquiry_id LIKE :enquiryId ORDER BY lastupdatedon DESC LIMIT 1")
    List<SubStatusMeetingEntity> getSubStatusMeetingList(String enquiryId);

    @Query("SELECT * FROM tbl_sub_status_meeting where enquiry_id LIKE :enquiryId")
    List<SubStatusMeetingEntity> getSubStatusMeetingForSync(String enquiryId);


    @Query("DELETE FROM tbl_sub_status_meeting where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
