package com.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Naresh on 09-Feb-18.
 */

public class NotificationsListRespModel {

    private boolean success;
    private String message;
    private ArrayList<Data> data;
    public static final int TERMS_AND_CONDITION = 0, BLOG = 1, FAQ = 2, PROJECT_LIST = 3,
            PROJECT_DETAIL = 4, UNIT_DETAILS = 5, UNIT_LIST = 6, PAYMENT = 7, PAYMENT_TYPE = 10;

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data {
        private String title;
        private String message;
        private String image;
        private int type;
        private TargetData target_info;
        private long timestamp;
        private String notification_id;
        private String isRead;

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public TargetData getTarget_info() {
            return target_info;
        }

        public void setTarget_info(TargetData target_info) {
            this.target_info = target_info;
        }


        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * Target: 0- ERROR, 1-BLOG, 2-FAQ, 3-PROJECT LIST, 4-PROJECT DETAIL, 5-UNIT Details, 6-UNIT LIST, 7-PAYMENT
         */
        public class TargetData {
            // for target -6
            private String Customer_Mobile_no;
            private String project_plan_img;
            private String Customer_Name;
            private String brokerage_terms;
            private Cords cords;



            private String project_type;

            //for target -7 means payment status
            private String coapplicant;
            private String coupen_code;
            private String display_name;
            private String order_no;
            private String paid_amount;
            private String size;
            private String payment_plan_name;
            private String payment_plan_desc;
            private String transaction_amount;
            private String transaction_datetime;
            private String order_id;
            private String order_status;

            public String getProject_type() {
                return project_type;
            }

            public void setProject_type(String project_type) {
                this.project_type = project_type;
            }

            public String getProject_plan_img() {
                return project_plan_img;
            }

            public void setProject_plan_img(String project_plan_img) {
                this.project_plan_img = project_plan_img;
            }

            public Cords getCords() {
                return cords;
            }

            public void setCords(Cords cords) {
                this.cords = cords;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getOrder_status() {
                return order_status;
            }

            public void setOrder_status(String order_status) {
                this.order_status = order_status;
            }

            public String getPayment_mode() {
                return payment_mode;
            }

            public void setPayment_mode(String payment_mode) {
                this.payment_mode = payment_mode;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getCustomer_telephone() {
                return customer_telephone;
            }

            public void setCustomer_telephone(String customer_telephone) {
                this.customer_telephone = customer_telephone;
            }

            public String getCustomer_address() {
                return customer_address;
            }

            public void setCustomer_address(String customer_address) {
                this.customer_address = customer_address;
            }

            public String getCustomer_email() {
                return customer_email;
            }

            public void setCustomer_email(String customer_email) {
                this.customer_email = customer_email;
            }

            public String getStatus_message() {
                return status_message;
            }

            public void setStatus_message(String status_message) {
                this.status_message = status_message;
            }

            public String getCoapplicantName() {
                return CoapplicantName;
            }

            public void setCoapplicantName(String coapplicantName) {
                CoapplicantName = coapplicantName;
            }

            public String getUnit_id() {
                return unit_id;
            }

            public void setUnit_id(String unit_id) {
                this.unit_id = unit_id;
            }

            public String getOrder_date() {
                return order_date;
            }

            public void setOrder_date(String order_date) {
                this.order_date = order_date;
            }

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

            private String payment_mode;
            private String amount;
            private String customer_name;
            private String customer_telephone;
            private String customer_address;
            private String customer_email;
            private String status_message;
            @SerializedName("Coapplicant Name")
            private String CoapplicantName;
            private String unit_id;
            //private String project_id;
            private String project_name;
            private String order_date;
            private String caption;
            private String value;
            private String transaction_no;
            private String unit_no;
            private String project_id;
            private String url;
            private String status;
            private String broker_code;
            private String broker_name;
            private String pan_no;

            public String getPan_no() {
                return pan_no;
            }

            public void setPan_no(String pan_no) {
                this.pan_no = pan_no;
            }

            public String getBroker_code() {
                return broker_code;
            }

            public void setBroker_code(String broker_code) {
                this.broker_code = broker_code;
            }

            public String getBroker_name() {
                return broker_name;
            }

            public void setBroker_name(String broker_name) {
                this.broker_name = broker_name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getProject_id() {
                return project_id;
            }

            public void setProject_id(String project_id) {
                this.project_id = project_id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCustomer_Mobile_no() {
                return Customer_Mobile_no;
            }

            public void setCustomer_Mobile_no(String customer_Mobile_no) {
                Customer_Mobile_no = customer_Mobile_no;
            }

            public String getCustomer_Name() {
                return Customer_Name;
            }

            public void setCustomer_Name(String customer_Name) {
                Customer_Name = customer_Name;
            }

            public String getBrokerage_terms() {
                return brokerage_terms;
            }

            public void setBrokerage_terms(String brokerage_terms) {
                this.brokerage_terms = brokerage_terms;
            }

            public String getCoapplicant() {
                return coapplicant;
            }

            public void setCoapplicant(String coapplicant) {
                this.coapplicant = coapplicant;
            }

            public String getCoupen_code() {
                return coupen_code;
            }

            public void setCoupen_code(String coupen_code) {
                this.coupen_code = coupen_code;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public String getPaid_amount() {
                return paid_amount;
            }

            public void setPaid_amount(String paid_amount) {
                this.paid_amount = paid_amount;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getPayment_plan() {
                return payment_plan_name;
            }

            public void setPayment_plan(String payment_plan) {
                this.payment_plan_name = payment_plan;
            }

            public String getPaymentPlanDesc() {
                return payment_plan_desc;
            }

            public void setPaymentPlanDesc(String payment_plan_desc) {
                this.payment_plan_desc = payment_plan_desc;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public String getTransaction_amount() {
                return transaction_amount;
            }

            public void setTransaction_amount(String transaction_amount) {
                this.transaction_amount = transaction_amount;
            }

            public String getTransaction_datetime() {
                return transaction_datetime;
            }

            public void setTransaction_datetime(String transaction_datetime) {
                this.transaction_datetime = transaction_datetime;
            }

            public String getTransaction_no() {
                return transaction_no;
            }

            public void setTransaction_no(String transaction_no) {
                this.transaction_no = transaction_no;
            }

            public String getUnit_no() {
                return unit_no;
            }

            public void setUnit_no(String unit_no) {
                this.unit_no = unit_no;
            }
        }

        public class Cords {
            private String se;
            private String nw;

            public String getSe() {
                return se;
            }

            public void setSe(String se) {
                this.se = se;
            }

            public String getNw() {
                return nw;
            }

            public void setNw(String nw) {
                this.nw = nw;
            }
        }// End of Cords class
    } // End of TargetData class
    // End of Data class

}// End of Class
