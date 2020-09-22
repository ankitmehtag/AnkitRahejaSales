package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_sub_status_master")
public class SubStatusMasterEntity {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String id;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    public SubStatusMasterEntity(@NonNull String id, @NonNull String title) {
        this.id = id;
        this.title = title;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
}
