package com.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Naresh on 03-May-17.
 */

public class UpdatePersonalDetailRespModel extends BaseRespModel implements Serializable {

    private int isUnitReserved;
    private Data data;

    public int getIsUnitReserved() {
        return isUnitReserved;
    }

    public void setIsUnitReserved(int isUnitReserved) {
        this.isUnitReserved = isUnitReserved;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private UserInformation user_information;
        private ServiceProviderInfo service_provider_information;
        private TransactionInfo transaction_info;
        private ArrayList<CardInfo> card_info;
        private ArrayList<NetbankingInfo> netbanking_info;

        public UserInformation getUser_information() {
            return user_information;
        }

        public void setUser_information(UserInformation user_information) {
            this.user_information = user_information;
        }

        public ServiceProviderInfo getService_provider_information() {
            return service_provider_information;
        }

        public void setService_provider_information(ServiceProviderInfo service_provider_information) {
            this.service_provider_information = service_provider_information;
        }

        public TransactionInfo getTransaction_info() {
            return transaction_info;
        }

        public void setTransaction_info(TransactionInfo transaction_info) {
            this.transaction_info = transaction_info;
        }

        public ArrayList<CardInfo> getCard_info() {
            return card_info;
        }

        public void setCard_info(ArrayList<CardInfo> card_info) {
            this.card_info = card_info;
        }

        public ArrayList<NetbankingInfo> getNetbanking_info() {
            return netbanking_info;
        }

        public void setNetbanking_info(ArrayList<NetbankingInfo> netbanking_info) {
            this.netbanking_info = netbanking_info;
        }
    }

    public class UserInformation implements Serializable {
        private String first_name;
        private String last_name;
        private String phone_number;
        private String email;
        private String address;
        private String city;
        private String state;
        private String country;
        private String zipcode;
        private String pan_no;

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
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

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }

    public class ServiceProviderInfo implements Serializable {
        private String amount;
        private String product_info;
        private int environment;
        private String unit_id;

        public String getUnit_id() {
            return unit_id;
        }

        public void setUnit_id(String unit_id) {
            this.unit_id = unit_id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getProduct_info() {
            return product_info;
        }

        public void setProduct_info(String product_info) {
            this.product_info = product_info;
        }

        public int getEnvironment() {
            return environment;
        }

        public void setEnvironment(int environment) {
            this.environment = environment;
        }
    }

    public class TransactionInfo implements Serializable {
        private String transaction_id;
        private String order_id;
        private String surl;
        private String furl;
        private String curl;
        private String hash;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getSurl() {
            return surl;
        }

        public void setSurl(String surl) {
            this.surl = surl;
        }

        public String getFurl() {
            return furl;
        }

        public void setFurl(String furl) {
            this.furl = furl;
        }

        public String getCurl() {
            return curl;
        }

        public void setCurl(String curl) {
            this.curl = curl;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }


    public class CardInfo implements Serializable {
        private String card_name;
        private String card_image_url;

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getCard_image_url() {
            return card_image_url;
        }

        public void setCard_image_url(String card_image_url) {
            this.card_image_url = card_image_url;
        }
    }

    public class NetbankingInfo implements Serializable {
        private String bank_name;
        private String index;
        private String image;
        private String bank_code;

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBank_code() {
            return bank_code;
        }

        public void setBank_code(String bank_code) {
            this.bank_code = bank_code;
        }
    }

}
