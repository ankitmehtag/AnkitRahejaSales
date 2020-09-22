package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SpMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SpMasterDao extends BaseDao<SpMasterEntity> {

    @Query("SELECT * FROM tbl_sp_master ")
    List<SpMasterEntity> getAllSalesMan();

    @Query("SELECT * FROM tbl_sp_master where sp_name LIKE :spName ")
    SpMasterEntity getSpId(String spName);

    @Query("SELECT * FROM tbl_sp_master where sp_id LIKE :spId ")
    SpMasterEntity getSpName(String spId);

    @Insert(onConflict = IGNORE)
    void insertAllSp(List<SpMasterEntity> spList);
}
