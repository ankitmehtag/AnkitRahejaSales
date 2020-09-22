package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.MeetingEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface MeetingDao extends BaseDao<MeetingEntity> {

    @Query("SELECT * FROM tbl_meeting ")
    List<MeetingEntity> getAllMeetings();

    @Query("SELECT * FROM tbl_meeting where enquiry_id LIKE :enquiryId  ORDER BY lastupdatedon DESC LIMIT 1")
    MeetingEntity getMeetingByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_meeting where enquiry_id LIKE :enquiryId  ORDER BY lastupdatedon DESC LIMIT 1")
    List<MeetingEntity> getMeetingList(String enquiryId);

    @Query("SELECT * FROM tbl_meeting where enquiry_id LIKE :enquiryId")
    List<MeetingEntity> getAllMeetingsForSync(String enquiryId);

    @Query("UPDATE tbl_meeting set isSynced = 0 where  enquiry_id LIKE :enquiryId")
    void updateSyncData(String enquiryId);

    @Insert(onConflict = IGNORE)
    void insertMeetingEntity(MeetingEntity meetingEntityList);

    @Query("DELETE FROM tbl_meeting where enquiry_id LIKE :enquiryId")
    void deleteSyncedData(String enquiryId);
}
