package com.AppEnums;

/**
 * Created by Naresh on 04-Feb-17.
 */

public enum Neighbourhoods {

    LANDMARKS("point_of_interest"),
    AIRPORTS("airport"),
    RAILWAYS("train_station"),
    BUS_STANDS("bus_station"),
    TAXI_SERVICES("taxi_stand"),
    SCHOOLS("school"),
    HOSPITALS("hospital"),
    SHOPPING_MALLS("shopping_mall"),
    DEPARTMENTAL_STORS("department_store"),
    PHARMACIES("pharmacy"),
    PARKS("park"),
    BANKS("bank"),
    ATMS("atm"),
    RESTAURANTS("restaurant"),
    HOTELS("restaurant"),
    MOVIE_THEATERS("movie_theater"),
    NIGHT_CLUBS("night_club"),

    ERROR("Error");

    public String value;
    Neighbourhoods(String value) {
        this.value = value;
    }

    public static Neighbourhoods getEnum(String value) {
        if (value == null) return ERROR;
        for (Neighbourhoods e : Neighbourhoods.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return ERROR;
    }

}
