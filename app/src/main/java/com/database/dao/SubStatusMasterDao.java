package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SubStatusMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubStatusMasterDao extends BaseDao<SubStatusMasterEntity> {

    @Query("SELECT * FROM tbl_sub_status_master ")
    List<SubStatusMasterEntity> getSubStatusMasterList();

    @Query("SELECT COUNT(*) from tbl_sub_status_master")
    int getSubStatusMasterCount();

    @Insert(onConflict = IGNORE)
    void insertSubStatusMasterList(List<SubStatusMasterEntity> statusList);
}
