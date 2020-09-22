package com.model;

/**
 * Created by Mohit on 3/27/2018.
 */

public class GetBadgeCount {
    private boolean success;
    private Data data;

    public GetBadgeCount(boolean success, Data data) {
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

    public static class Data {
        private String unread_count;

        public Data(String unread_count) {
            this.unread_count = unread_count;
        }

        public String getUnread_count() {
            return unread_count;
        }

        public void setUnread_count(String unread_count) {
            this.unread_count = unread_count;
        }
    }
}
