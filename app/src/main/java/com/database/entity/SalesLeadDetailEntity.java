package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.database.UpdateStatusConverter;

@Entity(tableName = "tbl_sales_lead_detail")
@TypeConverters({UpdateStatusConverter.class})
public class SalesLeadDetailEntity {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "customer_name")
    private String customerName;
    @ColumnInfo(name = "customer_email")
    private String customerEmail;
    @ColumnInfo(name = "customer_mobile")
    private String customerMobile;
    @ColumnInfo(name = "customer_Alternate_mobile")
    private String customerAlternateMobile;
    @ColumnInfo(name = "projects")
    private String projects;
    @ColumnInfo(name = "project_id")
    private String projectId;
    @ColumnInfo(name = "budget")
    private String budget;
    @ColumnInfo(name = "assigned_to")
    private String assignedTo;
    @ColumnInfo(name = "assigned_to_id")
    private String assignedToId;
    @ColumnInfo(name = "is_lead_type")
    private String isLeadType;
    @ColumnInfo(name = "current_status")
    private String currentStatus;
    @ColumnInfo(name = "status_id")
    private int statusId;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "scheduled_date_time")
    private String scheduledDateTime;
    @ColumnInfo(name = "is_assigned")
    private int isAssigned;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "no_of_person")
    private int noOfPerson;
    @ColumnInfo(name = "update_status_id")
    private String updateStatusId;
    @TypeConverters({UpdateStatusConverter.class})
    @ColumnInfo(name = "update_status_list")
    private String updateStatusList;
    // Parameter for closure detail
    @ColumnInfo(name = "unit_no")
    private String unitNo;
    @ColumnInfo(name = "amount")
    private String amount;
    @ColumnInfo(name = "tower_no")
    private String towerNo;
    @ColumnInfo(name = "cheque_number")
    private String chequeNumber;
    @ColumnInfo(name = "cheque_date")
    private String chequeDate;
    @ColumnInfo(name = "payment_mode")
    private String payment_mode;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;

    // Parameter for new status update

    @ColumnInfo(name = "new_status_id")
    private int newStatusId;
    @ColumnInfo(name = "new_lead_status")
    private String newLeadStatus;
    @ColumnInfo(name = "new_date")
    private String newDate;
    @ColumnInfo(name = "new_time")
    private String newTime;
    @ColumnInfo(name = "new_sub_status")
    private String new_sub_status;
    @ColumnInfo(name = "new_sub_status_id")
    private String new_sub_status_id;
    @ColumnInfo(name = "closure_project_id")
    private String closure_project_id;
    @ColumnInfo(name = "person_count")
    private int newPersonCount;
    @ColumnInfo(name = "new_address")
    private String newAddress;
    @ColumnInfo(name = "new_remark")
    private String newRemark;

    @ColumnInfo(name = "lead_owner")
    private String leadOwner;
    @ColumnInfo(name = "campaign_name")
    private String campaignName;
    @ColumnInfo(name = "campaign_date")
    private String campaignDate;
    @ColumnInfo(name = "campaign_id")
    private int campaignId;
    @ColumnInfo(name = "is_updated")
    private int isUpdated;
    @ColumnInfo(name = "is_synced")
    private int isSynced;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;
    @ColumnInfo(name = "designation")
    private String designationType;
    @ColumnInfo(name = "user_action")
    private int userAction;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;

    public SalesLeadDetailEntity(@NonNull String enquiryId, String customerName, String customerEmail, String customerMobile,
                                 String customerAlternateMobile, String projects, String projectId, String budget,
                                 String assignedTo, String assignedToId, String isLeadType, String currentStatus,
                                 int statusId, String scheduledDateTime, String date, String time, int isAssigned,
                                 String address, int noOfPerson, String updateStatusId, String updateStatusList,
                                 String unitNo, String amount, String towerNo, String chequeNumber, String chequeDate,
                                 String payment_mode, String leadType, String remark, int newStatusId, String newLeadStatus,
                                 String newDate, String newTime, String new_sub_status, String new_sub_status_id,
                                 String closure_project_id, int newPersonCount, String newAddress, String newRemark,
                                 String leadOwner, String campaignName, String campaignDate, int campaignId, int isUpdated,
                                 int isSynced, String lastupdatedon, String designationType, int userAction,
                                 String recordingFilePath) {
        this.enquiryId = enquiryId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerMobile = customerMobile;
        this.customerAlternateMobile = customerAlternateMobile;
        this.projects = projects;
        this.projectId = projectId;
        this.budget = budget;
        this.assignedTo = assignedTo;
        this.assignedToId = assignedToId;
        this.isLeadType = isLeadType;
        this.currentStatus = currentStatus;
        this.statusId = statusId;
        this.date = date;
        this.time = time;
        this.scheduledDateTime = scheduledDateTime;
        this.isAssigned = isAssigned;
        this.address = address;
        this.noOfPerson = noOfPerson;
        this.updateStatusId = updateStatusId;
        this.updateStatusList = updateStatusList;
        this.unitNo = unitNo;
        this.amount = amount;
        this.towerNo = towerNo;
        this.chequeNumber = chequeNumber;
        this.chequeDate = chequeDate;
        this.payment_mode = payment_mode;
        this.leadType = leadType;
        this.remark = remark;
        this.newStatusId = newStatusId;
        this.newLeadStatus = newLeadStatus;
        this.newDate = newDate;
        this.newTime = newTime;
        this.new_sub_status = new_sub_status;
        this.new_sub_status_id = new_sub_status_id;
        this.closure_project_id = closure_project_id;
        this.newPersonCount = newPersonCount;
        this.newAddress = newAddress;
        this.newRemark = newRemark;
        this.leadOwner = leadOwner;
        this.campaignName = campaignName;
        this.campaignDate = campaignDate;
        this.campaignId = campaignId;
        this.isUpdated = isUpdated;
        this.isSynced = isSynced;
        this.lastupdatedon = lastupdatedon;
        this.designationType = designationType;
        this.userAction = userAction;
        this.recordingFilePath = recordingFilePath;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAlternateMobile() {
        return customerAlternateMobile;
    }

    public void setCustomerAlternateMobile(String customerAlternateMobile) {
        this.customerAlternateMobile = customerAlternateMobile;
    }

    public String getUpdateStatusId() {
        return updateStatusId;
    }

    public void setUpdateStatusId(String updateStatusId) {
        this.updateStatusId = updateStatusId;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getIsLeadType() {
        return isLeadType;
    }

    public void setIsLeadType(String isLeadType) {
        this.isLeadType = isLeadType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(int isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNoOfPerson() {
        return noOfPerson;
    }

    public void setNoOfPerson(int noOfPerson) {
        this.noOfPerson = noOfPerson;
    }

    public String getUpdateStatusList() {
        return updateStatusList;
    }

    public void setUpdateStatusList(String updateStatusList) {
        this.updateStatusList = updateStatusList;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getNewStatusId() {
        return newStatusId;
    }

    public void setNewStatusId(int newStatusId) {
        this.newStatusId = newStatusId;
    }

    public String getNewLeadStatus() {
        return newLeadStatus;
    }

    public void setNewLeadStatus(String newLeadStatus) {
        this.newLeadStatus = newLeadStatus;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getNew_sub_status() {
        return new_sub_status;
    }

    public void setNew_sub_status(String new_sub_status) {
        this.new_sub_status = new_sub_status;
    }

    public String getNew_sub_status_id() {
        return new_sub_status_id;
    }

    public void setNew_sub_status_id(String new_sub_status_id) {
        this.new_sub_status_id = new_sub_status_id;
    }

    public String getClosure_project_id() {
        return closure_project_id;
    }

    public void setClosure_project_id(String closure_project_id) {
        this.closure_project_id = closure_project_id;
    }

    public int getNewPersonCount() {
        return newPersonCount;
    }

    public void setNewPersonCount(int newPersonCount) {
        this.newPersonCount = newPersonCount;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getNewRemark() {
        return newRemark;
    }

    public void setNewRemark(String newRemark) {
        this.newRemark = newRemark;
    }

    public String getLeadOwner() {
        return leadOwner;
    }

    public void setLeadOwner(String leadOwner) {
        this.leadOwner = leadOwner;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDate() {
        return campaignDate;
    }

    public void setCampaignDate(String campaignDate) {
        this.campaignDate = campaignDate;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
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

    public String getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(String scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getDesignationType() {
        return designationType;
    }

    public void setDesignationType(String designationType) {
        this.designationType = designationType;
    }

    public int getUserAction() {
        return userAction;
    }

    public void setUserAction(int userAction) {
        this.userAction = userAction;
    }

    public String getRecordingFilePath() {
        return recordingFilePath;
    }

    public void setRecordingFilePath(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTowerNo() {
        return towerNo;
    }

    public void setTowerNo(String towerNo) {
        this.towerNo = towerNo;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

}
