package com.model;

import java.io.Serializable;

/**
 * Created by Mohit on 7/4/2018.
 */

public class Banner implements Serializable {
    private String banner_text;
    private String banner_img;
    private String blog_id;

    public String getBanner_text() {
        return banner_text;
    }

    public void setBanner_text(String banner_text) {
        this.banner_text = banner_text;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }
}
