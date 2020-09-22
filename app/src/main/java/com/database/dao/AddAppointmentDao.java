package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.AddAppointmentEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface AddAppointmentDao extends BaseDao<AddAppointmentEntity> {

    @Query("SELECT * FROM tbl_add_appointment")
    List<AddAppointmentEntity> getAllAppointments();

    @Query("SELECT * FROM tbl_add_appointment where mobile_no LIKE :mobileNo ORDER BY time_stamp DESC LIMIT 1")
    AddAppointmentEntity getAppointmentByMobile(String mobileNo);

    @Query("SELECT * FROM tbl_add_appointment where mobile_no LIKE :mobileNo ORDER BY time_stamp DESC LIMIT 1")
    List<AddAppointmentEntity> getAppointmentList(String mobileNo);

    @Query("SELECT * FROM tbl_add_appointment where is_synced LIKE :readyToSync")
    List<AddAppointmentEntity> getAppointmentsToSync(int readyToSync);

    @Query("UPDATE tbl_add_appointment set is_Synced = 0 where  is_Synced LIKE :isSynced")
    void updateSyncData(int isSynced);

    @Query("DELETE FROM tbl_add_appointment where  is_Synced LIKE :isSynced")
    void deleteSyncData(int isSynced);

    @Query("DELETE FROM tbl_add_appointment where mobile_no LIKE :mobileNo ")
    void deleteSyncedData(String mobileNo);

    @Insert(onConflict = IGNORE)
    void insertAppointmentEntity(AddAppointmentEntity addAppointmentEntity);
}
