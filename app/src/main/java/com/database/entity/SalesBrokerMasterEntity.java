package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "tbl_sales_broker_master")
public class SalesBrokerMasterEntity {

    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "id")
    private String id;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "address")
    private String address;

    public SalesBrokerMasterEntity(String id, @NonNull String title, String address) {
        this.id = id;
        this.title = title;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
