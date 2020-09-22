package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.NotInterestedMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface NotInterestedMasterDao extends BaseDao<NotInterestedMasterEntity> {

    @Query("SELECT * FROM tbl_sales_not_interested_master ")
    List<NotInterestedMasterEntity> getNotInterestedMasterList();

    @Query("SELECT COUNT(*) from tbl_sales_not_interested_master")
    int getNotInterestedMasterCount();

    @Insert(onConflict = IGNORE)
    void insertAllMaster(List<NotInterestedMasterEntity> statusList);
}
