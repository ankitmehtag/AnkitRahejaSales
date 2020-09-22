package com.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.SubStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UpdateStatusConverter {
    @TypeConverter
    public static ArrayList<SubStatus> fromString(String value) {
        Type listType = new TypeToken<ArrayList<SubStatus>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<SubStatus> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
