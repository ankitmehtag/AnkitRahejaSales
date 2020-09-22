package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_status_master", indices = {@Index(value = {"status_id"}, unique = true)})
public class StatusMasterEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "status_name")
    private String statusName;
    @ColumnInfo(name = "status_id")
    @NonNull
    private String statusId;
    @ColumnInfo(name = "project_nullable")
    private int projectNullable;

    public StatusMasterEntity(String statusName, String statusId, int projectNullable) {
        setStatusName(statusName);
        setStatusId(statusId);
        setProjectNullable(projectNullable);
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public int getProjectNullable() {
        return projectNullable;
    }

    public void setProjectNullable(int projectNullable) {
        this.projectNullable = projectNullable;
    }
}
