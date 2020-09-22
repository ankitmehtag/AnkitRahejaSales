package com.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.database.entity.CorporateActivityMasterEntity;

import java.util.List;


@Dao
public interface CorporateActivityMasterDao extends BaseDao<CorporateActivityMasterEntity> {

    @Query("SELECT * FROM tbl_corporate_activity")
    List<CorporateActivityMasterEntity> getAllCorpActivity();

    @Query("SELECT * FROM tbl_corporate_activity where activity_id LIKE :activityId ORDER BY last_updated_datetime DESC LIMIT 1")
    CorporateActivityMasterEntity getCorpActivityByActivityId(String activityId);

    @Query("SELECT * FROM tbl_corporate_activity where activity_id LIKE :activityId ORDER BY last_updated_datetime DESC LIMIT 1")
    List<CorporateActivityMasterEntity> getCorpActivityList(String activityId);

}
