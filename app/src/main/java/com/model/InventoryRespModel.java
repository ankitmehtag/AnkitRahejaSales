package com.model;

import java.util.ArrayList;

public class InventoryRespModel {

    private boolean success;
    private String message;
    private String fromdate;
    private String todate;
    private ArrayList<Data> data;

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

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public ArrayList<Data> getData() {
        return data;
    }


    public class Data {

        private String download_path;
        private String file_name;
        private String file_type;
        private String category;
        private String upload_date;

        public String getDownload_path() {
            return download_path;
        }

        public void setDownload_path(String download_path) {
            this.download_path = download_path;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getFile_type() {
            return file_type;
        }

        public void setFile_type(String file_type) {
            this.file_type = file_type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getUpload_date() {
            return upload_date;
        }

        public void setUpload_date(String upload_date) {
            this.upload_date = upload_date;
        }
    }
}
