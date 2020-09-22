package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.CallRecordingEntity;

import java.util.List;

@Dao
public interface CallRecordingDao extends BaseDao<CallRecordingEntity> {

    @Query(value = "SELECT * FROM tbl_call_recording ")

    List<CallRecordingEntity> getAllCallRecording();

    @Query("SELECT * FROM tbl_call_recording where rec_file_name LIKE :fileName ")
    CallRecordingEntity getCallRecordingByFile(String fileName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCallRecording(CallRecordingEntity callRecordingEntity);

    @Query("SELECT * FROM tbl_call_recording where is_synced LIKE :readyToSync")
    List<CallRecordingEntity> getRecordingToSync(int readyToSync);

    @Query("DELETE FROM tbl_call_recording where rec_file_name LIKE :audioFileName")
    void deleteRecordByFile(String audioFileName);

    @Query("UPDATE tbl_call_recording set is_synced = 0 WHERE rec_file_name LIKE :audioFileName")
    void updateSyncStatus(String audioFileName);

    @Query("UPDATE tbl_call_recording set lead_update_status = 1 WHERE rec_file_name LIKE :audioFileName")
    void updateRecordingStatus(String audioFileName);
}
