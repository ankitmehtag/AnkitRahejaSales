package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 11-May-17.
 */

public class ChequeDDPaymentRespModel implements Serializable {

    private int isSuccess;
    private String message;
    private String imageLink;
    private ArrayList<Details> data;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public ArrayList<Details> getData() {
        return data;
    }

    public void setData(ArrayList<Details> data) {
        this.data = data;
    }

    public class Details implements Serializable{
        private String caption;
        private String value;

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
