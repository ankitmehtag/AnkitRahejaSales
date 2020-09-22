package com.model;

import java.util.ArrayList;

/**
 * Created by Mohit on 7/7/2018.
 */

public class RecentBlogModel {
    private boolean success;
    private Data data;

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

    public class Data {
        private ArrayList<Recent> recent;
        private ArrayList<Recent> popular;

        public ArrayList<Recent> getRecent() {
            return recent;
        }

        public void setRecent(ArrayList<Recent> recent) {
            this.recent = recent;
        }

        public ArrayList<Recent> getPopular() {
            return popular;
        }

        public void setPopular(ArrayList<Recent> popular) {
            this.popular = popular;
        }

        public class Recent {
            private String blog_id;
            private String image;
            private String title;
            private String content;
            private boolean isRead;

            public String getBlog_id() {
                return blog_id;
            }

            public void setBlog_id(String blog_id) {
                this.blog_id = blog_id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
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

            public boolean isRead() {
                return isRead;
            }

            public void setRead(boolean read) {
                isRead = read;
            }
        }

    }
}
