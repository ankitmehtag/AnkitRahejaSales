package com.model;

/**
 * Created by Naresh on 03-Feb-18.
 */

public class SubmitOfferJsonModel {
    private String offer_code;
    private String description;

    public SubmitOfferJsonModel(String offer_code, String description) {
        this.offer_code = offer_code;
        this.description = description;
    }

    public String getOffer_code() {
        return offer_code;
    }

    public void setOffer_code(String offer_code) {
        this.offer_code = offer_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
