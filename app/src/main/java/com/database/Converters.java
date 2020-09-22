package com.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.Projects;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Projects> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Projects>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Projects> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
