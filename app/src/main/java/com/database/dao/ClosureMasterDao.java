package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.ClosureMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface ClosureMasterDao extends BaseDao<ClosureMasterEntity> {

    @Query("SELECT * FROM tbl_sales_closure_master")
    List<ClosureMasterEntity> getAllClosureMasterList();

    @Query("SELECT COUNT(*) from tbl_sales_closure_master")
    int getClosuredMasterCount();

    @Insert(onConflict = IGNORE)
    void insertAllClosureMaster(List<ClosureMasterEntity> closureList);
}
