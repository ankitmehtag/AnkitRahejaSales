package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.StatusMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface StatusMasterDao extends BaseDao<StatusMasterEntity> {

    @Query("SELECT * FROM tbl_status_master ")
    List<StatusMasterEntity> getAllStatusMasterList();

    @Query("SELECT * FROM tbl_status_master where project_nullable LIKE :isNullable ")
    StatusMasterEntity getStatusByIsNullable(boolean isNullable);

    @Query("SELECT COUNT(*) from tbl_status_master")
    int getStatusMasterCount();

    @Insert(onConflict = IGNORE)
    void insertAllStatus(List<StatusMasterEntity> statusList);
}
