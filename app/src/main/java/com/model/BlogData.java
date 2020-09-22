package com.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohit on 7/5/2018.
 */

public class BlogData implements Parcelable {
    private String blog_id;
    private String image;
    private String title;
    private String category_name;
    private String content;
    private String date;
    private boolean isRead;

    public BlogData(String blog_id, String image, String title, String category_name, String content, String date, boolean isRead) {
        this.blog_id = blog_id;
        this.image = image;
        this.title = title;
        this.category_name = category_name;
        this.content = content;
        this.date = date;
        this.isRead = isRead;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blog_id);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeString(this.category_name);
        dest.writeString(this.content);
        dest.writeString(this.date);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
    }

    protected BlogData(Parcel in) {
        this.blog_id = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.category_name = in.readString();
        this.content = in.readString();
        this.date = in.readString();
        this.isRead = in.readByte() != 0;
    }

    public static final Creator<BlogData> CREATOR = new Creator<BlogData>() {
        @Override
        public BlogData createFromParcel(Parcel source) {
            return new BlogData(source);
        }

        @Override
        public BlogData[] newArray(int size) {
            return new BlogData[size];
        }
    };
}
