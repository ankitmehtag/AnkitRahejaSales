package com.ChatServerModel;


import java.util.ArrayList;

/**
 * Created by Mohit on 6/22/2018.
 */

public class InitialMessageList {
    private ArrayList<Data> data;

    public InitialMessageList(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data{
        private String message;
        private String created_at;
        private User user;

        public Data(String message, String created_at, User user) {
            this.message = message;
            this.created_at = created_at;
            this.user = user;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
    public class User{
        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
