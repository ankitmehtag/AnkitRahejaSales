package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.CallbackEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface CallbackDao extends BaseDao<CallbackEntity> {

    @Query("SELECT * FROM tbl_callback")
    List<CallbackEntity> getAllCallback();

    @Query("SELECT * FROM tbl_callback where enquiry_id LIKE :enquiryId ORDER BY lastUpdatedOn DESC LIMIT 1")
    CallbackEntity getCallbackByEnquiryId(String enquiryId);

    @Query("SELECT * FROM tbl_callback where enquiry_id LIKE :enquiryId ORDER BY lastUpdatedOn DESC LIMIT 1")
    List<CallbackEntity> getCallbackList(String enquiryId);

    @Query("SELECT * FROM tbl_callback where enquiry_id LIKE :enquiryId")
    List<CallbackEntity> getAllCallbackForSync(String enquiryId);

    @Query("UPDATE tbl_callback set isSynced = 0 where  enquiry_id LIKE :enquiryId")
    void updateSyncData(String enquiryId);

    @Query("DELETE FROM tbl_callback where enquiry_id LIKE :enquiryId ")
    void deleteSyncedData(String enquiryId);

    @Insert(onConflict = IGNORE)
    void insertCallbackEntity(CallbackEntity callbackEntityList);
}
