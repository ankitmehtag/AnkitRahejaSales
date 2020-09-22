package com.model;

import java.io.Serializable;

/**
 * Created by Naresh on 08-May-17.
 */

public class PayUResponseModel implements Serializable{

    private String id;
    private String mode;
    private String status;
    private String unmappedstatus;
    private String key;
    private String txnid;
    private String transaction_fee;
    private String amount;
    private String cardCategory;
    private String discount;
    private String additional_charges;
    private String addedon;
    private String productinfo;
    private String firstname;
    private String zipcode;
    private String email;
    private String phone;
    private String hash;
    private String field1;
    private String field2;
    private String field3;
    private String field5;
    private String field6;
    private String field9;
    private String payment_source;
    private String PG_TYPE;
    private String bank_ref_no;
    private String ibibo_code;
    private String error_code;
    private String Error_Message;
    private String name_on_card;
    private String card_no;
    private String is_seamless;
    private String surl;
    private String furl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnmappedstatus() {
        return unmappedstatus;
    }

    public void setUnmappedstatus(String unmappedstatus) {
        this.unmappedstatus = unmappedstatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(String transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAdditional_charges() {
        return additional_charges;
    }

    public void setAdditional_charges(String additional_charges) {
        this.additional_charges = additional_charges;
    }

    public String getAddedon() {
        return addedon;
    }

    public void setAddedon(String addedon) {
        this.addedon = addedon;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getPayment_source() {
        return payment_source;
    }

    public void setPayment_source(String payment_source) {
        this.payment_source = payment_source;
    }

    public String getPG_TYPE() {
        return PG_TYPE;
    }

    public void setPG_TYPE(String PG_TYPE) {
        this.PG_TYPE = PG_TYPE;
    }

    public String getBank_ref_no() {
        return bank_ref_no;
    }

    public void setBank_ref_no(String bank_ref_no) {
        this.bank_ref_no = bank_ref_no;
    }

    public String getIbibo_code() {
        return ibibo_code;
    }

    public void setIbibo_code(String ibibo_code) {
        this.ibibo_code = ibibo_code;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_Message() {
        return Error_Message;
    }

    public void setError_Message(String error_Message) {
        Error_Message = error_Message;
    }

    public String getName_on_card() {
        return name_on_card;
    }

    public void setName_on_card(String name_on_card) {
        this.name_on_card = name_on_card;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getIs_seamless() {
        return is_seamless;
    }

    public void setIs_seamless(String is_seamless) {
        this.is_seamless = is_seamless;
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
}
