package com.model;

import java.io.Serializable;

/**
 * Created by Naresh on 05-Oct-17.
 */

public class SectorData implements Serializable {

    private String sector_name;
    private String sector_id;
    private MicroMarketData micromarket;

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public MicroMarketData getMicromarket() {
        return micromarket;
    }

    public void setMicromarket(MicroMarketData micromarket) {
        this.micromarket = micromarket;
    }
}
