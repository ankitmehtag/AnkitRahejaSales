package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 06-Dec-16.
 */

public class SearchRespBean {

    private String success;
    private ArrayList<Data> data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data{
        private String title;
        private String subtitle;
        private String buildername;
        private String id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getBuildername() {
            return buildername;
        }

        public void setBuildername(String buildername) {
            this.buildername = buildername;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
