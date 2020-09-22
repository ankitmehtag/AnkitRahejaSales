package com.model;

import java.util.ArrayList;

/**
 * Created by Naresh on 25-Jan-18.
 */

public class ChatTagsRespModel {
    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String chat_id;
        private ArrayList<Tag> tag;
        private String user_chat_id;
        private String builder_chat_id;

        public String getChat_id() {
            return chat_id;
        }

        public void setChat_id(String chat_id) {
            this.chat_id = chat_id;
        }

        public ArrayList<Tag> getTag() {
            return tag;
        }

        public void setTag(ArrayList<Tag> tag) {
            this.tag = tag;
        }

        public String getUser_chat_id() {
            return user_chat_id;
        }

        public void setUser_chat_id(String user_chat_id) {
            this.user_chat_id = user_chat_id;
        }

        public String getBuilder_chat_id() {
            return builder_chat_id;
        }

        public void setBuilder_chat_id(String builder_chat_id) {
            this.builder_chat_id = builder_chat_id;
        }
    }

    public class Tag{
        private String tag_id;
        private String tag_string;
        private String message;

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getTag_string() {
            return tag_string;
        }

        public void setTag_string(String tag_string) {
            this.tag_string = tag_string;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
