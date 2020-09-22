package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_add_appointment")
public class AddAppointmentEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "builder_id")
    private String builderId;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    @ColumnInfo(name = "broker_id")
    private String brokerId;
    @ColumnInfo(name = "email_id")
    private String emailId;
    @ColumnInfo(name = "mobile_no")
    private String mobileNo;
    @ColumnInfo(name = "project")
    private String project;
    @ColumnInfo(name = "alternate_no")
    private String alternateNo;
    @ColumnInfo(name = "budget")
    private String budget;
    @ColumnInfo(name = "status_id")
    private String statusId;
    @ColumnInfo(name = "status_title")
    private String statusTitle;
    @ColumnInfo(name = "date_time")
    private String dateTime;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "no_of_persons")
    private String noOfPersons;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "activity_id")
    private String activityId;
    @ColumnInfo(name = "activity_name")
    private String activityName;
    @ColumnInfo(name = "is_Synced")
    private int isSynced;
    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    public AddAppointmentEntity(String builderId, String userId, String type, String customerName, String brokerId,
                                String emailId, String mobileNo, String project, String alternateNo,
                                String budget, String statusId, String statusTitle, String dateTime, String remark,
                                String noOfPersons, String address, String leadType, String activityId, String activityName,
                                int isSynced, String timeStamp) {
        setBuilderId(builderId);
        setUserId(userId);
        setType(type);
        setCustomerName(customerName);
        setBrokerId(brokerId);
        setEmailId(emailId);
        setMobileNo(mobileNo);
        setProject(project);
        setAlternateNo(alternateNo);
        setBudget(budget);
        setStatusId(statusId);
        setStatusTitle(statusTitle);
        setDateTime(dateTime);
        setRemark(remark);
        setNoOfPersons(noOfPersons);
        setAddress(address);
        setLeadType(leadType);
        setActivityId(activityId);
        setActivityName(activityName);
        setIsSynced(isSynced);
        setTimeStamp(timeStamp);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAlternateNo() {
        return alternateNo;
    }

    public void setAlternateNo(String alternateNo) {
        this.alternateNo = alternateNo;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(String noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
