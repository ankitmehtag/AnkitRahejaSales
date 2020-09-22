package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_sales_not_interested_master")
public class NotInterestedMasterEntity {
    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String id;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    public NotInterestedMasterEntity(@NonNull String id, @NonNull String title) {
        setId(id);
        setTitle(title);
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
