package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.UniversalContactsEntity;

import java.util.List;

@Dao
public interface UniversalContactsDao extends BaseDao<UniversalContactsEntity> {

    @Query(value = "SELECT * FROM  tbl_universal_contacts")
    List<UniversalContactsEntity> getUniversalContacts();

    @Query("SELECT * FROM tbl_universal_contacts where mobile_no LIKE :contactNo ")
    UniversalContactsEntity getUniversalContactsByMobile(String contactNo);

    @Query("SELECT * FROM tbl_universal_contacts where is_synced LIKE :readyToSync")
    List<UniversalContactsEntity> getRecordingToSync(int readyToSync);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUniversalContacts(List<UniversalContactsEntity> universalContactsEntities);
}
