package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_customer_info")
public class CustomerInfoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "customer_mobile")
    private String mobileNo;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    @ColumnInfo(name = "lead_phase")
    private String leadPhase;
    @ColumnInfo(name = "assign_type")
    private String assignType;

    public CustomerInfoEntity(String mobileNo, String enquiryId, String customerName, String leadPhase, String assignType) {
        this.mobileNo = mobileNo;
        this.enquiryId = enquiryId;
        this.customerName = customerName;
        this.leadPhase = leadPhase;
        this.assignType = assignType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLeadPhase() {
        return leadPhase;
    }

    public void setLeadPhase(String leadPhase) {
        this.leadPhase = leadPhase;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }
}
