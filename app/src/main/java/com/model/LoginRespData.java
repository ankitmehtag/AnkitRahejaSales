package com.model;

/**
 * Created by Naresh on 06-Oct-17.
 */

public class LoginRespData {
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

    public class Data {
        private String first_name;
        private String last_name;
        private String designation;
        private String user_id;
        private String parent_builder_name;
        private String parent_builder_id;
        private String emp_code;
        private String user_image;
        private String email;
        private String phone_number;
        private boolean pre_sales;

        public boolean getPreSales() {
            return pre_sales;
        }

        public void setPreSales(boolean pre_sales) {
            this.pre_sales = pre_sales;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getParent_builder_name() {
            return parent_builder_name;
        }

        public void setParent_builder_name(String parent_builder_name) {
            this.parent_builder_name = parent_builder_name;
        }

        public String getParent_builder_id() {
            return parent_builder_id;
        }

        public void setParent_builder_id(String parent_builder_id) {
            this.parent_builder_id = parent_builder_id;
        }

        public String getEmp_code() {
            return emp_code;
        }

        public void setEmp_code(String emp_code) {
            this.emp_code = emp_code;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }
    }
}
