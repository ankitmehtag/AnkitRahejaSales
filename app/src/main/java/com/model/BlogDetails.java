package com.model;

/**
 * Created by Mohit on 7/6/2018.
 */

public class BlogDetails {
    private boolean success;
    private Data data;

    public BlogDetails(boolean success, Data data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String title;
        private String content;
        private String image;
        private String date;
        private String blog_url;
        private String category_name;
        private String is_note;

        public String getIs_note() {
            return is_note;
        }

        public void setIs_note(String is_note) {
            this.is_note = is_note;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getBlog_url() {
            return blog_url;
        }

        public void setBlog_url(String blog_url) {
            this.blog_url = blog_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        private String author;
    }
}
