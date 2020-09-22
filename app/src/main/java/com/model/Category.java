package com.model;

import java.util.ArrayList;

/**
 * Created by Mohit on 7/5/2018.
 */

public class Category {
    private String category_id;
    private String category_name;
    private Banner banner;
    private ArrayList<BlogData> blog_data;

    public Category(String category_id, String category_name, Banner banner, ArrayList<BlogData> blog_data) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.banner = banner;
        this.blog_data = blog_data;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public ArrayList<BlogData> getBlog_data() {
        return blog_data;
    }

    public void setBlog_data(ArrayList<BlogData> blog_data) {
        this.blog_data = blog_data;
    }
}
