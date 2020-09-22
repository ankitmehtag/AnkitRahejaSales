package com.model;

import java.util.List;

public class ClosureDetailsModel {
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
        private String Enquiry_ID;
        private String Name;
        private String Email_ID;
        private String Mobile_No;
        private String Budget;
        private String Current_Status;
        private String scheduledatetime;
        private String subStatus;
        private String amount;
        private String project_name;
        private String unit_no;
        private String tower_no;
        private String remark;
        private String cheque_number;
        private String cheque_date;
        private String bank_name;
        private String lastupdatedon;

        public String getEnquiry_ID() {
            return Enquiry_ID;
        }

        public void setEnquiry_ID(String enquiry_ID) {
            Enquiry_ID = enquiry_ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getEmail_ID() {
            return Email_ID;
        }

        public void setEmail_ID(String email_ID) {
            Email_ID = email_ID;
        }

        public String getMobile_No() {
            return Mobile_No;
        }

        public void setMobile_No(String mobile_No) {
            Mobile_No = mobile_No;
        }

        public String getBudget() {
            return Budget;
        }

        public void setBudget(String budget) {
            Budget = budget;
        }

        public String getCurrent_Status() {
            return Current_Status;
        }

        public void setCurrent_Status(String current_Status) {
            Current_Status = current_Status;
        }

        public String getScheduledatetime() {
            return scheduledatetime;
        }

        public void setScheduledatetime(String scheduledatetime) {
            this.scheduledatetime = scheduledatetime;
        }

        public String getSubStatus() {
            return subStatus;
        }

        public void setSubStatus(String subStatus) {
            this.subStatus = subStatus;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public String getUnit_no() {
            return unit_no;
        }

        public void setUnit_no(String unit_no) {
            this.unit_no = unit_no;
        }

        public String getTower_no() {
            return tower_no;
        }

        public void setTower_no(String tower_no) {
            this.tower_no = tower_no;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCheque_number() {
            return cheque_number;
        }

        public void setCheque_number(String cheque_number) {
            this.cheque_number = cheque_number;
        }

        public String getCheque_date() {
            return cheque_date;
        }

        public void setCheque_date(String cheque_date) {
            this.cheque_date = cheque_date;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getLastupdatedon() {
            return lastupdatedon;
        }

        public void setLastupdatedon(String lastupdatedon) {
            this.lastupdatedon = lastupdatedon;
        }
    }
}
