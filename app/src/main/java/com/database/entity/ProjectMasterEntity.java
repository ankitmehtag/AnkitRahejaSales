package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_project_master", indices = {@Index(value = {"project_id"}, unique = true)})
public class ProjectMasterEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "project_name")
    private String projectName;
    @NonNull
    @ColumnInfo(name = "project_id")
    private String projectId;

    public ProjectMasterEntity(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
