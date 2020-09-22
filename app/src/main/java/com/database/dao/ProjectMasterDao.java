package com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.database.entity.ProjectMasterEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface ProjectMasterDao extends BaseDao<ProjectMasterEntity> {

    @Query("SELECT * FROM tbl_project_master ")
    List<ProjectMasterEntity> getAllProjects();

    @Query("SELECT * FROM tbl_project_master where project_name LIKE :projectName ")
    ProjectMasterEntity getProjectId(String projectName);

    @Query("SELECT * FROM tbl_project_master where project_id LIKE :projectId ")
    ProjectMasterEntity getProjectName(String projectId);

    @Insert(onConflict = IGNORE)
    void insertAllProjects(List<ProjectMasterEntity> projectsList);
}
