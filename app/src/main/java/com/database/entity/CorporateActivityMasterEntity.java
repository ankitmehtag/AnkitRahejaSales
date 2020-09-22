package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.database.Converters;

@Entity(tableName = "tbl_corporate_activity")
@TypeConverters({Converters.class})
public class CorporateActivityMasterEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "activity_id")
    private String activityId;
    @ColumnInfo(name = "activity_name")
    private String activityName;
    @TypeConverters({Converters.class})
    @ColumnInfo(name = "projects")
    private String projectList;
    @ColumnInfo(name = "last_updated_datetime")
    private String lastUpdateDateTime;

    public CorporateActivityMasterEntity(@NonNull String activityId, String activityName,
                                         String projectList, String lastUpdateDateTime) {
        setActivityId(activityId);
        setActivityName(activityName);
        setProjectList(projectList);
        setLastUpdateDateTime(lastUpdateDateTime);
    }

    @NonNull
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(@NonNull String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getProjectList() {
        return projectList;
    }

    public void setProjectList(String projectList) {
        this.projectList = projectList;
    }

    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }
}
