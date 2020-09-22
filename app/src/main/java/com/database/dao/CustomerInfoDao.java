package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.database.entity.CustomerInfoEntity;

import java.util.List;

@Dao
public interface CustomerInfoDao extends BaseDao<CustomerInfoEntity> {

    @Query(value = "SELECT * FROM tbl_customer_info ")
    List<CustomerInfoEntity> getAllCustomerInfo();

    @Query("SELECT * FROM tbl_customer_info where customer_mobile LIKE :customerMob ")
    CustomerInfoEntity getCustomerInfoByMobile(String customerMob);

    @Query("SELECT * FROM tbl_customer_info where enquiry_id LIKE :enquiryId ")
    CustomerInfoEntity getCustomerInfoByEnquiry(String enquiryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCustomerInfo(List<CustomerInfoEntity> customerInfoEntityList);

    @Query("UPDATE tbl_customer_info set customer_name = :customerName where  customer_mobile LIKE :mobileNo")
    void updateCustomerName(String customerName, String mobileNo);
}
