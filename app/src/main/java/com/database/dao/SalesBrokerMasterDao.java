package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.SalesBrokerMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SalesBrokerMasterDao extends BaseDao<SalesBrokerMasterEntity> {

    @Query("SELECT * FROM tbl_sales_broker_master ")
    List<SalesBrokerMasterEntity> getAllBrokerMaster();

    @Query("SELECT * FROM tbl_sales_broker_master where title LIKE :brokerName ")
    SalesBrokerMasterEntity getBrokerId(String brokerName);

    @Query("SELECT * FROM tbl_sales_broker_master where id LIKE :brokerId ")
    SalesBrokerMasterEntity getBrokerName(String brokerId);

    @Insert(onConflict = IGNORE)
    void insertAllBrokerMaster(List<SalesBrokerMasterEntity> brokerList);
}
