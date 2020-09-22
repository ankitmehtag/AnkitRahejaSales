package com.model;

import java.io.Serializable;

/**
 * Created by Naresh on 25-May-17.
 */

public class ProjectsData implements Serializable {

    private String project_id;
    private String project_url;
    private String project_name;
    private String project_city;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_url() {
        return project_url;
    }

    public void setProject_url(String project_url) {
        this.project_url = project_url;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_city() {
        return project_city;
    }

    public void setProject_city(String project_city) {
        this.project_city = project_city;
    }
}
