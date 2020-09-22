package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.database.UpdateStatusConverter;

@Entity(tableName = "tbl_sub_status_closure")
@TypeConverters({UpdateStatusConverter.class})
public class SubStatusClosureEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "old_status")
    private String oldStatus;
    @ColumnInfo(name = "old_status_id")
    private String oldStatusId;
    @ColumnInfo(name = "new_status")
    private String newStatus;
    @ColumnInfo(name = "new_status_id")
    private String newStatusId;
    @ColumnInfo(name = "update_status_id")
    private String updateStatusId;
    @TypeConverters({UpdateStatusConverter.class})
    @ColumnInfo(name = "update_status_list")
    private String updateStatusList;
    @ColumnInfo(name = "new_sub_status")
    private String newSubStatus;
    @ColumnInfo(name = "new_sub_status_id")
    private String newSubStatusId;
    @ColumnInfo(name = "cheque_no")
    private String chequeNo;
    @ColumnInfo(name = "bank_name")
    private String bankName;
    @ColumnInfo(name = "cheque_date")
    private String chequeDate;
    @ColumnInfo(name = "amount")
    private String amount;
    @ColumnInfo(name = "projects")
    private String projects;
    @ColumnInfo(name = "projects_id")
    private String projectsId;
    @ColumnInfo(name = "assign_to")
    private String assignTo;
    @ColumnInfo(name = "assignTo_Id")
    private String assignToId;
    @ColumnInfo(name = "closer_Project")
    private String closerProject;
    @ColumnInfo(name = "closer_ProjectId")
    private String closerProjectId;
    @ColumnInfo(name = "tower_no")
    private String towerNo;
    @ColumnInfo(name = "unit_no")
    private String unitNo;
    @ColumnInfo(name = "attachment")
    private String attachment;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;

    public SubStatusClosureEntity(String enquiryId, String userId, String projects, String projectsId,
                                  String assignTo, String assignToId, String oldStatus, String oldStatusId, String newStatus,
                                  String newStatusId,String updateStatusId, String updateStatusList, String newSubStatus,
                                  String newSubStatusId, String chequeNo, String bankName, String chequeDate, String amount,
                                  String closerProject, String closerProjectId, String towerNo, String unitNo,
                                  String attachment, String leadType, String remark, String lastupdatedon) {
        setEnquiryId(enquiryId);
        setUserId(userId);
        setProjects(projects);
        setProjectsId(projectsId);
        setAssignTo(assignTo);
        setAssignToId(assignToId);
        setOldStatus(oldStatus);
        setOldStatusId(oldStatusId);
        setNewStatus(newStatus);
        setNewStatusId(newStatusId);
        setUpdateStatusId(updateStatusId);
        setUpdateStatusList(updateStatusList);
        setNewSubStatus(newSubStatus);
        setNewSubStatusId(newSubStatusId);
        setChequeNo(chequeNo);
        setBankName(bankName);
        setChequeDate(chequeDate);
        setAmount(amount);
        setCloserProject(closerProject);
        setCloserProjectId(closerProjectId);
        setTowerNo(towerNo);
        setUnitNo(unitNo);
        setAttachment(attachment);
        setLeadType(leadType);
        setRemark(remark);
        setLastupdatedon(lastupdatedon);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getOldStatusId() {
        return oldStatusId;
    }

    public void setOldStatusId(String oldStatusId) {
        this.oldStatusId = oldStatusId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewStatusId() {
        return newStatusId;
    }

    public void setNewStatusId(String newStatusId) {
        this.newStatusId = newStatusId;
    }

    public String getUpdateStatusList() {
        return updateStatusList;
    }

    public void setUpdateStatusList(String updateStatusList) {
        this.updateStatusList = updateStatusList;
    }

    public String getUpdateStatusId() {
        return updateStatusId;
    }

    public void setUpdateStatusId(String updateStatusId) {
        this.updateStatusId = updateStatusId;
    }

    public String getNewSubStatus() {
        return newSubStatus;
    }

    public void setNewSubStatus(String newSubStatus) {
        this.newSubStatus = newSubStatus;
    }

    public String getNewSubStatusId() {
        return newSubStatusId;
    }

    public void setNewSubStatusId(String newSubStatusId) {
        this.newSubStatusId = newSubStatusId;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(String projectsId) {
        this.projectsId = projectsId;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getAssignToId() {
        return assignToId;
    }

    public void setAssignToId(String assignToId) {
        this.assignToId = assignToId;
    }

    public String getCloserProject() {
        return closerProject;
    }

    public void setCloserProject(String closerProject) {
        this.closerProject = closerProject;
    }

    public String getCloserProjectId() {
        return closerProjectId;
    }

    public void setCloserProjectId(String closerProjectId) {
        this.closerProjectId = closerProjectId;
    }

    public String getTowerNo() {
        return towerNo;
    }

    public void setTowerNo(String towerNo) {
        this.towerNo = towerNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }
}
