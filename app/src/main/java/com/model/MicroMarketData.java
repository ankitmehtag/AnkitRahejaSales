package com.model;

import java.io.Serializable;

/**
 * Created by Naresh on 05-Oct-17.
 */

public class MicroMarketData implements Serializable {

    private String micromarket_name;
    private String micromarket_id;
    private CityDataModel city;

    public String getMicromarket_name() {
        return micromarket_name;
    }

    public void setMicromarket_name(String micromarket_name) {
        this.micromarket_name = micromarket_name;
    }

    public String getMicromarket_id() {
        return micromarket_id;
    }

    public void setMicromarket_id(String micromarket_id) {
        this.micromarket_id = micromarket_id;
    }

    public CityDataModel getCity() {
        return city;
    }

    public void setCity(CityDataModel city) {
        this.city = city;
    }
}
