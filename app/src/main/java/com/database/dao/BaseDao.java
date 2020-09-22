package com.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> entityList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(T entity);

    @Delete
    void delete(T entity);

    @Delete
    void delete(T... entities);
}
