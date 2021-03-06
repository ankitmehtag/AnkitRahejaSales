package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.database.UpdateStatusConverter;

@Entity(tableName = "tbl_sub_status_callback")
@TypeConverters({UpdateStatusConverter.class})
public class SubStatusCallbackEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "projects")
    private String projects;
    @ColumnInfo(name = "projects_id")
    private String projectsId;
    @ColumnInfo(name = "assign_to")
    private String assignTo;
    @ColumnInfo(name = "assignTo_id")
    private String assignToId;
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
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "lead_type")
    private String leadType;
    @ColumnInfo(name = "remark")
    private String remark;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;

    public SubStatusCallbackEntity(String enquiryId, String userId, String projects, String projectsId,
                                   String assignTo, String assignToId, String oldStatus, String oldStatusId, String newStatus,
                                   String newStatusId,String updateStatusId, String updateStatusList, String date, String time,
                                   String leadType, String remark, String lastupdatedon) {
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
        setDate(date);
        setTime(time);
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
