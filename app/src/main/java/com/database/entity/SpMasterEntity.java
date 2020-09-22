package com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;


@Entity(tableName = "tbl_sp_master", indices = {@Index(value = {"sp_id"}, unique = true)})
public class SpMasterEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @NonNull
    @ColumnInfo(name = "sp_id")
    private String spId;
    @ColumnInfo(name = "sp_name")
    private String spName;

    public SpMasterEntity(String spId, String spName) {
        this.spId = spId;
        this.spName = spName;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }
}
