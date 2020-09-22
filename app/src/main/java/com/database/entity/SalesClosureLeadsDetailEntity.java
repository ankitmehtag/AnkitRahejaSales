package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_sales_closure_lead_detail")
public class SalesClosureLeadsDetailEntity {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    @ColumnInfo(name = "customer_mobile")
    private String customerMobile;
    @ColumnInfo(name = "customer_email")
    private String customerEmail;
    @ColumnInfo(name = "budget")
    private String budget;
    @ColumnInfo(name = "projects")
    private String projects;
    @ColumnInfo(name = "current_status")
    private String currentStatus;
    @ColumnInfo(name = "sub_status")
    private String subStatus;
    @ColumnInfo(name = "unit_no")
    private String unitNo;
    @ColumnInfo(name = "scheduledatetime")
    private String scheduledatetime;
    @ColumnInfo(name = "transaction_amount")
    private String transactionAmount;
    @ColumnInfo(name = "tower_no")
    private String towerNo;
    @ColumnInfo(name = "cheque_no")
    private String chequeNo;
    @ColumnInfo(name = "cheque_date")
    private String chequeDate;
    @ColumnInfo(name = "bank_name")
    private String bankName;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "is_updated")
    private int isUpdated;
    @ColumnInfo(name = "is_synced")
    private int isSynced;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;
    @ColumnInfo(name = "designation")
    private String designationType;

    public SalesClosureLeadsDetailEntity(@NonNull String enquiryId, String customerName, String customerMobile, String customerEmail,
                                         String budget, String projects, String currentStatus, String subStatus, String unitNo, String scheduledatetime,
                                         String transactionAmount, String towerNo, String chequeNo, String chequeDate, String bankName,
                                         String remark, int isUpdated, int isSynced, String lastupdatedon, String designationType) {
        setEnquiryId(enquiryId);
        setCustomerName(customerName);
        setCustomerMobile(customerMobile);
        setCustomerEmail(customerEmail);
        setBudget(budget);
        setProjects(projects);
        setCurrentStatus(currentStatus);
        setSubStatus(subStatus);
        setUnitNo(unitNo);
        setScheduledatetime(scheduledatetime);
        setTransactionAmount(transactionAmount);
        setTowerNo(towerNo);
        setChequeNo(chequeNo);
        setChequeDate(chequeDate);
        setBankName(bankName);
        setRemark(remark);
        setIsUpdated(isUpdated);
        setIsSynced(isSynced);
        setLastupdatedon(lastupdatedon);
        setDesignationType(designationType);
    }

    @NonNull
    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(@NonNull String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getScheduledatetime() {
        return scheduledatetime;
    }

    public void setScheduledatetime(String scheduledatetime) {
        this.scheduledatetime = scheduledatetime;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTowerNo() {
        return towerNo;
    }

    public void setTowerNo(String towerNo) {
        this.towerNo = towerNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }

    public String getDesignationType() {
        return designationType;
    }

    public void setDesignationType(String designationType) {
        this.designationType = designationType;
    }
}
