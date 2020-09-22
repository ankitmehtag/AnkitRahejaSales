package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_select_multiple_Projects")
public class SelectMultipleProjectsEntity {


    @ColumnInfo(name = "row_id")
    private int rowId;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "enquiry_id")
    private String enquiryId;
    @ColumnInfo(name = "project_id")
    private String projectId;
    @ColumnInfo(name = "project_name")
    private String projectName;
    @ColumnInfo(name = "isSynced")
    private int isSynced;
    @ColumnInfo(name = "recording_file_path")
    private String recordingFilePath;
    @ColumnInfo(name = "lastupdatedon")
    private String lastupdatedon;

    public SelectMultipleProjectsEntity(String enquiryId, String projectId, String projectName, int isSynced,
                                        String recordingFilePath, String lastupdatedon) {
        setEnquiryId(enquiryId);
        setProjectId(projectId);
        setProjectName(projectName);
        setIsSynced(isSynced);
        setRecordingFilePath(recordingFilePath);
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecordingFilePath() {
        return recordingFilePath;
    }

    public void setRecordingFilePath(String recordingFilePath) {
        this.recordingFilePath = recordingFilePath;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }
}
